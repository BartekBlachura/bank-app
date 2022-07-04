package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LokataTest {

    @Test
    void dataKoncowa() throws SQLException {
        Lokata testowaLokata = new Lokata();
        SQL_driver sql_driver = new SQL_driver();
        Date date = (Date) sql_driver.returnData();

        testowaLokata.dataKoncowa(testowaLokata, 12);
        assertEquals(Time.addMonth(date, 12), testowaLokata.getDataZamkniecia());    // Test zalezny od pozytywnego wyniku testu metody Time.addMoth !!!!!!

        testowaLokata.dataKoncowa(testowaLokata, 0);
        assertEquals(Time.addMonth(date, 0), testowaLokata.getDataZamkniecia());    // Test zalezny od pozytywnego wyniku testu metody Time.addMoth !!!!!!

        testowaLokata.dataKoncowa(testowaLokata, 45);
        assertEquals(Time.addMonth(date, 45), testowaLokata.getDataZamkniecia());    // Test zalezny od pozytywnego wyniku testu metody Time.addMoth !!!!!!
    }

    @Test
    void sprawdzenieLokat() {   // bez niezmiennej bazy testowej nie do przetestowania (?)
    }

    @Test
    void obliczZyskPrzycisk() throws SQLException {
        SQL_driver sql_driver = new SQL_driver();
        Lokata testowaLokata = new Lokata();

        double procent = sql_driver.returnLokataProcent();

        sql_driver.insertNewLokataProcent(2.5);
        assertEquals(10.46, testowaLokata.obliczZyskPrzycisk(1000, 5));

        sql_driver.insertNewLokataProcent(0.52);
        assertEquals(0.12, testowaLokata.obliczZyskPrzycisk(100, 3));

        sql_driver.insertNewLokataProcent(procent);
    }

    @Test
    void zalozenieLokaty() throws SQLException, TransferException, DatabaseException {              //testy zależne od poprawnego działania metod Customer.Transfer()
        Lokata testowaLokata = new Lokata();                                                        //oraz Time.addMonth() !!!!!
        Customer customer = new Customer();
        SQL_driver sql_driver = new SQL_driver();
        customer.logIn("LokataTest");
        int stareIdLokaty = sql_driver.lastIDLokat();
        double procent = sql_driver.returnLokataProcent();

        assertEquals("lokata została założona", testowaLokata.zalozenieLokaty(customer, 10000, 12));

        Lokata_DB testowaLokata_DB = sql_driver.returnLokata(stareIdLokaty + 1);

        assertEquals(stareIdLokaty + 1, sql_driver.lastIDLokat());
        assertEquals(customer.getAccountNo(), testowaLokata_DB.getNr_konta_klienta());
        assertEquals(procent, testowaLokata_DB.getProcent());
        assertEquals(sql_driver.returnData(), testowaLokata_DB.getData_zalozenia());
        assertEquals(Time.addMonth(sql_driver.returnData(), 12), testowaLokata_DB.getData_zamkniecia());
        assertEquals(10000, testowaLokata_DB.getAktualne_srodki());
        assertEquals(sql_driver.returnData(), testowaLokata_DB.getData_ostatniej_kapitalizacji());
        sql_driver.updateClientBalance(customer.getAccountNo(), 10000);

        stareIdLokaty = sql_driver.lastIDLokat();
        assertEquals("brak wystarczających środków na koncie", testowaLokata.zalozenieLokaty(customer, 10000.01, 2));
        assertEquals(stareIdLokaty, sql_driver.lastIDLokat());                                                       //jeśli ID się zmieni, lokata została błędnie założona
    }

    @Test
    void wyplataLokaty() throws SQLException, TransferException, DatabaseException {
        Lokata testowaLokata = new Lokata();
        Customer customer = new Customer();
        SQL_driver sql_driver = new SQL_driver();
        customer.logIn("LokataTest");

        testowaLokata.zalozenieLokaty(customer, 10000, 1);                     //test zależny od poprawnego działania metody Lokata.zalozenieLokaty()
                                                                                                    //z uwagi na brak bazy testowej
        assertEquals("wypłacono środki", testowaLokata.wyplataLokaty(sql_driver.lastIDLokat()));

        Lokata_DB testowaLokata_DB = sql_driver.returnLokata(sql_driver.lastIDLokat());
        assertEquals(sql_driver.returnData(), testowaLokata_DB.getData_zamkniecia());
        assertEquals(0, testowaLokata_DB.getAktualne_srodki());
    }
}