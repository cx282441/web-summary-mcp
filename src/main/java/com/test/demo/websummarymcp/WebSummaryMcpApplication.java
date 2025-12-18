package com.test.demo.websummarymcp;

import com.test.demo.websummarymcp.ai.WebSummaryTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebSummaryMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSummaryMcpApplication.class, args);
	}

}
