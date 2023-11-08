package org.didenko;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Collections;

@RequiredArgsConstructor
@SpringBootApplication
public class ApplicationRunner {

    public static void main(String[] args) {
        var applicationContext = SpringApplication.run(ApplicationRunner.class, args);

//        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/api/v1/hello"))
//                .method("GET", java.net.http.HttpRequest.BodyPublishers.noBody())
//                .build();
//
//        HttpResponse<String> response = null;
//        try {
//            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println();
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }


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
