package com.southsystem.desafio;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BankAccountProcessor implements ItemProcessor<BankAccountVO, BankAccountVO> {

    @Override
    public BankAccountVO process(BankAccountVO bankAccountVO) throws InterruptedException {
        // Exemplo como chamar o "serviço" do Banco Central.
        ReceitaService receitaService = new ReceitaService();
        Boolean result = receitaService.atualizarConta(bankAccountVO.getAgencia(), bankAccountVO.getContaFormatted(), bankAccountVO.getSaldo(), bankAccountVO.getStatus());
        bankAccountVO.setResultado(result);
        return bankAccountVO;
    }
}
