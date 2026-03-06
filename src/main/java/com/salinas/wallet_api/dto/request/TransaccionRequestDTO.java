package com.salinas.wallet_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @NotBlank
    private String identificadorOrigen;
    @NotBlank
    private String identificadorDestino;
    @NotBlank
    @Positive(message = "El monto a transferir debe ser mayor a cero")
    private BigDecimal monto;

}
