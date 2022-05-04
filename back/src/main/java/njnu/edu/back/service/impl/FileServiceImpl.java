package njnu.edu.back.service.impl;

import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.FileMapper;
import njnu.edu.back.proj.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Override
    public void addFile(AddFileDTO addFileDTO, String email) {
        addFileDTO.setUploader(email);
        fileMapper.addFile(addFileDTO);
    }

    @Override
    public List<Map<String, Object>> findByLevel(int level) {
        return fileMapper.findByLevel(level);
    }

    @Override
    public List<Map<String, Object>> findByParentId(String parentId) {
        return fileMapper.findByParentId(UUID.fromString(parentId));
    }

    @Override
    public List<String> getNoUpload(String MD5, String email, int total) {
        return LocalUploadUtil.getNoUploadChunk(MD5, basedir + email + "\\temp", total);
    }
}
