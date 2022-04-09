package njnu.edu.back.service;

import com.google.common.collect.Multimap;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/11:25
 * @Description:
 */
public interface UploadService {
    void uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    Map<String, Object> getUploadId(String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams, int totalPart, String md5, String account) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException;

    String mergeMultipartUpload(String objectName, String uploadId, int totalPart, String md5, String account) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException;

    int checkMergeStatus(String uuid);
}
