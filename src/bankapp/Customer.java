package bankapp;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class Customer extends User {
    private String accountNo;
    private String userName;
    private String passHash;
    private double balance;
    static SQL_driver sqlDriver = new SQL_driver();

    public Customer(){
    }

    public String getAccountNo(){
        return accountNo;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassHash() {
        return passHash;
    }

    public double getBalance() {
        return balance;
    }

//    metoda transfer wykonuje przelew z konta klienta na podany numer konta
//    metoda weryfkuje czy klient ma wystarczające środki aby wykonać przelew
//    metoda weryfikuje czy nr konta adresata istniej w bazie danych
//    metoda pobiera aktualną datę z bazy danych
//    metoda dodaje przelwe do listy przelewów w bazie danych
//    metoda zmienia stan konta nadawcy i odbiorcy przelewu
//    metoda weryfikuje poprawność wykonania przelewu
    public void transfer(String accountNoReceiver, double amount, String title) throws SQLException, TransferException {
        amount = Math.round(amount * 100) / 100.00;
        if (amount >= 0.01) {
            if (amount <= balance) {
                if (accountNoExist(accountNoReceiver) && !accountNo.equals(accountNoReceiver)) {

//                ze względu na testowy charakter aplikacji data nie jest aktualizowana na zewnętrznym serwerze,
//                a każdorazowo przesuwana o jeden dzień do przodu przy kolejnych operacjach
                    sqlDriver.insertNewDataPlusDay();

                    int correctSenderBalance = sqlDriver.updateClientBalance(accountNo, -amount);
                    int correctReceiverBalance = sqlDriver.updateClientBalance(accountNoReceiver, amount);
                    int correctTransfer = sqlDriver.newPrzelew(accountNo, accountNoReceiver, amount, title, sqlDriver.returnData());

                    if (correctSenderBalance != 1 || correctReceiverBalance != 1 || correctTransfer != 1){
                        throw new TransferException("przelew nieudany");
                    }
                }
                else {
                    throw new TransferException("błędny numer konta");
                }
            }
            else {
                throw new TransferException("brak środków na koncie");
            }
        }
        else {
            throw new TransferException("minimalna kwota przelewu to 0.01 PLN");
        }
    }

//    metoda ustawia zmienne obektu zgodnie z wartościami pobranymi z bazy danych
    @Override
    public void logIn(String userName) throws SQLException {
        Customer_DB tmp = sqlDriver.returnClient(userName);
        this.accountNo = tmp.getAccountNo();
        this.userName = tmp.getUserName();
        this.passHash = tmp.getPassHash();
        this.balance = Math.round(tmp.getBalance() * 100) / 100.00;
    }

//    metod zeruje zmienne klasy
//    by uniemożliwić ich ewentualne odczytanie nim obiekt zostanie usunięty przez Garbage Collectora
    @Override
    public void logOut() {
        this.accountNo = null;
        this.userName = null;
        this.passHash = null;
        this.balance = 0;
    }

//    metoda służy do odświeżania stanu konta klienta po każdej przeprowadzonej transakcji
    public void refresh() throws SQLException {
        Customer_DB tmp = sqlDriver.returnClient(userName);
        this.balance = Math.round(tmp.getBalance() * 100) / 100.00;
    }

//    metoda tworzy nowe konto klienta o otrzymanej nazwie użytkownika i hashu hasła
//    metoda wywołuje metodę tworzącą nowy numer konta bankowego
//    metoda wysyła nowego kliena do bazy danych
//    bonus startowy 1000 PLN
    public void newAccount(String userName, String passHash) throws SQLException {
        this.userName = userName;
        this.passHash = passHash;
        this.accountNo = newAccountNo();
        this.balance = 1000;

        sqlDriver.insertNewClient(this);
    }

//    metoda tworzy nowy numer konta bankowego
//    metoda losuje 16 ostatnich cyfr numeru, a następnie sprawdza czy taki numer już nie istnieje
//    jeśli numer już istnieje, losowany jest nowy numer, jeśli numer jest wolny - zostaje zwrócony
    public static String newAccountNo() throws SQLException {
        boolean finished = false;
        Random random = new Random();
        StringBuilder accountNo = new StringBuilder();

        while (!finished){
            accountNo = new StringBuilder("0000000000");
            for (int i = 0; i < 16; i++){
                accountNo.append(random.nextInt(10));
            }
            finished = !accountNoExist(accountNo.toString());
        }
        return accountNo.toString();
    }

//    metoda sprawdza, czy numer podany numer konta figureuje w bazie, zwraca true lub false
    public static boolean accountNoExist(String accountNo) throws SQLException {
        return sqlDriver.isExistAccountNo(accountNo);
    }

//    metoda zwraca tablicę wielowymiarową z historią transakcji dla danego numeru konta
    public String[][] returnMyTransactions() throws SQLException {
        List<Przelew_DB> myTransactions = sqlDriver.returnKlientaPrzelewy(accountNo);
        String[][] myTransactionsTable = new String[myTransactions.size()][5];

        for (int i = 0; i < myTransactions.size(); i++) {
            myTransactionsTable[i][0] = String.valueOf(myTransactions.get(i).getDate());
            myTransactionsTable[i][1] = myTransactions.get(i).getNr_nadawcy();
            myTransactionsTable[i][2] = myTransactions.get(i).getNr_odbiorcy();
            myTransactionsTable[i][3] = String.valueOf(myTransactions.get(i).getKwota());
            myTransactionsTable[i][4] = myTransactions.get(i).getTytul();
        }
        return myTransactionsTable;
    }

//    metoda zwraca tablicę wielowymiarową z listą kredytów dla danego numeru konta
    public String[][] returnMyLoans() throws SQLException {
        List<Kredyt_DB> myLoans = sqlDriver.returnKlientaKredyty(accountNo);
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
    public String[][] returnMyDeposits() throws SQLException {
        List<Lokata_DB> myDeposits = sqlDriver.returnKlientaLokaty(accountNo);
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
