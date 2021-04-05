package br.com.southsystem.receita.repository;

import br.com.southsystem.receita.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface APIRepository extends JpaRepository<Account, Long> {

}