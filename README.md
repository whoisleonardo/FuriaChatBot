# FuriaBot

**FuriaBot** é um bot para Telegram criado para oferecer informações sobre o time de eSports **FURIA**. Ele utiliza a API do Telegram e dados de um arquivo JSON para responder comandos e interações dos usuários.

## Funcionalidades

- **Próximo Jogo**: Exibe informações sobre o próximo jogo da FURIA.
- **Lineup**: Lista os jogadores atuais do time.
- **Última Partida**: Mostra o resultado da última partida jogada.
- **Títulos**: Exibe os títulos conquistados pela FURIA.
- **Redes Sociais**: Fornece links para as redes sociais oficiais da FURIA.
- **Mais Informações**: Apresenta um link para detalhes adicionais sobre o time.

## Pré-requisitos

Antes de iniciar o projeto, certifique-se de ter:

- Java 17 ou superior
- Maven (para gerenciamento de dependências)
- Conta no Telegram com um bot criado via [BotFather](https://core.telegram.org/bots#botfather)
- Token do bot gerado pelo BotFather

## Configuração

1. Clone o repositório:
   ```bash
   git clone <link-do-repositório>
   cd <nome-do-projeto>
   ```

2. Configure o token do bot:
   
   Crie uma variável de ambiente chamada `TELEGRAM_BOT_TOKEN` com o valor do seu token.

   - **Windows** (cmd):
     ```cmd
     set TELEGRAM_BOT_TOKEN=SEU_TOKEN
     ```
   - **Linux/Mac** (bash):
     ```bash
     export TELEGRAM_BOT_TOKEN=SEU_TOKEN
     ```

3. Verifique o arquivo JSON:
   
   Certifique-se de que o arquivo `jogos.json` esteja no diretório `api/` e contenha os dados necessários para funcionamento.

## Execução

1. Compile o projeto:
   ```bash
   mvn clean package
   ```

2. Execute o bot:
   ```bash
   mvn spring-boot:run
   ```

3. Acesse o Telegram e envie o comando `Ola` para iniciar a interação com o FuriaBot.

## Estrutura do Projeto

### Formato do Arquivo JSON

O arquivo `jogos.json` contém as informações usadas pelo bot. Exemplo de estrutura:

```json
{
    "matches": [
      {
        "team1": "FURIA",
        "team2": "The MongolZ",
        "date": "2025-04-09T09:50:00Z"
      }
    ],
    "lineup": [
      "KSCERATO",
      "yuurih",
      "YEKINDAR",
      "FalleN",
      "molodoy"
    ],
    "last_match": {
      "team1": "FURIA",
      "team2": "Virtus.pro",
      "score": "0-2",
      "date": "2025-04-08T06:05:00Z"
    },
    "titles": [
      "DreamHack Masters Spring 2020",
      "ESL Pro League Season 12",
      "IEM New York 2020",
      "Elisa Masters Espoo 2023"
    ],
    "socials": {
      "x": "https://x.com/FURIA",
      "instagram": "https://www.instagram.com/furiagg/",
      "youtube": "https://youtube.com/furiagg"
    },
    "more_info": "https://www.hltv.org/team/8297/furia"
  }
```

### Comandos Disponíveis

- `Ola`: Inicia a interação e apresenta os botões:
  - **Próximo Jogo**
  - **Lineup**
  - **Última Partida**
  - **Títulos**
  - **Redes Sociais**
  - **Mais Informações**

## Logs e Depuração

O bot gera logs no terminal para facilitar o monitoramento e a depuração. Exemplo de log:

```
[INFO] Bot iniciado com sucesso!
[INFO] Comando recebido: Ola
```

## Contribuindo

Contribuições são bem-vindas! Para colaborar:

1. Faça um fork do projeto.
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b minha-nova-feature
   ```
3. Faça os commits das alterações:
   ```bash
   git commit -m 'Minha nova feature'
   ```
4. Envie para o seu fork:
   ```bash
   git push origin minha-nova-feature
   ```
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---
