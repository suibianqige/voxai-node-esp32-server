package com.voxai.dialogue.llm.handler;

import com.voxai.ai.llm.memory.Conversation;
import com.voxai.dialogue.runtime.DialogueTurn;
import com.voxai.dialogue.runtime.PersonaListener;
import com.voxai.common.SerialTaskRegistry;
import com.voxai.dialogue.runtime.convert.DialogueTurnConverter;
import com.voxai.message.service.MessageService;
import com.voxai.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
/**
 * PersonaListener 的 Spring 管理实现（基础设施层）。
 * 负责对话消息持久化。
 */
@Slf4j
@Component
public class DialogueListener implements PersonaListener {

    @Resource
    private MessageService messageService;

    @Resource
    private DialogueTurnConverter dialogueTurnConverter;

    /**
     * 落库排进本会话队列执行：调用线程是 LLM 流的事件循环线程，不能在上面开阻塞事务。
     * <p>
     * 用户音频路径也在这一刻才从 DialogueTurn 取：落盘任务在 STT 出终稿时就排进了同一条队列、
     * 排在本任务之前，执行到这里时路径已回填，不用等也不会丢。
     */
    @Override
    public void onDialogueTurn(DialogueTurn turn) {
        SerialTaskRegistry.submit(turn.getConversation().getSessionId(), () -> {
            try {
                messageService.saveAll(dialogueTurnConverter.toMessages(turn));
            } catch (Exception e) {
                log.error("对话持久化失败", e);
            }
        });
    }

    @Override
    public void onDialogueTurnTruncated(Conversation conversation, Instant assistantMessageCreatedAt, String spokenText) {
        LocalDateTime createdAt = DateUtils.toDateTime(assistantMessageCreatedAt);
        SerialTaskRegistry.submit(conversation.getSessionId(), () -> {
            try {
                messageService.truncateAssistant(conversation.getOwnerId(), conversation.getRoleId(),
                        createdAt, spokenText);
            } catch (Exception e) {
                log.error("截断被打断的助手消息失败", e);
            }
        });
    }

    @Override
    public void onError(Throwable error) {
        if (error instanceof WebClientResponseException webErr) {
            log.error("LLM调用失败 status={} body={}",
                    webErr.getStatusCode(), webErr.getResponseBodyAsString(), error);
        } else {
            log.error("LLM调用失败", error);
        }
    }
}
