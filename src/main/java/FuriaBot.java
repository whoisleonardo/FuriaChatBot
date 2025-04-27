import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuriaBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "FuriaCS";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN"); 
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleTextMessage(Update update) {
        String mensagem = update.getMessage().getText();

        if (mensagem.equalsIgnoreCase("ola")) {
            SendMessage mensagemParaEnviar = new SendMessage();
            mensagemParaEnviar.setChatId(update.getMessage().getChatId().toString());
            mensagemParaEnviar.setText("Olá torcedor! Escolha uma opção abaixo para começar:");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton proximoJogoButton = new InlineKeyboardButton();
            proximoJogoButton.setText("Próximo Jogo");
            proximoJogoButton.setCallbackData("proximo_jogo");
            row1.add(proximoJogoButton);
            InlineKeyboardButton lineupButton = new InlineKeyboardButton();
            lineupButton.setText("Lineup");
            lineupButton.setCallbackData("lineup");
            row1.add(lineupButton);

            List<InlineKeyboardButton> row2 = new ArrayList<>();
            InlineKeyboardButton ultimaPartidaButton = new InlineKeyboardButton();
            ultimaPartidaButton.setText("Última Partida");
            ultimaPartidaButton.setCallbackData("ultima_partida");
            row2.add(ultimaPartidaButton);
            InlineKeyboardButton titulosButton = new InlineKeyboardButton();
            titulosButton.setText("Títulos");
            titulosButton.setCallbackData("titulos");
            row2.add(titulosButton);

            List<InlineKeyboardButton> row3 = new ArrayList<>();
            InlineKeyboardButton redesSociaisButton = new InlineKeyboardButton();
            redesSociaisButton.setText("Redes Sociais");
            redesSociaisButton.setCallbackData("redes_sociais");
            row3.add(redesSociaisButton);
            InlineKeyboardButton moreInfoButton = new InlineKeyboardButton();
            moreInfoButton.setText("Mais Informações");
            moreInfoButton.setCallbackData("mais_informacoes");
            row3.add(moreInfoButton);

            rows.add(row1);
            rows.add(row2);
            rows.add(row3);
            markup.setKeyboard(rows);

            mensagemParaEnviar.setReplyMarkup(markup);

            enviarMensagem(mensagemParaEnviar);
        }
    }

    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String resposta;

        switch (callbackData) {
            case "proximo_jogo":
                resposta = buscarProximoJogo();
                break;
            case "lineup":
                resposta = buscarLineup();
                break;
            case "ultima_partida":
                resposta = buscarUltimaPartida();
                break;
            case "titulos":
                resposta = listarTitulos();
                break;
            case "redes_sociais":
                resposta = listarRedesSociais();
                break;
            case "mais_informacoes":
                resposta = maisInformacoes();
                break;
            default:
                resposta = "Opção inválida.";
                break;
        }

        enviarMensagem(update.getCallbackQuery().getMessage().getChatId().toString(), resposta);
    }

    private void enviarMensagem(SendMessage mensagemParaEnviar) {
        try {
            execute(mensagemParaEnviar);
        } catch (TelegramApiException e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarMensagem(String chatId, String texto) {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId);
        mensagem.setText(texto);
        enviarMensagem(mensagem);
    }

    private String buscarProximoJogo() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray matches = jsonObject.getAsJsonArray("matches");

            if (matches != null && matches.size() > 0) {
                JsonObject match = matches.get(0).getAsJsonObject();
                String team1 = match.get("team1").getAsString();
                String team2 = match.get("team2").getAsString();
                String date = formatarData(match.get("date").getAsString());

                return String.format("Próximo jogo: %s vs %s em %s.", team1, team2, date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Nenhum jogo encontrado.";
    }

    private String buscarLineup() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray lineup = jsonObject.getAsJsonArray("lineup");

            if (lineup == null || lineup.size() == 0) {
                return "Nenhum jogador encontrado no lineup.";
            }

            StringBuilder lineupString = new StringBuilder("Lineup atual da FURIA:\n");
            for (int i = 0; i < lineup.size(); i++) {
                lineupString.append("- ").append(lineup.get(i).getAsString()).append("\n");
            }

            return lineupString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao buscar o lineup.";
        }
    }

    private String buscarUltimaPartida() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject lastMatch = jsonObject.getAsJsonObject("last_match");

            String team1 = lastMatch.get("team1").getAsString();
            String team2 = lastMatch.get("team2").getAsString();
            String score = lastMatch.get("score").getAsString();
            String date = formatarData(lastMatch.get("date").getAsString());

            return String.format("Última partida: %s %s x %s (%s).", team1, score, team2, date);
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao buscar a última partida.";
        }
    }

    private String listarTitulos() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray titles = jsonObject.getAsJsonArray("titles");

            StringBuilder response = new StringBuilder("Títulos da FURIA:\n");
            for (int i = 0; i < titles.size(); i++) {
                response.append("- ").append(titles.get(i).getAsString()).append("\n");
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao listar os títulos.";
        }
    }

    private String listarRedesSociais() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject socials = jsonObject.getAsJsonObject("socials");

            return String.format(
                "Siga a FURIA nas redes sociais:\n- X: %s\n- Instagram: %s\n- YouTube: %s",
                socials.get("x").getAsString(),
                socials.get("instagram").getAsString(),
                socials.get("youtube").getAsString()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao listar as redes sociais.";
        }
    }

    private String maisInformacoes() {
        try (FileReader reader = new FileReader("src/api/jogos.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String moreInfo = jsonObject.get("more_info").getAsString();

            return "Para mais informações, visite a página da Fúria na HLTV: " + moreInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao buscar mais informações.";
        }
    }

    private String formatarData(String isoDate) {
        ZonedDateTime dateTime = ZonedDateTime.parse(isoDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}