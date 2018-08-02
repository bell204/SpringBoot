package org.zerock.test;
 
import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)

// root-context.xml에서 불러오는 코드 여기가 문제 생김.
// 스프링 하위 버전과 호환 안 될 때 문제 생김.
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class DataSourceTest {
      
    @Inject
    private DataSource ds;
    
    @Test
    public void testConnection() throws Exception {
        try(Connection conn = ds.getConnection()) {
            System.out.println(conn);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

