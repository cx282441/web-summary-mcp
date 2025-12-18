package com.test.demo.websummarymcp.ai;

import jakarta.servlet.http.HttpServletRequest;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class WebSummaryTool {

    @McpTool(description = "通过URL提取网页主要内容并进行总结")
    public String summarizeWebPage(
            @McpToolParam(description = "要总结网页url") String url
    ) {

        // 1. 获取钉钉 Header
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String userId = request.getHeader("X-DingTalk-User-Id");
            String jobNumber = request.getHeader("X-DingTalk-User-Job-Number");

            System.out.println("userId=" + userId + ", jobNumber=" + jobNumber);
        }

        // 2. Tool 只做兜底或快速总结
        return "请结合 webpage://content Resource 和 web_summary_prompt 进行总结";
    }
}
