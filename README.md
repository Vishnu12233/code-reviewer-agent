# AI Code Reviewer Agent

A comprehensive Java-based AI code reviewer agent that analyzes Java source code for quality issues, best practices violations, and potential bugs.

## Features

- **Automatic Code Analysis**: Scans Java code for common issues
- **Multiple Review Categories**:
  - Exception Handling (empty catch blocks, generic exceptions)
  - Code Quality (magic numbers, naming conventions)
  - Thread Safety (public static mutable fields)
  - Code Complexity (method length, nested loops)
  - Logging Issues (System.out/err usage)
  - Code Duplication Detection
  
- **Severity Levels**: CRITICAL, MAJOR, MINOR, INFO
- **Detailed Reporting**: Line numbers, code snippets, suggestions
- **Performance Metrics**: Review execution time tracking
- **Configurable Analysis**: Customize review rules via ConfigManager

## Project Structure

```
src/main/java/com/ai/reviewer/
├── agent/
│   └── CodeReviewerAgent.java           # Main reviewer agent
├── analyzer/
│   ├── CodeAnalyzer.java               # Interface for analyzers
│   └── JavaCodeAnalyzer.java           # Java code analyzer implementation
├── model/
│   ├── ReviewComment.java              # Individual code review comment
│   ├── ReviewResult.java               # Complete review result
│   └── ReviewSeverity.java             # Severity levels
├── util/
│   ├── ConfigManager.java              # Configuration management
│   └── FileReader.java                 # File I/O utilities
└── CodeReviewerMain.java               # Main entry point
```

## Build & Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean package
```

### Run
```bash
mvn exec:java -Dexec.mainClass="com.ai.reviewer.CodeReviewerMain"
```

Or run the JAR:
```bash
java -jar target/code-reviewer-agent-1.0.0.jar
```

## Usage Example

```java
// Create the agent
CodeReviewerAgent agent = new CodeReviewerAgent("AI Code Reviewer", "1.0.0");

// Review code
String code = """
    public class Example {
        public void process() {
            try {
                // code here
            } catch (Exception e) {
            }
        }
    }
    """;

ReviewResult result = agent.reviewCode("Example.java", code);

// Display results
System.out.println(result);
System.out.println("Overall Score: " + result.getOverallScore());
System.out.println("Issues Found: " + result.getTotalIssues());
```

## Review Categories

### 1. Exception Handling
- Empty catch blocks
- Catching generic Exception
- Missing exception logging

### 2. Code Quality
- Magic numbers (constants without names)
- Naming conventions violations
- Code duplication

### 3. Thread Safety
- Public static mutable fields
- Shared state without synchronization

### 4. Code Complexity
- Long methods (>30 lines)
- Nested loops
- High cyclomatic complexity

### 5. Logging
- System.out/err usage instead of logging framework
- Missing error handling

## Extending the Agent

### Adding a Custom Analyzer

```java
public class CustomAnalyzer implements CodeAnalyzer {
    @Override
    public ReviewResult analyze(String fileName, String code) {
        ReviewResult result = new ReviewResult(fileName, code);
        // Add your analysis logic
        return result;
    }
}

// Use custom analyzer
CodeReviewerAgent agent = new CodeReviewerAgent("Agent", "1.0.0");
agent.setAnalyzer(new CustomAnalyzer());
```

## Configuration

Configure the agent via `ConfigManager`:

```java
ConfigManager config = new ConfigManager();
config.setProperty("agent.name", "My Reviewer");
config.setProperty("review.max.method.lines", "50");
config.setProperty("review.check.magic.numbers", "true");
```

## Severity Levels

- **CRITICAL** (Score -10): Issues that must be fixed (empty catch blocks, etc.)
- **MAJOR** (Score -5): Significant issues affecting code quality or safety
- **MINOR** (Score -2): Code quality improvements
- **INFO** (Score -0.5): Informational items (TODOs, style issues)

## Score Calculation

- Base score: 100
- Score decreases based on issue severity
- Minimum score: 0
- Final score = 100 - (sum of severity deductions)

## Logging

The agent uses SLF4J with Logback for logging. Configuration is in `src/main/resources/logback.xml`.

Logs are written to:
- Console (INFO level and above)
- File: `code-reviewer.log` (DEBUG level for com.ai.reviewer)

## Future Enhancements

- [ ] Integration with LLM APIs for AI-powered analysis
- [ ] Support for multiple programming languages
- [ ] HTML/JSON report generation
- [ ] IDE plugin support
- [ ] CI/CD pipeline integration
- [ ] Performance metrics analysis
- [ ] Security vulnerability detection
- [ ] Architecture pattern recognition

## License

MIT License

## Author

AI Code Reviewer Team
