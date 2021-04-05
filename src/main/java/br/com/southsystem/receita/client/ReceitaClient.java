package br.com.southsystem.receita.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "receita", url = "url")
public interface ReceitaClient {

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    boolean atualizarConta(@RequestParam String agencia, @RequestParam String conta, @RequestParam double saldo, @RequestParam String status) throws InterruptedException;


}
