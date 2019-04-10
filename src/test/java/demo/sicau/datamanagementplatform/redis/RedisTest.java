package demo.sicau.datamanagementplatform.redis;

import demo.sicau.datamanagementplatform.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 15:50 2018/10/30
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void StringTest(){
//        redisUtil.set("123","asnidn",5*60);
        System.out.println(redisUtil.get("123"));
    }
}
