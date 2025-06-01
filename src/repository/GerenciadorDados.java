package com.eventosapp.repository;

import com.eventosapp.model.CategoriaEvento;
import com.eventosapp.model.Evento;
import com.eventosapp.model.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Gerencia o armazenamento e carregamento de dados de usuários e eventos em arquivos.
 */
public class GerenciadorDados {
    private static final String DELIMITER = "##DELIMITER##";
    private static final String PARTICIPANT_LIST_DELIMITER = ",";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // --- Métodos para Usuários ---

    /**
     * Salva a lista de usuários em um arquivo.
     * @param usuarios Lista de usuários a serem salvos.
     * @param caminhoArquivo Caminho do arquivo onde os usuários serão salvos.
     */
    public void salvarUsuarios(List<Usuario> usuarios, String caminhoArquivo) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivo), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Usuario usuario : usuarios) {
                String linha = String.join(DELIMITER,
                        usuario.getEmail(), // Usando email como ID
                        usuario.getNome(),
                        usuario.getCidade());
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários no arquivo " + caminhoArquivo + ": " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de usuários de um arquivo.
     * @param caminhoArquivo Caminho do arquivo de onde os usuários serão carregados.
     * @return Lista de usuários carregados. Retorna lista vazia em caso de erro ou arquivo não encontrado.
     */
    public List<Usuario> carregarUsuarios(String caminhoArquivo) {
        List<Usuario> usuarios = new ArrayList<>();
        if (!Files.exists(Paths.get(caminhoArquivo))) {
            System.out.println("Arquivo de usuários não encontrado: " + caminhoArquivo + ". Iniciando com lista vazia.");
            return usuarios;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(caminhoArquivo), StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(DELIMITER, -1); // -1 para incluir campos vazios no final
                if (partes.length >= 3) {
                    try {
                        Usuario usuario = new Usuario(partes[1], partes[0], partes[2]);
                        usuarios.add(usuario);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha de usuário (ignorando): " + linha + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha de usuário malformada (ignorando): " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários do arquivo " + caminhoArquivo + ": " + e.getMessage());
        }
        return usuarios;
    }

    // --- Métodos para Eventos ---

    /**
     * Salva a lista de eventos em um arquivo.
     * @param eventos Lista de eventos a serem salvos.
     * @param caminhoArquivo Caminho do arquivo onde os eventos serão salvos.
     */
    public void salvarEventos(List<Evento> eventos, String caminhoArquivo) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivo), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Evento evento : eventos) {
                String participantesStr = evento.getParticipantesUserEmails().isEmpty() ?
                        "" : String.join(PARTICIPANT_LIST_DELIMITER, evento.getParticipantesUserEmails());
                String linha = String.join(DELIMITER,
                        evento.getId().toString(),
                        evento.getNome(),
                        evento.getEndereco(),
                        evento.getCategoria().name(), // Salva o nome do enum
                        evento.getHorario().format(DATE_TIME_FORMATTER),
                        evento.getDescricao(),
                        participantesStr);
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos no arquivo " + caminhoArquivo + ": " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de eventos de um arquivo.
     * @param caminhoArquivo Caminho do arquivo de onde os eventos serão carregados.
     * @return Lista de eventos carregados. Retorna lista vazia em caso de erro ou arquivo não encontrado.
     */
    public List<Evento> carregarEventos(String caminhoArquivo) {
        List<Evento> eventos = new ArrayList<>();
        if (!Files.exists(Paths.get(caminhoArquivo))) {
            System.out.println("Arquivo de eventos não encontrado: " + caminhoArquivo + ". Iniciando com lista vazia.");
            return eventos;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(caminhoArquivo), StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(DELIMITER, -1); // -1 para incluir campos vazios no final
                if (partes.length >= 6) { // Mínimo de 6 campos (ID, Nome, End, Cat, Horario, Desc)
                    try {
                        UUID id = UUID.fromString(partes[0]);
                        String nome = partes[1];
                        String endereco = partes[2];
                        CategoriaEvento categoria = CategoriaEvento.valueOf(partes[3]);
                        LocalDateTime horario = LocalDateTime.parse(partes[4], DATE_TIME_FORMATTER);
                        String descricao = partes[5];

                        List<String> participantes = new ArrayList<>();
                        if (partes.length > 6 && !partes[6].isEmpty()) {
                            participantes.addAll(Arrays.asList(partes[6].split(PARTICIPANT_LIST_DELIMITER)));
                        }

                        Evento evento = new Evento(id, nome, endereco, categoria, horario, descricao, participantes);
                        eventos.add(evento);
                    } catch (IllegalArgumentException | DateTimeParseException e) {
                        System.err.println("Erro ao processar dados de evento na linha (ignorando): " + linha + " - " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Erro inesperado ao processar linha de evento (ignorando): " + linha + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha de evento malformada (ignorando): " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do arquivo " + caminhoArquivo + ": " + e.getMessage());
        }
        return eventos;
    }
}
