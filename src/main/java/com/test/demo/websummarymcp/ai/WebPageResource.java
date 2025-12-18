package com.test.demo.websummarymcp.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebPageResource {

    @McpResource(
            uri = "webpage://content",
            name = "webpage_content",
            description = "根据URL获取网页正文内容"
    )
    public String getWebPageContent(String url) throws JsonProcessingException {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Readability4J readability = new Readability4J(url, doc.html());
            String text = readability.parse().getTextContent();

            return new ObjectMapper().writeValueAsString(Map.of(
                    "url", url,
                    "content", text
            ));

        } catch (Exception e) {
            return new ObjectMapper().writeValueAsString(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}
