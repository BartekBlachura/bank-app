package tests;

import bankapp.Bank;
import bankapp.Customer;
import bankapp.TransferException;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;


class BankTest {

    @Test
    void getBankNo() throws SQLException {
        assertEquals("00000000000000000000000000", Bank.getBankNo());
    }

    @Test
    void transferTest() throws SQLException, TransferException {
        Customer receiver1 = new Customer();
        receiver1.logIn("Lady");
        String receiverAccountNr = receiver1.getAccountNo();
        double amount = 100.00;
        String title = "BankPrzelew";
        Bank.transfer(receiverAccountNr,amount,title);
        double receiverBalanceAfterTransfer = Math.round((receiver1.getBalance() + amount) * 100) / 100.00;
        receiver1.refresh();
        assertEquals( receiverBalanceAfterTransfer, receiver1.getBalance());
    }
    @Test
    void TransferIncorrectReceiverAccountNrTest() {
        double amount = 100.00;
        String title = "BankPrzelewNaNiewlasciwyNumer";
        assertThrows(TransferException.class, () ->
                    Bank.transfer("12345678987654321", amount, title), "Właściwy numer konta odbiorcy,wyjatek nie wystapil");
    }

    @Test
    void transferWrongAmountTest() throws SQLException {
        Customer receiver2 = new Customer();
        receiver2.logIn("Lady");
        String receiverAccountNr = receiver2.getAccountNo();
        String title = "BankPrzelewNiewlasciwaKwota";
        assertThrows(TransferException.class, () ->
                    Bank.transfer(receiverAccountNr, 0.00, title),"Wlasciwa kwota przelewu, wyjatek nie wystapil");
        }
}

