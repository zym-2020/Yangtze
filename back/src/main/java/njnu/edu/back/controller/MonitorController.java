package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/9:56
 * @Description:
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    MonitorService monitorService;

    @RequestMapping(value = "/getAllSection", method = RequestMethod.GET)
    public JsonResult getAllSection() {
        return ResultUtils.success(monitorService.getAllSection());
    }

    @RequestMapping(value = "/getAllVerticalLine", method = RequestMethod.GET)
    public JsonResult getAllVerticalLine() {
        return ResultUtils.success(monitorService.getAllVerticalLine());
    }

    @RequestMapping(value = "/getAllSamplePoint", method = RequestMethod.GET)
    public JsonResult getAllSamplePoint() {
        return ResultUtils.success(monitorService.getAllSamplePoint());
    }

    @RequestMapping(value = "/getAllVelocityDirection", method = RequestMethod.GET)
    public JsonResult getAllVelocityDirection() {
        return ResultUtils.success(monitorService.getAllVelocityDirection());
    }

    @RequestMapping(value = "/getAllVelocityDirectionByTypeAndNameAndTime/{type}/{name}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllVelocityDirectionByTypeAndNameAndTime(@PathVariable String type, @PathVariable String name, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllVelocityDirectionByTypeAndNameAndTime(type, name, startTime, endTime));
    }

    @RequestMapping(value = "/getDistanceByName/{name}", method = RequestMethod.GET)
    public JsonResult getDistanceByName(@PathVariable String name) {
        return ResultUtils.success(monitorService.getDistanceByName(name));
    }

    @RequestMapping(value = "/getAllVelocityDirectionByTypeAndNameAndDistanceAndTime/{type}/{name}/{distance}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllVelocityDirectionByTypeAndNameAndDistanceAndTime(@PathVariable String type, @PathVariable String name, @PathVariable String distance, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllVelocityDirectionByTypeAndNameAndDistanceAndTime(type, name, distance, startTime, endTime));
    }

    @RequestMapping(value = "/getAllSandContent", method = RequestMethod.GET)
    public JsonResult getAllSandContent() {
        return ResultUtils.success(monitorService.getAllSandContent());
    }

    @RequestMapping(value = "/getAllSandContentByName/{name}", method = RequestMethod.GET)
    public JsonResult getAllSandContentByName(@PathVariable String name) {
        return ResultUtils.success(monitorService.getAllSandContentByName(name));
    }

    @RequestMapping(value = "/getAllSandContentByTimeAndType/{type}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllSandContentByTimeAndType(@PathVariable String type, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllSandContentByTimeAndType(type, startTime, endTime));
    }

    @RequestMapping(value = "/getAllSandContentByNameAndTime/{name}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllSandContentByNameAndTime(@PathVariable String name, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllSandContentByNameAndTime(name, startTime, endTime));
    }

    @RequestMapping(value = "/getAllFlux", method = RequestMethod.GET)
    public JsonResult getAllFlux() {
        return ResultUtils.success(monitorService.getAllFlux());
    }

    @RequestMapping(value = "/getAllFluxByTypeAndName/{type}/{name}", method = RequestMethod.GET)
    public JsonResult getAllFluxByTypeAndName(@PathVariable String type, @PathVariable String name) {
        return ResultUtils.success(monitorService.getAllFluxByTypeAndName(type, name));
    }

    @RequestMapping(value = "/getAllFluxByTypeAndNameAndTime/{type}/{name}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllFluxByTypeAndNameAndTime(@PathVariable String type, @PathVariable String name, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllFluxByTypeAndNameAndTime(type, name, startTime, endTime));
    }

    @RequestMapping(value = "/getAllLocus", method = RequestMethod.GET)
    public JsonResult getAllLocus() {
        return ResultUtils.success(monitorService.getAllLocus());
    }

    @RequestMapping(value = "/getAllLocusByName/{name}", method = RequestMethod.GET)
    public JsonResult getAllLocusByName(@PathVariable String name) {
        return ResultUtils.success(monitorService.getAllLocusByName(name));
    }

    @RequestMapping(value = "/getAllLocusByTime/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllLocusByTime(@PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllLocusByTime(startTime, endTime));
    }

    @RequestMapping(value = "/getAllLocusByNameAndTime/{name}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getAllLocusByNameAndTime(@PathVariable String name, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(monitorService.getAllLocusByNameAndTime(name, startTime, endTime));
    }
}
