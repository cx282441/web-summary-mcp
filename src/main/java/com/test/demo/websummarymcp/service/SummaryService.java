package com.test.demo.websummarymcp.service;

import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    public String summarize(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Readability4J readability = new Readability4J(url, doc.html());
            String text = readability.parse().getTextContent();

            String[] lines = text.split("\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(lines.length, 8); i++) {
                sb.append(lines[i]).append("\n");
            }
            return sb.toString().trim();

        } catch (Exception e) {
            return "Failed to summarize page: " + e.getMessage();
        }
    }
}
