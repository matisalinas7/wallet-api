package com.salinas.wallet_api.repository;

import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByCuentaOrigenOrCuentaDestinoOrderByFechaAltaDesc(Cuenta origen, Cuenta destino);

}
