package com.mailchimp.assessment.markdownhtmlconverter.services;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TestDataProvider {

    /**
     * Test data for testParseMarkdownWithGivenData
     */
    public static Stream<Arguments> provideMarkdownParserTestData() {
        return Stream.of(
                markdownParserdataProvider1(),
                markdownParserdataProvider2(),
                markdownParserdataProvider3(),
                markdownParserdataProvider4(),
                markdownParserdataProvider5(),
                markdownParserdataProvider6(),
                markdownParserdataProvider7(),
                markdownParserdataProvider8()
        );
    }

    private static Arguments markdownParserdataProvider1() {
        // test case from the hw assignment
        List<String> output = new ArrayList<>();
        output.add("# Sample Document");
        output.add("Hello!");
        output.add("This is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.\n\\n\\nThis section has a newline in it.");
        output.add("And this is another section.");
        return Arguments.of(
                "# Sample Document\n\nHello!\n\nThis is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.\n\\n\\nThis section has a newline in it.\n\nAnd this is another section.",
                output
        );
    }

    private static Arguments markdownParserdataProvider2() {
        // another test case from the hw assignment
        List<String> output = new ArrayList<>();
        output.add("# Header one");
        output.add("Hello there");
        output.add("How are you?\nWhat's going on?");
        output.add("## Another Header");
        output.add("This is a paragraph [with an inline link](http://google.com). Neat, eh?");
        output.add("## This is a header [with a link](http://yahoo.com)");
        return Arguments.of(
                "# Header one\n\nHello there\n\nHow are you?\nWhat's going on?\n\n## Another Header\n\nThis is a paragraph [with an inline link](http://google.com). Neat, eh?\n\n## This is a header [with a link](http://yahoo.com)",
                output
        );
    }

    private static Arguments markdownParserdataProvider3() {
        // test empty
        List<String> output = new ArrayList<>();
        return Arguments.of(
                "",
                output
        );
    }

    private static Arguments markdownParserdataProvider4() {
        // edge case testing for escaped new lines
        List<String> output = new ArrayList<>();
        output.add("\\n\n\\n");
        return Arguments.of(
                "\n\\n\n\\n",
                output
        );
    }

    private static Arguments markdownParserdataProvider5() {
        // edge case testing on fake headers
        List<String> output = new ArrayList<>();
        // this should be treated as one p tag
        output.add("#header#\n##header two##\nregular\n###header ##three##");
        return Arguments.of(
                "#header#\n##header two##\nregular\n###header ##three##",
                output
        );
    }

    private static Arguments markdownParserdataProvider6() {
        // testing broken up links
        List<String> output = new ArrayList<>();
        output.add("This is sample markdown for the [Mailchimp](https:\n//www.mailchimp.com) homework assignment.");
        output.add("This is sample markdown for the [Mailchimp](https:");
        output.add("//www.mailchimp.com) homework assignment.");
        return Arguments.of(
                "This is sample markdown for the [Mailchimp](https:\n//www.mailchimp.com) homework assignment.\n\nThis is sample markdown for the [Mailchimp](https:\n\n//www.mailchimp.com) homework assignment.",
                output
        );
    }

    private static Arguments markdownParserdataProvider7() {
        // testing header edge cases
        List<String> output = new ArrayList<>();
        output.add("# header #");
        output.add("## header two ##");
        output.add("regular");
        output.add("###header ##three##\n##");
        return Arguments.of(
                "# header #\n## header two ##\n###\nregular\n\n###header ##three##\n##\n\n###",
                output
        );
    }

    private static Arguments markdownParserdataProvider8() {
        // more edge case testing
        List<String> output = new ArrayList<>();
        output.add("# header #");
        output.add("####### header two ##\n###\nregular");
        output.add("###header ##three##\n##");
        output.add("#######");
        return Arguments.of(
                "# header #\n####### header two ##\n###\nregular\n\n###header ##three##\n##\n\n#######",
                output
        );
    }

    /**
     * Test data for testParseMarkdownWithGivenData
     */
    public static Stream<Arguments> provideMarkdownHTMLConverterTestData() {
        return Stream.of(
                markdownHTMLConverterDataProvider1(),
                markdownHTMLConverterDataProvider2(),
                markdownHTMLConverterDataProvider3(),
                markdownHTMLConverterDataProvider4()
        );
    }

    private static Arguments markdownHTMLConverterDataProvider1() {
        // hw test case (+ testing multiple links within a tag)
        List<String> input = new ArrayList<>();
        input.add("# Sample Document");
        input.add("Hello!");
        input.add("This is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.\n\\n\\nThis section has a newline in it.");
        input.add("And this is another section.");
        List<String> output = new ArrayList<>();
        output.add("<h1>Sample Document</h1>");
        output.add("<p>Hello!</p>");
        output.add("<p>This is sample markdown for the <a href=\"https://www.mailchimp.com\">Mailchimp</a> homework assignment.\n\\n\\nThis section has a newline in it.</p>");
        output.add("<p>And this is another section.</p>");
        return Arguments.of(input, output);
    }

    private static Arguments markdownHTMLConverterDataProvider2() {
        // hw test case
        List<String> input = new ArrayList<>();
        input.add("# Header one");
        input.add("Hello there");
        input.add("How are you?\nWhat's going on?");
        input.add("## Another Header");
        input.add("This is a paragraph [with an inline link](http://google.com). Neat, eh?");
        input.add("## This is a header [with a link](http://yahoo.com)");
        input.add("This is a paragraph [with an inline link](http://google.com). Neat, eh? \" This is a paragraph [with an inline link](http://google.com). Neat, eh?");
        List<String> output = new ArrayList<>();
        output.add("<h1>Header one</h1>");
        output.add("<p>Hello there</p>");
        output.add("<p>How are you?\nWhat's going on?</p>");
        output.add("<h2>Another Header</h2>");
        output.add("<p>This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh?</p>");
        output.add("<h2>This is a header <a href=\"http://yahoo.com\">with a link</a></h2>");
        output.add("<p>This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh? \" This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh?</p>");

        return Arguments.of(input, output);
    }

    private static Arguments markdownHTMLConverterDataProvider3() {
        // edge case testing - empty
        List<String> input = new ArrayList<>();
        List<String> output = new ArrayList<>();
        return Arguments.of(input, output);
    }

    private static Arguments markdownHTMLConverterDataProvider4() {
        // edge case testing - fake headers
        List<String> input = new ArrayList<>();
        input.add("#     Still a header");
        input.add("#######Not a header");
        List<String> output = new ArrayList<>();
        output.add("<h1>Still a header</h1>");
        output.add("<p>#######Not a header</p>");
        return Arguments.of(input, output);
    }
}
