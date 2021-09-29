package br.com.southsystem.receita.mapper;

import br.com.southsystem.receita.model.dto.AccountDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AccountMapper implements FieldSetMapper<AccountDTO> {
    @Override
    public AccountDTO mapFieldSet(FieldSet fieldSet) throws BindException {

        final AccountDTO accountDTO = new AccountDTO();

        accountDTO.setAgencia(fieldSet.readString("agencia"));
        accountDTO.setConta(fieldSet.readString("conta"));
        accountDTO.setSaldo(fieldSet.readDouble("saldo"));
        accountDTO.setStatus(fieldSet.readString("status"));
        return accountDTO;
    }
}
