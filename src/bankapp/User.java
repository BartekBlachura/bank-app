package bankapp;

import java.sql.SQLException;

public abstract class User{
    public abstract void transfer(String accountNoReceiver, double amount, String title) throws SQLException, TransferException;

    public abstract void logIn(String userName) throws SQLException;

    public abstract void logOut();
}