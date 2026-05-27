package com.Legal.awareness.DigitalAwareness.Controller;

import com.Legal.awareness.DigitalAwareness.entity.prompt;
import com.Legal.awareness.DigitalAwareness.entity.reply;
import com.Legal.awareness.DigitalAwareness.service.promptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt")
public class PromptController
{
    @Autowired
    private promptService promtService;

    @PostMapping
    public ResponseEntity<reply> getLegalResponse(@RequestBody prompt prompt){
        String ans=promtService.getReply(prompt.getQuery());
        return new ResponseEntity<>(new reply(ans), HttpStatus.OK);
    }

}
