package com.mailchimp.assessment.markdownhtmlconverter.services;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MarkdownHTMLConverter {

    // Per HW rules, we are only produce up to an H6 tag
    private static final Integer MAX_HEADER_COUNT = 6;

    /**
     * Convert our list of markdown strings into a list of html strings
     *
     * @param parsedMarkdown
     * @return List<String> list of html strings
     */
    public List<String> convertMarkdownLines(List<String> parsedMarkdown) {
        // Using our convert helper function as the mapper, convert each line into html
        // and return the collected list
        return parsedMarkdown
                .stream()
                .map(this::convertMarkdown)
                .collect(Collectors.toList());
    }

    /**
     * Helper function to focus on each line one by one
     *
     * @param markdownLine
     * @return String newly converted html string
     */
    private String convertMarkdown(String markdownLine) {
        // first determine if this needs a p tag or a h<x> tag (where x is the number header tag we want)
        Integer currentIndex = 0;
        while (markdownLine.charAt(currentIndex) == '#' && currentIndex < MAX_HEADER_COUNT) {
            currentIndex++;
        }

        // two cases it can be a p tag: if there are no #s to start or if the first character after the # is not a " "
        String tag = currentIndex == 0 || markdownLine.charAt(currentIndex) != ' ' ? "p" : "h" + currentIndex;
        StringBuilder outputLine = new StringBuilder();
        outputLine.append("<").append(tag).append(">");

        // if we do have a header tag, we want to move currentIndex up to first non-whitespace index
        if (tag.charAt(0) == 'h') {
            while (Character.isWhitespace(markdownLine.charAt(currentIndex))) {
                currentIndex++;
            }
        } else {
            // if we don't we want to make sure we captured the #'s that we didn't know were part of a p tag yet
            for (int i = 0; i < currentIndex; i++) {
                outputLine.append("#");
            }
        }

        // TODO: also need to check for <hx>, <p>, <a> tags in plain text as well

        // we then want to go through the rest of the line to discover if there are any anchor tags
        // conveniently, the header number is also the index we can start at to go through the rest of the line
        // we will use a regex expression to find these links
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]\\(([^\\(\\)]+)\\)");
        Matcher matcher = pattern.matcher(markdownLine);

        // as there can be multiple links within a line, we will loop through each match
        while (matcher.find()) {
            // we want to grab the start and end index of the match as well need to append from currentIndex to
            // the start of the link and then move that index up to end index for the next potential match

            Integer startLinkIndex = matcher.start();
            Integer endLinkIndex = matcher.end();

            // first lets check though if the link is actually supposed to appear in plain text, i.e. is it escaped
            // as links in plain text are escaped by \ as well
            if (markdownLine.charAt(startLinkIndex - 1) == '\\') {
                // since it's escaped, just append everything up to endLinkIndex
                outputLine.append(markdownLine, currentIndex, endLinkIndex);
                // and then move up our index
                currentIndex = endLinkIndex;
                continue;
            }
            // we know that its a real link
            // first, add everything from currentIndex up to the start of the link
            outputLine.append(markdownLine, currentIndex, startLinkIndex);

            // next, start building out our link tag and append it to the current line
            String linkText = matcher.group(1);
            String linkUrl = matcher.group(2);
            outputLine.append("<a href=\"").append(linkUrl).append("\">").append(linkText).append("</a>");

            // and move our current index up
            currentIndex = endLinkIndex;
        }

        // after we're done looking for matches, add the rest of markdownLine and then close off the tag
        outputLine.append(markdownLine, currentIndex, markdownLine.length());
        outputLine.append("</").append(tag).append(">");

        // markdown line should now have been converted
        return outputLine.toString();
    }
}
