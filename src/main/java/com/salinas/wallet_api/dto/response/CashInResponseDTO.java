package com.salinas.wallet_api.dto.response;

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
public class CashInResponseDTO {

    private Long id;
    private BigDecimal monto;
    private LocalDateTime fecha;

}
