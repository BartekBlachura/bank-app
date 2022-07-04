package bankapp;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class Lokata {
    private double balance;
    private int ostatnieIDLokaty;
    private String komunikatZwrotny;
    private Date dataZamkniecia;
    static SQL_driver sqlDriver = new SQL_driver();

    public Date getDataZamkniecia() {
        return dataZamkniecia;
    }

    public Lokata() {
    }

    public Lokata(Customer customer) throws SQLException {
        this.balance = customer.getBalance();
        this.ostatnieIDLokaty = sqlDriver.lastIDLokat() + 1;
        this.komunikatZwrotny = null;
        this.dataZamkniecia = null;
    }

    public Date dataKoncowa(Lokata lokata, int liczbaMiesiecy) throws SQLException {   //ustalenie daty zakonczenia lokaty
        lokata.dataZamkniecia = sqlDriver.returnData();
        int temp = lokata.dataZamkniecia.getMonth() + liczbaMiesiecy;
        lokata.dataZamkniecia.setMonth(temp);
     //   System.out.println(lokata.dataZamkniecia);                    // Kto nam wytłumaczy dlaczego lokata.data zmienia się o zadaną liczbę miesięcy
     //   System.out.println(lokata.data);                              // odpalając LokataTest, ten dostaje piwo ;D
        return lokata.dataZamkniecia;
    }

    public static void sprawdzenieLokat() throws SQLException, DatabaseException {    // metoda uruchamiana przy kazdym odswiezeniu panelu klienta (refresh())
        Date actualDate = sqlDriver.returnData();
        List<Lokata_DB> deposits = sqlDriver.returnAllLokatyNoZero();
        String[][] depositsTable = new String[deposits.size()][6];

        for (int i = 0; i < deposits.size(); i++) {
            depositsTable[i][0] = String.valueOf(deposits.get(i).getID());
            depositsTable[i][1] = String.valueOf(deposits.get(i).getAktualne_srodki());
            depositsTable[i][2] = String.valueOf(deposits.get(i).getProcent());
            depositsTable[i][3] = String.valueOf(deposits.get(i).getData_zalozenia());
            depositsTable[i][4] = String.valueOf(deposits.get(i).getData_zamkniecia());
            depositsTable[i][5] = String.valueOf(deposits.get(i).getData_ostatniej_kapitalizacji());
        }

        int dlugoscTabeli = depositsTable.length;                                                     // kapitalizujaca odsetki po uplynieciu kolejnego miesiaca od daty
        for (int i = 0; i < dlugoscTabeli; i++) {                                                       // zalozenia lokaty, az do daty zakonczenia lokaty (stałe oprocentowanie)
            Date poMiesiacu = new java.sql.Date(Time.addMonth(java.sql.Date.valueOf(depositsTable[i][5]),1).getTime());
            if (!actualDate.before(poMiesiacu) && !actualDate.after(java.sql.Date.valueOf(depositsTable[i][4]))) {
             int srodkiWInt = Kredyt.przeksztalcanie(Double.parseDouble(depositsTable[i][1]));
                double srodkiUpdate = Math.round(srodkiWInt * (1 + ((Kredyt.przeksztalcanie(Double.parseDouble(depositsTable[i][2])) / 100d / 12) / 100))) / 100d;
                int correctDepositUpdate = sqlDriver.updateLokata(srodkiUpdate, poMiesiacu, Integer.parseInt(depositsTable[i][0]));
                if (correctDepositUpdate != 1) {
                    throw new DatabaseException("błąd zapisu w bazie danych");
                }
            }
        }
    }

    public double obliczZyskPrzycisk(double kwota, int liczbaMiesiecy) throws SQLException {  //metoda uruchamiana przez przycisk w GUI do obliczenia zysku z lokaty
        double suma = kwota;
        for (int i = 0; i < liczbaMiesiecy; i++) {
            suma = Math.round((suma * (1 + (sqlDriver.returnLokataProcent() / 100 / 12))) * 100) / 100d;
        }
        return (Kredyt.przeksztalcanie(suma) - Kredyt.przeksztalcanie(kwota)) / 100d;
    }

    public String zalozenieLokaty(Customer customer, double kwota, int liczbaMiesiecy) throws SQLException, TransferException, DatabaseException {
        Lokata lokata = new Lokata(customer);
        if (lokata.balance >= kwota) {
            customer.transfer(Bank.getBankNo(), kwota, "Wpłata na lokatę nr: " + (lokata.ostatnieIDLokaty));
            int correctNewDeposit = sqlDriver.insertNewLokata(customer, sqlDriver.returnData(), dataKoncowa(lokata, liczbaMiesiecy), kwota, sqlDriver.returnData());
            if (correctNewDeposit != 1) {
                throw new DatabaseException("błąd zapisu w bazie danych");
            } else {
                lokata.komunikatZwrotny = "lokata została założona";
            }
        } else {
            lokata.komunikatZwrotny = "brak wystarczających środków na koncie";
        }
        return lokata.komunikatZwrotny;
    }

    public String wyplataLokaty(int id) throws SQLException, TransferException, DatabaseException {
        int correctDepositWithdrawal;
        Lokata_DB daneLokaty = sqlDriver.returnLokata(id);
        Bank.transfer(daneLokaty.getNr_konta_klienta(), daneLokaty.getAktualne_srodki(), "Wypłata lokaty nr: " + id);
        if (!sqlDriver.returnData().after(daneLokaty.getData_zamkniecia())) {
            correctDepositWithdrawal = sqlDriver.closeLokata(0d, sqlDriver.returnData(), id);
        } else {
            correctDepositWithdrawal = sqlDriver.closeLokata(0d, daneLokaty.getData_zamkniecia(), id);
        }
        if (correctDepositWithdrawal != 1) {
            throw new DatabaseException("błąd zapisu w bazie danych");
        } else {
            return "wypłacono środki";
        }
    }
}

// zmiana lokat z ilosci lat na ilosc miesiecy
// Całe zobowiązanie - max 40%!!

// Czemu aktualna data w bazie jest późniejsza o dzień od daty przelewu w bazie, skoro zgodnie z wywołaniem metod w Transferze NAJPIERW jest aktualizowana data w bazie
// a dpiero POTEM pobierana do insertu przelewu do historii w bazie????

// W momencie błędu zapisu transakcji w historii w bazie danych, operacja na środkach jest przeprowadzana, ale brak info o tym w histirii
// - np brak lokaty -> może warto anulować taką transakcję?