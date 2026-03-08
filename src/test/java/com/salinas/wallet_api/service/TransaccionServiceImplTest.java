package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.CashInRequestDTO;
import com.salinas.wallet_api.dto.request.TransaccionRequestDTO;
import com.salinas.wallet_api.dto.response.CashInResponseDTO;
import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Transaccion;
import com.salinas.wallet_api.enums.TipoTransaccion;
import com.salinas.wallet_api.exception.SaldoInsuficienteException;
import com.salinas.wallet_api.repository.CuentaRepository;
import com.salinas.wallet_api.repository.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {
    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    @Test
    void realizarTransferencia_conSaldoSuficiente_debeRetornarDTO() {

        // --- ARRANGE (preparamos el escenario) ---
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(new BigDecimal("1000.00"));

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(new BigDecimal("500.00"));

        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setIdentificadorOrigen("origen@email.com");
        request.setIdentificadorDestino("destino@email.com");
        request.setMonto(new BigDecimal("200.00"));

        Transaccion transaccionGuardada = new Transaccion();
        transaccionGuardada.setMonto(new BigDecimal("200.00"));
        transaccionGuardada.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);

        // Le decimos al Mock qué devolver cuando lo llamen
        when(cuentaRepository.findByUsuarioEmail("origen@email.com"))
                .thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findByUsuarioEmail("destino@email.com"))
                .thenReturn(Optional.of(cuentaDestino));
        when(transaccionRepository.save(any(Transaccion.class)))
                .thenReturn(transaccionGuardada);

        // --- ACT (ejecutamos lo que queremos probar) ---
        TransaccionResponseDTO resultado = transaccionService.realizarTransferencia(request);

        // --- ASSERT (verificamos que el resultado sea el esperado) ---
        assertNotNull(resultado);
        assertEquals(new BigDecimal("800.00"), cuentaOrigen.getSaldo());
        assertEquals(new BigDecimal("700.00"), cuentaDestino.getSaldo());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void realizarTransferencia_conSaldoInsuficiente_debeLanzarExcepcion() {

        // --- ARRANGE ---
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(new BigDecimal("100.00"));

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(new BigDecimal("500.00"));

        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setIdentificadorOrigen("origen@email.com");
        request.setIdentificadorDestino("destino@email.com");
        request.setMonto(new BigDecimal("500.00")); // más de lo que tiene origen

        when(cuentaRepository.findByUsuarioEmail("origen@email.com"))
                .thenReturn(Optional.of(cuentaOrigen));
        when(cuentaRepository.findByUsuarioEmail("destino@email.com"))
                .thenReturn(Optional.of(cuentaDestino));

        // --- ACT & ASSERT ---
        assertThrows(SaldoInsuficienteException.class, () ->
                transaccionService.realizarTransferencia(request)
        );

        // Verificamos que nunca se llamó a save() porque falló antes
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void realizarCashIn_conDatosValidos_debeRetornarDTO() {

        // --- ARRANGE ---
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(new BigDecimal("500.00"));

        CashInRequestDTO request = new CashInRequestDTO();
        request.setIdentificadorDestino("destino@email.com");
        request.setMonto(new BigDecimal("300.00"));

        Transaccion transaccionGuardada = new Transaccion();
        transaccionGuardada.setMonto(new BigDecimal("300.00"));
        transaccionGuardada.setTipoTransaccion(TipoTransaccion.CASH_IN);

        when(cuentaRepository.findByUsuarioEmail("destino@email.com"))
                .thenReturn(Optional.of(cuentaDestino));
        when(transaccionRepository.save(any(Transaccion.class)))
                .thenReturn(transaccionGuardada);

        // --- ACT ---
        CashInResponseDTO resultado = transaccionService.realizarCashIn(request);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(new BigDecimal("800.00"), cuentaDestino.getSaldo());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void obtenerHistorial_conCuentaExistente_debeRetornarLista() {

        // --- ARRANGE ---
        Cuenta cuenta = new Cuenta();

        Transaccion tx1 = new Transaccion();
        tx1.setMonto(new BigDecimal("100.00"));
        tx1.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);

        Transaccion tx2 = new Transaccion();
        tx2.setMonto(new BigDecimal("200.00"));
        tx2.setTipoTransaccion(TipoTransaccion.CASH_IN);

        when(cuentaRepository.findByUsuarioEmail("juan@email.com"))
                .thenReturn(Optional.of(cuenta));
        when(transaccionRepository.findByCuentaOrigenOrCuentaDestinoOrderByFechaAltaDesc(cuenta, cuenta))
                .thenReturn(List.of(tx1, tx2));

        // --- ACT ---
        List<TransaccionResponseDTO> resultado = transaccionService.obtenerHistorial("juan@email.com");

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(transaccionRepository, times(1))
                .findByCuentaOrigenOrCuentaDestinoOrderByFechaAltaDesc(cuenta, cuenta);
    }
}
