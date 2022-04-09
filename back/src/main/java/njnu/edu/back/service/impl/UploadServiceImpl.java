package njnu.edu.back.service.impl;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.CustomMinioClient;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/11:26
 * @Description:
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Value("${minio.endpoint}")
    String ENDPOINT;
    @Value("${minio.bucketName}")
    String BUCKET_NAME;
    @Value("${minio.accessKey}")
    String ACCESS_KEY;
    @Value("${minio.secretKey}")
    String SECRET_KEY;

    @Autowired
    RedisService redisService;

    @Override
    public void uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //创建一个MinIO的Java客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        String filename = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 设置存储对象名称
        String objectName = sdf.format(new Date()) + "/" + filename;
        // 使用putObject上传一个文件到存储桶中
        PutObjectArgs putObjectArgs =PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
        minioClient.putObject(putObjectArgs);
        System.out.println(ENDPOINT + "/" + BUCKET_NAME + "/" + objectName);

    }

    @Override
    public Map<String, Object> getUploadId(String region, String objectName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams, int totalPart, String md5, String account) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        String upload = (String) redisService.get(md5+objectName+account);
        CustomMinioClient client = new CustomMinioClient(minioClient);
        Map<String, Object> result = new HashMap<>();
        //redis中存在缓存，进行断点续传，检查哪些分片已经上传，最后一片默认重新上传
        if(upload != null) {
            try {
                List<Map<String, Object>> urlList = new ArrayList<>();
                ListPartsResponse partsResponse = client.listParts(BUCKET_NAME, null, objectName, totalPart, 0, upload, null, null);
                List<Part> parts = partsResponse.result().partList();
                int flag = 0;
                for(int i = 0;i < totalPart;i++) {
                    if(flag >= parts.size()) {
                        Map<String, Object> temp = new HashMap();
                        temp.put("url", getURL(client, upload, objectName, i));
                        temp.put("number", i);
                        urlList.add(temp);
                    } else if(parts.get(flag).partNumber() != i || parts.get(flag).partSize() != 5242880) {
                        Map temp = new HashMap();
                        temp.put("url", getURL(client, upload, objectName, i));
                        temp.put("number", i);
                        urlList.add(temp);
                        if(parts.get(flag).partNumber() == i) {
                            flag++;
                        }
                    } else {
                        flag++;
                    }

                }
                result.put("uploadId", upload);
                result.put("urlList", urlList);
                return result;
            } catch (Exception e) {
                //出现异常时全部重新上传
                System.out.println(e.getMessage());
                if(isDuplicateName(objectName, client)) {
                    //存在重名
                    throw new MyException(ResultEnum.DUPLICATE_NAME_ERROR);
                }
                String uploadId = client.initMultipartUpload(BUCKET_NAME, region, objectName, headers, extraQueryParams);
                result.put("uploadId", uploadId);
                List<Map<String, Object>> urlList = new ArrayList<>();
                for(int i = 0; i < totalPart; i++) {
                    Map<String, Object> temp = new HashMap();
                    temp.put("url", getURL(client, uploadId, objectName, i));
                    temp.put("number", i);
                    urlList.add(temp);
                }
                redisService.set(md5+objectName+account, uploadId, 60*24*7l);
                redisService.set(objectName, uploadId, 60*24*7l);
                result.put("urlList", urlList);
                return result;
            }
        }

        //缓存中没有记录，重新上传
        if(isDuplicateName(objectName, client)) {
            //存在重名
            throw new MyException(ResultEnum.DUPLICATE_NAME_ERROR);
        }
        String uploadId = client.initMultipartUpload(BUCKET_NAME, region, objectName, headers, extraQueryParams);
        redisService.set(md5+objectName+account, uploadId, 60*24*7l);
        redisService.set(objectName, uploadId, 60*24*7l);
        result.put("uploadId", uploadId);
        List<Map<String, Object>> urlList = new ArrayList<>();
        for(int i = 0; i < totalPart; i++) {
            Map<String, Object> temp = new HashMap();
            temp.put("url", getURL(client, uploadId, objectName, i));
            temp.put("number", i);
            urlList.add(temp);
        }
        result.put("urlList", urlList);
        return result;
    }

    private String getURL(CustomMinioClient client, String uploadId, String objectName, int number) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        reqParams.put("partNumber", String.valueOf(number));
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .method(Method.PUT)
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .expiry(1, TimeUnit.DAYS)
                        .extraQueryParams(reqParams)
                        .build()
        );
    }

    private boolean isDuplicateName(String objectName, MinioClient client) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if(redisService.get(objectName) != null) {
            return true;
        }
        Iterable<Result<Item>> results = client.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(BUCKET_NAME)
                        .prefix(objectName)
                        .build());
        for(Result<Item> result : results) {
            if(result.get().objectName().equals(objectName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String mergeMultipartUpload(String objectName, String uploadId, int totalPart, String md5, String account) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        CustomMinioClient client = new CustomMinioClient(minioClient);
        ListPartsResponse partsResponse = client.listParts(BUCKET_NAME, null, objectName, totalPart, 0, uploadId, null, null);
        UUID uuid = UUID.randomUUID();
        redisService.set(uuid.toString(), 0, 24*60l);

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    ObjectWriteResponse objectWriteResponse = client.completeMultipartUpload(BUCKET_NAME, null, objectName, uploadId, partsResponse.result().partList().toArray(new Part[]{}), null, null);
                } catch (Exception e) {
                    redisService.set(uuid.toString(), -1, 3*60l);
                    System.out.println(e.getMessage());
                }
                redisService.set(uuid.toString(), 1, 3*60l);
                if(redisService.get(md5+objectName+account) != null) {
                    redisService.del(md5+objectName+account);
                }
                if(redisService.get(objectName) != null) {
                    redisService.del(objectName);
                }
            }
        }.start();


        return uuid.toString();
    }

    @Override
    public int checkMergeStatus(String uuid) {
        int status = (int) redisService.get(uuid);
        if(status == 1 || status == -1) {
            redisService.del(uuid);
        }
        return status;
    }
}
