package org.it4innov;

import com.vdurmont.emoji.EmojiParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.it4innov.config.TelegramConfig;
import org.it4innov.entity.MessageReceived;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.stickers.GetCustomEmojiStickers;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TelegramBot extends TelegramLongPollingBot{

    public final TelegramConfig config;

    private static final String HELP_TEXT = """
        Ce Bot est crée pour présenter le framework Quarkus
        Je m'appele MkutanoTalk_Bot et je suis la pour vous guider:
        
          /start pour commencer
        
          /question pour nous poser des questions techniques
                    
          /help pour avoir de l'aide
        """;

    public TelegramBot(TelegramConfig config) {
        this.config = config;
        initBotCommand();
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            String username = update.getMessage().getChat().getFirstName();
            Log.info("message "+ messageText);
            long chatId = update.getMessage().getChatId();
            GetCustomEmojiStickers.builder().customEmojiId("sds").build();

            String thanksMessage = EmojiParser.parseToUnicode("Merci pour votre retour " + username +" :blush:");

            try {
                switch (messageText) {
                    case "/start" -> startCommandReceived(chatId, username);
                    case "/question" -> questionCommandReceived(chatId, username);
                    case "/help" -> sendMessage(chatId, HELP_TEXT);
                    default -> {
                        sendMessage(chatId, thanksMessage);
                        MessageReceived message = MessageReceived.builder()
                                .messageText(messageText)
                                .dateTime(LocalDateTime.now())
                                .firstName(username).build();
                        message.persistAndFlush();
                    }
                }
            } catch (TelegramApiException e) {
                Log.info("Error when process sending response message to telegram");
                throw new RuntimeException(e);
            }
        }

    }

    private void startCommandReceived(long chatId, String username) throws TelegramApiException {
        String response = username + " bienvenue sur MKutano Talks, veuillez nous proposer un theme pour notre prochaine rencontre.";
        sendMessage(chatId, response);
    }

    private void questionCommandReceived(long chatId, String username) throws TelegramApiException {
        String response = username + " Posez toutes vos questions et nous les repondrons lors de la prochaine rencontre. ";
        sendMessage(chatId, response);
    }

    private void sendMessage(long chatId, String messageText) throws TelegramApiException {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(chatId);
        messageToSend.setText(messageText);

        execute(messageToSend);
    }

    private void initBotCommand() {
        List<BotCommand> listBotCommand = new ArrayList<>();
        listBotCommand.add(new BotCommand("/start", "Initiation de la conversation"));
        listBotCommand.add(new BotCommand("/question", "Rédiger votre question et nous vous repondrons trés vite"));
        listBotCommand.add(new BotCommand("/help", "Manuel du MkutanoTalkBot"));
        try {
            execute(new SetMyCommands(listBotCommand, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            Log.info("erreur lors de l'initialisation du bot");
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getBotUsername() {
        return config.chatName();
    }

    @Override
    public String getBotToken() {
        return config.token();
    }

    @Override
    public void onRegister() {
        super.onRegister();

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
