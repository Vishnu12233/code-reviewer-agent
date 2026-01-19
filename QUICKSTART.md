# Quick Start Guide - AI Code Reviewer Agent

## Installation & Setup

### Requirements
- Java 11 or higher
- Windows, macOS, or Linux

### 1. Navigate to Project
```bash
cd c:\java\javaproject
```

### 2. Compile (One-time setup)
```bash
javac -d target\classes src\main\java\com\ai\reviewer\model\ReviewSeverity.java src\main\java\com\ai\reviewer\model\ReviewComment.java src\main\java\com\ai\reviewer\model\ReviewResult.java src\main\java\com\ai\reviewer\analyzer\CodeAnalyzer.java src\main\java\com\ai\reviewer\analyzer\JavaCodeAnalyzer.java src\main\java\com\ai\reviewer\agent\CodeReviewerAgent.java src\main\java\com\ai\reviewer\util\ConfigManager.java src\main\java\com\ai\reviewer\util\FileReader.java src\main\java\com\ai\reviewer\CodeReviewerMain.java
```

### 3. Run the Demo
```bash
java -cp target\classes com.ai.reviewer.CodeReviewerMain
```

## Basic Usage

### As a Standalone Program

```java
// Create agent
CodeReviewerAgent agent = new CodeReviewerAgent("My Code Reviewer", "1.0.0");

// Review a Java file
String code = """
    public class MyClass {
        public void process() {
            // Your code here
        }
    }
    """;

ReviewResult result = agent.reviewCode("MyClass.java", code);

// Print detailed report
System.out.println(result);

// Access individual metrics
System.out.println("Score: " + result.getOverallScore());
System.out.println("Critical Issues: " + result.getCriticalIssues());
System.out.println("Total Issues: " + result.getTotalIssues());
```

### Review Multiple Files

```java
CodeReviewerAgent agent = new CodeReviewerAgent("Batch Reviewer", "1.0.0");

// Read files and review
String file1Content = FileReader.readFile("src/MyClass.java");
String file2Content = FileReader.readFile("src/Helper.java");

ReviewResult result1 = agent.reviewCode("MyClass.java", file1Content);
ReviewResult result2 = agent.reviewCode("Helper.java", file2Content);

// Generate summary
System.out.println("File 1: " + result1.getOverallScore() + "%");
System.out.println("File 2: " + result2.getOverallScore() + "%");
```

## What Gets Checked

The reviewer analyzes code for:

| Category | Severity | Examples |
|----------|----------|----------|
| Exception Handling | CRITICAL | Empty catch blocks |
| Exception Handling | MAJOR | Catching generic Exception |
| Thread Safety | MAJOR | Public static mutable fields |
| Code Quality | MINOR | Magic numbers |
| Naming | MINOR | Single-letter variables |
| Logging | MINOR | System.out instead of logger |
| Code Duplication | MINOR | Repeated code |

## Score Interpretation

- **90-100**: Excellent code quality
- **80-89**: Good code with minor issues
- **70-79**: Acceptable but needs improvement
- **60-69**: Significant issues to address
- **Below 60**: Critical issues found

## Project Structure

```
src/main/java/com/ai/reviewer/
├── CodeReviewerMain.java           ← Run this
├── agent/CodeReviewerAgent.java     ← Main reviewer class
├── analyzer/JavaCodeAnalyzer.java   ← Analysis logic
├── model/                           ← Review result classes
└── util/                            ← Helper utilities
```

## Extending the Agent

### Create a Custom Analyzer

```java
import com.ai.reviewer.analyzer.CodeAnalyzer;
import com.ai.reviewer.model.ReviewResult;

public class MyCustomAnalyzer implements CodeAnalyzer {
    @Override
    public ReviewResult analyze(String fileName, String code) {
        ReviewResult result = new ReviewResult(fileName, code);
        
        // Add your custom analysis rules
        if (code.contains("TODO")) {
            // Add review comments as needed
        }
        
        return result;
    }
}

// Use it
CodeReviewerAgent agent = new CodeReviewerAgent("Custom Agent", "1.0.0");
agent.setAnalyzer(new MyCustomAnalyzer());
```

## Accessing Review Details

```java
ReviewResult result = agent.reviewCode("Test.java", code);

// Get all comments
List<ReviewComment> allComments = result.getComments();

// Get by severity
List<ReviewComment> critical = result.getCommentsBySeverity(ReviewSeverity.CRITICAL);
List<ReviewComment> major = result.getCommentsBySeverity(ReviewSeverity.MAJOR);

// For each comment
for (ReviewComment comment : allComments) {
    System.out.println("Line " + comment.getLineNumber());
    System.out.println("Severity: " + comment.getSeverity());
    System.out.println("Issue: " + comment.getMessage());
    System.out.println("Fix: " + comment.getSuggestion());
}
```

## Tips & Tricks

1. **Batch Processing**: Review multiple files using FileReader utility
2. **Custom Rules**: Extend with your own analyzer implementation
3. **CI/CD Integration**: Embed in your build pipeline
4. **Configuration**: Use ConfigManager to customize behavior
5. **Logging**: All operations are logged (change logback.xml for details)

## Troubleshooting

### Class Not Found Error
Make sure you're running from the project root directory:
```bash
cd c:\java\javaproject
java -cp target\classes com.ai.reviewer.CodeReviewerMain
```

### Compilation Errors
Ensure Java 11+ is installed:
```bash
java -version
```

### No Output
Run with error output enabled:
```bash
java -cp target\classes com.ai.reviewer.CodeReviewerMain 2>&1
```

## Next Steps

1. **Review your code**: Pass your Java files to the agent
2. **Analyze results**: Look for patterns in issues
3. **Improve quality**: Address the suggestions provided
4. **Extend functionality**: Add custom analyzers for your needs
5. **Integrate**: Use in your CI/CD pipeline

---

**Ready to review code!** Run the agent and let it improve your code quality.
