package bankapp;

import java.sql.Date;
// DOKUMENTACJA klasa stworzona na potrzeby zapisania rekordu z Bazy Danych o jednakowej nazwie

public class Lokata_DB {
    private int ID;
    private String nr_konta_klienta;
    private double procent;
    private Date data_zalozenia;
    private Date data_zamkniecia;
    private Double aktualne_srodki;
    private Date data_ostatniej_kapitalizacji;

    public Date getData_zalozenia() {
        return data_zalozenia;
    }

    public int getID() {
        return ID;
    }

    public Double getAktualne_srodki() {
        return aktualne_srodki;
    }

    public String getNr_konta_klienta() {
        return nr_konta_klienta;
    }

    public double getProcent() {
        return procent;
    }

    public Date getData_zamkniecia() {
        return data_zamkniecia;
    }

    public Date getData_ostatniej_kapitalizacji() {return data_ostatniej_kapitalizacji;}

    public void setData_zalozenia(Date data_zalozenia) {
        this.data_zalozenia = data_zalozenia;
    }

    public void setData_zamkniecia(Date data_zamkniecia) {
        this.data_zamkniecia = data_zamkniecia;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setAktualne_srodki(Double aktualne_srodki) {
        this.aktualne_srodki = aktualne_srodki;
    }

    public void setNr_konta_klienta(String nr_konta_klienta) {
        this.nr_konta_klienta = nr_konta_klienta;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public void setData_ostatniej_kapitalizacji(Date data_ostatniej_kapitalizacji) {
        this.data_ostatniej_kapitalizacji = data_ostatniej_kapitalizacji;
    }
}
