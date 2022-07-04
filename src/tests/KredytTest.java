package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KredytTest {

    @Test
    void przeksztalcanie() {
        int[] expected = new int[]{0, 0, 0, 0, 1, 10, 110, 100, 123, -1};
        double[] actual = new double[]{0.0, 0, 0.001, 0.009, 0.01, 0.1, 1.1, 1.00, 1.23, -0.01};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], Kredyt.przeksztalcanie(actual[i]), "Test zakonczony niepowodzeniem dla danej wejsciowej o indeksie: " + i);
        }
    }

    @Test
    void kwotaDoSplaty() {
        Kredyt testowyKredyt = new Kredyt();

        testowyKredyt.setKwota(1000);
        testowyKredyt.setOprocentowanieKredytu(2.1);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.kwotaDoSplaty(testowyKredyt);

        assertEquals(1021, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");

        testowyKredyt.setKwota(0);
        testowyKredyt.setOprocentowanieKredytu(4.3);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.kwotaDoSplaty(testowyKredyt);

        assertEquals(0, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");

        testowyKredyt.setKwota(13240);
        testowyKredyt.setOprocentowanieKredytu(0);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.kwotaDoSplaty(testowyKredyt);

        assertEquals(13240, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");


        testowyKredyt.setKwota(-240);
        testowyKredyt.setOprocentowanieKredytu(2.0);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.kwotaDoSplaty(testowyKredyt);

        assertEquals(-244.8, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");
    }

    @Test
    void dataNastepnejRaty() throws SQLException {
        Kredyt testowyKredyt = new Kredyt();
        SQL_driver sql_driver = new SQL_driver();
        Date date = (Date) sql_driver.returnData();

        testowyKredyt.dataNastepnejRaty(testowyKredyt);
        assertEquals(Time.addMonth(date, 1), testowyKredyt.getDataNastepnejRaty());    // Test zalezny od pozytywnego wyniku testu metody Time.addMoth !!!!!!
    }

    @Test
    void wysokoscRaty() {
        Kredyt testowyKredyt = new Kredyt();

        testowyKredyt.setWysokoscRaty(0);
        testowyKredyt.setPozostalaLiczbaRat(12);
        testowyKredyt.setKwotaDoSplaty(1200.12);
        testowyKredyt.wysokoscRaty(testowyKredyt);

        assertEquals(100.01, testowyKredyt.getWysokoscRaty(), "blad obliczen");

        testowyKredyt.setWysokoscRaty(0);
        testowyKredyt.setPozostalaLiczbaRat(12);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.wysokoscRaty(testowyKredyt);

        assertEquals(0, testowyKredyt.getWysokoscRaty(), "blad obliczen");

        testowyKredyt.setWysokoscRaty(0);
        testowyKredyt.setPozostalaLiczbaRat(12);
        testowyKredyt.setKwotaDoSplaty(-1200.12);
        testowyKredyt.wysokoscRaty(testowyKredyt);

        assertEquals(-100.01, testowyKredyt.getWysokoscRaty(), "blad obliczen");
    }

    @Test
    void obliczRataPrzycisk() throws SQLException {
        SQL_driver sql_driver = new SQL_driver();
        Kredyt testowyKredyt = new Kredyt();

        double procent = sql_driver.returnKredytProcent();

        sql_driver.insertNewKredytProcent(2.5);
        assertEquals(85.42, testowyKredyt.obliczRataPrzycisk(1000, 12));

        sql_driver.insertNewKredytProcent(0.52);
        assertEquals(33.51, testowyKredyt.obliczRataPrzycisk(100, 3));

        sql_driver.insertNewKredytProcent(procent);
    }

    @Test
    void pomniejszenieKwotyDoSplaty() {
        Kredyt testowyKredyt = new Kredyt();

        testowyKredyt.setWysokoscRaty(123.45);
        testowyKredyt.setKwotaDoSplaty(1123.45);
        testowyKredyt.pomniejszenieKwotyDoSplaty(testowyKredyt);

        assertEquals(1000, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");

        testowyKredyt.setWysokoscRaty(123.45);
        testowyKredyt.setKwotaDoSplaty(2355.13);
        testowyKredyt.pomniejszenieKwotyDoSplaty(testowyKredyt);

        assertEquals(2231.68, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");

        testowyKredyt.setWysokoscRaty(0);
        testowyKredyt.setKwotaDoSplaty(0);
        testowyKredyt.pomniejszenieKwotyDoSplaty(testowyKredyt);

        assertEquals(0, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");

        testowyKredyt.setWysokoscRaty(54.54);
        testowyKredyt.setKwotaDoSplaty(54.54);
        testowyKredyt.pomniejszenieKwotyDoSplaty(testowyKredyt);

        assertEquals(0, testowyKredyt.getKwotaDoSplaty(), "blad obliczen");
    }

    @Test
    void przyznanieKredytu() {
    }

    @Test
    void splataRaty() {
    }
}