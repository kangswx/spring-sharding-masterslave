import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ShardingTest {

    @Resource(name = "msDataSource")
    private DataSource dataSource;

    /**
     * 利用ShardingJDBC的读写分离进行写入数据的操作
     * @throws SQLException
     */
    @Test
    public void testInsert() throws SQLException {
        Connection connection = dataSource.getConnection();
        String sql = "INSERT INTO t_user (id, username, password, birthday) VALUES (null, 'test insert', '134', '2019-07-23 14:52:05')";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    /**
     * 利用ShardingJDBC的读写分离进行数据读取的操作
     * @throws SQLException
     */
    @Test
    public void selectTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        String sql = "select * from t_user";
        for (int i = 0; i < 10; i++) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();
            preparedStatement.close();
        }
        connection.close();
    }
}
