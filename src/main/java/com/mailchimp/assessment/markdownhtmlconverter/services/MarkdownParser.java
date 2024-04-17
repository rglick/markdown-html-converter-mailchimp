package com.mailchimp.assessment.markdownhtmlconverter.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MarkdownParser {

    // Per HW rules, we are only produce up to an H6 tag
    private static final Integer MAX_HEADER_COUNT = 6;

    /**
     * Parses out the given markdown into separated out lines
     *
     * @param content markdown content to later convert into HTML
     *
     * @return separated out markdown lines to convert into HTML
     */
    List<String> parseMarkdown(String content) {
        // using the API example, content will look something like:
        // # Sample Document\n\nHello!\n\nThis is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.\n

        // we want to make sure we are splitting on \n for headings and \n\n for unformatted text as that is the separation between
        // different markdown content

        // edge case - the markdown can contain /n/n within its content. we would expect that to be properly escaped
        // with a /, but that means we can't just split on /n/n - we need to check for escape characters as well
        List<String> parsedMarkdown = new ArrayList<>();
        StringBuilder currentSection = new StringBuilder();
        boolean isHeaderSection = false;
        boolean isPreviousCharacterNewLine = false;

        // we only need one pass through content
        for (int index = 0; index < content.length(); index++) {
            Character current = content.charAt(index);
            // we need to determine if we're starting a new header section or not
            if (current.equals('#') && (currentSection.isEmpty() || isPreviousCharacterNewLine && isHeaderSection)) {
                // if we've satisfied one of these two conditions (they should be mutually exclusive),
                // we've potentially found a new header section. this is important as a header section will only
                // be one line

                // first though, we might need to close off our current section - do that if needed
                if (isPreviousCharacterNewLine && isHeaderSection) {
                    parsedMarkdown.add(currentSection.toString());
                    currentSection.setLength(0);
                    isPreviousCharacterNewLine = false;
                }

                // we now need to determine if this is actually a header section or not
                // index check to make sure we don't go out of bounds
                while (current.equals('#') && index < content.length() - 1) {
                    // get to the next non # character
                    currentSection.append(current);
                    index++;
                    current = content.charAt(index);
                }

                // if the next character after the #s is a new line or we've hit the end and there was actually no content,
                // we can just ignore what we added - just reset
                if ((current == '\n' || index == content.length() - 1) && currentSection.length() < MAX_HEADER_COUNT) {
                    currentSection.setLength(0);
                    continue;
                } else if (current == ' ' && currentSection.length() < MAX_HEADER_COUNT) {
                    // this means we do actually have a header section - append that and move on as well
                    isHeaderSection = true;
                }
                // either way if it's a header section or not, we need to append and continue
                currentSection.append(current);
            } else if (current.equals('\n')) {
                // we've discovered a new line character - we need to determine if that means closing off the
                // section or not

                if (currentSection.isEmpty()) {
                    // if we're starting a new section, just ignore it
                    continue;
                }

                if (isHeaderSection) {
                    // if it's a header section, one new line means the HTML section is complete - just
                    // add to parsedMarkdown and reset
                    parsedMarkdown.add(currentSection.toString());
                    currentSection.setLength(0);
                    isHeaderSection = false;
                    continue;
                }

                // if it's not a header section, we need to check if this is the first or second new line character
                // we've seen
                if (isPreviousCharacterNewLine) {
                    // if it's the second one in a row we've seen, the current section is done
                    // remove the last new character, add it to parsedMarkdown and reset
                    currentSection.deleteCharAt(currentSection.length() - 1);
                    parsedMarkdown.add(currentSection.toString());
                    currentSection.setLength(0);
                    isPreviousCharacterNewLine = false;
                } else {
                    // if it's not, still append, but mark it for the next character
                    isPreviousCharacterNewLine = true;
                    currentSection.append(current);
                }
            } else {
                // base case - just append as normal
                currentSection.append(current);
                isPreviousCharacterNewLine = false;
            }
        }

        // add whatever is left
        if (!currentSection.isEmpty()) {
            parsedMarkdown.add(currentSection.toString());
        }

        // and return the result
        return parsedMarkdown;
    }
}
