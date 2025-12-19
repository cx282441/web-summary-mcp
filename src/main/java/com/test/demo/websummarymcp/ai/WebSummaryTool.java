package com.test.demo.websummarymcp.ai;

import jakarta.servlet.http.HttpServletRequest;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.stream.Collectors;

@Component
public class WebSummaryTool {

    @McpTool(description = "通过URL提取网页主要内容并进行总结")
    public String summarizeWebPage(
            @McpToolParam(description = "要总结网页url") String url
    ) {

        // 1. 获取钉钉 Header
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Readability4J readability = new Readability4J(url, doc.html());
            String text = readability.parse().getTextContent();

            if (text == null || text.isBlank()) {
                return "未能提取到网页正文内容";
            }

            return text.lines()
                    .limit(10)
                    .collect(Collectors.joining("\n"));

        } catch (Exception e) {
            return "网页解析失败：" + e.getMessage();
        }
    }
}
