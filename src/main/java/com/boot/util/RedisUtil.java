package com.boot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * 推荐使用有过期时间的方法
 * 如果不对缓存数据进行操作 使用get set方法即可
 * 如果对缓存进行排序等操作，使用对应的map，list方法
 * @author huguoju
 */
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;

    public void set(final String key,final Object val){
        redisTemplate.opsForValue().set(key,val);
    }
    /**
     * @param key
     * @param val
     * @param time：秒
     */
    public void set(final String key,final Object val,final long time){
        redisTemplate.opsForValue().set(key,val,time, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在，则设置当前key成功，返回1
     * 如果当前key已经存在，则设置当前key失败，返回0
     * @param key
     * @param val
     */
    public boolean setNx(final String key,final Object val){
       return redisTemplate.opsForValue().setIfAbsent(key,val);
    }
    /**
     * 将map的所有(key,value)单独存入redis
     * @param map
     */
    public void multiSet(final Map map){
        redisTemplate.opsForValue().multiSet(map);
    }

    public List multiGet(final List keys){
        return redisTemplate.opsForValue().multiGet(keys);
    }
    /**
     * 在key键对应值的右面追加值value
     * @param key
     * @param val
     */
    public Integer append(final String key,final String val){
        return redisTemplate.opsForValue().append(key,val);
    }

    /**
     * 获取key
     * @param key
     * @return
     */
    public Object get(final String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 将key获取的对象转换为指定对象
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> type) {
        return (T)redisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     * @param key
     * @param newVal
     * @return
     */
    public Object getAndSet(final String key,final Object newVal){
        return redisTemplate.opsForValue().getAndSet(key,newVal);
    }

    /**
     * 缓存map，如使用此方法，获取时使用getMap()方法
     * @param key
     * @param map
     */
    public void setMap(final String key,final Map map){
        delete(key);
        redisTemplate.opsForHash().putAll(key,map);
    }

    /**
     * 缓存map，如使用此方法，获取时使用getMap()方法
     * @param key
     * @param map
     * @param time
     */
    public void setMap(final String key,final Map map,final long time){
        setMap(key,map);
        expire(key,time);
    }

    /**
     * 根据key查询map
     * @param key
     * @return
     */
    public Map getMap(final String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 保存list
     * 成功返回1
     * @param key
     * @param val
     */
    public Long setList(final String key, final List val){
        delete(key);
        return redisTemplate.opsForList().leftPush(key,val);
    }

    public void setList(final String key,final List val,final long time){
        setList(key,val);
        expire(key,time);
    }
    /**
     * 取出list中的key并在缓存中删除
     * @param key
     * @return
     */
    public List getList(final String key){
        return (List) ((List) redisTemplate.opsForList().leftPop(key)).get(0);
    }

    /**
     * 获取list
     * @param key
     * @return
     */
    public List rangeList(final String key){
        return (List)((List) redisTemplate.opsForList().range(key,0,-1)).get(0);
    }

    /**
     * 保存set
     * @param key
     * @param val
     */
    public Long setSet(final String key, final Set val){
        delete(key);
        return redisTemplate.opsForSet().add(key,val);
    }
    public void setSet(final String key,final Set val,final long time){
        setSet(key,val);
        expire(key,time);
    }

    /**
     * 获取set
     * @param key
     * @return
     */
    public Set getSet(final String key){
        LinkedHashSet set= (LinkedHashSet) redisTemplate.opsForSet().members(key);
        Iterator iterator=set.iterator();
        if (iterator.hasNext()){
            return (Set) iterator.next();
        }
        return null;
    }
    /**
     * 是否存在key
     * @param key
     * @return
     */
    public boolean exist(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置key缓存时间
     * @param key
     * @param time：秒
     */
    public boolean expire(final String key,final long time){
        return redisTemplate.expire(key,time,TimeUnit.SECONDS);
    }

    /**
     * 查询key过期时间
     * @param key
     * @return
     */
    public Long getExpire(final String key){
        return redisTemplate.getExpire(key);
    }
    /**
     * 删除缓存
     * @param key
     */
    public void delete(final String key){
        if (exist(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * 返回key所对应的value值得长度
     * @param key
     * @return
     */
    public Long size(final String key){
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 设置key永不过期
     * @param key
     */
    public boolean persist(final String key){
        return redisTemplate.persist(key);
    }

    //todo 分布式锁
}
