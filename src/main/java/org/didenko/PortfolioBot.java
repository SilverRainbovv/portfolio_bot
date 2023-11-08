package org.didenko;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.didenko.command.AuthenticationHandler;
import org.didenko.command.Commands;
import org.didenko.command.PortfolioHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;


@RequiredArgsConstructor
//@Component
public class PortfolioBot extends TelegramLongPollingBot {

    public PortfolioBot(String botToken){
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() || update.hasCallbackQuery()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            SendMessage sendMessage = null;

            sendMessage = checkForCommand(messageText, chatId).orElse(sendMessage);

            sendMessage = checkForAuthentication(messageText, chatId).orElse(sendMessage);


            try{
                if (sendMessage != null) {
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
//            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//
//            List<InlineKeyboardButton> firstButtonRow = new ArrayList<>();
//            List<InlineKeyboardButton> secondButtonRow = new ArrayList<>();
//
//            InlineKeyboardButton button1 = new InlineKeyboardButton();
//            button1.setText("portfolio1");
//            button1.setCallbackData("A1");
//            firstButtonRow.add(button1);
//
//            InlineKeyboardButton button2 = new InlineKeyboardButton();
//            button2.setText("portfolio2");
//            button2.setCallbackData("B1");
//            secondButtonRow.add(button2);
//
//            keyboard.setKeyboard(List.of(firstButtonRow, secondButtonRow));
//
//            String chatId = update.getMessage().getChatId().toString();
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(chatId);
//            sendMessage.setText("portfolios list");
//            sendMessage.setReplyMarkup(keyboard);
//
//            try {
//                execute(sendMessage);
//            }catch (TelegramApiException e){
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public String getBotUsername() {
        return "PortfolioAssistant_bot";
    }

    public Optional<SendMessage> checkForAuthentication(String messageText, Long chatId){
        Optional<SendMessage> sendMessage = Optional.empty();

        if (messageText.contains("#email") && messageText.contains("#token")){
            Pattern emailPattern = Pattern.compile("#email=(\\S+)");
            Pattern tokenPattern = Pattern.compile("#token=(\\S+)");

            var emailMatcher = emailPattern.matcher(messageText);
            var tokenMatcher = tokenPattern.matcher(messageText);

            String email = null;
            String token = null;

            if (emailMatcher.find())
                email = emailMatcher.group(1);
            if (tokenMatcher.find())
                token = tokenMatcher.group(1);

            return AuthenticationHandler.authenticate(email, token, chatId);
        }

        return Optional.empty();

    }

    public Optional<SendMessage> checkForCommand(String maybeCommand, Long chatId){

        Optional<SendMessage> sendMessage = Optional.empty();

        if(Arrays.stream(Commands.values()).map(Commands::getCommandValue).noneMatch(c -> c.equals(maybeCommand))){
            SendMessage message = new SendMessage();
            sendMessage = Optional.of(message);
        }

        try {
            Commands command = Commands.fromString(maybeCommand);
            switch (command){
                case PORTFOLIO_LIST -> sendMessage = PortfolioHandler.handle(command, chatId);
            }
        } catch (IllegalArgumentException e){
        }

        return sendMessage;
    }

}
