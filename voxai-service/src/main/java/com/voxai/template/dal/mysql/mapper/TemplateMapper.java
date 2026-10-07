package com.voxai.template.dal.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voxai.template.dal.mysql.dataobject.TemplateDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateMapper extends BaseMapper<TemplateDO> {
}
