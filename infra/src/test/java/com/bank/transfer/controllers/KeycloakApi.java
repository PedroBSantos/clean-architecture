package com.bank.transfer.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class KeycloakApi {

    public static String getKeycloakAccessToken(String authHost, String client, String grantType, String username, String password) {
        try {
            var encodedUrl = "client_id=" + client + "&grant_type=" + grantType + "&username=" + username + "&password="
                    + password + "&score=openid";
            var httpClient = HttpClient.newHttpClient();
            var uri = new URI(authHost);
            var httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("charset", "utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(encodedUrl))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var stringResponse = httpResponse.body();
            var mapper = new ObjectMapper();
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
            };
            var responseMap = mapper.readValue(stringResponse, typeRef);
            return responseMap.get("access_token");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
