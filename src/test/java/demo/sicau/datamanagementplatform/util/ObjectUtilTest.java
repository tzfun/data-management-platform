package demo.sicau.datamanagementplatform.util;

import demo.sicau.datamanagementplatform.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 13:53 2018/11/9
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectUtilTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void objectToMapTest(){
        System.out.println(ObjectTransformUtil.objectToMap(userDao.selectAllUser().get(0)).get("account"));
    }
}
