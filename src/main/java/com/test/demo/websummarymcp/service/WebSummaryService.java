package com.test.demo.websummarymcp.service;

import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class WebSummaryService {

    @Tool(description = "Extract and summarize main content from a web page by URL")
    public String summarizeWebPage(
            @ToolParam(description = "The full URL of the web page") String url
    ) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Readability4J readability = new Readability4J(url, doc.html());
            String text = readability.parse().getTextContent();

            if (text == null || text.isBlank()) {
                return "No readable content found on this page.";
            }

            String[] lines = text.split("\n");
            StringBuilder summary = new StringBuilder();

            for (int i = 0; i < Math.min(lines.length, 10); i++) {
                summary.append(lines[i].trim()).append("\n");
            }

            return summary.toString().trim();

        } catch (Exception e) {
            return "Failed to summarize page: " + e.getMessage();
        }
    }
}
