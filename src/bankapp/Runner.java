package bankapp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Runner {
    Customer customer = new Customer();
    Admin admin = new Admin();
    Lokata lokata = new Lokata();
    Kredyt kredyt = new Kredyt();
    SQL_driver sqlDriver = new SQL_driver();

    private boolean needsRefresh = false;
    private boolean loggedInAsAdmin = false;
    private Date localDate;
    private String messages;
    private String[][] transactionsTable;
    private String[][] loansTable;
    private String[][] depositsTable;
    private String profit = "";
    private String amountOfInstallment = "";

    public Runner() {
    }

    // ------------------------SEKCJA RUNNER---------------------

//    metoda służy do odświeżania danych przekazywanych do GUI
    public void refresh() throws SQLException, DatabaseException {
        setLocalDate();
        if (admin.getUserName() != null || customer.getUserName() != null) {
            Lokata.sprawdzenieLokat();
            if (!loggedInAsAdmin) {
                customer.refresh();
            }
            setTransactionsTable();
            setLoansTable();
            setDepositsTable();
        }
        needsRefresh = true;
    }

//    metoda ustawia loklaną datę na podstawie ostatniej daty w bazie danych
    public void setLocalDate() {
        try {
            localDate = sqlDriver.returnData();
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
    }

//    metoda służy do pobierania historii transakcji klienta / historii wszystkich transakcji
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public void setTransactionsTable() {
        try {
            if (loggedInAsAdmin) {
                transactionsTable = admin.returnAllTransactions();
            }
            else {
                transactionsTable = customer.returnMyTransactions();
            }
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
    }

//    metoda służy do pobierania listy kredytów klienta / wszystkich
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public void setLoansTable() {
        try {
            if (loggedInAsAdmin) {
                loansTable = admin.returnAllLoans();
            }
            else {
                loansTable = customer.returnMyLoans();
            }
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
    }

//    metoda służy do pobierania listy lokat klienta / wszystkich
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public void setDepositsTable() {
        try {
            if (loggedInAsAdmin) {
                depositsTable = admin.returnAllDeposits();
            }
            else {
                depositsTable = customer.returnMyDeposits();
            }
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
    }

    // ------------------------SEKCJA KOMUNIKACJI Z GUI----------

//    metoda zwraca wątek wykorzystywany do ciągłego odświeżania danych w zadanych odstępach (long millis)
    public Thread autoRefresh(long millis) {
        while (true){
            try {
                Thread.sleep(millis);
                if (!String.valueOf(localDate).equals(String.valueOf(sqlDriver.returnData()))) {
                    if (getCustomerUserName() != null || getAdminUserName() != null) {
                        refresh();
                    }
                }
            }
            catch (Exception e) {
                messages = e.getMessage();
            }
        }
    }

//    metoda zwraca informację czy potrzeba odświeżyć panele w GUI
//    metoda po przekazaniu informacji ustawia flagę needsRefresh na false
    public boolean isNeedsRefresh() {
        return needsRefresh;
    }

//    metoda pozwala GUI zasygnalizować, że dane zostały odświeżone
    public void wasRefreshed() {
        needsRefresh = false;
    }

//    metoda zwraca informację czy użytkownik jest zalogowany na konto administratora
    public boolean isLoggedInAsAdmin() {
        return loggedInAsAdmin;
    }

//    metoda zwraca loklaną datę
    public String getLocalDate() {
        return String.valueOf(localDate);
    }

//    metoda służy do przekazaywania do GUI komuinikatów i przechwytywanych wyjątków
//    metoda po przekazaniu informacji czyści zmienną messages
    public String getMessages() {
        String outputValue = messages;
        messages = "";
        return outputValue;
    }

//    metoda zwraca historię transakcji klienta / historię wszystkich transakcji
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public String[][] getTransactionsTable() {
        return transactionsTable;
    }

//    metoda zwraca listę kredytów klienta / wszystkich
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public String[][] getLoansTable() {
        return loansTable;
    }

//    metoda zwraca listę lokat klienta / wszystkich
//    zależnie czy użytkownik jest zalogowany na konto klienta czy administratora
    public String[][] getDepositsTable() {
        return depositsTable;
    }

//    metoda zwraca obliczony zysk z lokaty
    public String getProfit() {
        return profit;
    }

//    metoda zwraca obliczoną wysokośc raty
    public String getAmountOfInstallment() {
        return amountOfInstallment;
    }

    // ------------------------SEKCJA WSPÓLNA--------------------

//    metoda służy do logowania się na konto klienta lub administratora
//    jeśli dany login znajduje w bazie klientów następuje próba zalogowania się jako klient
//    jeśli dany login znajduje w bazie administartorów następuje próba zalogowania się jako administrator
//    metoda wywołuje metodę logInCustomer() lub logInAdmin()
//    zależnie od konta ma które zaloguje się użytkownik zmieniana jest wartość zmiennej loggedInAsAdmin (true/false)
//    jeśli dany login nie znajduje się ani w bazie klientów, ani w bazie administartorów
//    użytkownik jest informowany o braku konta o podanym loginie poprzez zmienną messages
//    jeśli wystąpi wyjątek zostaj on przekazany użytkownikowi poprzez zmienną messages
    public boolean logIn(String userName, String passwoard) {
        boolean outputValue = false;

        if (userName.matches("[A-Za-z\\d]+") && passwoard.matches("[A-Za-z\\d]+")) {
            try {
                if (sqlDriver.isExistCustomerLogin(userName)) {
                    outputValue = logInCustomer(userName, passwoard);
                }
                if (!outputValue && sqlDriver.isExistAdminLogin(userName)) {
                    outputValue = logInAdmin(userName, passwoard);
                }
                if (!sqlDriver.isExistCustomerLogin(userName) && !sqlDriver.isExistAdminLogin(userName))  {
                    messages = "brak konta o podanym loginie";
                }
                if (outputValue) {
                    refresh();
                }
            }
            catch (Exception e) {
                messages = e.getMessage();
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
        }
        return outputValue;
    }

//    metoda służy do wylogowania zalogowanego użytkownika (klienta lub administratora)
//    metoda wywołuje metodę logOutCustomer() lub logOutAdmin()
    public boolean logOut() {
        boolean outputValue;

        if (loggedInAsAdmin) {
            outputValue = logOutAdmin();
        }
        else {
            outputValue = logOutCustomer();
        }
        return outputValue;
    }

//    metoda służy do wykonywania przelewów przez zalogowanego użytkownika (klienta lub administratora)
//    metoda parsuje orzymaną kowtę na wartość numeryczną
//    metoda wywołuje metodę transferCustomer() lub transferAdmin()
    public boolean transfer(String accountNoReceiver, String amount, String title) {
        boolean outputValue = false;
        double amountNumeric;

        if (accountNoReceiver.matches("\\d{26}") && amount.matches("[.,\\d]+") && title.matches("[A-Za-z\\d]+")) {
            try {
                amountNumeric = Double.parseDouble(amount.replace(',', '.'));
                if (amountNumeric >= 0.01) {
                    if (loggedInAsAdmin) {
                        outputValue = transferAdmin(accountNoReceiver, amountNumeric, title);
                    }
                    else {
                        outputValue = transferCustomer(accountNoReceiver, amountNumeric, title);
                    }
                    if (outputValue) {
                        refresh();
                    }
                }
                else {
                    throw new TransferException("minimalna kwota przelewu to 0.01 PLN");
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            }
            catch (Exception e) {
                messages = e.getMessage();
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
        }
        return outputValue;
    }

    // ------------------------SEKCJA KLIENTA--------------------

    public String getCustomerAccountNo(){
        return customer.getAccountNo();
    }

    public String getCustomerUserName(){
        return customer.getUserName();
    }

    public String getCustomerPassHash() {
        return customer.getPassHash();
    }

    public double getCustomerBalance() {
        return customer.getBalance();
    }

    public boolean transferCustomer(String accountNoReceiver, double amount, String title) {
        try {
            customer.transfer(accountNoReceiver, amount, title);
            return true;
        }
        catch (Exception e) {
            messages = e.getMessage();
            return false;
        }
    }

    public boolean logInCustomer(String userName, String passwoard) {
        try {
            passwoard = Hasher.sha256(passwoard);
            if (passwoard.equals(sqlDriver.returnClient(userName).getPassHash())) {
                customer.logIn(userName);
                return true;
            }
            else {
                messages = "podano błędne hasło";
            }
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
        return false;
    }

//    metod zeruje zmienne klasy
//    by uniemożliwić ich ewentualne odczytanie nim obiekt zostanie usunięty przez Garbage Collectora
    public boolean logOutCustomer() {
        customer.logOut();
        transactionsTable = null;
        if (customer.getAccountNo() == null && customer.getUserName() == null
                && customer.getPassHash() == null && customer.getBalance() == 0 && transactionsTable == null) {
            return true;
        }
        else {
            messages = "nie wylogowano";
            return false;
        }
    }

    public boolean newAccount(String newUserName, String newPassword, String repeatPassword)  {
        if (newUserName.matches("[A-Za-z\\d]+")
                && newPassword.matches("[A-Za-z\\d]+") && repeatPassword.matches("[A-Za-z\\d]+")) {
            if (newUserName.length() >= 4 && newPassword.length() >=4) {
                if (Objects.equals(newPassword, repeatPassword) )
                    try {
                        if (!sqlDriver.isExistCustomerLogin(newUserName) && !sqlDriver.isExistAdminLogin(newUserName)){
                            customer.newAccount(newUserName, Hasher.sha256(newPassword));
                            messages = "utworzono nowe konto, możesz się na nie zalogować";
                            return true;
                        }
                        else {
                            messages = "wybrany login już istnieje";
                        }
                    }
                    catch (Exception e) {
                        messages = e.getMessage();
                    }
                else {
                    messages = "podane hasła róznią się";
                }
            }
            else {
                messages = "zaproponowano login lub hasło niezgodne z wytycznymi";
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
        }
        return false;
    }

    // ------------------------SEKCJA LOKAT----------------------

//    metoda służy do założenia nowej lokaty
    public boolean newDeposit(String amount, String numberOfMonths) {
        if (amount.matches("[.,\\d]+") && numberOfMonths.matches("\\d+")) {
            try {
                double amountNumeric = Double.parseDouble(amount.replace(',', '.'));
                amountNumeric = Math.round(amountNumeric * 100) / 100.00;
                int numberOfMonthsNumeric = Integer.parseInt(numberOfMonths);
                if (amountNumeric > 0 && numberOfMonthsNumeric > 0) {
                    messages = lokata.zalozenieLokaty(customer, amountNumeric, numberOfMonthsNumeric);
                    refresh();
                    return (Objects.equals(messages, "lokata została założona"));
                }
                else {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

//    metoda służy do wypłaty środków z danej lokaty (int id)
    public boolean withdrawDeposit(String id) {
        if (id.matches("\\d+")) {
            try {
                int idNumeric = Integer.parseInt(id);
                ArrayList<String> idList = new ArrayList<>();
                for (String[] deposits : depositsTable) {
                    idList.add(deposits[0]);
                }
                if (idList.contains(id)) {
                    messages = lokata.wyplataLokaty(idNumeric);
                    refresh();
                    return true;
                }
                else {
                    messages = "nie posiadasz lokaty o podanym ID";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

//    metoda służy do obliczeniu zysku z lokaty
    public boolean calculateProfit(String amount, String numberOfMonths) {
        if (amount.matches("[.,\\d]+") && numberOfMonths.matches("\\d+")) {
            try {
                double amountNumeric = Double.parseDouble(amount.replace(',', '.'));
                amountNumeric = Math.round(amountNumeric * 100) / 100.00;
                int numberOfMonthsNumeric = Integer.parseInt(numberOfMonths);

                if (amountNumeric > 0 && numberOfMonthsNumeric > 0) {
                    profit = String.valueOf(lokata.obliczZyskPrzycisk(amountNumeric, numberOfMonthsNumeric));
                    return true;
                }
                else {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

    // ------------------------SEKCJA KREDYTOW-------------------

//    metoda służy do brania nowego kredytu
    public boolean newLoan(String amount, String numberOfInstallments, String income) {
        if (amount.matches("[.,\\d]+") && numberOfInstallments.matches("\\d+")
                && income.matches("[.,\\d]+")) {
            try {
                double amountNumeric = Double.parseDouble(amount.replace(',', '.'));
                amountNumeric = Math.round(amountNumeric * 100) / 100.00;
                int numberOfInstallmentsNumeric = Integer.parseInt(numberOfInstallments);
                double incomeNumeric = Double.parseDouble(income.replace(',', '.'));
                incomeNumeric = Math.round(incomeNumeric * 100) / 100.00;

                if (amountNumeric > 0 && numberOfInstallmentsNumeric > 0 && incomeNumeric > 0) {
                    messages = kredyt.przyznanieKredytu(customer, amountNumeric, numberOfInstallmentsNumeric, incomeNumeric);
                    refresh();
                    return (Objects.equals(messages, "przyznano kredyt"));
                }
                else {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

//    metoda służy do obliczenia kwoty raty
    public boolean calculateAmountOfInstallment(String amount, String numberOfInstallments) {
        if (amount.matches("[.,\\d]+") && numberOfInstallments.matches("\\d+")) {
            try {
                double amountNumeric = Double.parseDouble(amount.replace(',', '.'));
                amountNumeric = Math.round(amountNumeric * 100) / 100.00;
                int numberOfInstallmentsNumeric = Integer.parseInt(numberOfInstallments);

                if (amountNumeric > 0 && numberOfInstallmentsNumeric > 0) {
                    amountOfInstallment = String.valueOf(kredyt.obliczRataPrzycisk(amountNumeric, numberOfInstallmentsNumeric));
                    return true;
                }
                else {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

//    metoda służy do spłacenia raty z danego kreytu (int id)
    public boolean payInstallment(String id) {
        if (id.matches("\\d+")) {
            try {
                int idNumeric = Integer.parseInt(id);
                ArrayList<String> idList = new ArrayList<>();
                for (String[] deposits : loansTable) {
                    idList.add(deposits[0]);
                }
                if (idList.contains(id)) {
                    messages = kredyt.splataRaty(customer, idNumeric);
                    refresh();
                    return (!Objects.equals(messages, "brak wystarczających środków na koncie"));
                }
                else {
                    messages = "nie posiadasz kredytu o podanym ID";
                    return false;
                }
            }
            catch (NumberFormatException e) {
                messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                return false;
            }
            catch (Exception e) {
                messages = e.getMessage();
                return false;
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
            return false;
        }
    }

    // ------------------------SEKCJA ADMINA---------------------

    public String getAdminUserName(){
        return admin.getUserName();
    }

    public String getAdminPassHash() {
        return admin.getPassHash();
    }

    public boolean transferAdmin(String accountNoReceiver, double amount, String title) {
        try {
            admin.transfer(accountNoReceiver, amount, title);
            return true;
        }
        catch (Exception e) {
            messages = e.getMessage();
            return false;
        }
    }

    public boolean logInAdmin(String userName, String passwoard) {
        try {
            passwoard = Hasher.sha256(passwoard);
            if (passwoard.equals(sqlDriver.returnAdmin(userName).getPassHash())) {
                admin.logIn(userName);
                loggedInAsAdmin = true;
                return true;
            }
            else {
                messages = "podano błędne hasło";
            }
        }
        catch (Exception e) {
            messages = e.getMessage();
        }
        return false;
    }

//    metod zeruje zmienne klasy
//    by uniemożliwić ich ewentualne odczytanie nim obiekt zostanie usunięty przez Garbage Collectora
    public boolean logOutAdmin() {
        admin.logOut();
        transactionsTable = null;
        loggedInAsAdmin = false;
        if (admin.getUserName() == null && admin.getPassHash() == null && transactionsTable == null) {
            return true;
        }
        else {
            messages = "nie wylogowano";
            return false;
        }
    }

//    metoda służy do służy do ustawiania nowego oprocentowania lokat
    public boolean setPercentageDeposit(String newPercentage){
        if (newPercentage.matches("[.,\\d]+")) {
            if (loggedInAsAdmin) {
                try {
                    double newPercentageNumeric = Double.parseDouble(newPercentage.replace(',', '.'));
                    newPercentageNumeric = Math.round(newPercentageNumeric * 100) / 100.00;

                    if (newPercentageNumeric > 0) {
                        return admin.setPercentageDeposit(newPercentageNumeric);
                    }
                    else {
                        messages = "minimalne oprocentowanie to 0.01%";
                    }
                }
                catch (NumberFormatException e) {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                }
                catch (Exception e) {
                    messages = e.getMessage();
                }
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
        }
        return false;
    }

//    metoda zwraca aktualne oprocentowanie kredytu
    public String getPercentageDeposit(){
        try {
            return String.valueOf(Math.round(sqlDriver.returnLokataProcent() * 100) / 100.00);
        }
        catch (Exception e) {
            messages = e.getMessage();
            return null;
        }
    }

//    metoda służy do służy do ustawiania nowego oprocentowania kredytu
    public boolean setPercentageLoan(String newPercentage){
        if (newPercentage.matches("[.,\\d]+")) {
            if (loggedInAsAdmin) {
                try {
                    double newPercentageNumeric = Double.parseDouble(newPercentage.replace(',', '.'));
                    newPercentageNumeric = Math.round(newPercentageNumeric * 100) / 100.00;

                    if (newPercentageNumeric > 0) {
                        return admin.setPercentageLoan(newPercentageNumeric);
                    }
                    else {
                        messages = "minimalne oprocentowanie to 0.01%";
                    }
                }
                catch (NumberFormatException e) {
                    messages = "wprowadzono nieprawidłowe lub niekompletne dane";
                }
                catch (Exception e) {
                    messages = e.getMessage();
                }
            }
        }
        else {
            messages = "wprowadzono nieprawidłowe lub niekompletne dane";
        }
        return false;
    }

//    metoda zwraca aktualne oprocentowanie kredytu
    public String getPercentageLoan(){
        try {
            return String.valueOf(Math.round(sqlDriver.returnKredytProcent() * 100) / 100.00);
        }
        catch (Exception e) {
            messages = e.getMessage();
            return null;
        }
    }
}

