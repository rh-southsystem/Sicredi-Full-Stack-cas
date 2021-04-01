package br.com.southsystem.receita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AccountDTO {

    private String agencia;
    private String conta;
    private Double saldo;
    private String status;
    private Boolean resultado;
}
