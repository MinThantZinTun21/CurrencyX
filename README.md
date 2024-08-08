# Project Currency

This project demonstrates the use of HTTP caching to allow offline access to fetched data for up to 30 minutes. Additionally, it handles API rate limits by using mock JSON responses when necessary.

## Features

- **HTTP Cache**: Utilizes HTTP caching to store fetched data, allowing the application to work without an internet connection for up to 30 minutes after the initial fetch.
- **Mock JSON**: Implements mock JSON data to handle scenarios where the API key usage has reached its limit.

## Usage

### Working with the API

1. **Update API Key**:
   Ensure that you have a valid API key to work with the live data. Replace the placeholder in your code with your actual API key.

2. **Fetching and Transforming Data**:
   When fetching data from the API, transform the `QuoteDto` as shown below:

   ```kotlin
   QuoteDto(
       source = source,
       to = it.key.removePrefix("USD"),
       rate = it.value
   )'''
to

 ```kotlin
   QuoteDto(
       source = source,
       to = it.key.removePrefix(source),
       rate = it.value
   )'''


```quoteList.add(QuoteDto(source = source, to = "USD", rate = 1.0))'''
to
```quoteList.add(QuoteDto(source = source, to = source, rate = 1.0))'''

