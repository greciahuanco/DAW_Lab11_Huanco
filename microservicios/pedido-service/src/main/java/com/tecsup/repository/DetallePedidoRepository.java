package com.tecsup.repository;

import com.tecsup.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByIdPedidoOrderByIdDetalle(Long idPedido);
    void deleteByIdPedido(Long idPedido);
}
