package com.eventosapp.service;

import com.eventosapp.model.CategoriaEvento;
import com.eventosapp.model.Evento;
import com.eventosapp.model.Usuario;
import com.eventosapp.repository.GerenciadorDados;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe de serviço que gerencia a lógica de negócios para usuários e eventos.
 */
public class SistemaEventos {
    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private Usuario usuarioLogado;
    private final GerenciadorDados gerenciadorDados;

    private static final String ARQUIVO_USUARIOS = "users.data";
    private static final String ARQUIVO_EVENTOS = "events.data";
    private static final long DURACAO_PADRAO_EVENTO_HORAS = 3; // Duração assumida para um evento

    public SistemaEventos() {
        this.gerenciadorDados = new GerenciadorDados();
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
    }

    /**
     * Inicializa o sistema carregando dados dos arquivos.
     */
    public void inicializarSistema() {
        this.usuarios = gerenciadorDados.carregarUsuarios(ARQUIVO_USUARIOS);
        this.eventos = gerenciadorDados.carregarEventos(ARQUIVO_EVENTOS);
        System.out.println(usuarios.size() + " usuário(s) carregado(s).");
        System.out.println(eventos.size() + " evento(s) carregado(s).");
    }

    /**
     * Finaliza o sistema salvando os dados atuais nos arquivos.
     */
    public void finalizarSistema() {
        gerenciadorDados.salvarUsuarios(usuarios, ARQUIVO_USUARIOS);
        gerenciadorDados.salvarEventos(eventos, ARQUIVO_EVENTOS);
        System.out.println("Dados salvos. Sistema finalizado.");
    }

    // --- Gerenciamento de Usuários ---
    public Usuario cadastrarUsuario(String nome, String email, String cidade) {
        if (usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            System.out.println("Erro: Email já cadastrado.");
            return null;
        }
        Usuario novoUsuario = new Usuario(nome, email, cidade);
        usuarios.add(novoUsuario);
        System.out.println("Usuário " + nome + " cadastrado com sucesso!");
        return novoUsuario;
    }

    public boolean loginUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
        if (usuarioOpt.isPresent()) {
            this.usuarioLogado = usuarioOpt.get();
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + ".");
            return true;
        }
        System.out.println("Erro: Email não encontrado ou senha incorreta (senha não implementada).");
        this.usuarioLogado = null;
        return false;
    }

    public void logoutUsuario() {
        if (this.usuarioLogado != null) {
            System.out.println(this.usuarioLogado.getNome() + " deslogado.");
            this.usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Optional<Usuario> getUsuarioPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }


    // --- Gerenciamento de Eventos ---
    public Evento cadastrarEvento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        Evento novoEvento = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(novoEvento);
        System.out.println("Evento '" + nome + "' cadastrado com sucesso!");
        return novoEvento;
    }

    public Optional<Evento> getEventoPorId(UUID id) {
        return eventos.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public boolean confirmarPresenca(UUID eventoId) {
        if (usuarioLogado == null) {
            System.out.println("Erro: Nenhum usuário logado para confirmar presença.");
            return false;
        }
        Optional<Evento> eventoOpt = getEventoPorId(eventoId);
        if (eventoOpt.isPresent()) {
            Evento evento = eventoOpt.get();
            if (evento.adicionarParticipante(usuarioLogado.getEmail())) {
                System.out.println("Presença confirmada no evento: " + evento.getNome());
                return true;
            } else {
                System.out.println("Você já está participando deste evento.");
                return false;
            }
        }
        System.out.println("Erro: Evento não encontrado com ID: " + eventoId);
        return false;
    }

    public boolean cancelarPresenca(UUID eventoId) {
        if (usuarioLogado == null) {
            System.out.println("Erro: Nenhum usuário logado para cancelar presença.");
            return false;
        }
        Optional<Evento> eventoOpt = getEventoPorId(eventoId);
        if (eventoOpt.isPresent()) {
            Evento evento = eventoOpt.get();
            if (evento.removerParticipante(usuarioLogado.getEmail())) {
                System.out.println("Presença cancelada no evento: " + evento.getNome());
                return true;
            } else {
                System.out.println("Você não estava participando deste evento.");
                return false;
            }
        }
        System.out.println("Erro: Evento não encontrado com ID: " + eventoId);
        return false;
    }

    public List<Evento> getEventosConfirmadosPeloUsuarioLogado() {
        if (usuarioLogado == null) {
            return new ArrayList<>();
        }
        return eventos.stream()
                .filter(e -> e.isParticipante(usuarioLogado.getEmail()))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    public List<Evento> getTodosEventos() {
        return new ArrayList<>(eventos); // Retorna uma cópia para evitar modificação externa
    }

    public List<Evento> getEventosFuturosOrdenados() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream()
                .filter(e -> e.getHorario().isAfter(agora))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    public List<Evento> getEventosOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream()
                .filter(e -> e.getHorario().isBefore(agora) &&
                        agora.isBefore(e.getHorario().plusHours(DURACAO_PADRAO_EVENTO_HORAS)))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    public List<Evento> getEventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        return eventos.stream()
                .filter(e -> e.getHorario().plusHours(DURACAO_PADRAO_EVENTO_HORAS).isBefore(agora))
                .sorted(Comparator.comparing(Evento::getHorario).reversed()) // Mais recentes primeiro
                .collect(Collectors.toList());
    }
}
