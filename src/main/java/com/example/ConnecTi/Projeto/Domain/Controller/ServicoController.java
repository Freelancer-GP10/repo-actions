package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Dto.Servico.AtualizarServicoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Servico.CadastrarServicoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Servico.ListarServicoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Servico.Mapper.MapperServico;
import com.example.ConnecTi.Projeto.Domain.Dto.Servico.TelaPagamentoDto;
import com.example.ConnecTi.Projeto.Domain.Repository.*;
import com.example.ConnecTi.Projeto.Domain.Security.Configuration.AutenticacaoService;
import com.example.ConnecTi.Projeto.Enum.Status;
import com.example.ConnecTi.Projeto.Model.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servico")
@RequiredArgsConstructor
public class ServicoController {
    private final RepositoryServico servico;
    private final RepositoryStatuServico repositoryStatuServico;
    private final AutenticacaoService autenticacaoService;
    private final RepositoryFreelancer repositoryFreelancer;
    private final RepostioryEmpresa repositoryEmpresa;
    private final ConexaoRepository conexaoRepository;
    private Queue<Servico> filaDeServicos = new LinkedList<>();
    private Stack<Servico> pilhaDeServicosRecentes = new Stack<>();
    private Servico ultimoServicoPostado;

    public Queue<Servico> getFilaDeServicos() {
        return filaDeServicos;
    }

    public Stack<Servico> getPilhaServicosAceitos() {
        return pilhaDeServicosRecentes;
    }

    // PRA GERAR A FILA E A PILHA DE SERVIÇOS ASSIM QUE LIGAR A APLICACAO PUXANDO DO BANCO
    @PostConstruct
    public void inicializarEstruturas() {
        List<Servico> servicos = servico.findAllByOrderByDataDePostagemAsc();

        for (Servico s : servicos) {
            filaDeServicos.offer(s);
            pilhaDeServicosRecentes.push(s);
        }
    }
    @GetMapping("/pay")
    public ResponseEntity<List<TelaPagamentoDto>> pay() {
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        System.out.println("AAAAAAA");
        System.out.println(usuario.getEmail());
        Freelancer freelancer = repositoryFreelancer.findByEmail(usuario.getEmail());

        System.out.println(freelancer.getIdFreelancer());
        List<Servico> servicosPendentes = servico.findServicosPendentesByFreelancer(freelancer);

        List<TelaPagamentoDto> servicoDtos = new ArrayList<>();
        for (Servico servico : servicosPendentes) {
            TelaPagamentoDto servicoDto = new TelaPagamentoDto();
            servicoDto.setId(servico.getIdServico());
            servicoDto.setNome(servico.getNome());
            servicoDto.setPrazo(servico.getPrazo());
            servicoDto.setValor(servico.getValor());
            servicoDtos.add(servicoDto);
        }

        return ResponseEntity.ok(servicoDtos);
    }

//    @PutMapping("/sacar")
//    public ResponseEntity<TelaPagamentoDto> RealizarSaque(Double valor){
//        ResponseEntity totalCarteira = getWalletBalance();
//    }
    @GetMapping("/carteira")
    public ResponseEntity<Double> getWalletBalance() {
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Freelancer freelancer = repositoryFreelancer.findByEmail(usuario.getEmail());

        List<Servico> servicosFinalizados = servico.findServicosFinalizadosByFreelancer(freelancer);

        double totalBalance = 0.0;

        for (Servico servico : servicosFinalizados) {
            totalBalance += servico.getValor();
        }

        return ResponseEntity.ok(totalBalance);
    }

    // quando CADASTRA ELE FICA NA FILA E NA PILHA FILA PRA INSERIR FILA PRA DESFAZER
    @PostMapping
    public ResponseEntity<Servico> cadastrar(@RequestBody CadastrarServicoDto servico){
        Servico servicoSalvo =  new Servico();
        Usuario usuariologado = autenticacaoService.getUsuarioFromUsuarioDetails();
        System.out.println(usuariologado.getEmail());

       Empresa empresa = repositoryEmpresa.findByEmail(usuariologado.getEmail()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,("Não existe empresa com esse nome")));

        servicoSalvo.setNome(servico.nome());
        servicoSalvo.setDescricao(servico.descricao());
        servicoSalvo.setValor(servico.valor());
        servicoSalvo.setEmpresa(empresa);
        servicoSalvo.setStatus(Status.PENDENTE);
        servicoSalvo.setPrazo(servico.prazo());
        servicoSalvo.setDataInicio(servico.dataInicio());
        servicoSalvo.setDataDePostagem(LocalDateTime.now());

        filaDeServicos.offer(servicoSalvo); // Adiciona o serviço na fila
        pilhaDeServicosRecentes.push(servicoSalvo); // Adiciona o serviço na pilha de serviços recentes

        this.servico.save(servicoSalvo);
        return ResponseEntity.ok(servicoSalvo);
    }

    @PostMapping("finalizar-servico/{id}")
    public ResponseEntity<String> trocarStatus(@PathVariable int id){
        Servico servico = this.servico.findById((long) id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Serviço nao encontrado"));

        servico.setStatus(Status.FINALIZADO);
        this.servico.save(servico);
        return ResponseEntity.ok("Serviço finalizado com sucesso");
    }

    // ESSE METODO PROFESSORA TIRA DA PILHA E DA FILA
    @PostMapping("/desfazer-postagem")
    public ResponseEntity<String> desfazerPostagem(){
        Usuario usuarioLogado = autenticacaoService.getUsuarioFromUsuarioDetails();
        Empresa empresaLogada = repositoryEmpresa.findByEmail(usuarioLogado.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe empresa com esse nome"));

        while (!pilhaDeServicosRecentes.isEmpty()) {
            Servico servicoAtual = pilhaDeServicosRecentes.peek(); // Verifica o serviço no topo da pilha

            // Verifica se o serviço já foi aceito ou finalizado
            if (conexaoRepository.existsByServico(servicoAtual) || servicoAtual.getStatus().equals(Status.FINALIZADO)) {
                pilhaDeServicosRecentes.pop();
                continue;
            }
            // Verifica se o serviço atual pertence à empresa logada
            if (servicoAtual.getEmpresa().getIdEmpresa().equals(empresaLogada.getIdEmpresa())) {
                // Se pertence, desfaz a postagem do serviço
                pilhaDeServicosRecentes.pop();
                filaDeServicos.remove(servicoAtual);
                servico.delete(servicoAtual);
                return ResponseEntity.ok("Postagem de serviço desfeita com sucesso: " + servicoAtual.getNome());
            }
            // Se o serviço não é da empresa logada, passe para o próximo serviço
            pilhaDeServicosRecentes.pop();
        }

        return ResponseEntity.badRequest().body("Nenhuma postagem recente para desfazer ou todos os serviços recentes já foram aceitos ou finalizados.");
    }

    @GetMapping("/proximo-servico")
    public ResponseEntity<ListarServicoDto> obterProximoServico() {
        if (!filaDeServicos.isEmpty()) {
            Servico proximoServico = filaDeServicos.peek();
            ListarServicoDto servicoDto = MapperServico.fromEntity(proximoServico);
            return ResponseEntity.ok(servicoDto);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/lista-proximos-servicos")
    public ResponseEntity<List<ListarServicoDto>> listaProximoServico() {
        if (!filaDeServicos.isEmpty()) {
            List<ListarServicoDto> servicosDto = filaDeServicos.stream()
                    .map(MapperServico::fromEntity)
                    .collect(Collectors.toList());

            // Invertendo a ordem dos serviços
            Collections.reverse(servicosDto);

            return ResponseEntity.ok(servicosDto);
        }

        return ResponseEntity.noContent().build();
    }


//    @GetMapping
//    public ResponseEntity<Stack> listar(){
//      if(pilhaDeServicosRecentes.isEmpty()) {
//          return ResponseEntity.noContent().build();
//      }
//
//        return ResponseEntity.ok().body(pilhaDeServicosRecentes);
//    }
    // busque todos os serviços que estão pendentes
    @GetMapping
    public ResponseEntity<List<Servico>> listar(){
        List<Servico> servicos = this.servico.findAll();
        if (servicos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(servicos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Servico> listarPorId(@PathVariable Long id){
        Servico servico = this.servico.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Serviço nao encontrado"));
        return ResponseEntity.ok().body(servico);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Servico> deletar(@PathVariable Long id){
        this.servico.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id,
                                             @RequestBody AtualizarServicoDto servico){
        Servico servicoAtualizado = this.servico.getReferenceById(id);
        servicoAtualizado.setNome(servico.nome());
        servicoAtualizado.setDescricao(servico.descricao());
        servicoAtualizado.setValor(servico.valor());
        servicoAtualizado.setPrazo(servico.prazo());
        this.servico.save(servicoAtualizado);
        return ResponseEntity.ok().build();
    }


}
