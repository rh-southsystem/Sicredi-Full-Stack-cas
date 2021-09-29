package br.com.southsystem.receita.service;

import br.com.southsystem.receita.model.Account;
import br.com.southsystem.receita.model.dto.AccountDTO;
import br.com.southsystem.receita.repository.APIRepository;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class APIService {

    private static final String SUFFIX_CSV = ".csv";

    private final APIRepository apiRepository;
    private final ReceitaService receitaService;

    @Autowired
    public APIService(APIRepository apiRepository, ReceitaService receitaService) {
        this.apiRepository = apiRepository;
        this.receitaService = receitaService;
    }

    public Resource getAllProcessedCSV() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

        List<Account> accountList = apiRepository.findAll();
        List<AccountDTO> list = new ArrayList<>();
        list.addAll(mapListToDTOList(accountList));
        File arquivo = File.createTempFile("processadas", SUFFIX_CSV);

        Writer writer = new FileWriter(arquivo.getPath());

        ColumnPositionMappingStrategy<AccountDTO> strat = new ColumnPositionMappingStrategy();
        strat.setType(AccountDTO.class);
        String[] columns = new String[]{"agencia", "conta", "saldo", "status", "resultado"};
        strat.setColumnMapping(columns);

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(';')
                .withMappingStrategy(strat)
                .build();

        sbc.write(list);
        writer.close();

        return new InputStreamResource(FileUtils.openInputStream(arquivo));
    }


    private List<AccountDTO> mapListToDTOList(List<Account> accountList) {
        return accountList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO mapToDTO(Account account) {
        return AccountDTO.builder()
                .agencia(account.getAgencia())
                .conta(account.getConta())
                .saldo(account.getSaldo())
                .status(account.getStatus())
                .resultado(account.getResultado())
                .build();
    }

    public List<Account> create(MultipartFile file) throws IOException {
        ColumnPositionMappingStrategy<AccountDTO> strat = new ColumnPositionMappingStrategy();
        strat.setType(AccountDTO.class);
        String[] columns = new String[]{"agencia", "conta", "saldo", "status"};
        strat.setColumnMapping(columns);

        List<AccountDTO> list = new CsvToBeanBuilder(new InputStreamReader(file.getInputStream()))
                .withType(AccountDTO.class)
                .withEscapeChar(',')
                .withMappingStrategy(strat)
                .withSkipLines(1)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        List<AccountDTO> accounts = list.stream().map(this::mapAccountProccessed).collect(Collectors.toList());

        return apiRepository.saveAll(mapDtoListToEntity(accounts));

    }

    private List<Account> mapDtoListToEntity(List<AccountDTO> accounts) {
        return accounts.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    private Account mapToEntity(AccountDTO accountDTO) {
        return Account.builder()
                .agencia(Optional.ofNullable(accountDTO.getAgencia()).orElse(null))
                .conta(Optional.ofNullable(accountDTO.getConta()).orElse(null))
                .saldo(Optional.ofNullable(accountDTO.getSaldo()).orElse(null))
                .status(Optional.ofNullable(accountDTO.getStatus()).orElse(null))
                .resultado(Optional.ofNullable(accountDTO.getResultado()).orElse(null))
                .build();
    }

    private AccountDTO mapAccountProccessed(AccountDTO accountDTO) {

        try {

            boolean account = receitaService.atualizarConta(accountDTO.getAgencia(), accountDTO.getUnformattedAccount(accountDTO.getConta()), accountDTO.getSaldo(), accountDTO.getStatus());

            accountDTO.setResultado(account);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return accountDTO;
    }

}
