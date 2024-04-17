package com.mailchimp.assessment.markdownhtmlconverter.services;

import com.mailchimp.assessment.markdownhtmlconverter.entities.HTMLResponse;
import com.mailchimp.assessment.markdownhtmlconverter.entities.MarkdownRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkdownConverterService {

    @Autowired
    private MarkdownParser markdownParser;

    @Autowired
    private MarkdownHTMLConverter markdownHTMLConverter;

    public HTMLResponse convert(MarkdownRequest markdown) {
        // The logic is a two-step process

        // First, parse the markdown so that we could more easily determine where to add header and p tags
        List<String> parsedMarkdown = markdownParser.parseMarkdown(markdown.getContent());

        // Second, using that parsed markdown, format the strings into html strings - this will go through and also add
        // link tags where necessary
        // TODO: add validation somehow?
        List<String> htmlLines = markdownHTMLConverter.convertMarkdownLines(parsedMarkdown);

        // Afterward, create an HTML response and send back to the controller so that it can return the response
        // back to the client
        return HTMLResponse
                .builder()
                .htmlLines(htmlLines)
                .build();
    }
}
