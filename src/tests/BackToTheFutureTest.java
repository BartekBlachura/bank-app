package tests;

import bankapp.DatabaseException;
import bankapp.SQL_driver;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackToTheFutureTest {
    SQL_driver sqlDriver = new SQL_driver();

    @Test
    void addDays() throws SQLException, DatabaseException {
        long currentDay = sqlDriver.returnData().getTime();
        int days = 15;
        BackToTheFuture.addDays(days);
        assertEquals(currentDay + days * 86400000, sqlDriver.returnData().getTime());
    }
    @Test
    void addDay() throws SQLException, DatabaseException {
        long currentDay = sqlDriver.returnData().getTime();
        BackToTheFuture.addDay();
        assertEquals(currentDay + 86400000, sqlDriver.returnData().getTime());
    }

//    @Test
//    void addDaysTime() throws SQLException, DatabaseException {
//        long currentDay = sqlDriver.returnData().getTime();
//        int days = 10;
//        long startTime = System.currentTimeMillis();
//        BackToTheFuture.addDays(days);
//        long endTime = System.currentTimeMillis();
//        System.out.println(((endTime - startTime)) + " ms");
//        assertEquals(currentDay + days * 86400000, sqlDriver.returnData().getTime());
//    }
}