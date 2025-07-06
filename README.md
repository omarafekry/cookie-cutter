# 🍪 Cookie Cutter

A Java CLI application to find the most active cookies (i.e. the cookies with the most entries) for a given date from a CSV log file.

## Features
- Parses large CSV files efficiently (uses streaming processing)
- Finds the most active cookies for a specified date
- Clean, modular, and testable codebase
- Robust error handling and logging (SLF4J)
- Extensible architecture (easy to add new fields or features)

## Requirements
- **Java** 17 or higher
- **Maven** 3.6 or higher
- **Bash** (for running cookie-cutter.sh on Unix/macOS/WSL; use Git Bash on Windows)

## Usage

### Run
```
./cookie-cutter.sh -f <cookie_log.csv> -d <yyyy-MM-dd>
```

#### Example
```
# Using the provided cookie_log.csv
./cookie-cutter.sh -f cookie_log.csv -d 2018-12-09
```

This will output:
```
Most active cookies:
1. AtY0laUfhglK3lC7
```

The cookie `AtY0laUfhglK3lC7` is the most active cookie on 2018-12-09, appearing twice in the sample log.

### Command Line Options
- `-f, --file <FILE>`: Path to the CSV file to be processed (required)
- `-d, --date <DATE>`: Date filter in `yyyy-MM-dd` format (required)
- `-?, --help`: Show help message

## CSV Format
The CSV file should have a header and rows like:
```
cookie,timestamp
AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00
```

## Logging
- Uses SLF4J (with slf4j-simple by default)
- Configure log level in `src/main/resources/simplelogger.properties`
- Example: `org.slf4j.simpleLogger.defaultLogLevel=info`

## Testing
- Unit tests are in `src/test/java/`
- Run tests with:
```
mvn test
```

## Project Structure
- `org.ibrahim.cli` — Command-line parsing and help
- `org.ibrahim.io` — File and CSV parsing, printing
- `org.ibrahim.business` — Business logic (finding most active cookies)
- `org.ibrahim.model` — Data models
- `org.ibrahim.exception` — Custom exceptions
- `org.ibrahim.service` — Application workflow orchestration
- `org.ibrahim.Main` — Application entry point

## Extending
- To add new fields to cookies, update `Cookie.java` and adjust printers as needed.
- To change logging, update `simplelogger.properties` or use another SLF4J backend.
