package com.southsystem.desafio;

import lombok.Data;

@Data
public class BankAccountVO {

    private String agencia;
    private String conta;
    private Double saldo;
    private String status;
    private Boolean resultado;

}
