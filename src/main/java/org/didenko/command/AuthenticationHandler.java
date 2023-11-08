package org.didenko.command;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class AuthenticationHandler implements CommandHandler{

    public static Optional<SendMessage> authenticate(String email, String token, Long chatId){

        SendMessage message = new SendMessage();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/telegram/authenticate"))
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .header("email", email)
                .header("token", token)
                .header("chatId", chatId.toString())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            message.setText(response.body());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return Optional.of(message);
    };

}
