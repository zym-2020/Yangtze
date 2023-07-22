package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/14:32
 * @Description:
 */
public interface MonitorService {
    List<Map<String, Object>> getAllSection();

    List<Map<String, Object>> getAllVerticalLine();

    List<Map<String, Object>> getAllSamplePoint();

    List<Map<String, Object>> getAllVelocityDirection();

    List<Map<String, Object>> getAllVelocityDirectionByTypeAndNameAndTime(String type, String name, String startTime, String endTime);

    List<String> getDistanceByName(String name);

    List<Map<String, Object>> getAllVelocityDirectionByTypeAndNameAndDistanceAndTime(String type, String name, String distance, String startTime, String endTime);

    List<Map<String, Object>> getAllSandContent();

    List<Map<String, Object>> getAllSandContentByName(String name);

    List<Map<String, Object>> getAllSandContentByTimeAndType(String type, String startTime, String endTime);

    List<Map<String, Object>> getAllSandContentByNameAndTime(String name, String startTime, String endTime);

    List<Map<String, Object>> getAllFlux();

    List<Map<String, Object>> getAllFluxByTypeAndName(String type, String name);

    List<Map<String, Object>> getAllFluxByTypeAndNameAndTime(String type, String name, String startTime, String endTime);

    List<Map<String, Object>> getAllLocus();

    List<Map<String, Object>> getAllLocusByName(String name);

    List<Map<String, Object>> getAllLocusByTime(String startTime, String endTime);

    List<Map<String, Object>> getAllLocusByNameAndTime(String name, String startTime, String endTime);
}
