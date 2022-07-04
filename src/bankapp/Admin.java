package bankapp;

import java.sql.SQLException;
import java.util.List;

public class Admin extends User{

    private String userName;
    private String passHash;
    SQL_driver sqlDriver = new SQL_driver();

//    limit przelewu dla administratora
    double transferLimit = 100000.00;

    public Admin(){
    }

    public String getUserName() {
        return userName;
    }

    public String getPassHash() {
        return passHash;
    }

//    metoda transfer wykonuje przelew na podany numer konta
//    administrator może wykonać przelew do kwoty okrślonej jako transferLimit
//    metoda weryfikuje czy nr konta adresata istniej w bazie danych
//    metoda dodaje przelwe do listy przelewów w bazie danych
//    metoda zmienia stan konta odbiorcy przelewu
//    metoda weryfikuje poprawność wykonania przelewu
    @Override
    public void transfer(String accountNoReceiver, double amount, String title) throws SQLException, TransferException {
        amount = Math.round(amount * 100) / 100.00;
        if (amount >= 0.01) {
            if (amount <= transferLimit) {
                if (Customer.accountNoExist(accountNoReceiver)) {

//                ze względu na testowy charakter aplikacji data nie jest aktualizowana na zewnętrznym serwerze,
//                a każdorazowo przesuwana o jeden dzień do przodu przy kolejnych operacjach
                    sqlDriver.insertNewDataPlusDay();

                    int correctReceiverBalance = sqlDriver.updateClientBalance(accountNoReceiver, amount);
                    int correctTransfer = sqlDriver.newPrzelew(Bank.getBankNo(), accountNoReceiver, amount, title, sqlDriver.returnData());

                    if (correctReceiverBalance != 1 || correctTransfer != 1) {
                        throw new TransferException("przelew nieudany");
                    }
                } else {
                    throw new TransferException("błędny numer konta");
                }
            } else {
                throw new TransferException("kwota przekracza dopuszczalny limit transakcji");
            }
        }
        else {
            throw new TransferException("minimalna kwota przelewu to 0.01 PLN");
        }
    }

//    metoda ustawia zmienne obektu zgodnie z wartościami pobranymi z bazy danych
    @Override
    public void logIn(String userName) throws SQLException {
        Customer_DB tmp = sqlDriver.returnAdmin(userName);
        this.userName = tmp.getUserName();
        this.passHash = tmp.getPassHash();
    }

//    metod zeruje zmienne klasy
//    by uniemożliwić ich ewentualne odczytanie nim obiekt zostanie usunięty przez Garbage Collectora
    @Override
    public void logOut() {
        this.userName = null;
        this.passHash = null;
    }

    public boolean setPercentageDeposit(double newPercentage) throws SQLException {
        return (sqlDriver.insertNewLokataProcent(newPercentage) == 1);
    }
    public boolean setPercentageLoan(double newPercentage) throws SQLException{
        return (sqlDriver.insertNewKredytProcent(newPercentage) == 1);
    }

//    metoda zwraca tablicę wielowymiarową z historią wszystkich transakcji
    public String[][] returnAllTransactions() throws SQLException {
        List<Przelew_DB> myTransactions = sqlDriver.returnAllPrzelewy();
        String[][] allTransactionsTable = new String[myTransactions.size()][5];

        for (int i = 0; i < myTransactions.size(); i++) {
            allTransactionsTable[i][0] = String.valueOf(myTransactions.get(i).getDate());
            allTransactionsTable[i][1] = myTransactions.get(i).getNr_nadawcy();
            allTransactionsTable[i][2] = myTransactions.get(i).getNr_odbiorcy();
            allTransactionsTable[i][3] = String.valueOf(myTransactions.get(i).getKwota());
            allTransactionsTable[i][4] = myTransactions.get(i).getTytul();
        }
        return allTransactionsTable;
    }

//    metoda zwraca tablicę wielowymiarową z listą kredytów dla danego numeru konta
    public String[][] returnAllLoans() throws SQLException {
        List<Kredyt_DB> myLoans = sqlDriver.returnAllKredyty();
        String[][] myLoansTable = new String[myLoans.size()][5];

        for (int i = 0; i < myLoans.size(); i++) {
            myLoansTable[i][0] = String.valueOf(myLoans.get(i).getID());
            myLoansTable[i][1] = String.valueOf(myLoans.get(i).getCala_kwota_splaty());
            myLoansTable[i][2] = String.valueOf(myLoans.get(i).getKwota_raty());
            if (myLoans.get(i).getTermin_raty() != null){
                myLoansTable[i][3] = String.valueOf(myLoans.get(i).getTermin_raty());
            }
            else {
                myLoansTable[i][3] = "-";
            }
            myLoansTable[i][4] = String.valueOf(myLoans.get(i).getIle_zostalo_splacic());
        }
        return myLoansTable;
    }

//    metoda zwraca tablicę wielowymiarową z listą lokat dla danego numeru konta
    public String[][] returnAllDeposits() throws SQLException {
        List<Lokata_DB> myDeposits = sqlDriver.returnAllLokaty();
        String[][] myDepositsTable = new String[myDeposits.size()][6];

        for (int i = 0; i < myDeposits.size(); i++) {
            myDepositsTable[i][0] = String.valueOf(myDeposits.get(i).getID());
            myDepositsTable[i][1] = String.valueOf(myDeposits.get(i).getAktualne_srodki());
            myDepositsTable[i][2] = String.valueOf(myDeposits.get(i).getProcent());
            myDepositsTable[i][3] = String.valueOf(myDeposits.get(i).getData_zalozenia());
            myDepositsTable[i][4] = String.valueOf(myDeposits.get(i).getData_zamkniecia());
            myDepositsTable[i][5] = String.valueOf(myDeposits.get(i).getData_ostatniej_kapitalizacji());
        }
        return myDepositsTable;
    }
}
