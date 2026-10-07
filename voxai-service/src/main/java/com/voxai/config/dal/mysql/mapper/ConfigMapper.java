package com.voxai.config.dal.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voxai.config.dal.mysql.dataobject.ConfigDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper extends BaseMapper<ConfigDO> {
}
