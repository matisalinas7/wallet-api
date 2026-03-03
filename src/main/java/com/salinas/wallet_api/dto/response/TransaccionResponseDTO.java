package com.salinas.wallet_api.dto.response;

import com.salinas.wallet_api.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponseDTO {

    private Long id;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private TipoTransaccion tipoTransaccion;

}
