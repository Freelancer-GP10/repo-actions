package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryApiEndereco;
import com.example.ConnecTi.Projeto.Model.ApiEndereco;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/endereco")
public class ApiEnderecoController {
    private RepositoryApiEndereco repository;
    private final RestTemplate restTemplate = new RestTemplate();
    @PostMapping
    public ResponseEntity<ApiEndereco> cadastrar(@RequestBody ApiEndereco apiEndereco) {
        ApiEndereco apiEnderecoSalvo = this.repository.save(apiEndereco);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ApiEndereco>> listar() {
        List<ApiEndereco> enderecos = this.repository.findAll();
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiEndereco> litarPorId(@PathVariable Long id) {
        Optional<ApiEndereco> enderecoOpt = this.repository.findById(id);
        return ResponseEntity.of(enderecoOpt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiEndereco> atualizar(@PathVariable Long id, @RequestBody ApiEndereco apiEndereco) {
        ApiEndereco endereco = this.repository.getReferenceById(id);
        if (endereco != null) {
            endereco.setCep(apiEndereco.getCep());
            endereco.setLogradouro(apiEndereco.getLogradouro());
            endereco.setUF(apiEndereco.getUF());
            endereco.setNumero(apiEndereco.getNumero());
            this.repository.save(endereco);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


        @DeleteMapping("/{id}")
        public ResponseEntity<ApiEndereco> deletar (@PathVariable Long id){
            Optional<ApiEndereco> enderecoOpt = this.repository.findById(id);
            if (enderecoOpt.isPresent()) {
                this.repository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.noContent().build();
    }
    @GetMapping("/cep/{cep}")
    public ResponseEntity<String> getCepInfo(@PathVariable String cep){
        String uri = String.format("https://viacep.com.br/ws/%s/json/", cep);
        String result = restTemplate.getForObject(uri, String.class);
        return ResponseEntity.ok(result);
    }
}
