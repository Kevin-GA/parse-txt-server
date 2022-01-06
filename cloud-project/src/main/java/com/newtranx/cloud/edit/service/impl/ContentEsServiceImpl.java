//package com.newtranx.cloud.edit.service.impl;
//
//import com.newtranx.cloud.edit.entities.ContentEsBean;
//import com.newtranx.cloud.edit.esdao.ContentESDao;
//import com.newtranx.cloud.edit.service.ContentEsService;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Service("ContentEsService")
//public class ContentEsServiceImpl implements ContentEsService {
//
//    @Resource
//    private ContentESDao contentESDao;
//
//    @Override
//    public void save(ContentEsBean bean) {
//        contentESDao.save(bean);
//    }
//
//    @Override
//    public List<ContentEsBean> findByFileId(String fileId) {
//        return contentESDao.findByFileId(fileId);
//    }
//}
