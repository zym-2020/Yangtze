package njnu.edu.back.service.impl;

import njnu.edu.back.pojo.dto.AddMessageDTO;
import njnu.edu.back.service.DownloadService;
import njnu.edu.back.service.MessageService;
import njnu.edu.back.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import njnu.edu.back.dao.MessageMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UploadService uploadService;
    @Autowired
    DownloadService downloadService;

    @Override
    public void addMessage(AddMessageDTO addMessageDTO, String id) {
        addMessageDTO.setMessageId(id);
        addMessageDTO.setId(UUID.randomUUID().toString());
        messageMapper.addMessage(addMessageDTO);
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
}


