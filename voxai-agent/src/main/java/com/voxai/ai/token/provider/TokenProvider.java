package com.voxai.ai.token.provider;

import com.voxai.common.model.bo.ConfigBO;
import com.voxai.ai.token.TokenCache;

import java.util.Collection;

/**
 * 第三方 token 获取策略。
 */
public interface TokenProvider {

    Collection<String> getSupportedProviders();

    TokenCache fetchToken(ConfigBO config);
}
