package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryPortifolio;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryUsuario;
import com.example.ConnecTi.Projeto.Domain.Security.Configuration.AutenticacaoService;
import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Portifolio;
import com.example.ConnecTi.Projeto.Model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import static java.util.UUID.*;

@RestController
@RequestMapping("/portifolio")
public class PortifolioController {

    @Autowired
    private RepositoryPortifolio repository;
    @Autowired
    private RepositoryFreelancer repositoryFreelancer;
    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private RepositoryUsuario usuarioRepository;
    // Define o caminho base para onde os arquivos serão salvos

    private static final String BASE_PATH = "C:\\Users\\thiag\\Desktop\\Teste";

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                     @RequestParam String git) {
        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum arquivo enviado");
        }

        // Gerar nome único para o arquivo
        String uniqueFilename = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
        Long userLogado = autenticacaoService.getUsuarioFromUsuarioDetails().getId();
        try {
            // Salvar o arquivo no caminho especificado
            Path path = Paths.get(BASE_PATH, uniqueFilename);
            Files.createDirectories(path.getParent()); // Garante que os diretórios existam
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Obter o freelancer do banco de dados
            Usuario freelancer = usuarioRepository.findById(userLogado)
                    .orElseThrow(() -> new EntityNotFoundException("Freelancer não encontrado com o ID: " + userLogado));

            // Verificar se o freelancer já tem um portfólio
            if (freelancer.getFreelancer().getPortifolio() != null) {
                // Se já tiver, atualize as informações do portfólio existente
                Portifolio portifolio = freelancer.getFreelancer().getPortifolio();
                portifolio.setArquivo(new File(path.toString()));
                portifolio.setLinkRepositorio(git);
                portifolio.setFreelancer(freelancer.getFreelancer()); // Associe o Portifolio ao Freelancer

                // Salvar as atualizações no banco de dados
                repository.save(portifolio);
            } else {
                // Se não tiver, crie um novo portfólio
                Portifolio portifolio = new Portifolio();
                portifolio.setArquivo(new File(path.toString()));
                portifolio.setLinkRepositorio(git);
                portifolio.setFreelancer(freelancer.getFreelancer()); // Associe o Portifolio ao Freelancer

                // Salvar o novo portfólio no banco de dados
                repository.save(portifolio);

                // Atualizar a referência do portfólio no freelancer
                freelancer.getFreelancer().setPortifolio(portifolio);
            }

            return ResponseEntity.ok("Arquivo recebido com sucesso");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo.");
        }
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() {
        Usuario userlogado = autenticacaoService.getUsuarioFromUsuarioDetails();
        // Busque o objeto Portifolio no banco de dados pelo ID
        System.out.println(userlogado+"AAAAAAAAAAAAAAAAAAA");
        Freelancer freelancer = repositoryFreelancer.findByEmail(userlogado.getEmail());

        Portifolio portifolio = repository.getPortifolioByFreelancer(freelancer.getIdFreelancer()).orElseThrow(()-> new RuntimeException("Portifolio nao encotrado"));

        // Pegue o caminho do arquivo
        Path path = Paths.get(portifolio.getArquivo().toURI());
        Resource resource;
        try {
            resource = (Resource) new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Arquivo não encontrado");
            }

            // Determine o tipo de conteúdo pelo nome do arquivo
            String contentType = Files.probeContentType(path);

            // Se o tipo não puder ser determinado, defina como "application/octet-stream"
            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            // Retorne o arquivo para download
            String filename = path.getFileName().toString();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao formar a URI do arquivo", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao determinar o tipo de arquivo", e);
        }
    }

    private List<Long> listaDeDownload = new ArrayList<>();

    @PostMapping("/selecionar-para-download")
    public ResponseEntity<String> selecionarParaDownload(@RequestBody List<Long> portifolioId) {
        List<Portifolio> portifolios = repository.findAllById(portifolioId);
        if (portifolios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        portifolios.forEach(portifolio -> listaDeDownload.add(portifolio.getIdPortifolio()));
        return ResponseEntity.ok("Portfólio adicionado à lista de download");
    }
    @PostMapping("/remover-da-selecao")
    public ResponseEntity<String> removerDaSelecao(@RequestBody List<Long> portifolioId) {
       // faça esse endoint receber uma lista de IDs a serem tirados da lista de donwloads
        portifolioId.forEach(id -> listaDeDownload.remove(id));
        return ResponseEntity.ok("Portfólios removido da lista de download");
    }

    @GetMapping("/baixar-selecionados")
    public ResponseEntity<?> baixarSelecionados() throws IOException {
        if (listaDeDownload.isEmpty()) {
            return ResponseEntity.badRequest().body("Não há portfólios selecionados para download");
        }

        // Criar um arquivo ZIP temporário
        File zipFile = File.createTempFile("portfolios-", ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (Long portifolioId : listaDeDownload) {
                Portifolio portifolio = repository.findById(portifolioId)
                        .orElseThrow(() -> new RuntimeException("Portfólio não encontrado: " + portifolioId));
                File file = portifolio.getArquivo();
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }

        // Limpar a lista de download após a criação do ZIP
        listaDeDownload.clear();

        // Preparar o recurso para download
        Path zipPath = zipFile.toPath();
        Resource resource = new UrlResource(zipPath.toUri());
        String filename = zipPath.getFileName().toString();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }






}
