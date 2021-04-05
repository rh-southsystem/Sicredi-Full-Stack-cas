package br.com.southsystem.receita.controller;


import br.com.southsystem.receita.model.Account;
import br.com.southsystem.receita.service.APIService;
import br.com.southsystem.receita.utils.ResponseEntityUtils;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/receita")
@Api(value = "API para CSV e integração receita")
public class APIController {

    @Autowired
    private APIService apiService;

    @GetMapping(produces = {"text/csv"})
    @ApiOperation("Busca todos processados")
    @ApiResponses({
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = ""),
            @ApiResponse(code = 500, message = "")})
    public ResponseEntity<Resource> getAll() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        Resource resource = apiService.getAllProcessedCSV();

        return ResponseEntityUtils.responseResource(resource, resource.getFilename());
    }

    @PostMapping
    @ApiOperation("Efetiva csv")
    @ApiResponses({
            @ApiResponse(code = 201, message = ""),
            @ApiResponse(code = 400, message = ""),
            @ApiResponse(code = 500, message = "")})
    public ResponseEntity<List<Account>> create(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.created(null).body(apiService.create(file));
    }

}