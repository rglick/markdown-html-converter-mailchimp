# markdown-html-converter
This a web service to convert Markdown into HTML. This takes in a
new-line delineated string passed in as content and returns an HTML-formatted string.

Note - I developed this project using a Spring Boot project template provided by IntelliJ
as that is what I have the most experience using. There is some boilerplate code provided
there to help get the service up and running. Everything I've written is located within:
- /src/
- /api/
- README.md

## API
Choosing to go with a web service over a CLI tool, the API is where I had to make an assumption on how it looked. I talk
more about what I could have chosen under Future Improvements section, but since there was limited time, I decided on the
format of the data in the request object being {"content": "markdown string"}

For the request, I made the assumption that the Markdown would be passed in as a new-line delineated string, passed in as part of 
a content field in a passed in JSON object. For the response, I made the assumption that it would just be a HTML object
and can be rendered directly.

You can check out the API at /api/markdown-converter.yaml

## Business Logic
This project tries to adhere more to SOLID programming (easier maintainability, readability, etc.)
while not sacrificing too much performance. While it is possible to convert markdown into HTML
going through one pass of the passed-in content string using a REGEX find and replace, we elect to make three passes
to break down each piece into easy to read and understand pieces.
- Parse out each individual markdown string so that it's easier to understand where tags will go
- Add the proper html tags to each parsed-out string
- construct and return the html object

Breaking these out allows us to also add cleaner testing coverage to each piece of logic

### Edge Cases
These are a couple of the edge cases that I feel like are worth mentioning
- Links appearing in plain text
- new line characters appearing in plain text
- "Fake" header sections - sections that at first look like header sections, but more processing shows that they're not

## Testing
I used two main forms of testing for this project - unit testing and manual testing.
Unit tests are stored in /src/test - I use a data provider to provide sample data to each test

For manual testing, I used both curl requests from a terminal and Postman (as Postman provides better formatting
for the response). I also used writing this README.md to help me try to find edge cases.

Here is a sample curl request:
curl -X POST http://localhost:8080/markdown \
-H "Content-Type: application/json" \
-d '{"content": "# Sample Document\n\nHello!\n\nThis is sample markdown for the [Mailchimp](https://www.mailchimp.com) homework assignment.\n"}'

Note that FQDN (Fully Qualified Domain Name) is not currently set up for the service

### FUTURE IMPROVEMENTS
As there is limited time to work on this assignment, it's not a perfect system. Here are some of the
improvements we can make here

#### API
To make the service more user-friendly, we can expand upon what is an 
acceptable request
- Accept a Markdown file in as part of the request
- A ccept separated out lines as data

We could also add authorization in the form of OAuth tokens. While there really isn't a need
at the moment to make this more secure, access control is normally really important - we wouldn't want someone to be
able to abuse the system.

#### Business Logic
Here are just a couple of the different improvements we can make on the business logic side
of things

- Caching using a Document NoSQL DB

There could easily be a use case where we see duplicate requests or duplicate lines.
As Document NoSQL DBs are made to handle large HTML objects, we can use it to cache by markdown
representation of the line and save ourselves some processing

- Request Validation

Right now, we just take the request as given and apply our rules to them. A future improvement could be to try to weed out
some edge cases such as false header tags (## followed by a new line) and send as part of the response back to the client
that there was data passed in accidentally formatted incorrectly

- Tracking Metrics

There is an opportunity to track metrics such as number of requests, request latency, how many p/hx/a tags were creating, etc.
that might be of interest to an Analytics team

#### Testing
There wasn't enough time to add integration testing for the service. Validating the whole service
works end-to-end would be a good next step to take.

#### Infrastructure
Right now, this is only a service that can run on a local machine. To get it more production ready, there are
a couple of steps we can do

- Creating a web server (+ multiple clones if traffic starts getting really heavy) - this project needs a place to live
- Setting up a Document NoSQL DB (see above)
