package tests;

import bankapp.JDBC;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTest {

    @Test
    void getConnection(){
        Connection conn = JDBC.getConnection();

        try {
            assertFalse(conn.isClosed());
        }catch (Exception e) {
            fail();
        }
    }

}