package njnu.edu.back.service.impl;

import lombok.SneakyThrows;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.FileMapper;
import njnu.edu.back.proj.File;
import njnu.edu.back.proj.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:45
 * @Description:
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Value("${basedir}")
    String basedir;

    @Autowired
    RedisService redisService;

    @Override
    public void addFile(AddFileDTO addFileDTO, String email) {
        addFileDTO.setUploader(email);
        addFileDTO.setId(UUID.randomUUID().toString());
        fileMapper.addFile(addFileDTO);
    }

    @Override
    public List<Map<String, Object>> findByLevel(int level, String email) {
        return fileMapper.findByLevel(level, email);
    }

    @Override
    public List<Map<String, Object>> findByParentId(String parentId) {
        return fileMapper.findByParentId(parentId);
    }

    @Override
    public List<String> getNoUpload(String MD5, String email, int total) {
        return LocalUploadUtil.getNoUploadChunk(MD5, basedir + email, total);
    }

    @Override
    public void uploadFile(MultipartFile multipartFile, String MD5, String email, String name) {
        String dir = basedir + email;
        LocalUploadUtil.UploadFile(multipartFile, name, dir, MD5);
    }

    @Override
    public String mergeFile(String email, String MD5, String type, String name, int total, int level, String parentId, String meta) {
        String uuid = UUID.randomUUID().toString();
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        String from = basedir + email + "\\temp\\" + MD5;
        String to = basedir + email + "\\upload\\" + type + "\\" + uuid + "." + suffix;
        String key = UUID.randomUUID().toString();
        redisService.set(key, 0, 24*60*3l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                int state = LocalUploadUtil.merge(from, to, total);
                if(state == 1) {
                    redisService.set(key, 1, 24*60*3l);
                    AddFileDTO addFileDTO = new AddFileDTO(uuid, name, to, uuid + "." + suffix, level, parentId, email, meta, false);
                    fileMapper.addFile(addFileDTO);
                } else {
                    redisService.set(key, -1, 24*60*3l);
                }
                LocalUploadUtil.DeleteFolder(from);
            }
        }.start();
        return key;
    }

    @Override
    public int checkMergeState(String key) {
        int state = (int) redisService.get(key);
        redisService.del(key);
        return state;
    }

    @Override
    public void rename(String id, String name) {
        fileMapper.rename(id, name);
    }

    @Override
    public void deleteFile(String id) {
        File file = fileMapper.findById(id);
        String address = file.getAddress();
        LocalUploadUtil.DeleteFolder(address);
        fileMapper.delete(id);
    }
}
