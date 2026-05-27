package com.Legal.awareness.DigitalAwareness.service;

import com.Legal.awareness.DigitalAwareness.entity.Content;
import com.Legal.awareness.DigitalAwareness.entity.GeminiRequest;
import com.Legal.awareness.DigitalAwareness.entity.Part;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AiService {
    private final WebClient webClient;
    private final String API_KEY="";

    public AiService() {
        this.webClient= WebClient.builder().baseUrl("https://generativelanguage.googleapis.com").build();
    }
    public String getReply(String prompt){
        Part part=new Part(prompt);
        Content content=new Content(List.of(part));
        GeminiRequest geminiRequest=new GeminiRequest(List.of(content));

        String response=webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-1.5-flash:generateContent")
                        .queryParam("key",API_KEY)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(geminiRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}
