import com.imis.jxufe.user.facade.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author zhongping
 * @date 2017/3/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:user-spring-main.xml")
public class MapperTest {


    @Autowired
    private ApplicationContext cxt;

    @Resource(name = "userService")
    private UserService userService;



    private Logger LOGGER = LoggerFactory.getLogger(MapperTest.class);


    @Test
    public void test1(){
        boolean a = userService.userIsExist("5");
        LOGGER.debug("logger *************************" + a);
    }
}
