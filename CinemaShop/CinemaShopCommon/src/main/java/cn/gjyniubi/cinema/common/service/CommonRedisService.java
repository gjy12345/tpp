package cn.gjyniubi.cinema.common.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CommonRedisService
 */
@Service
@AllArgsConstructor
public class CommonRedisService {

    private final RedisTemplate<String,Object> redisTemplate;

    public void setValue(String key,Object value){
        this.redisTemplate.opsForValue().set(key,value);
    }

    public void setValue(String key, Object value, long time, TimeUnit unit){
        this.redisTemplate.opsForValue().set(key,value,time,unit);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key){
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    /**
     *
     * @param key 键
     * @param onMissValueAction key失效时返回值
     * @param <T> 返回值
     * @return 缓存
     */
    public <T> T getValue(String key,OnMissValueAction<T> onMissValueAction){
        T data=getValue(key);
        return data==null?onMissValueAction.onMissValue(key):data;
    }

    public Long getExpire(String redisKey, TimeUnit unit) {
        return redisTemplate.getExpire(redisKey,unit);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public Boolean setExpire(String key, int expire, TimeUnit unit) {
        return redisTemplate.expire(key,expire,unit);
    }

    public interface OnMissValueAction<T>{
        T onMissValue(String key);
    }
}
