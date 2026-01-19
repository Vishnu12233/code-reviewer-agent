package com.ai.reviewer;

import com.ai.reviewer.agent.CodeReviewerAgent;
import com.ai.reviewer.model.ReviewComment;
import com.ai.reviewer.model.ReviewResult;
import com.ai.reviewer.model.ReviewSeverity;
import java.util.List;

public class ReviewUserProgram {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  AI CODE REVIEWER - ANALYZING USER PROGRAM              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Create the AI reviewer agent
        CodeReviewerAgent agent = new CodeReviewerAgent("Intelligent Code Analyzer", "1.0.0");
        
        // Read the user program
        String userCode = """
            import java.util.ArrayList;
            import java.util.List;
            
            public class UserManagementSystem {
                public static List<String> users = new ArrayList<>();
                
                public void addUser(String name) {
                    try {
                        if (name == null || name.isEmpty()) {
                            throw new Exception("Invalid name");
                        }
                        
                        for (String u : users) {
                            if (u.equals(name)) {
                                System.out.println("User already exists");
                                return;
                            }
                        }
                        
                        users.add(name);
                        System.out.println("User added: " + name);
                    } catch (Exception e) {
                        // Silent catch - no handling
                    }
                }
                
                public void removeUser(String n) {
                    users.remove(n);
                    System.out.println("User removed");
                }
                
                public void processUsers() {
                    int total = 0;
                    for (int i = 0; i < users.size(); i++) {
                        for (int j = 0; j < users.size(); j++) {
                            if (i != j) {
                                total += 10;
                            }
                        }
                    }
                    System.err.println("Total: " + total);
                }
                
                public void displayAllUsers() {
                    if (users.size() > 1000) {
                        System.out.println("Too many users");
                    }
                    
                    for (String user : users) {
                        System.out.println("- " + user);
                    }
                    
                    // TODO: Add pagination
                    // FIXME: Handle large datasets
                }
                
                public static void main(String[] args) {
                    UserManagementSystem system = new UserManagementSystem();
                    system.addUser("Alice");
                    system.addUser("Bob");
                    system.displayAllUsers();
                    system.processUsers();
                }
            }
            """;
        
        // AI Agent reviews the code
        System.out.println("🤖 AI AGENT ANALYZING YOUR PROGRAM...\n");
        ReviewResult result = agent.reviewCode("UserManagementSystem.java", userCode);
        
        // Display detailed review results
        System.out.println(result);
        
        // Additional analysis
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║           DETAILED ISSUE BREAKDOWN                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Critical issues
        List<ReviewComment> criticalIssues = result.getCommentsBySeverity(ReviewSeverity.CRITICAL);
        if (criticalIssues.size() > 0) {
            System.out.println("🔴 CRITICAL ISSUES (" + criticalIssues.size() + "):");
            for (ReviewComment issue : criticalIssues) {
                System.out.println("  ✗ Line " + issue.getLineNumber() + ": " + issue.getMessage());
                System.out.println("    → " + issue.getSuggestion());
            }
            System.out.println();
        }
        
        // Major issues
        List<ReviewComment> majorIssues = result.getCommentsBySeverity(ReviewSeverity.MAJOR);
        if (majorIssues.size() > 0) {
            System.out.println("🟠 MAJOR ISSUES (" + majorIssues.size() + "):");
            for (ReviewComment issue : majorIssues) {
                System.out.println("  ⚠ Line " + issue.getLineNumber() + ": " + issue.getMessage());
                System.out.println("    → " + issue.getSuggestion());
            }
            System.out.println();
        }
        
        // Minor issues
        List<ReviewComment> minorIssues = result.getCommentsBySeverity(ReviewSeverity.MINOR);
        if (minorIssues.size() > 0) {
            System.out.println("🟡 MINOR ISSUES (" + minorIssues.size() + "):");
            for (ReviewComment issue : minorIssues) {
                System.out.println("  ℹ Line " + issue.getLineNumber() + ": " + issue.getMessage());
                System.out.println("    → " + issue.getSuggestion());
            }
            System.out.println();
        }
        
        // Info issues
        List<ReviewComment> infoIssues = result.getCommentsBySeverity(ReviewSeverity.INFO);
        if (infoIssues.size() > 0) {
            System.out.println("ℹ️  INFO ITEMS (" + infoIssues.size() + "):");
            for (ReviewComment issue : infoIssues) {
                System.out.println("  ℹ Line " + issue.getLineNumber() + ": " + issue.getMessage());
                System.out.println("    → " + issue.getSuggestion());
            }
            System.out.println();
        }
        
        // Final recommendation
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║            AI AGENT RECOMMENDATION                      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        double score = result.getOverallScore();
        String status;
        
        if (score >= 90) {
            status = "✅ EXCELLENT - Code quality is very good";
        } else if (score >= 80) {
            status = "👍 GOOD - Minor improvements needed";
        } else if (score >= 70) {
            status = "⚠️  ACCEPTABLE - Several issues to fix";
        } else if (score >= 60) {
            status = "❌ NEEDS IMPROVEMENT - Address major issues";
        } else {
            status = "🚨 CRITICAL - Significant problems found";
        }
        
        System.out.println("Quality Score: " + score + "/100");
        System.out.println("Status: " + status);
        System.out.println("\nTotal Issues Found: " + result.getTotalIssues());
        System.out.println("  - Critical: " + result.getCriticalIssues());
        System.out.println("  - Major: " + result.getMajorIssues());
        System.out.println("  - Minor: " + result.getMinorIssues());
        System.out.println("  - Info: " + result.getInfoIssues());
        System.out.println("\nAnalysis Time: " + result.getReviewTimeMs() + "ms");
    }
}
