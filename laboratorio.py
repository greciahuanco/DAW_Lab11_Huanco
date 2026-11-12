import argparse
import os
import signal
import socket
import subprocess
import time
import urllib.request
from pathlib import Path

root = Path(__file__).resolve().parent
parser = argparse.ArgumentParser()
parser.add_argument('accion', choices=['compilar', 'iniciar', 'detener', 'estado'])
parser.add_argument('--perfil', choices=['dev', 'prod'], default='dev')
parser.add_argument('--balanceo', action='store_true')
args = parser.parse_args()
runtime = root / '.runtime' / args.perfil
runtime.mkdir(parents=True, exist_ok=True)
env = os.environ.copy()
for line in (root / '.env').read_text().splitlines():
    if '=' in line:
        key, value = line.split('=', 1)
        env[key] = value
env['SPRING_PROFILES_ACTIVE'] = args.perfil
if not env.get('JAVA_HOME'):
    env['JAVA_HOME'] = subprocess.check_output(['/usr/libexec/java_home', '-v', '21'], text=True).strip()
env['PATH'] = env['JAVA_HOME'] + os.pathsep + env['PATH']
java = str(Path(env['JAVA_HOME']) / 'bin/java')
offset = 1010 if args.perfil == 'prod' else 0
services = {'eureka-server': 8761, 'cliente-service': 8081 + offset,
            'categoria-service': 8082 + offset, 'producto-service': 8083 + offset,
            'pedido-service': 8084 + offset, 'gateway-service': 8080}
if args.balanceo:
    services['categoria-secundaria'] = 8085 + offset

def module(name):
    return 'categoria-service' if name == 'categoria-secundaria' else name

def endpoint(name):
    if name == 'eureka-server':
        return '/'
    if name == 'gateway-service':
        return '/api/categorias'
    return '/api/configuracion'

def available(name, port):
    try:
        with urllib.request.urlopen(f'http://127.0.0.1:{port}{endpoint(name)}', timeout=2) as response:
            return response.status == 200
    except Exception:
        return False

def stop():
    for name in dict.fromkeys([*services, 'categoria-secundaria']):
        path = runtime / (name + '.pid')
        if not path.exists():
            continue
        pid = int(path.read_text())
        try:
            command = subprocess.check_output(['ps', '-p', str(pid), '-o', 'args='], text=True)
            if str(root / 'microservicios' / module(name) / 'target') in command:
                os.kill(pid, signal.SIGTERM)
        except (ProcessLookupError, subprocess.CalledProcessError):
            pass
        path.unlink(missing_ok=True)
    deadline = time.monotonic() + 20
    while time.monotonic() < deadline:
        busy = []
        for port in services.values():
            with socket.socket() as sock:
                if sock.connect_ex(('127.0.0.1', port)) == 0:
                    busy.append(port)
        if not busy:
            print('Servicios detenidos')
            return
        time.sleep(0.25)
    raise SystemExit('Todavía hay puertos ocupados: ' + str(busy))

if args.accion == 'compilar':
    for name in dict.fromkeys(map(module, services)):
        project = root / 'microservicios' / module(name)
        subprocess.run([str(project / 'mvnw'), '-B', 'clean', 'package'], cwd=project, env=env, check=True)
elif args.accion == 'detener':
    stop()
elif args.accion == 'estado':
    for name, port in services.items():
        print(f'{name}: {"EJECUTANDO" if available(name, port) else "DETENIDO"} - {port}')
else:
    for name, port in services.items():
        with socket.socket() as sock:
            if sock.connect_ex(('127.0.0.1', port)) == 0:
                raise SystemExit(f'El puerto {port} está ocupado')
        jar = root / 'microservicios' / module(name) / 'target' / (module(name) + '-0.0.1-SNAPSHOT.jar')
        if not jar.exists():
            raise SystemExit('Primero ejecute python3 laboratorio.py compilar')
    try:
        for name, port in services.items():
            project = root / 'microservicios' / module(name)
            jar = project / 'target' / (module(name) + '-0.0.1-SNAPSHOT.jar')
            with (runtime / (name + '.log')).open('a') as log:
                process = subprocess.Popen([java, '-Xms64m', '-Xmx256m', '-jar', str(jar), f'--server.port={port}'], cwd=project, env=env, stdout=log, stderr=subprocess.STDOUT, start_new_session=True)
            (runtime / (name + '.pid')).write_text(str(process.pid))
            if name == 'eureka-server':
                deadline = time.monotonic() + 30
                while not available(name, port):
                    if process.poll() is not None or time.monotonic() > deadline:
                        raise RuntimeError('Eureka no está disponible. Revise eureka-server.log')
                    time.sleep(0.5)
        deadline = time.monotonic() + 150
        while not all(available(name, port) for name, port in services.items()):
            if time.monotonic() > deadline:
                raise RuntimeError('Revise los archivos de .runtime/' + args.perfil)
            time.sleep(1)
        for name, port in services.items():
            print(f'{name}: http://localhost:{port}{endpoint(name)}')
    except BaseException:
        stop()
        raise
