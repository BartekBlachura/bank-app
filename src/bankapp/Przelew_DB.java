package bankapp;

import java.sql.Date;
// DOKUMENTACJA klasa stworzona na potrzeby zapisania rekordu z Bazy Danych o jednakowej nazwie

public class Przelew_DB {
    private Date data;
    private String nr_nadawcy;
    private String nr_odbiorcy;
    private Double kwota;
    private String tytul;

    public Przelew_DB() {
    }

    public Date getDate() {
        return data;
    }

    public Double getKwota() {
        return kwota;
    }

    public String getNr_nadawcy() {
        return nr_nadawcy;
    }

    public String getNr_odbiorcy() {
        return nr_odbiorcy;
    }

    public String getTytul() {
        return tytul;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setKwota(Double kwota) {
        this.kwota = kwota;
    }

    public void setNr_nadawcy(String nr_nadawcy) {
        this.nr_nadawcy = nr_nadawcy;
    }

    public void setNr_odbiorcy(String nr_odbiorcy) {
        this.nr_odbiorcy = nr_odbiorcy;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }
}
