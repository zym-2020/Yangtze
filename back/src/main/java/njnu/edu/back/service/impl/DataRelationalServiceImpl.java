package njnu.edu.back.service.impl;

import njnu.edu.back.dao.main.DataRelationalMapper;
import njnu.edu.back.service.DataRelationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/30/10:33
 * @Description:
 */
@Service
public class DataRelationalServiceImpl implements DataRelationalService {
    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Override
    public void addRelational(String dataListId, List<String> fileIdList) {
        dataRelationalMapper.batchInsert(fileIdList, dataListId);
    }

    @Override
    public void updateRelational(String dataListId, List<String> fileList) {
        List<Map<String, Object>> maps = dataRelationalMapper.findFileIdByDataListId(dataListId);
        List<String> adds = new ArrayList<>();
        List<String> removes = new ArrayList<>();
        for(int i = 0; i < fileList.size(); i++) {
            for(int j = 0; j < maps.size(); j++) {
                if(fileList.get(i).equals(maps.get(j).get("fileId"))) {
                    fileList.remove(i);
                    maps.remove(j);
                    i = i - 1;
                    j = j - 1;
                    break;
                }
            }
        }
        for(int i = 0; i < maps.size(); i++) {
            removes.add((String) maps.get(i).get("fileId"));
        }
        for(int i = 0; i < fileList.size(); i++) {
            adds.add(fileList.get(i));
        }
        if(adds.size() > 0) {
            dataRelationalMapper.batchInsert(adds, dataListId);
        }
        if(removes.size() > 0) {
            dataRelationalMapper.batchDelete(removes, dataListId);
        }
    }
}
