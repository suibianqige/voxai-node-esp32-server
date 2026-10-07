package com.voxai.operationlog.dal.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voxai.operationlog.dal.mysql.dataobject.OperationLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogDO> {
}
