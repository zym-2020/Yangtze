package njnu.edu.back.service.impl;

import njnu.edu.back.dao.monitor.*;
import njnu.edu.back.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/14:32
 * @Description:
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    SectionMapper sectionMapper;

    @Autowired
    VerticalLineMapper verticalLineMapper;

    @Autowired
    SamplePointMapper samplePointMapper;

    @Autowired
    VelocityDirectionResultMapper velocityDirectionResultMapper;

    @Autowired
    SandContentResultMapper sandContentResultMapper;

    @Autowired
    FluxResultMapper fluxResultMapper;

    @Autowired
    LocusResultMapper locusResultMapper;

    @Override
    public List<Map<String, Object>> getAllSection() {
        return sectionMapper.getAllSection();
    }

    @Override
    public List<Map<String, Object>> getAllVerticalLine() {
        return verticalLineMapper.getAllVerticalLine();
    }

    @Override
    public List<Map<String, Object>> getAllSamplePoint() {
        return samplePointMapper.getAllSamplePoint();
    }

    @Override
    public List<Map<String, Object>> getAllVelocityDirection() {
        return velocityDirectionResultMapper.getAllResult();
    }

    @Override
    public List<Map<String, Object>> getAllVelocityDirectionByTypeAndNameAndTime(String type, String name, String startTime, String endTime) {
        if (name.length() >= 4) {
            name = "S-" + name;
        } else {
            name = "T-" + name;
        }
        return velocityDirectionResultMapper.getAllResultByTypeAndNameAndTime(type, name, startTime, endTime);
    }

    @Override
    public List<String> getDistanceByName(String name) {
        return velocityDirectionResultMapper.getDistanceByName(name);
    }

    @Override
    public List<Map<String, Object>> getAllVelocityDirectionByTypeAndNameAndDistanceAndTime(String type, String name, String distance, String startTime, String endTime) {
        if (name.length() >= 4) {
            name = "S-" + name;
        } else {
            name = "T-" + name;
        }
        return velocityDirectionResultMapper.getAllResultByTypeAndNameAndDistanceAndTime(type, name, distance, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getAllSandContent() {
        return sandContentResultMapper.getAllResult();
    }

    @Override
    public List<Map<String, Object>> getAllSandContentByName(String name) {
        return sandContentResultMapper.getAllResultByName(name);
    }

    @Override
    public List<Map<String, Object>> getAllSandContentByTimeAndType(String type, String startTime, String endTime) {
        return sandContentResultMapper.getAllResultByTimeAndType(startTime, endTime, type);
    }

    @Override
    public List<Map<String, Object>> getAllSandContentByNameAndTime(String name, String startTime, String endTime) {
        return sandContentResultMapper.getAllResultByNameAndTime(name, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getAllFlux() {
        return fluxResultMapper.getAllResult();
    }

    @Override
    public List<Map<String, Object>> getAllFluxByTypeAndName(String type, String name) {
        return fluxResultMapper.getAllResultByTypeAndName(type, name);
    }

    @Override
    public List<Map<String, Object>> getAllFluxByTypeAndNameAndTime(String type, String name, String startTime, String endTime) {
        return fluxResultMapper.getAllResultByTypeAndNameAndTime(type, name, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getAllLocus() {
        return locusResultMapper.getAllLocus();
    }

    @Override
    public List<Map<String, Object>> getAllLocusByName(String name) {
        return locusResultMapper.getAllLocusByName(name);
    }

    @Override
    public List<Map<String, Object>> getAllLocusByTime(String startTime, String endTime) {
        return locusResultMapper.getAllLocusByTime(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getAllLocusByNameAndTime(String name, String startTime, String endTime) {
        return locusResultMapper.getAllLocusByNameAndTime(name, startTime, endTime);
    }
}
