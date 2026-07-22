package com.Legal.awareness.DigitalAwareness.blog.service;

import com.Legal.awareness.DigitalAwareness.blog.dto.ClassificationRequest;
import com.Legal.awareness.DigitalAwareness.blog.dto.ClassificationResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.CreateBlog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BlogClassificationService {

    private final RestClient restClient;

    public BlogClassificationService(
            RestClient.Builder builder,
            @Value("${classification.url}") String baseUrl
    ) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public boolean classify(CreateBlog createBlog) {

        ClassificationRequest request =
                new ClassificationRequest(
                        createBlog.getTitle(),
                        createBlog.getContent()
                );
        ClassificationResponse response = restClient.post()
                .uri("/classify")
                .body(request)
                .retrieve()
                .body(ClassificationResponse.class);
        return response != null && response.isLegal();
    }

}