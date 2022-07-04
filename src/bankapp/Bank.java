package bankapp;

import java.sql.SQLException;

//Dokumentacja:
//1. Pobieranie numeru konta Banku z BD.
//2. Wykonywanie przelewów przez Bank na konta klientów, którzy zaciągnęli kredyt lub którym zakończyła się lokata.

public class Bank {

    public static SQL_driver sqlDriver= new SQL_driver();
    public static String getBankNo() throws SQLException{
        return sqlDriver.returnClient("Bank").getAccountNo();
    }

    public static void transfer(String accountNoReceiver, double amount, String title) throws SQLException, TransferException {
        amount = Math.round(amount * 100) / 100.00;
        if (amount >= 0.01) {
            if (Customer.accountNoExist(accountNoReceiver)) {

//            ze względu na testowy charakter aplikacji data nie jest aktualizowana na zewnętrznym serwerze,
//            a każdorazowo przesuwana o jeden dzień do przodu przy kolejnych operacjach
                sqlDriver.insertNewDataPlusDay();

                int correctReceiverBalance = sqlDriver.updateClientBalance(accountNoReceiver, amount);
                int correctTransfer = sqlDriver.newPrzelew(Bank.getBankNo(), accountNoReceiver, amount, title, sqlDriver.returnData());

                if (correctReceiverBalance != 1 || correctTransfer != 1) {
                    throw new TransferException("przelew nieudany");
                }
            } else {
                throw new TransferException("błędny numer konta");
            }
        }
        else {
            throw new TransferException("minimalna kwota przelewu to 0.01 PLN");
        }
    }
}

