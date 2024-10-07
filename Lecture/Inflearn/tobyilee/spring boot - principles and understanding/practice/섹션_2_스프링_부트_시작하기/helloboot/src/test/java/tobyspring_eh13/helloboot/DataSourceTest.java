package tobyspring_eh13.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@HelloBootTest
public class DataSourceTest {
    @Autowired
    DataSource dataSource;

    @Test
    void connection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
    }
}
