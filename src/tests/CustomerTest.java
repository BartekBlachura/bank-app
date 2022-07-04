package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    SQL_driver sqlDriver = new SQL_driver();

    @Test
    void newCustomer() {
        Customer customer = new Customer();
        assertNull(customer.getAccountNo());
        assertNull(customer.getUserName());
        assertNull(customer.getPassHash());
        assertEquals(0, customer.getBalance());
    }

    @Test
    void logInLogOut() throws SQLException {
        Customer customer = new Customer();
        customer.logIn("NIE_UZYWAC");

        assertEquals("00000000001111111111111111", customer.getAccountNo());
        assertEquals("NIE_UZYWAC", customer.getUserName());
        assertEquals("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", customer.getPassHash());
        assertEquals(1000, customer.getBalance());

        customer.logOut();

        assertNull(customer.getAccountNo());
        assertNull(customer.getUserName());
        assertNull(customer.getPassHash());
        assertEquals(0, customer.getBalance());
    }

    @Test
    void transfer() throws SQLException, TransferException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();
        long beforeDate = sqlDriver.returnData().getTime();

        Customer customer = new Customer();
        customer.logIn("testS");
        double amount = 101.34;
        double beforeBalanceSender = customer.getBalance();
        double afterBalanceSender = Math.round((beforeBalanceSender - amount) * 100) / 100.00;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amount) * 100) / 100.00;

        customer.transfer(accountNoReceiver, amount, "testCustomer");

        customer.refresh();
        assertEquals(afterBalanceSender, customer.getBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
        assertEquals(beforeDate + 86400000, sqlDriver.returnData().getTime());
    }

    @Test
    void transferZero() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Customer customer = new Customer();
        customer.logIn("testS");
        double amount = 0;
        double beforeBalanceSender = customer.getBalance();

        try {
            assertThrows(TransferException.class, () ->
                    customer.transfer(accountNoReceiver, amount, "testCustomer"), "no exception");
            customer.transfer(accountNoReceiver, amount, "testAdmin");
        }
        catch (Exception e) {
            assertEquals("minimalna kwota przelewu to 0.01 PLN", e.getMessage());}

        assertEquals(beforeBalanceSender, customer.getBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferNegative() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Customer customer = new Customer();
        customer.logIn("testS");
        double amount = -5;
        double beforeBalanceSender = customer.getBalance();

        try {
            assertThrows(TransferException.class, () ->
                    customer.transfer(accountNoReceiver, amount, "testCustomer"), "no exception");
            customer.transfer(accountNoReceiver, amount, "testAdmin");
        }
        catch (Exception e) {
            assertEquals("minimalna kwota przelewu to 0.01 PLN", e.getMessage());}

        assertEquals(beforeBalanceSender, customer.getBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferOverLimit() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Customer customer = new Customer();
        customer.logIn("testS");
        double amount = 9000000;
        double beforeBalanceSender = customer.getBalance();
        try {
            assertThrows(TransferException.class, () ->
                    customer.transfer(accountNoReceiver, amount, "testCustomer"), "no exception");
            customer.transfer(accountNoReceiver, amount, "test");
        }
        catch (Exception e) {
            assertEquals("brak środków na koncie", e.getMessage());}

        assertEquals(beforeBalanceSender, customer.getBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferWrongNumber() throws SQLException {
        String accountNoReceiver = "99999999999999999999999999";

        Customer customer = new Customer();
        customer.logIn("testS");
        double amount = 5;
        double beforeBalanceSender = customer.getBalance();
        try {
            assertThrows(TransferException.class, () ->
                    customer.transfer(accountNoReceiver, amount, "testCustomer"), "no exception");
            customer.transfer(accountNoReceiver, amount, "test");
        }
        catch (Exception e) {
            assertEquals("błędny numer konta", e.getMessage());}

        assertEquals(beforeBalanceSender, customer.getBalance());
    }

    @Test
    void transferMyNumber() throws SQLException {
        Customer customer = new Customer();
        customer.logIn("testS");
        String accountNoReceiver = customer.getAccountNo();
        double amount = 5;
        double beforeBalanceSender = customer.getBalance();
        try {
            assertThrows(TransferException.class, () ->
                    customer.transfer(accountNoReceiver, amount, "testCustomer"), "no exception");
            customer.transfer(accountNoReceiver, amount, "test");
        }
        catch (Exception e) {
            assertEquals("błędny numer konta", e.getMessage());}

        assertEquals(beforeBalanceSender, customer.getBalance());
    }

    @Test
    void newAccount() throws SQLException, NoSuchAlgorithmException {
        Random random = new Random();
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < 16; i++){
            newName.append(random.nextInt(10));
        }

        String testUserName = "testNew" + newName;
        String testPassHash = Hasher.sha256(testUserName);

        Customer customer = new Customer();
        customer.newAccount(testUserName, testPassHash);

        assertEquals(testUserName, customer.getUserName());
        assertEquals(testPassHash, customer.getPassHash());
        assertEquals(1000, customer.getBalance());

        Customer_DB tmp = new SQL_driver().returnClient(testUserName);
        assertEquals(customer.getAccountNo(), tmp.getAccountNo());
        assertEquals(testUserName, tmp.getUserName());
        assertEquals(testPassHash, tmp.getPassHash());
        assertEquals(1000, tmp.getBalance());
    }

    @Test
    void newAccountNo() throws SQLException {
        assertFalse(Customer.accountNoExist(Customer.newAccountNo()));
    }

    @Test
    void accountNoExist() throws SQLException {
        assertTrue(Customer.accountNoExist("00000000001234567890123456"));
        assertFalse(Customer.accountNoExist("00000000001234567890123459"));
    }

    @Test
    void returnMyTransactions() throws SQLException {
        String[] transactionsTable = {
                "1932-03-12|00000000001111111111111111|00000000004559629884812935|-97.5|transfer#1|",
                "1932-03-13|00000000001111111111111111|00000000004559629884812935|-100.0|transfer#2|",
                "1932-03-14|00000000000000000000000000|00000000001111111111111111|350.0|Wyplata kredytu nr: 26|",
                "1932-03-15|00000000000000000000000000|00000000001111111111111111|460.7|Wyplata kredytu nr: 27|",
                "1932-03-16|00000000001111111111111111|00000000000000000000000000|-200.0|Wplata na lokate nr: 10|",
                "1932-03-17|00000000001111111111111111|00000000000000000000000000|-150.3|Wplata na lokate nr: 11|",
                "1932-03-18|00000000009378915142826011|00000000001111111111111111|97.37|transfer3|"};
        int i = 0;
        Customer customer = new Customer();
        customer.logIn("NIE_UZYWAC");

        String[][] myTransactions = customer.returnMyTransactions();
        assertEquals(transactionsTable.length, myTransactions.length);

        for (String[] strings : myTransactions) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                stringBuilder.append(s).append("|");
            }
            assertEquals(transactionsTable[i], stringBuilder.toString());
            i++;
        }
    }

    @Test
    void returnMyLoans() throws SQLException {
        String[] loansTable = {
                "26|393.05|56.15|1932-04-14|393.05|",
                "27|517.37|172.46|1932-04-15|517.37|"};
        int i = 0;
        Customer customer = new Customer();
        customer.logIn("NIE_UZYWAC");

        String[][] myLoans = customer.returnMyLoans();
        assertEquals(loansTable.length, myLoans.length);

        for (String[] strings : myLoans) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                stringBuilder.append(s).append("|");
            }
            assertEquals(loansTable[i], stringBuilder.toString());
            i++;
        }
    }

    @Test
    void returnMyDeposits() throws SQLException {
        String[] depositsTable = {
                "10|200.77|4.6|1932-03-16|1932-09-16|1932-05-28|",
                "11|151.46|4.6|1932-03-17|1933-03-17|1933-03-07|"};
        int i = 0;
        Customer customer = new Customer();
        customer.logIn("NIE_UZYWAC");

        String[][] myDeposits = customer.returnMyDeposits();
        assertEquals(depositsTable.length, myDeposits.length);

        for (String[] strings : myDeposits) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                stringBuilder.append(s).append("|");
            }
            assertEquals(depositsTable[i], stringBuilder.toString());
            i++;
        }
    }

    @Test
    void returnMyDataNoData() throws SQLException {
        Customer customer = new Customer();
        customer.logIn("NIE_UZYWAC2");

        String[][] myTransactions = customer.returnMyTransactions();
        assertEquals(0, myTransactions.length);

        String[][] myLoans = customer.returnMyLoans();
        assertEquals(0, myLoans.length);

        String[][] myDeposits = customer.returnMyDeposits();
        assertEquals(0, myDeposits.length);
    }
}