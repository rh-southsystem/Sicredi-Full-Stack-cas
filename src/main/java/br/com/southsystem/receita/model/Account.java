package br.com.southsystem.receita.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "agencia")
    private String agencia;

    @Column(name = "conta")
    private String conta;

    @Column(name = "saldo")
    private Double saldo;

    @Column(name = "status")
    private String status;

    @Column(name = "resultado")
    private Boolean resultado;
}
