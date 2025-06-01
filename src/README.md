# Sistema de Gerenciamento de Eventos Urbanos em Java

Este projeto é um sistema de console em Java para cadastro, consulta e notificação de eventos que ocorrem em uma cidade. Ele permite que usuários se cadastrem, criem eventos, confirmem presença e visualizem eventos futuros, atuais e passados.

## Funcionalidades Principais

* **Gerenciamento de Usuários:**
    * Cadastro de novos usuários (nome, email, cidade).
    * Login e Logout de usuários (email como identificador).

* **Gerenciamento de Eventos:**
    * Cadastro de novos eventos com os atributos: nome, endereço, categoria, horário e descrição.
    * Categorias de eventos predefinidas (Festas, Esportes, Shows, etc.).

* **Participação em Eventos:**
    * Usuários logados podem consultar eventos e confirmar presença.
    * Visualizar eventos com presença confirmada.
    * Cancelar participação em eventos.

* **Consulta e Notificação de Eventos:**
    * Ordenação de eventos por proximidade de horário.
    * Identificação de eventos que estão ocorrendo no momento.
    * Listagem de eventos futuros.
    * Listagem de eventos passados.

* **Persistência de Dados:**
    * As informações de usuários são salvas no arquivo `users.data`.
    * As informações dos eventos são salvas no arquivo `events.data`.
    * Os dados são carregados automaticamente ao iniciar o programa e salvos ao finalizar.

* **Interface de Usuário:**
    * Interação via console (linha de comando).

## Tecnologias Utilizadas

* **Linguagem:** Java (JDK 24)
* **Paradigma:** Orientação a Objetos

* **Bibliotecas Principais:**
    * Java Time API (java.time) para manipulação de datas e horários.
    * Java NIO (java.nio.file) para manipulação de arquivos.
    * Java Util (java.util) para coleções, UUID, etc.

## Estrutura do Projeto

O projeto está organizado nos seguintes pacotes e classes principais:

* **`com.eventosapp`**: Pacote raiz da aplicação.
    * `Main.java`: Classe principal que contém a interface de usuário (menu do console) e o método `main`.
* **`com.eventosapp.model`**: Contém as classes de modelo (entidades).
    * `Usuario.java`: Representa um usuário.
    * `Evento.java`: Representa um evento.
    * `CategoriaEvento.java`: Enumeração para as categorias de eventos.
* **`com.eventosapp.service`**: Contém as classes de serviço com a lógica de negócios.
    * `SistemaEventos.java`: Orquestra as operações do sistema, gerenciando usuários e eventos.
* **`com.eventosapp.repository`**: Contém as classes responsáveis pela persistência de dados.
    * `GerenciadorDados.java`: Lida com a leitura e escrita dos dados de usuários e eventos nos arquivos de texto.

## Pré-requisitos

* Java Development Kit (JDK) versão 11 ou superior instalado e configurado no sistema.

## Como Compilar e Executar

1.  **Clone o Repositório (ou baixe os arquivos):**
    ```bash
    git clone [https://github.com/Luizgustavogithub/Event-projet.git](https://github.com/Luizgustavogithub/Event-projet.git)
    cd Event-projet/
```

2.  **Estrutura de Pastas Esperada:**
    Certifique-se de que os arquivos `.java` estejam na seguinte estrutura dentro de uma pasta `src`:
    ```
    seu-repositorio-eventos/
    ├── src/
    │   └── com/
    │       └── eventosapp/
    │           ├── Main.java
    │           ├── model/
    │           │   ├── CategoriaEvento.java
    │           │   ├── Evento.java
    │           │   └── Usuario.java
    │           ├── repository/
    │           │   └── GerenciadorDados.java
    │           └── service/
    │               └── SistemaEventos.java
    └── README.md
    ```

3.  **Compilar o Projeto:**
    Abra um terminal ou prompt de comando na raiz do projeto (`seu-repositorio-eventos/`) e execute o comando de compilação. Este comando criará uma pasta `out` para os arquivos `.class`.
    ```bash
    javac -d out src/com/eventosapp/Main.java src/com/eventosapp/model/*.java src/com/eventosapp/repository/*.java src/com/eventosapp/service/*.java
    ```
    Se a pasta `out` não existir, o compilador a criará.

4.  **Executar o Projeto:**
    Após a compilação bem-sucedida, execute o programa a partir da raiz do projeto:
    ```bash
    java -cp out com.eventosapp.Main
    ```
    Isso iniciará o sistema de gerenciamento de eventos no console.

## Arquivos de Dados

* `users.data`: Armazena os dados dos usuários cadastrados.
* `events.data`: Armazena os dados dos eventos cadastrados.

Estes arquivos serão criados automaticamente no diretório raiz do projeto (`seu-repositorio-eventos/`) na primeira vez que o programa for executado e precisar salvar dados, ou quando dados forem salvos.

## Possíveis Melhorias Futuras

* Implementar um sistema de senhas para usuários.
* Adicionar funcionalidade de edição para usuários e eventos.
* Permitir que o criador do evento o gerencie (editar, excluir).
* Adicionar filtros mais avançados para busca de eventos (por cidade do usuário, por exemplo).
* Melhorar a interface do usuário (talvez migrar para uma GUI ou Web).
* Implementar testes unitários.
* Adicionar um limite de participantes para os eventos.

---
