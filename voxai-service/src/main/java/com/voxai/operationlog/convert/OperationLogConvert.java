package com.voxai.operationlog.convert;

import com.voxai.common.model.bo.OperationLogBO;
import com.voxai.operationlog.dal.mysql.dataobject.OperationLogDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperationLogConvert {

    OperationLogDO toDO(OperationLogBO bo);
}
