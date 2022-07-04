package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    SQL_driver sqlDriver = new SQL_driver();

    @Test
    void newCustomer() {
        Admin admin = new Admin();
        assertNull(admin.getUserName());
        assertNull(admin.getPassHash());
    }

    @Test
    void logInLogOut() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");

        assertEquals("Admin", admin.getUserName());
        assertEquals("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", admin.getPassHash());

        admin.logOut();

        assertNull(admin.getUserName());
        assertNull(admin.getPassHash());
    }

    @Test
    void transfer() throws SQLException, TransferException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();
        long beforeDate = sqlDriver.returnData().getTime();

        Admin admin = new Admin();
        admin.logIn("Admin");
        double amount = 101.34;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amount) * 100) / 100.00;

        admin.transfer(accountNoReceiver, amount, "testAdmin");

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
        assertEquals(beforeDate + 86400000, sqlDriver.returnData().getTime());
    }

    @Test
    void transferZero() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Admin admin = new Admin();
        admin.logIn("Admin");
        double amount = 0;

        try {
            assertThrows(TransferException.class, () ->
                    admin.transfer(accountNoReceiver, amount, "testAdmin"), "no exception");
            admin.transfer(accountNoReceiver, amount, "testAdmin");
        }
        catch (Exception e) {
            assertEquals("minimalna kwota przelewu to 0.01 PLN", e.getMessage());}

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferNegative() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Admin admin = new Admin();
        admin.logIn("Admin");
        double amount = -5;

        try {
            assertThrows(TransferException.class, () ->
                    admin.transfer(accountNoReceiver, amount, "testAdmin"), "no exception");
            admin.transfer(accountNoReceiver, amount, "testAdmin");
        }
        catch (Exception e) {
            assertEquals("minimalna kwota przelewu to 0.01 PLN", e.getMessage());}

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferOverLimit() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Admin admin = new Admin();
        admin.logIn("Admin");
        double amount = 900000.00;

        try {
            assertThrows(TransferException.class, () ->
                    admin.transfer(accountNoReceiver, amount, "testAdmin"), "no exception");
            admin.transfer(accountNoReceiver, amount, "testAdmin");
        }
        catch (Exception e) {
            assertEquals("kwota przekracza dopuszczalny limit transakcji", e.getMessage());}

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferWrongNumber() throws SQLException {
        String accountNoReceiver = "99999999999999999999999999";

        Admin admin = new Admin();
        admin.logIn("Admin");
        double amount = 5;
        try {
            assertThrows(TransferException.class, () ->
                    admin.transfer(accountNoReceiver, amount, "testAdmin"), "no exception");
            admin.transfer(accountNoReceiver, amount, "test");
        }
        catch (Exception e) {
            assertEquals("błędny numer konta", e.getMessage());}
    }

    @Test
    void setPercentageDeposit() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");
        admin.setPercentageDeposit(6.12);
        assertEquals(6.12, sqlDriver.returnLokataProcent());
    }

    @Test
    void setPercentageLoan() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");
        admin.setPercentageLoan(16.12);
        assertEquals(16.12, sqlDriver.returnKredytProcent());
    }

    @Test
    void returnAllTransactions() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");
        String[][] transactionsTable = admin.returnAllTransactions();
        Przelew_DB tmp;

        int id = 1;
        for (int i = 0; i < transactionsTable.length;) {
            tmp = sqlDriver.returnPrzelew(id);
            if (tmp.getDate() != null) {
                assertEquals(String.valueOf(tmp.getDate()), transactionsTable[i][0]);
                assertEquals(String.valueOf(tmp.getNr_nadawcy()), transactionsTable[i][1]);
                assertEquals(String.valueOf(tmp.getNr_odbiorcy()), transactionsTable[i][2]);
                assertEquals(String.valueOf(tmp.getKwota()), transactionsTable[i][3]);
                assertEquals(String.valueOf(tmp.getTytul()), transactionsTable[i][4]);
                i++;
            }
            id++;
        }
    }

    @Test
    void returnAllLoans() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");
        String[][] loansTable = admin.returnAllLoans();
        Kredyt_DB tmp;

        int id = 1;
        for (int i = 0; i < loansTable.length;) {
            tmp = sqlDriver.returnOneKredyt(id);
            if (tmp.getID() != 0) {
                assertEquals(String.valueOf(tmp.getID()), loansTable[i][0]);
                assertEquals(String.valueOf(tmp.getCala_kwota_splaty()), loansTable[i][1]);
                assertEquals(String.valueOf(tmp.getKwota_raty()), loansTable[i][2]);
                if (tmp.getTermin_raty() != null){
                    assertEquals(String.valueOf(tmp.getTermin_raty()), loansTable[i][3]);
                }
                else {
                    assertEquals("-", loansTable[i][3]);
                }
                assertEquals(String.valueOf(tmp.getIle_zostalo_splacic()), loansTable[i][4]);
                i++;
            }
            id++;
        }
    }

    @Test
    void returnAllDeposits() throws SQLException {
        Admin admin = new Admin();
        admin.logIn("Admin");
        String[][] depositsTable = admin.returnAllDeposits();
        Lokata_DB tmp;

        int id = 1;
        for (int i = 0; i < depositsTable.length;) {
            tmp = sqlDriver.returnLokata(id);
            if (tmp.getID() != 0) {
                assertEquals(String.valueOf(tmp.getID()), depositsTable[i][0]);
                assertEquals(String.valueOf(tmp.getAktualne_srodki()), depositsTable[i][1]);
                assertEquals(String.valueOf(tmp.getProcent()), depositsTable[i][2]);
                assertEquals(String.valueOf(tmp.getData_zalozenia()), depositsTable[i][3]);
                assertEquals(String.valueOf(tmp.getData_zamkniecia()), depositsTable[i][4]);
                assertEquals(String.valueOf(tmp.getData_ostatniej_kapitalizacji()), depositsTable[i][5]);
                i++;
            }
            id++;
        }
    }
}