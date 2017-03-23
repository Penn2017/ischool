package com.imis.jxufe.redis.facade.impl;

import com.google.gson.Gson;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.function.Function;


public class RedisServiceFacadeImpl implements RedisServiceFacade {

    /**
     * 设置成功标识符
     */
    public static final String SET_SUCCESS = "ok";

    /**
     * 序列化工具
     **/
    private Gson gson = new Gson();


    private ShardedJedisPool shardedJedisPool;

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }
    protected static final Logger logger = LoggerFactory.getLogger(RedisServiceFacadeImpl.class);


    private <T> T execute(Function<ShardedJedis, T> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.apply(shardedJedis);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }

     @Override
    public boolean set(final String key, final String value) {
        String result = this.execute((redis) -> redis.set(key, value));
        if (StringUtils.equalsIgnoreCase(SET_SUCCESS, result)) {
            logger.debug("set key:"+key+" value:"+value);
            return true;
        }
        return false;
    }

    @Override
    public String get(final String key) {
        String value = null;
        try {
            value = this.execute((redis) -> redis.get(key));
        } catch (Exception e) {
            logger.error("key is not exist :" + key);
        }
        return value;
    }

    @Override
    public boolean del(final String key) {
        Long updateNum = this.execute((redis) -> redis.del(key));
        return updateNum == null ? false : updateNum > 0;
    }

    @Override
    public boolean expire(final String key, final Integer seconds) {
        Long updateNum = this.execute((redis) -> redis.expire(key, seconds));
        return updateNum == null ? false : updateNum > 0;
    }

    @Override
    public boolean setexpire(final String key, final String value, final Integer seconds) {
        String execute = this.execute((redis) -> redis.setex(key, seconds, value));
        if (StringUtils.equalsIgnoreCase(SET_SUCCESS, execute)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean setObject(final String key, final Object src) {
        if (src == null) {
            logger.error("object cant be null!");
            return false;
        }
        String value = gson.toJson(src);
        return this.set(key, value);

    }

    @Override
    public boolean setObjectExpire(final String key, final Object src, Integer seconds) {
        if (src == null) {
            logger.error("object cant be null!");
            return false;
        }
        String value = gson.toJson(src);
        return this.setexpire(key, value, seconds);
    }

    @Override
    public <T> T getObject(final String key, Class<T> clazz) {
        T value = null;
        String result = this.get(key);
        if (result != null) {
            value = gson.fromJson(result, clazz);
        }
        return value;

    }


}
