package com.imis.jxufe.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongping
 * @date 2017/3/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:redis/spring-redis.xml"}
)

public class RedisServiceTest {


    @Autowired
    private ApplicationContext cxts;
    private Logger LOGGER = LoggerFactory.getLogger(RedisServiceTest.class);

    @Test
    public void setMethod() throws Exception {
        RedisService re = cxts.getBean(RedisService.class);
        re.setexpire("aa", "9", 33);
    }

    @Test
    public void setObjectTest(){
        RedisService re = cxts.getBean(RedisService.class);
        Map map = new HashMap();
        map.put("aa", "ff");
        map.put("rr", "ff");
        map.put("re", "ff");
        re.setObject("kk", map );
    }

    @Test
    public void gsetObjectTest(){
        RedisService re = cxts.getBean(RedisService.class);
        LOGGER.debug("======"+re.get("kk"));
        LOGGER.debug("======>"+re.getObject("kk",Map.class));
    }


}