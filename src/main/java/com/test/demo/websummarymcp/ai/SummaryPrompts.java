package com.test.demo.websummarymcp.ai;

import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SummaryPrompts {

    @McpPrompt(
            name = "web_summary_prompt",
            description = "用于总结网页内容的提示模板"
    )
    public List<Message> webSummaryPrompt() {

        return List.of(
                new SystemMessage(
                        "你是一个专业的网页内容分析与总结助手，输出应简洁、准确、结构清晰"
                ),
                new UserMessage(
                        """
                        请对下面的网页内容进行总结：
                        1. 核心主题
                        2. 关键要点（不超过5条）
                        3. 一句话总结
        
                        网页内容：
                        {{content}}
                        """
                )
        );
    }
}

