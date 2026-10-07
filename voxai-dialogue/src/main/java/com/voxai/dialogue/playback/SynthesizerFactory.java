package com.voxai.dialogue.playback;

import com.voxai.communication.common.ChatSession;
import com.voxai.ai.tts.TtsService;

/**
 * Synthesizer 工厂，创建对应的 Synthesizer 实现。
 */
public class SynthesizerFactory {

    public static Synthesizer create(ChatSession session, TtsService ttsService, Player player) {
        return new FileSynthesizer(session, ttsService, player);
    }
}
