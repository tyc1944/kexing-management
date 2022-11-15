package org.kexing.management.infrastruction.service.sql_server;

import org.kexing.management.domin.model.sql_server.*;
import org.kexing.management.infrastruction.repository.mybatis.sql_server.ErpTagUIDUploadDataQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lh
 */
@Service
public class JZDeviceService {

    @Autowired
    ErpTagUIDUploadDataQueryMapper erpTagUIDUploadDataQueryMapper;
  public List<JZWeiKeOldDataUploadDate> getLastJZWeiKeOldDataUploadDate(String jzDeviceId) {
    List<TagUIDUploadData> tagUIDUploadData =
        erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
    return JZWeiKeOldDataUploadDate.convert(tagUIDUploadData);
  }
  public List<JZHuNeiDataUploadDate> getLastJZHuNeiDataUploadDate(String jzDeviceId){
      List<TagUIDUploadData> tagUIDUploadData =
              erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
      return JZHuNeiDataUploadDate.convert(tagUIDUploadData);
  }

  public List<JZHuWaiDataUploadDate> getLastJZHuWaiDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZHuWaiDataUploadDate.convert(tagUIDUploadData);
    }

    public List<JZWeiKeNewDataUploadDate> getLastJZWeiKeNewDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZWeiKeNewDataUploadDate.convert(tagUIDUploadData);
    }

    public JZWeiKeOldDataUploadDate getTodayLastJZWeiKeOldDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZWeiKeOldDataUploadDate.screen(tagUIDUploadData);
    }

    public JZHuNeiDataUploadDate getTodayLastJZHuNeiDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZHuNeiDataUploadDate.screen(tagUIDUploadData);
    }

    public JZHuWaiDataUploadDate getTodayLastJZHuWaiDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZHuWaiDataUploadDate.screen(tagUIDUploadData);
    }

    public JZWeiKeNewDataUploadDate getTodayLastJZWeiKeNewDataUploadDate(String jzDeviceId){
        List<TagUIDUploadData> tagUIDUploadData =
                erpTagUIDUploadDataQueryMapper.selectTagUIDUploadDataByTagUIDLike(jzDeviceId);
        return JZWeiKeNewDataUploadDate.screen(tagUIDUploadData);
    }
}
