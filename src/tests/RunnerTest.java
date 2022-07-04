package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    SQL_driver sqlDriver = new SQL_driver();

    // ------------------------SEKCJA WSPÓLNA--------------------

    @Test
    void logInLogOutAsCustomer() {
        Runner runner = new Runner();

        assertTrue(runner.logIn("NIE_UZYWAC", "1234"));
        assertEquals("00000000001111111111111111", runner.getCustomerAccountNo());
        assertEquals("NIE_UZYWAC", runner.getCustomerUserName());
        assertEquals("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", runner.getCustomerPassHash());
        assertEquals(1000, runner.getCustomerBalance());

        runner.logOut();
        assertNull(runner.getCustomerAccountNo());
        assertNull(runner.getCustomerUserName());
        assertNull(runner.getCustomerPassHash());
        assertEquals(0, runner.getCustomerBalance());

        assertFalse(runner.logIn("NIE_UZYWAC", "12345"));
        assertEquals("podano błędne hasło", runner.getMessages());

        assertFalse(runner.logIn("takiegoLoginuNieMa", "12345"));
        assertEquals("brak konta o podanym loginie", runner.getMessages());

        assertTrue(runner.logIn("Admin", "0000"));
        assertEquals("00000000008753961372637667", runner.getCustomerAccountNo());
        assertEquals("Admin", runner.getCustomerUserName());
        assertEquals("9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0", runner.getCustomerPassHash());
        assertEquals(1000, runner.getCustomerBalance());
    }

    @Test
    void logInLogOutAsAdmin() {
        Runner runner = new Runner();

        assertTrue(runner.logIn("Admin", "1234"));
        assertEquals("Admin", runner.getAdminUserName());
        assertEquals("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", runner.getAdminPassHash());
        assertTrue(runner.isLoggedInAsAdmin());

        runner.logOut();
        assertNull(runner.getAdminUserName());
        assertNull(runner.getAdminPassHash());
        assertFalse(runner.isLoggedInAsAdmin());

        assertFalse(runner.logIn("Admin", "12345"));
        assertEquals("podano błędne hasło", runner.getMessages());
    }

    @Test
    void transferAsCustomer() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        String amount = "101.34";
        double amountNumeric = 101.34;
        double beforeBalanceSender = runner.getCustomerBalance();
        double afterBalanceSender = Math.round((beforeBalanceSender - amountNumeric) * 100) / 100.00;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amountNumeric) * 100) / 100.00;

        assertTrue(runner.transfer(accountNoReceiver, amount, "testRunner"));
        assertEquals(afterBalanceSender, runner.getCustomerBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferAsCustomerComa() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        String amount = "101,34";
        double amountNumeric = 101.34;
        double beforeBalanceSender = runner.getCustomerBalance();
        double afterBalanceSender = Math.round((beforeBalanceSender - amountNumeric) * 100) / 100.00;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amountNumeric) * 100) / 100.00;

        assertTrue(runner.transfer(accountNoReceiver, amount, "testRunner"));
        assertEquals(afterBalanceSender, runner.getCustomerBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferAsCustomerWrongAmount() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        String amount = "x";
        double beforeBalanceSender = runner.getCustomerBalance();

        assertFalse(runner.transfer(accountNoReceiver, amount, "testRunner"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());

        assertEquals(beforeBalanceSender, runner.getCustomerBalance());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferAsAdmin() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("Admin", "1234");
        String amount = "101.34";
        double amountNumeric = 101.34;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amountNumeric) * 100) / 100.00;

        assertTrue(runner.transfer(accountNoReceiver, amount, "testRunner"));

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferAsAdminComa() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("Admin", "1234");
        String amount = "101,34";
        double amountNumeric = 101.34;
        double afterBalanceReceiver = Math.round((beforeBalanceReceiver + amountNumeric) * 100) / 100.00;

        assertTrue(runner.transfer(accountNoReceiver, amount, "testRunner"));

        receiver = sqlDriver.returnClient("testR");
        assertEquals(afterBalanceReceiver, receiver.getBalance());
    }

    @Test
    void transferAsAdminWrongAmount() throws SQLException {
        Customer_DB receiver = sqlDriver.returnClient("testR");
        String accountNoReceiver = receiver.getAccountNo();
        double beforeBalanceReceiver = receiver.getBalance();

        Runner runner = new Runner();
        runner.logIn("Admin", "1234");
        String amount = "x";

        assertFalse(runner.transfer(accountNoReceiver, amount, "testRunner"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());

        receiver = sqlDriver.returnClient("testR");
        assertEquals(beforeBalanceReceiver, receiver.getBalance());
    }

    // ------------------------SEKCJA KLIENTA--------------------

    @Test
    void newAccount() throws SQLException, NoSuchAlgorithmException {
        Random random = new Random();
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < 16; i++){
            newName.append(random.nextInt(10));
        }

        String testUserName = "testNew" + newName;
        String testPassHash = Hasher.sha256(testUserName);

        Runner runner = new Runner();
        assertTrue(runner.newAccount(testUserName, testUserName, testUserName));

        Customer_DB tmp = new SQL_driver().returnClient(testUserName);
        assertEquals(runner.getCustomerAccountNo(), tmp.getAccountNo());
        assertEquals(testUserName, tmp.getUserName());
        assertEquals(testPassHash, tmp.getPassHash());
        assertEquals(1000, tmp.getBalance());
        assertEquals("utworzono nowe konto, możesz się na nie zalogować", runner.getMessages());

        assertFalse(runner.newAccount(String.valueOf(newName), "testPassword", testUserName));
        assertEquals("podane hasła róznią się", runner.getMessages());
        assertFalse(runner.newAccount("x", testUserName, testUserName));
        assertEquals("zaproponowano login lub hasło niezgodne z wytycznymi", runner.getMessages());
        assertFalse(runner.newAccount(String.valueOf(newName), "x", "x"));
        assertEquals("zaproponowano login lub hasło niezgodne z wytycznymi", runner.getMessages());

    }

    @Test
    void newAccountExistLogin() {
        Runner runner = new Runner();

        assertFalse(runner.newAccount("Admin", "password", "password"));
        assertEquals("wybrany login już istnieje", runner.getMessages());
        assertFalse(runner.newAccount("User", "password", "password"));
        assertEquals("wybrany login już istnieje", runner.getMessages());
    }

    // ------------------------SEKCJA LOKAT----------------------

    @Test
    public void newDeposit() {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        assertTrue(runner.newDeposit("500.37", "12"));
        assertEquals(Math.round((beforeBalance - 500.37) * 100) / 100.00, runner.getCustomerBalance());
        beforeBalance = runner.getCustomerBalance();
        assertTrue(runner.newDeposit("500,37", "12"));
        assertEquals(Math.round((beforeBalance - 500.37) * 100) / 100.00, runner.getCustomerBalance());
    }
    @Test
    public void newDepositWrongInput() {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        assertFalse(runner.newDeposit("x", "12"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newDeposit("500", "11.5"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newDeposit("500", "x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertEquals(beforeBalance, runner.getCustomerBalance());
    }

    @Test
    public void withdrawDeposit() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        runner.newDeposit("500.37", "12");
        double beforeBalance = runner.getCustomerBalance();
        Lokata_DB tmp;
        String [][] depositsTable = runner.getDepositsTable();
        int id = Integer.parseInt(depositsTable[depositsTable.length - 1][0]);

        tmp = sqlDriver.returnLokata(id);
        assertEquals(500.37, tmp.getAktualne_srodki());
        assertTrue(runner.withdrawDeposit(String.valueOf(id)));
        tmp = sqlDriver.returnLokata(id);
        assertEquals(0, tmp.getAktualne_srodki());
        assertEquals(Math.round((beforeBalance + 500.37) * 100) / 100.00 , runner.getCustomerBalance());
        assertTrue(runner.isNeedsRefresh());
    }

    @Test
    public void withdrawDepositWrongInput() {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        assertFalse(runner.withdrawDeposit("x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.withdrawDeposit("10"));
        assertEquals("nie posiadasz lokaty o podanym ID", runner.getMessages());
        assertEquals(beforeBalance, runner.getCustomerBalance());
    }

    @Test
    public void calculateProfit() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");

        double amount = 1000;
        int months = 12;
        double profit = amount;
        for (int i = 0; i < months; i++) {
            profit = Math.round((profit * (1 + (sqlDriver.returnLokataProcent() / 100 / 12))) * 100) / 100.00;
        }
        profit = Math.round((profit - amount) * 100) / 100.00;

        assertTrue(runner.calculateProfit(String.valueOf(amount), String.valueOf(months)));
        assertEquals(String.valueOf(profit), runner.getProfit());
    }

//    @Test
//    public void calculateProfitWrongInput() throws SQLException {
//        Runner runner = new Runner();
//        runner.logIn("testS", "1234");
//
//        double amount = 1000;
//        int months = 12;
//        double profit = amount;
//        for (int i = 0; i < months; i++) {
//            profit = Math.round((profit * (1 + (sqlDriver.returnLokataProcent() / 100 / 12))) * 100) / 100.00;
//        }
//        profit = Math.round((profit - amount) * 100) / 100.00;
//
//        assertTrue(runner.calculateProfit(String.valueOf(amount), String.valueOf(months)));
//        assertEquals(String.valueOf(profit), runner.getProfit());
//    }

    // ------------------------SEKCJA KREDYTOW-------------------

    @Test
    public void newLoan() {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        assertTrue(runner.newLoan("500.37", "12", "10000"));
        assertEquals(Math.round((beforeBalance + 500.37) * 100) / 100.00, runner.getCustomerBalance());

        beforeBalance = runner.getCustomerBalance();
        assertTrue(runner.newLoan("500,37", "12", "10000"));
        assertEquals(Math.round((beforeBalance + 500.37) * 100) / 100.00, runner.getCustomerBalance());
    }
    @Test
    public void newLoanWrongInput() {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        assertFalse(runner.newLoan("x", "12", "10000"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newLoan("500", "11.5", "10000"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newLoan("500", "x", "10000"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newLoan("500", "12", "x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.newLoan("500000", "12", "1"));
        assertEquals("wniosek o kredyt został odrzucony", runner.getMessages());
        assertEquals(beforeBalance, runner.getCustomerBalance());
    }
    @Test
    public void payInstallment() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        runner.newLoan("500.37", "12", "10000");
        double beforeBalance = runner.getCustomerBalance();

        Kredyt_DB tmp;
        String [][] loansTable = runner.getLoansTable();
        int id = Integer.parseInt(loansTable[loansTable.length - 1][0]);
        tmp = sqlDriver.returnOneKredyt(id);
        double installment = tmp.getKwota_raty();

        assertTrue(runner.payInstallment(String.valueOf(id)));
        assertEquals(Math.round((beforeBalance - installment) * 100) / 100.00 , runner.getCustomerBalance());
        assertTrue(runner.isNeedsRefresh());
    }

    @Test
    public void payInstallmentWrongInput() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double beforeBalance = runner.getCustomerBalance();

        Kredyt_DB tmp;
        String[][] loansTable = runner.getLoansTable();
        int id = Integer.parseInt(loansTable[loansTable.length - 1][0]);
        tmp = sqlDriver.returnOneKredyt(id);

        assertFalse(runner.payInstallment("x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
        assertFalse(runner.payInstallment("26"));
        assertEquals("nie posiadasz kredytu o podanym ID", runner.getMessages());
        assertEquals(beforeBalance, runner.getCustomerBalance());
    }

    @Test
    public void calculateAmountOfInstallment() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("testS", "1234");
        double amount = 200;
        int numberOfInstallments = 2;

        assertTrue(runner.calculateAmountOfInstallment(String.valueOf(amount), String.valueOf(numberOfInstallments)));
        assertEquals(String.valueOf(Math.round((amount * (1 + sqlDriver.returnKredytProcent()/100)/2) * 100) / 100.00),
                runner.getAmountOfInstallment());
    }

//    @Test
//    public void calculateAmountOfInstallmentWrongInput() throws SQLException {
//        Runner runner = new Runner();
//        runner.logIn("testS", "1234");
//        double amount = 200;
//        int numberOfInstallments = 2;
//
//        assertTrue(runner.calculateAmountOfInstallment(String.valueOf(amount), String.valueOf(numberOfInstallments)));
//        assertEquals(String.valueOf(Math.round((amount * (1 + sqlDriver.returnKredytProcent()/100)/2) * 100) / 100.00),
//                runner.getAmountOfInstallment());
//    }

    // ------------------------SEKCJA ADMINA---------------------

    @Test
    void setPercentageDeposit() {
        Runner runner = new Runner();
        runner.logInAdmin("Admin", "1234");

        assertTrue(runner.setPercentageDeposit("5.15"));
        assertEquals("5.15", runner.getPercentageDeposit());

        assertTrue(runner.setPercentageDeposit("4,13"));
        assertEquals("4.13", runner.getPercentageDeposit());

        assertTrue(runner.setPercentageDeposit("5.157"));
        assertEquals("5.16", runner.getPercentageDeposit());
    }

    @Test
    void setPercentageDepositWrongInput() {
        Runner runner = new Runner();
        runner.logInAdmin("Admin", "1234");

        assertFalse(runner.setPercentageDeposit("0"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageDeposit("0.001"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageDeposit("-5"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageDeposit("x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
    }

    @Test
    void setPercentageLoans() {
        Runner runner = new Runner();
        runner.logInAdmin("Admin", "1234");

        assertTrue(runner.setPercentageLoan("15.15"));
        assertEquals("15.15", runner.getPercentageLoan());

        assertTrue(runner.setPercentageLoan("14,13"));
        assertEquals("14.13", runner.getPercentageLoan());

        assertTrue(runner.setPercentageLoan("15.157"));
        assertEquals("15.16", runner.getPercentageLoan());
    }

    @Test
    void setPercentageLoansWrongInput() {
        Runner runner = new Runner();
        runner.logInAdmin("Admin", "1234");

        assertFalse(runner.setPercentageLoan("0"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageLoan("0.001"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageLoan("-5"));
        assertEquals("minimalne oprocentowanie to 0.01%", runner.getMessages());

        assertFalse(runner.setPercentageLoan("x"));
        assertEquals("wprowadzono nieprawidłowe lub niekompletne dane", runner.getMessages());
    }

    @Test
    public void TransactionsTableAsCustomer() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("NIE_UZYWAC", "1234");

        int myTransactionsLength = sqlDriver.returnKlientaPrzelewy(runner.getCustomerAccountNo()).size();
        assertEquals(myTransactionsLength, runner.getTransactionsTable().length);
    }

    @Test
    public void TransactionsTableAsAdmin() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("Admin", "1234");

        int myTransactionsLength = sqlDriver.returnAllPrzelewy().size();
        assertEquals(myTransactionsLength, runner.getTransactionsTable().length);
    }

    @Test
    public void LoansTableAsCustomer() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("NIE_UZYWAC", "1234");

        int loansLength = sqlDriver.returnKlientaKredyty(runner.getCustomerAccountNo()).size();
        assertEquals(loansLength, runner.getLoansTable().length);
    }

    @Test
    public void LoansTableAsAdmin() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("Admin", "1234");

        int loansLength = sqlDriver.returnAllKredyty().size();
        assertEquals(loansLength, runner.getLoansTable().length);
    }

    @Test
    public void DepositsTableAsCustomer() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("NIE_UZYWAC", "1234");

        int depositsLength = sqlDriver.returnKlientaLokaty(runner.getCustomerAccountNo()).size();
        assertEquals(depositsLength, runner.getDepositsTable().length);
    }

    @Test
    public void DepositsTableAsAdmin() throws SQLException {
        Runner runner = new Runner();
        runner.logIn("Admin", "1234");

        int depositsLength = sqlDriver.returnAllLokaty().size();
        assertEquals(depositsLength, runner.getDepositsTable().length);
    }
}