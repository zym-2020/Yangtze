package njnu.edu.back.service.impl;

import njnu.edu.back.dao.main.AnalyticParameterMapper;
import njnu.edu.back.service.AnalyticParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/17/17:24
 * @Description:
 */
@Service
public class AnalyticParameterServiceImpl implements AnalyticParameterService {
    @Autowired
    AnalyticParameterMapper analyticParameterMapper;

    @Override
    public List<Map<String, Object>> findByType(String type) {
        return analyticParameterMapper.findByType(type);
    }
}
