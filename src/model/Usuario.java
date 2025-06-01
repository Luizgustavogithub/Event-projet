package com.eventosapp.model;

import java.util.Objects;

/**
 * Representa um usuário no sistema.
 * O email é usado como identificador único.
 */
public class Usuario {
    private String nome;
    private String email; // Identificador único
    private String cidade;
    // Os eventos em que o usuário está inscrito são gerenciados pela lista de participantes em cada Evento.

    public Usuario(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Usuário: " + nome + " (Email: " + email + ", Cidade: " + cidade + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email); // Compara usuários pelo email
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
