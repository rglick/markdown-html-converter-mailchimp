package com.mailchimp.assessment.markdownhtmlconverter.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HTMLResponse {
    private List<String> htmlLines;

    public String toHTML() {
        return String.join("\n", htmlLines);
    }
}
