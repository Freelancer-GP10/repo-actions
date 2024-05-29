package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Dto.Conexao.AceitarServicoDTO;
import com.example.ConnecTi.Projeto.Domain.Dto.Conexao.CadastrarConexaoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Conexao.ConexaoInfoDTO;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryServico;
import com.example.ConnecTi.Projeto.Domain.Service.Conexao.ConexaoService;
import com.example.ConnecTi.Projeto.Domain.Service.ListObj;
import com.example.ConnecTi.Projeto.Model.Conexao;
import com.example.ConnecTi.Projeto.Model.Servico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conexao")
public class ConexaoController {
    @Autowired
    private RepositoryServico servico;
    @Autowired
    private RepositoryFreelancer repositoryFreelancer;
    @Autowired
    private ConexaoService conexaoService;
    public ConexaoController(ConexaoService conexaoService) {
        this.conexaoService = conexaoService;
    }

    @PostMapping("/aceitar-servico")
    public ResponseEntity<Conexao> aceitarServico(@RequestBody @Valid AceitarServicoDTO aceitarServicoDto) throws Exception {

        System.out.println(aceitarServicoDto.fkServico());
            // Lógica para aceitar o serviço e criar uma conexão
            Conexao novaConexao = conexaoService.aceitarServicoECreateConexao(aceitarServicoDto);
            return new ResponseEntity<>(novaConexao, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Conexao>> listarConexoes(){
        try {
            List<Conexao> conexoes = conexaoService.listarTodasConexoesOrdenadasPorIdDesc();
            return new ResponseEntity<>(conexoes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar-info")
    public ResponseEntity<List<ConexaoInfoDTO>> listarInfoConexoes() {
        try {
            List<ConexaoInfoDTO> conexoesInfo = conexaoService.listarInfoConexoes();
            return new ResponseEntity<>(conexoesInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/cadastrar")
    public ResponseEntity<Conexao> criarConexao(@RequestBody CadastrarConexaoDto novaConexaoDto) {
        try {
            Conexao conexao = conexaoService.criarConexao(novaConexaoDto);
            return new ResponseEntity<>(conexao, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conexao> getConexaoPorId(@PathVariable int id) {
        try {
            Conexao conexao = conexaoService.getConexaoPorId((long) id);
            if (conexao != null) {
                return new ResponseEntity<>(conexao, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/list-csv-files")
    public ResponseEntity<List<String>> listCsvFiles() {
        try {
            File folder = new File("C:\\Users\\thiag\\Downloads\\");
            File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            // NAO ESQUECER DE COLOCAR O CAMINHO RELATIVO

            List<String> fileNames = Arrays.stream(listOfFiles)
                    .map(File::getName)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(fileNames, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download-csv/{fileName}")
    public ResponseEntity<Resource> downloadCsvFile(@PathVariable String fileName) {
        try {
            Path path = Paths.get("C:\\Users\\thiag\\Downloads\\" + fileName);
            Resource resource = new InputStreamResource(Files.newInputStream(path));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    public String getDownloadsPath() {
//        String userHome = System.getProperty("user.home");
//        String os = System.getProperty("os.name").toLowerCase();
//
//        if (os.contains("win")) {
//            return userHome + "\\Downloads\\";
//        } else if (os.contains("mac")) {
//            return userHome + "/Downloads/";
//        } else if (os.contains("nix") || os.contains("nux")) {
//            return userHome + "/Downloads/";  // geralmente, mas pode variar dependendo da distribuição
//        } else {
//            throw new UnsupportedOperationException("Sistema operacional não reconhecido");
//        }
//    }









//    @PutMapping("/{id}")
//    public ResponseEntity<Conexao> atualizarPorId(@PathVariable int id, @RequestBody Conexao conexaoAtualizada){
//        if (id >= 0 && id < listaConexoes.size()){
//            listaConexoes.set(id, conexaoAtualizada);
//            return ResponseEntity.ok(conexaoAtualizada);
//        }
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Conexao> deletarPorId(@PathVariable int id){
//        if (id >= 0 && id < listaConexoes.size()){
//            listaConexoes.remove(id);
//            return ResponseEntity.status(204).build();
//        }
//        return ResponseEntity.noContent().build();
//    }
}


