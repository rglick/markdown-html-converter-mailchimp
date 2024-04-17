package com.mailchimp.assessment.markdownhtmlconverter.controllers;

import com.mailchimp.assessment.markdownhtmlconverter.entities.HTMLResponse;
import com.mailchimp.assessment.markdownhtmlconverter.entities.MarkdownRequest;
import com.mailchimp.assessment.markdownhtmlconverter.services.MarkdownConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MarkdownConverterController {

    @Autowired
    MarkdownConverterService markdownConverterService;

    @PostMapping("/markdown")
    public ResponseEntity<String> convert(@RequestBody MarkdownRequest request) {
        HTMLResponse response = markdownConverterService.convert(request);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_HTML)
                .body(response.toHTML());
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
