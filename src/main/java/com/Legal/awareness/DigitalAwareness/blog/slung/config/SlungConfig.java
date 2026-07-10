package com.Legal.awareness.DigitalAwareness.blog.slung.config;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlungConfig {



    @Bean
    public Slugify  slugify() {
        return Slugify.builder()
                .lowerCase(true)
                .transliterator(true)
                .build();
    }


}
