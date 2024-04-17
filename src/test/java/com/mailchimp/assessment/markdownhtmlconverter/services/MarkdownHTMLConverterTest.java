package com.mailchimp.assessment.markdownhtmlconverter.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MarkdownHTMLConverterTest {

    @InjectMocks
    private MarkdownHTMLConverter markdownHTMLConverter;

    @ParameterizedTest
    @MethodSource("com.mailchimp.assessment.markdownhtmlconverter.services.TestDataProvider#provideMarkdownHTMLConverterTestData")
    public void testConvertMarkdownWithGivenData(List<String> input, List<String> output) {
        List<String> response = markdownHTMLConverter.convertMarkdownLines(input);
        assertEquals(response, output);
    }
}
