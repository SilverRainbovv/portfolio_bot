package org.didenko;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@SpringBootApplication
public class ApplicationRunner {

    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(ApplicationRunner.class, args);
        var portfolioBot = applicationContext.getBean("portfolioBot", PortfolioBot.class);

        try {
            var bot = applicationContext.getBean("portfolioBot", PortfolioBot.class);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(portfolioBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }

}
