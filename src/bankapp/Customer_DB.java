package bankapp;

// DOKUMENTACJA klasa stworzona na potrzeby zapisania rekordu z Bazy Danych o jednakowej nazwie
public class Customer_DB {
    private String accountNo;
    private String userName;
    private String passHash;
    private double balance;

    public Customer_DB() {
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassHash() {
        return passHash;
    }

    public double getBalance() {
        return balance;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
