package com.tecsup.services;

import com.tecsup.dto.PedidoDTO;
import com.tecsup.dto.PedidoRegistroDTO;
import com.tecsup.dto.DetallePedidoDTO;
import com.tecsup.client.ClienteClient;
import com.tecsup.client.ProductoClient;
import com.tecsup.model.EstadoPedido;
import java.time.LocalDate;
import java.time.ZoneId;
import com.tecsup.dto.PedidoResponse;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Pedido;
import com.tecsup.model.DetallePedido;
import com.tecsup.repository.PedidoRepository;
import com.tecsup.repository.DetallePedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final DetallePedidoRepository detalles;

    private final ClienteClient clientes;
    private final ProductoClient productos;

    public PedidoService(PedidoRepository repo, DetallePedidoRepository detalles,
                         ClienteClient clientes, ProductoClient productos) {
        this.repo = repo;
        this.detalles = detalles;
        this.clientes = clientes;
        this.productos = productos;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return repo.findAll().stream().map(this::respuesta).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id) {
        return respuesta(buscar(id));
    }

    private Pedido buscar(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional
    public PedidoResponse crear(PedidoRegistroDTO registro) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdCliente(registro.clienteId());
        dto.setFecha(registro.fecha() == null ? LocalDate.now(ZoneId.of("America/Lima")) : registro.fecha());
        if (registro.detalles() == null) {
            var producto = productos.obtener(registro.productoId());
            DetallePedidoDTO detalle = new DetallePedidoDTO();
            detalle.setIdProducto(producto.id());
            detalle.setCantidad(registro.cantidad());
            detalle.setPrecio(producto.precio());
            dto.setDetalles(List.of(detalle));
            dto.setTotal(producto.precio().multiply(BigDecimal.valueOf(registro.cantidad())));
            if (dto.getTotal().compareTo(new BigDecimal("9999999999.99")) > 0) {
                throw new IllegalArgumentException("El total excede el importe permitido");
            }
        } else {
            dto.setDetalles(registro.detalles());
            dto.setTotal(registro.total());
        }
        return guardar(new Pedido(), dto);
    }

    @Transactional
    public PedidoResponse actualizar(Long id, PedidoDTO dto) {
        Pedido existente = bloquear(id);
        if (existente.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden editar pedidos pendientes");
        }
        validarTotal(dto);
        detalles.deleteByIdPedido(id);
        return guardar(existente, dto);
    }

    private void validarTotal(PedidoDTO dto) {
        BigDecimal calculado = dto.getDetalles().stream()
                .map(d -> d.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (dto.getTotal().compareTo(calculado) != 0) {
            throw new IllegalArgumentException("El total debe coincidir con la suma de los detalles");
        }
    }

    private PedidoResponse guardar(Pedido pedido, PedidoDTO dto) {
        validarTotal(dto);
        pedido.setFecha(dto.getFecha());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.setTotal(dto.getTotal());
        Pedido guardado = repo.saveAndFlush(pedido);
        var nuevos = dto.getDetalles().stream().map(d -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdPedido(guardado.getIdPedido());
            detalle.setIdProducto(d.getIdProducto());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecio(d.getPrecio());
            return detalle;
        }).toList();
        detalles.saveAllAndFlush(nuevos);
        return respuesta(guardado);
    }

    private PedidoResponse respuesta(Pedido p) {
        var cliente = clientes.obtener(p.getIdCliente());
        var lineas = detalles.findByIdPedidoOrderByIdDetalle(p.getIdPedido()).stream()
                .map(d -> new PedidoResponse.Detalle(d.getIdDetalle(), d.getIdPedido(), d.getIdProducto(),
                        d.getCantidad(), d.getPrecio(), productos.obtener(d.getIdProducto())))
                .toList();
        var unica = lineas.size() == 1 ? lineas.getFirst() : null;
        return new PedidoResponse(p.getIdPedido(), p.getIdPedido(), p.getFecha(), p.getIdCliente(),
                p.getTotal(), p.getEstado(), cliente, unica == null ? null : unica.cantidad(),
                unica == null ? null : unica.producto(), lineas);
    }

    @Transactional
    public void cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = bloquear(id);
        if (pedido.getEstado() == estado) {
            return;
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Solo se puede cambiar el estado de un pedido pendiente");
        }
        pedido.setEstado(estado);
    }

    private Pedido bloquear(Long id) {
        return repo.buscarParaActualizar(id).orElseThrow(() ->
                new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }
}
