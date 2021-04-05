package br.com.southsystem.receita.enums;

import lombok.Getter;

public enum EventStatusEnum {

    EVENT_SUCCESS("-", "Sucesso ao realizar operação"),
    RESOURCE_NOT_FOUND("-", "Recurso não encontrado"),
    INTEGRATION_ERROR("-", "Erro de integracom api externa."),
    GENERIC_ERROR("-", "Erro ao realizar operação");

    @Getter
    private final String code;

    @Getter
    private final String messsage;

    EventStatusEnum(String code, String messsage) {
        this.code = code;
        this.messsage = messsage;
    }
}
