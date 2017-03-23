package com.imis.jxufe.redis.facade;

/**
 * @author zhongping
 * @date 2017/3/22
 */
public interface RedisServiceFacade {

    /**
     * 执行SET操作
     *
     * @param key
     * @param value
     * @return
     */
     boolean set(final String key, final String value);

    /**
     * 执行GET操作
     *
     * @param key
     * @return
     */
     String get(final String key);

    /**
     * 删除key
     *
     * @param key
     * @return
     */
     boolean del(final String key) ;

    /**
     * 设置生存时间，单位为：秒
     *
     * @param key
     * @param seconds
     * @return
     */
     boolean expire(final String key, final Integer seconds) ;

    /**
     * 设置String类型的值，并且指定生存时间，单位为：秒
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
     boolean setexpire(final String key, final String value, final Integer seconds) ;


    /**
     * 设置一个对象
     *
     * @param key
     * @param src
     * @return
     */
     boolean setObject(final String key, final Object src) ;

     boolean setObjectExpire(final String key, final Object src, Integer seconds) ;

    /**
     * 从redis中获取一个对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
     <T> T getObject(final String key, Class<T> clazz) ;


}
