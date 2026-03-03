package com.salinas.wallet_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionRequestDTO {

    private String identificadorOrigen;
    private String identificadorDestino;
    private BigDecimal monto;

}
