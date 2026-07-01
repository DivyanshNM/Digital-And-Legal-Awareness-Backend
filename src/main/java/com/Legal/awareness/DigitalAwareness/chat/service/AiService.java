package com.Legal.awareness.DigitalAwareness.chat.service;

import com.Legal.awareness.DigitalAwareness.chat.entity.Content;
import com.Legal.awareness.DigitalAwareness.chat.entity.GeminiRequest;
import com.Legal.awareness.DigitalAwareness.chat.entity.GeminiResponse;
import com.Legal.awareness.DigitalAwareness.chat.entity.Part;
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

        GeminiResponse geminiResponse =webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash-lite:generateContent")
                        .queryParam("key",API_KEY)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(geminiRequest)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();

        if (geminiResponse == null
                || geminiResponse.getCandidates() == null
                || geminiResponse.getCandidates().isEmpty()) {
            return "No response generated";
        }
        return geminiResponse.getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }
}
