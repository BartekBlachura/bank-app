package bankapp;

import java.sql.Date;

// DOKUMENTACJA klasa stworzona na potrzeby zapisania rekordu z Bazy Danych o jednakowej nazwie

public class Kredyt_DB {
    private int ID;
    private String nr_konta_klienta;
    private double procent;
    private Date data_zalozenia;
    private double cala_kwota_splaty;
    private int ile_zostalo_rat;
    private double ile_zostalo_splacic;
    private Date termin_raty;
    private double kwota_raty;


    public Date getData_zalozenia() {
        return data_zalozenia;
    }

    public int getID() {
        return ID;
    }

    public double getKwota_startowa() {
        return cala_kwota_splaty;
    }

    public String getNr_konta_klienta() {
        return nr_konta_klienta;
    }

    public double getProcent() {
        return procent;
    }

    public double getCala_kwota_splaty() {
        return cala_kwota_splaty;
    }

    public int getIle_zostalo_rat() { return ile_zostalo_rat;}

    public double getIle_zostalo_splacic(){return ile_zostalo_splacic;}

    public Date getTermin_raty() {return termin_raty;}

    public double getKwota_raty() {return kwota_raty;}

    public void setData_zalozenia(Date data_zalozenia) {
        this.data_zalozenia = data_zalozenia;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setCala_kwota_splaty(double cala_kwota_splaty) {
        this.cala_kwota_splaty = cala_kwota_splaty;
    }

    public void setNr_konta_klienta(String nr_konta_klienta) {
        this.nr_konta_klienta = nr_konta_klienta;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public void setIle_zostalo_rat(int ile_zostalo_rat) {this.ile_zostalo_rat = ile_zostalo_rat;}

    public void setIle_zostalo_splacic(double ile_zostalo_splacic) {this.ile_zostalo_splacic = ile_zostalo_splacic;}

    public void setTermin_raty(Date termin_raty) {this.termin_raty = termin_raty;    }

    public void setKwota_raty(double kwota_raty) {this.kwota_raty = kwota_raty;}

}
