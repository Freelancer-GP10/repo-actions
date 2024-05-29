package com.example.ConnecTi.Projeto.Domain.Service.TxtExporter;

import com.example.ConnecTi.Projeto.Model.Conexao;
import com.example.ConnecTi.Projeto.Model.Empresa;
import com.example.ConnecTi.Projeto.Model.Servico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxtExporter {

        private static final String HEADER_PREFIX = "H|";
        private static final String BODY_PREFIX = "C|";

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        public static void gravaRegistro(String registro, String nomeArq) {
            try (BufferedWriter saida = new BufferedWriter(new FileWriter(nomeArq, true))) {
                saida.append(registro).append("\n");
            } catch (IOException erro) {
                System.out.println("Erro ao gravar o arquivo");
                erro.printStackTrace();
            }
        }

        public static String exportHeader() {
            return HEADER_PREFIX + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "01";
        }

        public static String exportConexao(Conexao conexao) {
            return BODY_PREFIX +
                    "ID_Conexao:" + conexao.getIdConexao() +
                    "|Aceito:" + conexao.isAceito() +
                    "|DataInsercao:" + conexao.getDataInsercao().format(DATE_TIME_FORMATTER) +
                    "|FreelancerID:" + conexao.getFreelancer().getIdFreelancer() +
                    "|ServicoID:" + conexao.getServico().getIdServico() +
                    "|AvaliacaoID:" + (conexao.getAvaliacao() != null ? conexao.getAvaliacao().getIdAvaliacao() : "") +
                    "|";
        }

        public static String exportEmpresa(Empresa empresa) {
            return BODY_PREFIX +
                    "ID_Empresa:" + empresa.getIdEmpresa() +
                    "|Nome:" + empresa.getNome() +
                    "|Email:" + empresa.getEmail() +
                    "|Senha:" + empresa.getSenha() +
                    "|Ramo:" + empresa.getRamo() +
                    "|CNPJ:" + empresa.getCnpj() +
                    "|Telefone:" + empresa.getTelefone() +
                    "|EnderecoID:" + empresa.getEndereco().getIdEndereco() +
                    "|UsuarioID:" + empresa.getUsuario().getId() +
                    "|";
        }

        public static String exportServico(Servico servico) {
            return BODY_PREFIX +
                    "ID_Servico:" + servico.getIdServico() +
                    "|Nome:" + servico.getNome() +
                    "|DataInicio:" + servico.getDataInicio() +
                    "|Valor:" + servico.getValor() +
                    "|Descricao:" + servico.getDescricao() +
                    "|DataDePostagem:" + servico.getDataDePostagem().format(DATE_TIME_FORMATTER) +
                    "|Status:" + servico.getStatus() +
                    "|EspecialidadeID:" + servico.getEspecialidade().getIdEspecialidade() +
                    "|EmpresaID:" + servico.getEmpresa().getIdEmpresa() +
                    "|";
        }

}
