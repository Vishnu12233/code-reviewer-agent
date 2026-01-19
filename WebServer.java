package com.ai.reviewer;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    public static void main(String[] args) {
        port(4567); // Set the port for the web server

        Gson gson = new Gson();
        CodeReviewerAgent reviewerAgent = new CodeReviewerAgent();

        // Define the /review endpoint
        post("/review", (req, res) -> {
            res.type("application/json");

            // Get the file path from the request
            String filePath = req.queryParams("filePath");
            if (filePath == null || filePath.isEmpty()) {
                res.status(400);
                return gson.toJson(Map.of("error", "filePath parameter is required"));
            }

            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                res.status(400);
                return gson.toJson(Map.of("error", "File not found: " + filePath));
            }

            // Read the file content
            StringBuilder code = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    code.append(line).append("\n");
                }
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Error reading file: " + e.getMessage()));
            }

            // Perform the code review
            ReviewResult result = reviewerAgent.reviewCode(file.getName(), code.toString());

            // Return the review result as JSON
            return gson.toJson(result);
        });

        // Define a simple health check endpoint
        get("/health", (req, res) -> {
            res.type("application/json");
            return gson.toJson(Map.of("status", "ok"));
        });

        System.out.println("Web server is running on http://localhost:4567");
    }
}