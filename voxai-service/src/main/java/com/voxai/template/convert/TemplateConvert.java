package com.voxai.template.convert;

import com.voxai.common.model.bo.TemplateBO;
import com.voxai.common.model.req.TemplateCreateReq;
import com.voxai.common.model.req.TemplateUpdateReq;
import com.voxai.common.model.resp.TemplateResp;
import com.voxai.template.dal.mysql.dataobject.TemplateDO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TemplateConvert {

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    TemplateDO toDO(TemplateBO templateBO);

    TemplateBO toBO(TemplateDO templateDO);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    TemplateBO toBO(TemplateCreateReq req);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    TemplateBO toBO(TemplateUpdateReq req);

    TemplateResp toResp(TemplateBO bo);

    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDO(TemplateBO bo, @MappingTarget TemplateDO templateDO);
}
