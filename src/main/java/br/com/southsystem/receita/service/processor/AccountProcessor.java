package br.com.southsystem.receita.service.processor;

import br.com.southsystem.receita.model.dto.AccountDTO;
import br.com.southsystem.receita.service.ReceitaService;
import org.springframework.batch.item.ItemProcessor;


public class AccountProcessor implements ItemProcessor<AccountDTO, AccountDTO> {

    @Override
    public AccountDTO process(AccountDTO accountDTO) throws Exception {

        ReceitaService receitaService = new ReceitaService();
        String unformattedAccount = "";

        if(accountDTO.getConta() != null){
            unformattedAccount = accountDTO.getConta().replace("-","");
        }
        Boolean result = receitaService.atualizarConta(accountDTO.getAgencia(), unformattedAccount, accountDTO.getSaldo(), accountDTO.getStatus());
        accountDTO.setResultado(result);
        return accountDTO;
    }
}
