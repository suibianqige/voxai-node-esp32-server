package com.voxai.message.convert;

import com.voxai.common.model.bo.ConversationBO;
import com.voxai.common.model.bo.MessageBO;
import com.voxai.common.model.bo.MessageMetadataBO;
import com.voxai.common.model.resp.ConversationResp;
import com.voxai.common.model.resp.MessageResp;
import com.voxai.message.dal.mysql.dataobject.ConversationDO;
import com.voxai.message.dal.mysql.dataobject.MessageDO;
import com.voxai.message.model.MessageProjection;
import com.voxai.utils.JsonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public interface MessageConvert {

    MessageBO toBO(MessageDO messageDO);

    MessageDO toDO(MessageBO messageBO);

    MessageResp toResp(MessageProjection projection);

    /** 角色名只在分页 SQL 里关联出来，单表读不带 */
    @Mapping(target = "roleName", ignore = true)
    ConversationBO toBO(ConversationDO conversation);

    ConversationResp toResp(ConversationBO conversation);

    /**
     * DO.metadata (JSON 字符串) → BO.metadata (值对象)。
     * MapStruct 自动识别方法签名并用于 toBO 映射。
     */
    default MessageMetadataBO jsonToMetadata(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return JsonUtil.fromJson(json, MessageMetadataBO.class);
    }

    /**
     * BO.metadata (值对象) → DO.metadata (JSON 字符串)。
     * MapStruct 自动识别方法签名并用于 toDO 映射。
     */
    default String metadataToJson(MessageMetadataBO metadata) {
        if (metadata == null) {
            return null;
        }
        return JsonUtil.toJson(metadata);
    }
}
