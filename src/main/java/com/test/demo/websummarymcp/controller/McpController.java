package com.test.demo.websummarymcp.controller;


import com.test.demo.websummarymcp.service.SummaryService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
public class McpController {

    private final SummaryService summaryService;

    public McpController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    /* =========================
       健康检查
       ========================= */
    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of("status", "ok");
    }

    /* =========================
       MCP Root（钉钉隐式必需）
       ========================= */
    @GetMapping("/mcp")
    public Map<String, Object> mcp() {
        return Map.of(
                "protocol", "mcp",
                "version", "1.0",
                "capabilities", Map.of(
                        "tools", true,
                        "resources", false
                )
        );
    }

    /* =========================
       Manifest
       ========================= */
    @GetMapping("/manifest")
    public Map<String, Object> manifest() {
        return Map.of(
                "name", "summary-mcp",
                "version", "1.0.0",
                "description", "Summarize webpage content by URL",
                "tools", List.of(
                        Map.of(
                                "name", "summarize_webpage",
                                "description", "Extract and summarize a webpage",
                                "input_schema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "url", Map.of("type", "string")
                                        ),
                                        "required", List.of("url")
                                )
                        )
                )
        );
    }

    /* =========================
       Tools 列表
       ========================= */
    @GetMapping("/tools")
    public Map<String, Object> tools() {
        return Map.of(
                "tools", List.of(
                        Map.of(
                                "name", "summarize_webpage",
                                "description", "Extract and summarize a webpage",
                                "inputSchema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "url", Map.of("type", "string")
                                        ),
                                        "required", List.of("url")
                                )
                        )
                )
        );
    }

    /* =========================
       Tool Call（JSON + SSE）
       ========================= */
    @PostMapping(value = "/tools/call",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object callTool(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Accept", required = false) String accept
    ) {
        String name = (String) body.get("name");
        Map<String, Object> args = (Map<String, Object>) body.get("arguments");

        if (!"summarize_webpage".equals(name)) {
            return Map.of("error", "ToolNotFound");
        }

        String url = args == null ? null : (String) args.get("url");
        if (url == null) {
            return Map.of("error", "Missing url");
        }

        // ------------------------
        // 钉钉检测：JSON 直返
        // ------------------------
        if (accept == null || !accept.contains("text/event-stream")) {
            return Map.of(
                    "type", "tool_result",
                    "tool_name", "summarize_webpage",
                    "result", Map.of(
                            "summary", summaryService.summarize(url)
                    )
            );
        }

        // ------------------------
        // StreamableHTTP（SSE）
        // ------------------------
        return Flux.just(
                ServerSentEvent.builder(
                        Map.of(
                                "type", "tool_result",
                                "tool_name", "summarize_webpage",
                                "result", Map.of(
                                        "summary", summaryService.summarize(url)
                                )
                        )
                ).build()
        ).delayElements(Duration.ofMillis(100));
    }
}
