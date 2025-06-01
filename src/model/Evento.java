package com.eventosapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Representa um evento no sistema.
 */
public class Evento {
    private UUID id;
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime horario;
    private String descricao;
    private List<String> participantesUserEmails; // Lista de emails dos usuários participantes

    public Evento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantesUserEmails = new ArrayList<>();
    }

    // Construtor para carregar do arquivo, incluindo ID e participantes
    public Evento(UUID id, String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao, List<String> participantesUserEmails) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantesUserEmails = new ArrayList<>(participantesUserEmails); // Garante que a lista seja mutável
    }


    // Getters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<String> getParticipantesUserEmails() {
        return participantesUserEmails;
    }

    // Setters (geralmente para edição, se necessário)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setCategoria(CategoriaEvento categoria) {
        this.categoria = categoria;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Métodos para gerenciar participantes
    public boolean adicionarParticipante(String userEmail) {
        if (!participantesUserEmails.contains(userEmail)) {
            participantesUserEmails.add(userEmail);
            return true;
        }
        return false;
    }

    public boolean removerParticipante(String userEmail) {
        return participantesUserEmails.remove(userEmail);
    }

    public boolean isParticipante(String userEmail) {
        return participantesUserEmails.contains(userEmail);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Evento: " + nome + "\n" +
                "  ID: " + id + "\n" +
                "  Endereço: " + endereco + "\n" +
                "  Categoria: " + categoria.getDescricao() + "\n" +
                "  Horário: " + horario.format(formatter) + "\n" +
                "  Descrição: " + descricao + "\n" +
                "  Participantes (Emails): " + (participantesUserEmails.isEmpty() ? "Nenhum" : String.join(", ", participantesUserEmails));
    }

    public String toStringBasico() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("ID: %s | %s (%s) - %s em %s",
                id.toString().substring(0,8), // ID curto para exibição
                nome,
                categoria.getDescricao(),
                horario.format(formatter),
                endereco);
    }
}
