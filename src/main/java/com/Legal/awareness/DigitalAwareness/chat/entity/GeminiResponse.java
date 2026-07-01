package com.Legal.awareness.DigitalAwareness.chat.entity;

import lombok.Data;
import java.util.List;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;
}