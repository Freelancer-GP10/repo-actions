package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Service.Conexao.ConexaoService;
import com.example.ConnecTi.Projeto.Model.Conexao;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RequestMapping("/txt")
@RequiredArgsConstructor
@RestController

public class TxtController {

    private final ConexaoService conexaoService;

    @GetMapping("/exportar-txt")
    public ResponseEntity<Resource> exportarConexoesTxt() throws FileNotFoundException {
        String filename = "conexao_" + conexaoService.getFormattedCurrentDateTime() + ".txt";

        // Usar um caminho relativo ao diretório do projeto
        String relativePath = "arquivosTxt/" + filename;

        // Obter o caminho absoluto a partir do caminho relativo
        String absolutePath = new File(relativePath).getAbsolutePath();

        List<Conexao> conexoes = conexaoService.listarTodasConexoes();
        conexoes.forEach(conexao -> conexaoService.exportConexaoEspecificaTxt(conexao, absolutePath));

        File file = new File(absolutePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }


    @PostMapping("/importar-txt")
    public ResponseEntity<String> importarConexoesTxt(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo não enviado.");
        }

        String filename = "conexao_importada_" + conexaoService.getFormattedCurrentDateTime() + ".txt";

        // Usar um caminho relativo ao diretório do projeto
        String relativePath = "arquivosTxt/" + filename;

        // Obter o caminho absoluto a partir do caminho relativo
        String absolutePath = new File(relativePath).getAbsolutePath();

        try {
            // Salvar o arquivo em um local temporário
            file.transferTo(new File(absolutePath));

            // Importar conexões do arquivo
            List<Conexao> conexoesImportadas = conexaoService.importarConexoesDeTxt(absolutePath);


            return ResponseEntity.ok("Arquivo importado com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao importar o arquivo: " + e.getMessage());
        }
    }





}
