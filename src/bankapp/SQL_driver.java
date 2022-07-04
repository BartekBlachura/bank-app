package bankapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQL_driver {

    //DOKUMENTACJA Klasa odpowiadajaca za zapytania do Bazy Danych SQL, jest ona
    // podzielona na Sekcje - Klient, Przelew, Lokaty, Kredyty, Admin i Procenty
    // w kazdej dostepne sa zawolania insert i return
    // jesli ktos chce tworzyc nowe zapytania prosze przeanalizowac dowolny insert/update z returnem
    // insert & update posiadaja metode executeUpdate(), a return executeQuery()


    static Connection conn = JDBC.getConnection();

    // ------------------------SEKCJA KLIENTA--------------------
    // klient dodaj nowego, zaktualizuj saldo, zwroc dane klienta
    public int insertNewClient(Customer klient) throws SQLException {
        String querry = "Insert into Klient(login, haslo_hash, nr_konta, saldo) VALUES (?, ?, ? ,?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, klient.getUserName());
        ps.setString(2, klient.getPassHash());
        ps.setString(3, klient.getAccountNo());
        ps.setDouble(4, klient.getBalance());
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public int insertNewClient(Customer_DB klient) throws SQLException {
        String querry = "Insert into Klient(login, haslo_hash, nr_konta, saldo) VALUES (?, ?, ? ,?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, klient.getUserName());
        ps.setString(2, klient.getPassHash());
        ps.setString(3, klient.getAccountNo());
        ps.setDouble(4, klient.getBalance());
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

//    public int UpdateClientBalance(Customer klient) throws SQLException {
//        String querry = "Update Klient set saldo = ? where login = ? and haslo_hash = ?";
//        PreparedStatement ps = conn.prepareStatement(querry);
//        ps.setDouble(1, klient.getBalance());
//        ps.setString(2, klient.getUserName());
//        ps.setString(3, klient.getPassHash());
//        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
//        return result;
//    }
    public int updateClientBalance(String account_nr, double kwota) throws SQLException {
        String querry = "Update Klient set saldo = ? where nr_konta= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        double balance = Math.round((returnBalance(account_nr) + kwota) * 100) / 100.00;
        ps.setDouble(1, balance);
        ps.setString(2, account_nr);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

//    public int updateClientPassHash(int ID, String passHash) throws SQLException {
//        String querry = "Update Klient set haslo_hash = ? where ID= ?";
//        PreparedStatement ps = conn.prepareStatement(querry);
//        ps.setString(1, passHash);
//        ps.setInt(2, ID);
//        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
//        return result;
//    }

    private double returnBalance(String account_nr) throws SQLException {
        //metoda ukryta tylko do uzytku UpdateClientBalance
        String querry = "Select * from Klient where nr_konta= ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, account_nr);
        Customer_DB klient_fromDB = new Customer_DB();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return (rs.getDouble("saldo"));
        }
        return 0;
    }

    public Customer_DB returnClient(String login) throws SQLException {
        String querry = "Select * from Klient where login= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, login);
        Customer_DB klient_fromDB = new Customer_DB();
        ResultSet rs = ps.executeQuery();
        boolean check = false;

        while (rs.next()) {
            check = true;
            klient_fromDB.setUserName(rs.getString("login"));
            klient_fromDB.setPassHash(rs.getString("haslo_hash"));
            klient_fromDB.setAccountNo(rs.getString("nr_konta"));
            klient_fromDB.setBalance(rs.getDouble("saldo"));
        }

        if (check == true) {
            return klient_fromDB;
        } else {
            return null;
        }
    }

    public boolean isExistAccountNo(String nr_konta) throws SQLException {
        String querry = "Select * from Klient where nr_konta= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, nr_konta);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean isExistCustomerLogin(String login) throws SQLException {
        String querry = "Select * from Klient where login= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, login);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean isExistAdminLogin(String login) throws SQLException {
        String querry = "Select * from Admin where login= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, login);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return true;
        }
        return false;
    }

    // ------------------------SEKCJA PRZELEWOW--------------------
    // Przelew wykonaj przelew, pokaz wprzelewy wszystkich, pokaz przelewy jednego
    // klienta
    public int newPrzelew(String nadawca, String odbiorca, Double kwota, String tytul, Date data) throws SQLException {
        String querry = "Insert into Przelewy(nadawca_konto, odbiorca_konto, kwota, tytul, Data) VALUES (?, ?, ? ,?, ?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, nadawca);
        ps.setString(2, odbiorca);
        ps.setDouble(3, kwota);
        ps.setString(4, tytul);
        java.sql.Date sqlDate = new java.sql.Date(data.getTime());
        ps.setDate(5, sqlDate);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public List<Przelew_DB> returnAllPrzelewy() throws SQLException {
        String querry = "Select * from Przelewy";
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        List<Przelew_DB> ls = new ArrayList<Przelew_DB>();

        while (rs.next()) {
            Przelew_DB transaction = new Przelew_DB();
            transaction.setNr_nadawcy(rs.getString("nadawca_konto"));
            transaction.setNr_odbiorcy(rs.getString("odbiorca_konto"));
            transaction.setKwota(rs.getDouble("kwota"));
            transaction.setTytul(rs.getString("tytul"));
            transaction.setData(rs.getDate("Data"));
            ls.add(transaction);
        }
        return ls;
    }

    public List<Przelew_DB> returnKlientaPrzelewy(String nr_konta_klienta) throws SQLException {
        String querry = "Select * from Przelewy where nadawca_konto = ? or odbiorca_konto = ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, nr_konta_klienta);
        ps.setString(2, nr_konta_klienta);
        ResultSet rs = ps.executeQuery();
        List<Przelew_DB> ls = new ArrayList<Przelew_DB>();
        String nadawca;
        String odbiorca;
        while (rs.next()) {
            Przelew_DB transaction = new Przelew_DB();
            nadawca = rs.getString("nadawca_konto");
            transaction.setNr_nadawcy(nadawca);
            odbiorca = rs.getString("odbiorca_konto");
            transaction.setNr_odbiorcy(odbiorca);
            if (nr_konta_klienta.equals(nadawca)) {
                transaction.setKwota(-(rs.getDouble("kwota")));
            } else {
                transaction.setKwota(rs.getDouble("kwota"));
            }
            transaction.setTytul(rs.getString("tytul"));
            transaction.setData(rs.getDate("Data"));
            ls.add(transaction);
        }
        return ls;
    }

    public Przelew_DB returnPrzelew(int ID) throws SQLException {
        String querry = "Select * from Przelewy where ID  = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        Przelew_DB transaction = new Przelew_DB();
        while (rs.next()) {
            transaction.setNr_nadawcy(rs.getString("nadawca_konto"));
            transaction.setNr_odbiorcy(rs.getString("odbiorca_konto"));
            transaction.setKwota(rs.getDouble("kwota"));
            transaction.setTytul(rs.getString("tytul"));
            transaction.setData(rs.getDate("Data"));
        }
        return transaction;
    }

    // ------------------------SEKCJA LOKAT--------------------
    // Lokaty zaloz nowa, zamknij, pokaz wszystkie lokaty klienta, pokaz dana lokate
    public int insertNewLokata(Customer klient, Date data_start, Date data_koniec, Double wartosc_startowa, Date ostatni_miesiac_kapitalizacji) throws SQLException {
        double procent = returnLokataProcent();
        String querry = "Insert into Lokaty(nr_konta , procent, data_zalozenia, data_zamkniecia, aktualne_srodki, data_ostatniej_kapitalizacji ) VALUES (?, ?, ? ,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, klient.getAccountNo());
        ps.setDouble(2, procent);
        java.sql.Date sqlDate_start = new java.sql.Date(data_start.getTime());
        ps.setDate(3, (java.sql.Date) sqlDate_start);
        java.sql.Date sqlDate_koniec = new java.sql.Date(data_koniec.getTime());
        ps.setDate(4, (java.sql.Date) sqlDate_koniec);
        ps.setDouble(5, wartosc_startowa);
        ps.setDate(6, (java.sql.Date) ostatni_miesiac_kapitalizacji);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public int closeLokata(Double kwota, Date data_zamkniecia, int ID) throws SQLException {
        String querry = "Update Lokaty set aktualne_srodki = ? , data_zamkniecia = ?  where ID = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setDouble(1, kwota);
        ps.setDate(2, (java.sql.Date) data_zamkniecia);
        ps.setInt(3, ID);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public List<Lokata_DB> returnKlientaLokaty(String nr_konta_klienta) throws SQLException {
        String querry = "Select * from Lokaty where nr_konta = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, nr_konta_klienta);
        ResultSet rs = ps.executeQuery();
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        while (rs.next()) {
            Lokata_DB lokat = new Lokata_DB();
            lokat.setID(rs.getInt("ID"));
            lokat.setNr_konta_klienta(rs.getString("nr_konta"));
            lokat.setProcent(rs.getDouble("procent"));
            lokat.setData_zalozenia(rs.getDate("data_zalozenia"));
            lokat.setData_zamkniecia(rs.getDate("data_zamkniecia"));
            lokat.setAktualne_srodki(rs.getDouble("aktualne_srodki"));
            lokat.setData_ostatniej_kapitalizacji(rs.getDate("data_ostatniej_kapitalizacji"));
            ls.add(lokat);
        }
        return ls;
    }

    public List<Lokata_DB> returnAllLokaty() throws SQLException {
        String querry = "Select * from Lokaty ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        while (rs.next()) {
            Lokata_DB lokat = new Lokata_DB();
            lokat.setID(rs.getInt("ID"));
            lokat.setNr_konta_klienta(rs.getString("nr_konta"));
            lokat.setProcent(rs.getDouble("procent"));
            lokat.setData_zalozenia(rs.getDate("data_zalozenia"));
            lokat.setData_zamkniecia(rs.getDate("data_zamkniecia"));
            lokat.setAktualne_srodki(rs.getDouble("aktualne_srodki"));
            lokat.setData_ostatniej_kapitalizacji(rs.getDate("data_ostatniej_kapitalizacji"));
            ls.add(lokat);
        }
        return ls;
    }

    public Lokata_DB returnLokata(int ID) throws SQLException {
        String querry = "Select * from Lokaty where ID  = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        Lokata_DB lokat = new Lokata_DB();
        while (rs.next()) {
            lokat.setID(rs.getInt("ID"));
            lokat.setNr_konta_klienta(rs.getString("nr_konta"));
            lokat.setProcent(rs.getDouble("procent"));
            lokat.setData_zalozenia(rs.getDate("data_zalozenia"));
            lokat.setData_zamkniecia(rs.getDate("data_zamkniecia"));
            lokat.setAktualne_srodki(rs.getDouble("aktualne_srodki"));
            lokat.setData_ostatniej_kapitalizacji(rs.getDate("data_ostatniej_kapitalizacji"));
        }
        return lokat;
    }

    public List<Lokata_DB> returnAllLokatyNoZero() throws SQLException {
        String querry = "Select * from Lokaty where aktualne_srodki != 0";
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        while (rs.next()) {
            Lokata_DB lokat = new Lokata_DB();
            lokat.setID(rs.getInt("ID"));
            lokat.setNr_konta_klienta(rs.getString("nr_konta"));
            lokat.setProcent(rs.getDouble("procent"));
            lokat.setData_zalozenia(rs.getDate("data_zalozenia"));
            lokat.setData_zamkniecia(rs.getDate("data_zamkniecia"));
            lokat.setAktualne_srodki(rs.getDouble("aktualne_srodki"));
            lokat.setData_ostatniej_kapitalizacji(rs.getDate("data_ostatniej_kapitalizacji"));
            ls.add(lokat);
        }
        return ls;
    }

    public List<Lokata_DB> returnAllLokatyAfter30(Date data) throws SQLException {

        String querry = "Select * from Lokaty where data_ostatniej_kapitalizacji = ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        java.sql.Date sqlDate = new java.sql.Date(Time.addMonth(data, -1).getTime());
        ps.setDate(1, sqlDate);
        ResultSet rs = ps.executeQuery();
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        while (rs.next()) {
            Lokata_DB lokat = new Lokata_DB();
            lokat.setID(rs.getInt("ID"));
            lokat.setNr_konta_klienta(rs.getString("nr_konta"));
            lokat.setProcent(rs.getDouble("procent"));
            lokat.setData_zalozenia(rs.getDate("data_zalozenia"));
            lokat.setData_zamkniecia(rs.getDate("data_zamkniecia"));
            lokat.setAktualne_srodki(rs.getDouble("aktualne_srodki"));
            lokat.setData_ostatniej_kapitalizacji(rs.getDate("data_ostatniej_kapitalizacji"));
            ls.add(lokat);
        }
        return ls;
    }

    public int updateLokata(double aktualne_srodki, Date data_ostatniej_kapitalizacji, int ID) throws SQLException{
        String querry = "Update Lokaty set aktualne_srodki = ? , data_ostatniej_kapitalizacji = ?  where ID= ?";
        PreparedStatement ps = conn.prepareStatement(querry);

        ps.setDouble(1, aktualne_srodki);
        ps.setDate(2, (java.sql.Date) data_ostatniej_kapitalizacji);
        ps.setInt(3, ID);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public int lastIDLokat()throws SQLException{
        String querry = "SELECT * FROM Lokaty ORDER BY ID DESC LIMIT 1";//;
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("ID");
        }
        return 0; // ? jak dac null
    }


    // ------------------------SEKCJA KREDYTOW--------------------
    // Kredyty wez nowy kredyt, zaktualizuj splate, zobacz wszystkie kredyty
    // klienta, pokaz dany kredyt
    public int insertNewKredyt(Customer klient, Date data_start, double cala_kwota_splaty, int ile_zostalo_rat, double ile_zostalo_splacic,
                               Date termin_raty, double kwota_raty) throws SQLException {
        double procent = returnKredytProcent();
        String querry = "Insert into Kredyty(nr_konta_klienta , procent, data_zalozenia, cala_kwota_splaty ,ile_zostalo_rat, ile_zostalo_splacic,termin_raty,kwota_raty) VALUES (?, ?, ? ,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, klient.getAccountNo());
        ps.setDouble(2, procent);
        java.sql.Date sqlDate_start = new java.sql.Date(data_start.getTime());
        ps.setDate(3, sqlDate_start);
        ps.setDouble(4, cala_kwota_splaty);
        ps.setInt(5, ile_zostalo_rat);
        ps.setDouble(6, ile_zostalo_splacic);
        ps.setDate(7, (java.sql.Date) termin_raty);
        ps.setDouble(8, kwota_raty);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public Kredyt_DB returnOneKredyt(int id) throws SQLException {
        String querry = "Select * from Kredyty where ID  = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Kredyt_DB kredyty = new Kredyt_DB();
        while (rs.next()) {
            kredyty.setID(rs.getInt("ID"));

            kredyty.setNr_konta_klienta(rs.getString("nr_konta_klienta"));
            kredyty.setProcent(rs.getDouble("procent"));
            kredyty.setData_zalozenia(rs.getDate("data_zalozenia"));
            kredyty.setCala_kwota_splaty(rs.getDouble("cala_kwota_splaty"));
            kredyty.setIle_zostalo_rat(rs.getInt("ile_zostalo_rat"));
            kredyty.setIle_zostalo_splacic(rs.getDouble("ile_zostalo_splacic"));
            kredyty.setTermin_raty(rs.getDate("termin_raty"));
            kredyty.setKwota_raty(rs.getDouble("kwota_raty"));
        }
        return kredyty;
    }

    public List<Kredyt_DB> returnKlientaKredyty(String nr_konta_klienta) throws SQLException {
        String querry = "Select * from Kredyty where nr_konta_klienta  = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, nr_konta_klienta);
        ResultSet rs = ps.executeQuery();
        List<Kredyt_DB> ls = new ArrayList<Kredyt_DB>();
        while (rs.next()) {
            Kredyt_DB kredyty = new Kredyt_DB();
            kredyty.setID(rs.getInt("ID"));
            kredyty.setNr_konta_klienta(rs.getString("nr_konta_klienta"));
            kredyty.setProcent(rs.getDouble("procent"));
            kredyty.setData_zalozenia(rs.getDate("data_zalozenia"));
            kredyty.setCala_kwota_splaty(rs.getDouble("cala_kwota_splaty"));
            kredyty.setIle_zostalo_rat(rs.getInt("ile_zostalo_rat"));
            kredyty.setIle_zostalo_splacic(rs.getDouble("ile_zostalo_splacic"));
            kredyty.setTermin_raty(rs.getDate("termin_raty"));
            kredyty.setKwota_raty(rs.getDouble("kwota_raty"));
            ls.add(kredyty);
        }
        return ls;
    }

    public List<Kredyt_DB> returnAllKredyty() throws SQLException {
        String querry = "Select * from Kredyty ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        List<Kredyt_DB> ls = new ArrayList<Kredyt_DB>();
        while (rs.next()) {
            Kredyt_DB kredyty = new Kredyt_DB();
            kredyty.setID(rs.getInt("ID"));
            kredyty.setNr_konta_klienta(rs.getString("nr_konta_klienta"));
            kredyty.setProcent(rs.getDouble("procent"));
            kredyty.setData_zalozenia(rs.getDate("data_zalozenia"));
            kredyty.setCala_kwota_splaty(rs.getDouble("cala_kwota_splaty"));
            kredyty.setIle_zostalo_rat(rs.getInt("ile_zostalo_rat"));
            kredyty.setIle_zostalo_splacic(rs.getDouble("ile_zostalo_splacic"));
            kredyty.setTermin_raty(rs.getDate("termin_raty"));
            kredyty.setKwota_raty(rs.getDouble("kwota_raty"));
            ls.add(kredyty);
        }
        return ls;
    }

    public double returnIleSplacono(int ID) throws SQLException {
        String querry = "SELECT * FROM Kredyty where id = ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getDouble("ile_splacono");
        }
        return 0; // ? jak dac null
    }

    public int splacRate(Date nastepny_termin, double wys_raty, int ile_jeszcze_rat, double ile_jeszcze_splaty, int ID) throws SQLException {
        //ile_zostalo_splacic, ile_zostalo_rat, termin_raty
        String querry = "Update Kredyty set ile_zostalo_splacic = ? , ile_zostalo_rat = ? , termin_raty = ?, kwota_raty = ?  where ID = ? ";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setDouble(1, ile_jeszcze_splaty);
        ps.setInt(2, ile_jeszcze_rat);
        if(nastepny_termin != null) {
            java.sql.Date sqlDate = new java.sql.Date(nastepny_termin.getTime());
            ps.setDate(3, sqlDate);
        }
        else {
            ps.setNull(3,5);
        }
        ps.setDouble(4, wys_raty);
        ps.setInt(5, ID);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }


    public int lastIDKredyt()throws SQLException{
        String querry = "SELECT * FROM Kredyty ORDER BY ID DESC LIMIT 1";//;
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("ID");
        }
        return 0; // ? jak dac null
    }

    // ------------------------SEKCJA ADMINA--------------------
    // Admin pobierz dane admina z BD do sprawdzenia czy admin

    public Customer_DB returnAdmin(String login) throws SQLException {
        String querry = "Select * from Admin where login= ?";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setString(1, login);
        Customer_DB admin_fromDB = new Customer_DB();
        ResultSet rs = ps.executeQuery();
        boolean check = false;

        while (rs.next()) {
            check = true;
            admin_fromDB.setUserName(rs.getString("login"));
            admin_fromDB.setPassHash(rs.getString("haslo_hash"));
        }
        if (check == true) {
            return admin_fromDB;
        } else {
            return null;
        }
    }

    // ------------------------SEKCJA PROCENTOW--------------------
    // Lokata i Kredyt dodaj nowy procent i odczytaj aktyalny procent
    public int insertNewLokataProcent(double procent) throws SQLException {
        String querry = "Insert into BankLokata(Procent_lokata) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setDouble(1, procent);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public double returnLokataProcent() throws SQLException {
        String querry = "SELECT * FROM BankLokata ORDER BY ID DESC LIMIT 1";//
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getDouble("Procent_lokata");
        }
        return 0; // ? jak dac null
    }

    public int insertNewKredytProcent(double procent) throws SQLException {
        String querry = "Insert into BankKredyt(Procent_kredyt) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        ps.setDouble(1, procent);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public double returnKredytProcent() throws SQLException {
        String querry = "SELECT * FROM BankKredyt ORDER BY ID DESC LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getDouble("Procent_kredyt");
        }
        return 0; // ? jak dac null
    }

    // ------------------------SEKCJA DATY--------------------
    // Insert i Return data

    public Date returnData() throws SQLException {
        String querry = "SELECT * FROM AktualnaData ORDER BY ID LIMIT 1";//;
        PreparedStatement ps = conn.prepareStatement(querry);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Date DataPobrana = new Date();
            DataPobrana = rs.getDate("DataSymulowana");
            return DataPobrana;
        }
        return null; // ? jak dac null
    }

    public int insertNewDataPlusDay() throws SQLException {
        Date data = new Date();
        data = returnData();

        String querry = "Update AktualnaData set DataSymulowana = ? where ID= 1";
        //String querry = "Insert into AktualnaData(DataSymulowana) VALUE (?)";
        PreparedStatement ps = conn.prepareStatement(querry);
        java.sql.Date sqlDate = new java.sql.Date(Time.addTime(data,1).getTime());
        ps.setDate(1, sqlDate);
        int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
        return result;
    }

    public int insertNewData(Date data) throws SQLException {
        Date dataSQL = new Date();
        dataSQL = returnData();
        if (data.after(dataSQL)) {
            String querry = "Update AktualnaData set DataSymulowana = ? where ID= 1";
            //String querry = "Insert into AktualnaData(DataSymulowana) VALUE (?)";
            PreparedStatement ps = conn.prepareStatement(querry);
            java.sql.Date sqlDate = new java.sql.Date(Time.addTime(data, 0).getTime());
            ps.setDate(1, sqlDate);
            int result = ps.executeUpdate(); // zwraca ile rekordow w BD zostalo zmienionych
            return result;
        }
        else return 0; //oznacza ze data ktora chcesz wprowadzic juz przeminela
    }

}
