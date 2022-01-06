package com.newtranx.cloud.edit.service.impl;

import com.newtranx.cloud.edit.entities.DocJob;
import com.newtranx.cloud.edit.dao.DocJobMapper;
import com.newtranx.cloud.edit.service.DocJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author niujiaxin
 * @since 2021-12-27
 */
@Service
public class DocJobServiceImpl extends ServiceImpl<DocJobMapper, DocJob> implements DocJobService {

    @Override
    public int insertReturnId(DocJob docJob) {
        return baseMapper.insertReturnId(docJob);
    }
}
