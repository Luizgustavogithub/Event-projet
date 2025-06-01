package com.eventosapp.model;

/**
 * Enumeração para as categorias de eventos.
 */
public enum CategoriaEvento {
    FESTA("Festa"),
    ESPORTE("Evento Esportivo"),
    SHOW("Show Musical"),
    CONFERENCIA("Conferência"),
    TEATRO("Peça de Teatro"),
    WORKSHOP("Workshop"),
    GASTRONOMIA("Evento Gastronômico"),
    OUTRO("Outro");

    private final String descricao;

    CategoriaEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna uma lista de todas as categorias para exibição.
     * @return String formatada com as categorias.
     */
    public static String listarCategorias() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values().length; i++) {
            sb.append(i + 1).append(". ").append(values()[i].getDescricao()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Retorna uma categoria pelo seu índice (base 1).
     * @param indice O índice da categoria.
     * @return A CategoriaEvento correspondente ou null se o índice for inválido.
     */
    public static CategoriaEvento getPorIndice(int indice) {
        if (indice > 0 && indice <= values().length) {
            return values()[indice - 1];
        }
        return null;
    }
}
