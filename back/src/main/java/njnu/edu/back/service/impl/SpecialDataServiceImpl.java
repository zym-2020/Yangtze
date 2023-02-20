package njnu.edu.back.service.impl;

import njnu.edu.back.dao.main.SpecialDataMapper;
import njnu.edu.back.service.SpecialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/20/15:54
 * @Description:
 */
@Service
public class SpecialDataServiceImpl implements SpecialDataService {
    @Autowired
    SpecialDataMapper specialDataMapper;

    @Override
    public void addRecord(String id) {
        specialDataMapper.addRecord(id);
    }

    @Override
    public void delRecord(String id) {
        specialDataMapper.delRecord(id);
    }

    @Override
    public List<String> getAllSpecialData() {
        return specialDataMapper.getAllSpecialData();
    }

    @Override
    public List<Map<String, Object>> getIdAndDataListName(int size, int start) {
        List<Map<String, Object>> maps = specialDataMapper.getIdAndDataListName();
        List<Map<String, Object>> result = new ArrayList<>();
        int temp = start;
        if (temp > maps.size()) {
            temp = (temp + 1) % maps.size();
        }
        int count = 0;
        while (count < size && count < maps.size()) {
            result.add(maps.get(temp));
            temp = (temp + 1) % maps.size();
            count++;
        }
        return result;
    }
}
