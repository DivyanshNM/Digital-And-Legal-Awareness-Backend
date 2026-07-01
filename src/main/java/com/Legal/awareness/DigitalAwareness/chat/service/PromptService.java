package com.Legal.awareness.DigitalAwareness.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptService {
//    @Autowired
    private final AiService AiService;

    public PromptService(AiService AiService) {
        this.AiService = AiService;
    }


    public String getReply(String prompt){
        if((prompt==null) || prompt.isBlank()){
            throw new RuntimeException("Prompt cannot be empty");
        }
        return AiService.getReply(prompt);
    }
}
