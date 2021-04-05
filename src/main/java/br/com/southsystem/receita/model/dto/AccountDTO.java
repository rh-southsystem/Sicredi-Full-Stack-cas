package br.com.southsystem.receita.model.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    @CsvBindByName(column = "agencia")
    private String agencia;
    @CsvBindByName(column = "conta")
    private String conta;
    @CsvBindByName(column = "saldo")
    private Double saldo;
    @CsvBindByName(column = "status")
    private String status;

    private Boolean resultado;

    public String getUnformattedAccount(String conta) {
        if (getConta() != null) {
            return getConta().replace("-", "");
        }
        return conta;
    }

}
