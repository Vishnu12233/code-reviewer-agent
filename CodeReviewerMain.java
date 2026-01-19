package com.ai.reviewer;

import com.ai.reviewer.agent.CodeReviewerAgent;
import com.ai.reviewer.model.ReviewResult;
import com.ai.reviewer.util.ConfigManager;
import java.util.logging.Logger;

/**
 * Main entry point for the AI Code Reviewer Agent
 */
public class CodeReviewerMain {
    private static final Logger logger = Logger.getLogger(CodeReviewerMain.class.getName());

    public static void main(String[] args) {
        logger.info("Starting AI Code Reviewer Agent");

        // Initialize configuration
        ConfigManager config = new ConfigManager();
        String agentName = config.getString("agent.name");
        String version = config.getString("agent.version");

        // Create the reviewer agent
        CodeReviewerAgent agent = new CodeReviewerAgent(agentName, version);
        logger.info("Agent initialized: " + agent);

        // Example 1: Review a simple Java class with poor practices
        String sampleCode1 = "public class BadExample {\n" +
                "    public static int MAX_SIZE = 1000;\n" +
                "    public void processData(Object[] data) {\n" +
                "        try {\n" +
                "            for (Object d : data) {\n" +
                "                System.out.println(d);\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        ReviewResult result1 = agent.reviewCode("BadExample.java", sampleCode1);
        System.out.println(result1);

        // Example 2: Review better quality code
        String sampleCode2 = "public class GoodExample {\n" +
                "    private static final int MAX_SIZE = 1000;\n" +
                "    public void processData(Object[] data) {\n" +
                "        if (data == null) {\n" +
                "            throw new IllegalArgumentException(\"Data\");\n" +
                "        }\n" +
                "        for (Object item : data) {\n" +
                "            handleItem(item);\n" +
                "        }\n" +
                "    }\n" +
                "    private void handleItem(Object item) {\n" +
                "    }\n" +
                "}\n";

        ReviewResult result2 = agent.reviewCode("GoodExample.java", sampleCode2);
        System.out.println(result2);

        // Example 3: Complex code with multiple issues
        String sampleCode3 = "public class ComplexExample {\n" +
                "    public String analyzeData(int[][] matrix) {\n" +
                "        int x = 0;\n" +
                "        for (int i = 0; i < matrix.length; i++) {\n" +
                "            for (int j = 0; j < matrix[i].length; j++) {\n" +
                "                x += matrix[i][j];\n" +
                "            }\n" +
                "        }\n" +
                "        if (x > 999) {\n" +
                "            System.out.println(\"Value: \" + x);\n" +
                "        }\n" +
                "        try {\n" +
                "            String result = processResult(x);\n" +
                "            return result;\n" +
                "        } catch (Exception e) {\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n" +
                "}\n";

        ReviewResult result3 = agent.reviewCode("ComplexExample.java", sampleCode3);
        System.out.println(result3);

        // Summary
        logger.info("Code review analysis complete!");
        System.out.println("\nREVIEW SUMMARY");
        System.out.printf("File 1 - Score: %.1f%%, Issues: %d\n", result1.getOverallScore(), result1.getTotalIssues());
        System.out.printf("File 2 - Score: %.1f%%, Issues: %d\n", result2.getOverallScore(), result2.getTotalIssues());
        System.out.printf("File 3 - Score: %.1f%%, Issues: %d\n", result3.getOverallScore(), result3.getTotalIssues());
    }
}
