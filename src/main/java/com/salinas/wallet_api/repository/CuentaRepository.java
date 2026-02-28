package com.salinas.wallet_api.repository;

import com.salinas.wallet_api.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

    Optional<Cuenta> findByCvu(String cvu);
    Optional<Cuenta> findByAlias(String alias);
    Optional<Cuenta> findByUsuarioEmail(String email);

}
