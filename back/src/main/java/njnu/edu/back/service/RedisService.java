package njnu.edu.back.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:16
 * @Description:
 */
public interface RedisService {
    void set(String key, Object obj);

    void set(String key, Object obj, Long time);

    Object get(String key);

    void del(String key);

    void expire(String key, Long time);
}
