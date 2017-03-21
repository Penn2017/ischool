import com.imis.jxufe.user.mapper.UserMapper;
import com.imis.jxufe.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author zhongping
 * @date 2017/3/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-main.xml")
public class MapperTest {


    @Autowired
    private ApplicationContext cxt;

    private Logger LOGGER = LoggerFactory.getLogger(MapperTest.class);


    @Test
    public void test1(){

        UserMapper userMapper = cxt.getBean(UserMapper.class);
        List<User> users = userMapper.selectAll();
        LOGGER.debug("=================="+users);


    }
}
