package com.Legal.awareness.DigitalAwareness.chat.Controller;

import com.Legal.awareness.DigitalAwareness.chat.entity.prompt;
import com.Legal.awareness.DigitalAwareness.chat.entity.reply;
import com.Legal.awareness.DigitalAwareness.chat.service.PromptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt")
public class PromptController {
    //    @Autowired
    private final PromptService promptService;

    public PromptController(
            PromptService promptService
    ) {
        this.promptService = promptService;
    }

    @PostMapping
    public ResponseEntity<reply> getLegalResponse(@RequestBody prompt prompt) {
        String ans = promptService.getReply(prompt.getQuery());
        return new ResponseEntity<>(new reply(ans), HttpStatus.OK);
    }
}
