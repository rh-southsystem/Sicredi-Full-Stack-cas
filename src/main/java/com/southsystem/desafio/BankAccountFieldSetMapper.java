package com.southsystem.desafio;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class BankAccountFieldSetMapper implements FieldSetMapper<BankAccountVO> {

    @Override
    public BankAccountVO mapFieldSet(FieldSet fieldSet) {
        final BankAccountVO bankAccountVO = new BankAccountVO();
        bankAccountVO.setAgencia(fieldSet.readString("agencia"));
        bankAccountVO.setConta(fieldSet.readString("conta"));
        bankAccountVO.setSaldo(fieldSet.readDouble("saldo"));
        bankAccountVO.setStatus(fieldSet.readString("status"));
        return bankAccountVO;
    }
}
