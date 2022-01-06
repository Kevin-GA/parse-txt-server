package com.newtranx.cloud.edit.dao;

import com.newtranx.cloud.edit.entities.DocJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author niujiaxin
 * @since 2021-12-27
 */
public interface DocJobMapper extends BaseMapper<DocJob> {

    int insertReturnId(DocJob docJob);
}
