package com.Legal.awareness.DigitalAwareness.blog.slung.services;

import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    private final Slugify slugify;

    public  SlugService(Slugify slugify) {
        this.slugify = slugify;
    }

    public String generateSlugTitle(String title) {
        return slugify.slugify(title);
    }
}
