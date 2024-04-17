package com.mailchimp.assessment.markdownhtmlconverter.services;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MarkdownParserTest {

    @InjectMocks
    private MarkdownParser markdownParser;

    @ParameterizedTest
    @MethodSource("com.mailchimp.assessment.markdownhtmlconverter.services.TestDataProvider#provideMarkdownParserTestData")
    public void testParseMarkdownWithGivenData(String input, List<String> output) {
        List<String> response = markdownParser.parseMarkdown(input);
        assertEquals(response, output);
    }
}
