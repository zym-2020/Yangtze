package njnu.edu.back.service.impl;

import njnu.edu.back.pojo.dto.AddMessageDTO;
import njnu.edu.back.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import njnu.edu.back.dao.main.MessageMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public void addMessage(AddMessageDTO addMessageDTO, String id,String email) {
        addMessageDTO.setFileId(id);
        addMessageDTO.setMessageReceiver(email);
        addMessageDTO.setId(UUID.randomUUID().toString());
        messageMapper.addMessage(addMessageDTO);
    }

    @Override
    public void addResponseMessage(AddMessageDTO addMessageDTO, String id,String email) {
        addMessageDTO.setFileId(id);
        addMessageDTO.setMessageReceiver(email);
        addMessageDTO.setId(UUID.randomUUID().toString());
        messageMapper.addResponseMessage(addMessageDTO);
    }

    @Override
    public Map<String, Object> pageQuerys(int page, int size, String property, boolean flag) {
        Map<String, Object> result = new HashMap<>();
        //result.put("total", MessageMapper.countFuzzyQuery(keyWords));
        if (flag) {
            result.put("list", messageMapper.pageQueryASC(size, page * size, property));
        } else {
            result.put("list", messageMapper.pageQueryDESC(size, page * size, property));
        }
        return result;
    }

    @Override
    public Map<String, Object> QueryByType(String property) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByType(property));
        return result;
    }

    @Override
    public Map<String, Object> QueryByUser(String property, String type) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByUser(property, type));
        return result;
    }

    @Override
    public Map<String, Object> QueryByReceiver(String property) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByReceiver(property));
        return result;
    }

    @Override
    public Map<String, Object> QueryByTime(String property) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByTime(property));
        return result;
    }

    @Override
    public Map<String, Object> QueryByUserEmail(String email) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByUserEmail(email));
        return result;
    }

    @Override
    public Map<String, Object> QueryByUserType(String property,String email) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryByUserType(property,email));
        return result;
    }

    @Override
    public void offlineMessage(String property,String dataUploadTime,String email){
        messageMapper.offlineMessage(property,email,dataUploadTime);
    }

    @Override
    public void offlineUserMessage(String property,String dataUploadTime,String email){
        messageMapper.offlineUserMessage(property,email,dataUploadTime);
    }

    @Override
    public Map<String, Object> QueryHistoryMessage(String email) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryHistoryMessage(email));
        return result;
    }

    @Override
    public Map<String, Object> QueryAllHistoryMessage() {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.QueryAllHistoryMessage());
        return result;
    }

    @Override
    public Map<String, Object> showMessageDetails(String property) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", messageMapper.showMessageDetails(property));
        return result;
    }
    @Override
    public void responseMessage(String response,String id){
        messageMapper.responseMessage(response,id);
    }
}


