import argparse
import json
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
    env['JAVA_HOME'] = subprocess.check_output(['/usr/libexec/java_home', '-v', '25'], text=True).strip()
env['PATH'] = env['JAVA_HOME'] + '/bin:' + env['PATH']
java = str(Path(env['JAVA_HOME']) / 'bin/java')
services = {'cliente': 8081, 'categoria': 8082, 'producto': 8083, 'pedido': 8084}
offset = 1010 if args.perfil == 'prod' else 0

def available(port):
    try:
        with urllib.request.urlopen(f'http://127.0.0.1:{port}/api/configuracion', timeout=2) as response:
            return json.load(response).get('ambiente') == ('desarrollo' if args.perfil == 'dev' else 'produccion')
    except Exception:
        return False

def stop():
    for name in services:
        path = runtime / (name + '.pid')
        if not path.exists():
            continue
        pid = int(path.read_text())
        try:
            command = subprocess.check_output(['ps', '-p', str(pid), '-o', 'args='], text=True)
            if str(root / 'microservicios' / (name + '-service') / 'target') in command:
                os.kill(pid, signal.SIGTERM)
        except (ProcessLookupError, subprocess.CalledProcessError):
            pass
        path.unlink(missing_ok=True)
    deadline = time.monotonic() + 20
    while time.monotonic() < deadline:
        busy = []
        for port in services.values():
            with socket.socket() as sock:
                if sock.connect_ex(('127.0.0.1', port + offset)) == 0:
                    busy.append(port + offset)
        if not busy:
            print('Servicios detenidos')
            return
        time.sleep(0.25)
    raise SystemExit('Todavía hay puertos ocupados: ' + str(busy))

if args.accion == 'compilar':
    for name in services:
        project = root / 'microservicios' / (name + '-service')
        subprocess.run([str(project / 'mvnw'), '-B', 'clean', 'package'], cwd=project, env=env, check=True)
elif args.accion == 'detener':
    stop()
elif args.accion == 'estado':
    for name, port in services.items():
        print(f'{name}: {"EJECUTANDO" if available(port + offset) else "DETENIDO"} - {port + offset}')
else:
    for name, port in services.items():
        with socket.socket() as sock:
            if sock.connect_ex(('127.0.0.1', port + offset)) == 0:
                raise SystemExit(f'El puerto {port + offset} está ocupado')
        jar = root / 'microservicios' / (name + '-service') / 'target' / (name + '-service-0.0.1-SNAPSHOT.jar')
        if not jar.exists():
            raise SystemExit('Primero ejecute python3 laboratorio.py compilar')
    try:
        for name, port in services.items():
            project = root / 'microservicios' / (name + '-service')
            jar = project / 'target' / (name + '-service-0.0.1-SNAPSHOT.jar')
            with (runtime / (name + '.log')).open('a') as log:
                process = subprocess.Popen([java, '-Xms64m', '-Xmx256m', '-jar', str(jar)], cwd=project, env=env, stdout=log, stderr=subprocess.STDOUT, start_new_session=True)
            (runtime / (name + '.pid')).write_text(str(process.pid))
        deadline = time.monotonic() + 150
        while not all(available(port + offset) for port in services.values()):
            if time.monotonic() > deadline:
                raise RuntimeError('Revise los archivos de .runtime/' + args.perfil)
            time.sleep(1)
        for name, port in services.items():
            resource = {'categoria': 'categorias', 'cliente': 'clientes', 'producto': 'productos', 'pedido': 'pedidos'}[name]
            print(f'{name}: http://localhost:{port + offset}/api/{resource}')
    except BaseException:
        stop()
        raise
