/**
 * EXAMPLE INTEGRATION FILE
 * 
 * This file shows how to integrate the AI Code Reviewer Agent into your project
 * Copy and modify as needed for your use case
 */

import com.ai.reviewer.agent.CodeReviewerAgent;
import com.ai.reviewer.model.ReviewComment;
import com.ai.reviewer.model.ReviewResult;
import com.ai.reviewer.model.ReviewSeverity;
import com.ai.reviewer.util.ConfigManager;
import com.ai.reviewer.util.FileReader;
import java.util.List;

public class CodeReviewerIntegrationExample {

    /**
     * Example 1: Simple single file review
     */
    public static void singleFileReview() {
        CodeReviewerAgent agent = new CodeReviewerAgent("My Reviewer", "1.0.0");
        
        String javaCode = """
            public class Calculator {
                public int add(int a, int b) {
                    return a + b;
                }
                
                public int multiply(int a, int b) {
                    try {
                        return a * b;
                    } catch (Exception e) {
                        // Silent catch
                    }
                    return 0;
                }
            }
            """;
        
        ReviewResult result = agent.reviewCode("Calculator.java", javaCode);
        System.out.println(result);
    }

    /**
     * Example 2: Batch review multiple files
     */
    public static void batchReview(String[] filePaths) {
        CodeReviewerAgent agent = new CodeReviewerAgent("Batch Reviewer", "1.0.0");
        
        double totalScore = 0;
        int fileCount = 0;
        
        for (String filePath : filePaths) {
            try {
                String code = FileReader.readFile(filePath);
                String fileName = FileReader.getFileName(filePath);
                
                ReviewResult result = agent.reviewCode(fileName, code);
                totalScore += result.getOverallScore();
                fileCount++;
                
                System.out.println(result);
                System.out.println("\n");
                
            } catch (Exception e) {
                System.err.println("Error reviewing " + filePath + ": " + e.getMessage());
            }
        }
        
        if (fileCount > 0) {
            System.out.println("Average Score: " + (totalScore / fileCount) + "%");
        }
    }

    /**
     * Example 3: Review with filtering by severity
     */
    public static void reviewWithSeverityFilter() {
        CodeReviewerAgent agent = new CodeReviewerAgent("Filter Reviewer", "1.0.0");
        
        String code = """
            public class Example {
                public static int X = 100;
                
                public void process() {
                    try {
                        doSomething();
                    } catch (Exception e) {
                    }
                }
                
                private void doSomething() {
                    System.out.println("Processing");
                }
            }
            """;
        
        ReviewResult result = agent.reviewCode("Example.java", code);
        
        // Show only critical and major issues
        List<ReviewComment> criticalIssues = result.getCommentsBySeverity(ReviewSeverity.CRITICAL);
        List<ReviewComment> majorIssues = result.getCommentsBySeverity(ReviewSeverity.MAJOR);
        
        System.out.println("CRITICAL ISSUES: " + criticalIssues.size());
        for (ReviewComment comment : criticalIssues) {
            System.out.println("  - Line " + comment.getLineNumber() + ": " + comment.getMessage());
            System.out.println("    Fix: " + comment.getSuggestion());
        }
        
        System.out.println("\nMAJOR ISSUES: " + majorIssues.size());
        for (ReviewComment comment : majorIssues) {
            System.out.println("  - Line " + comment.getLineNumber() + ": " + comment.getMessage());
            System.out.println("    Fix: " + comment.getSuggestion());
        }
    }

    /**
     * Example 4: Custom configuration
     */
    public static void configureReviewer() {
        ConfigManager config = new ConfigManager();
        
        // Customize configuration
        config.setProperty("agent.name", "My Custom Reviewer");
        config.setProperty("agent.version", "2.0.0");
        config.setProperty("review.max.method.lines", "50");
        config.setProperty("review.check.magic.numbers", "true");
        config.setProperty("review.check.naming", "true");
        
        // Create agent with custom config
        CodeReviewerAgent agent = new CodeReviewerAgent(
            config.getString("agent.name"),
            config.getString("agent.version")
        );
        
        String code = """
            public class Service {
                private void longMethod() {
                    // ... many lines of code (>50 lines)
                }
            }
            """;
        
        ReviewResult result = agent.reviewCode("Service.java", code);
        System.out.println(result);
    }

    /**
     * Example 5: CI/CD Pipeline Integration
     */
    public static int reviewCodeForCIPipeline(String[] sourceFiles, double minScore) {
        CodeReviewerAgent agent = new CodeReviewerAgent("CI Pipeline Reviewer", "1.0.0");
        
        int failedFiles = 0;
        
        for (String filePath : sourceFiles) {
            try {
                String code = FileReader.readFile(filePath);
                ReviewResult result = agent.reviewCode(filePath, code);
                
                if (result.getOverallScore() < minScore) {
                    System.out.println("FAIL: " + filePath);
                    System.out.println("Score: " + result.getOverallScore() + "% (minimum: " + minScore + "%)");
                    System.out.println("Critical Issues: " + result.getCriticalIssues());
                    System.out.println("Major Issues: " + result.getMajorIssues());
                    failedFiles++;
                }
            } catch (Exception e) {
                System.err.println("Error reviewing: " + filePath);
                failedFiles++;
            }
        }
        
        return failedFiles;
    }

    /**
     * Example 6: Detailed issue analysis
     */
    public static void analyzeIssuesInDetail() {
        CodeReviewerAgent agent = new CodeReviewerAgent("Analysis Reviewer", "1.0.0");
        
        String code = """
            public class DetailedExample {
                public static int CONFIG = 5000;
                
                public void process(String s) {
                    int r = s.length();
                    try {
                        doWork(r);
                    } catch (Exception e) {
                    }
                }
                
                private void doWork(int val) {
                    if (val > 100) {
                        System.out.println("Large");
                    }
                }
            }
            """;
        
        ReviewResult result = agent.reviewCode("DetailedExample.java", code);
        
        System.out.println("File: " + result.getFileName());
        System.out.println("Overall Score: " + result.getOverallScore() + "/100");
        System.out.println("Review took: " + result.getReviewTimeMs() + "ms");
        System.out.println("Total Issues: " + result.getTotalIssues());
        System.out.println();
        
        // Detailed analysis by category
        for (ReviewComment comment : result.getComments()) {
            System.out.println("[" + comment.getSeverity().getLabel() + "]");
            System.out.println("Location: " + comment.getCategory() + " at line " + comment.getLineNumber());
            System.out.println("Issue: " + comment.getMessage());
            System.out.println("Code: " + comment.getCode());
            System.out.println("Suggestion: " + comment.getSuggestion());
            System.out.println("---");
        }
    }

    /**
     * Example 7: Create report summary
     */
    public static void generateReportSummary(List<ReviewResult> results) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         CODE REVIEW SUMMARY REPORT                      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        
        double totalScore = 0;
        int totalIssues = 0;
        int totalCritical = 0;
        int totalMajor = 0;
        
        for (ReviewResult result : results) {
            totalScore += result.getOverallScore();
            totalIssues += result.getTotalIssues();
            totalCritical += result.getCriticalIssues();
            totalMajor += result.getMajorIssues();
            
            String status = result.getOverallScore() >= 80 ? "✓ PASS" : "✗ FAIL";
            System.out.printf("%s | %-30s | Score: %6.1f%% | Issues: %3d\n",
                status,
                result.getFileName(),
                result.getOverallScore(),
                result.getTotalIssues()
            );
        }
        
        System.out.println();
        System.out.println("SUMMARY STATISTICS:");
        System.out.printf("Average Score:     %.1f%%\n", totalScore / results.size());
        System.out.printf("Total Issues:      %d\n", totalIssues);
        System.out.printf("Critical Issues:   %d\n", totalCritical);
        System.out.printf("Major Issues:      %d\n", totalMajor);
        System.out.println();
    }

    // Main method to run examples
    public static void main(String[] args) {
        System.out.println("=== AI CODE REVIEWER INTEGRATION EXAMPLES ===\n");
        
        System.out.println("1. Single File Review:");
        System.out.println("------------------------");
        singleFileReview();
        
        System.out.println("\n\n2. Review with Severity Filtering:");
        System.out.println("-----------------------------------");
        reviewWithSeverityFilter();
        
        System.out.println("\n\n3. Detailed Analysis:");
        System.out.println("--------------------");
        analyzeIssuesInDetail();
    }
}
