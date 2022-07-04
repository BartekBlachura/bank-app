package bankapp;

//DOKUMENTACJA:

//Jesli klient chce otzymac kredyt powinien wywolac metode "przyznanieKredytu".
//Parametry wejsciowe: obiekt zalogowanego usera, kwota udzielanego kredytu, liczba rat.
//Wykonywane jest sprawdzenie czy stan konta jest dodatni. Jesli nie, wniosek o kredyt jest odrzucony, a jesli tak to wykonywany jest przelew z banku
//na konto klienta. Tworzone jest ID kredytu, obliczana calkowita kwota do splaty, ustalana data splaty raty oraz jej wysokosc. Dane przesylane sa
//do bazy danych i wyswietlany jest komunikat o przyznaniu kredytu.
//Na wyjsciu dostajemy komunikat o przyznaniu lub odrzuceniu wniosku o udzielenie kredytu.

//Jesli klient chce splacic rate kredytu powinien wywolac metode "splataRaty".
//Parametry wejsciowe: obiekt zalogowanego usera, wysokosc nastepnej raty, data nastepnej raty, ID kredytu, pozostala kwota do splaty i pozostala liczba rat.
//Wykonywane jest sprawdzenie czy na koncie jest wystarczajaca ilosc pieniedzy aby splacic rate kredytu o podanym ID. Jesli nie, wyswietlany jest
//komunikat o braku srodkow a jesli tak wykonywany jest przelew na konto banku. Nastepuje zmiana daty nastepnej raty, pomniejszenie pozostalej kwoty do
//splaty, ilosci pozostalych rat oraz wyswietlenie komunikatu o splacie raty. W przypadku gdy do splaty pozostanie ostatnia rata, wysokosc tej raty
//zmieniana jest na pozostala do splaty kwote kredytu. Jesli ostatnia rata zostala splacona, wyswietlany jest komunikat z gratulacjami, data nastepnej
//raty i wysokosc raty sa zerowane. Aktualne dane kredytu sa wysylane do bazy.

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Kredyt {
    private String accountNo;
    private double kwota;
    private double balance;
    private int pozostalaLiczbaRat;
    private double oprocentowanieKredytu;
    private int ostatnieIDKredytu;
    private double kwotaDoSplaty;
    private String komunikatZwrotny;
    private Date dataRaty;                              // niewykorzystana z uwagi na przyjety system progresu daty w bazie danych (wirtualna data)
    private Date dataNastepnejRaty;
    private double wysokoscRaty;
    SQL_driver sqlDriver = new SQL_driver();

    public Kredyt(){
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setKwota(double kwota) {
        this.kwota = kwota;
    }

    public void setOprocentowanieKredytu(double oprocentowanieKredytu) {
        this.oprocentowanieKredytu = oprocentowanieKredytu;
    }

    public void setKwotaDoSplaty(double kwotaDoSplaty) {
        this.kwotaDoSplaty = kwotaDoSplaty;
    }

    public void setWysokoscRaty(double wysokoscRaty) {
        this.wysokoscRaty = wysokoscRaty;
    }

    public void setPozostalaLiczbaRat(int pozostalaLiczbaRat) {
        this.pozostalaLiczbaRat = pozostalaLiczbaRat;
    }

    public double getKwotaDoSplaty() {
        return kwotaDoSplaty;
    }

    public double getWysokoscRaty() {
        return wysokoscRaty;
    }

    public Date getDataNastepnejRaty() {
        return dataNastepnejRaty;
    }

    private Kredyt(Customer customer, double kwota, int pozostalaLiczbaRat) throws SQLException { // konstruktor do uzycia w metodzie przyznanieKredytu
        this.accountNo = customer.getAccountNo();
        this.kwota = kwota;
        this.balance = customer.getBalance();
        this.pozostalaLiczbaRat = pozostalaLiczbaRat;
        this.oprocentowanieKredytu = sqlDriver.returnKredytProcent();
        this.ostatnieIDKredytu = sqlDriver.lastIDKredyt() + 1;
        this.kwotaDoSplaty = 0;
        this.komunikatZwrotny = null;
        this.dataRaty = sqlDriver.returnData();
        this.dataNastepnejRaty = null;
        this.wysokoscRaty = 0;
    }

    private Kredyt(Customer customer, Kredyt_DB kredytBaza) {// konstruktor do uzycia w metodzie splacRate
        this.balance = customer.getBalance();
        this.wysokoscRaty = kredytBaza.getKwota_raty();
        this.dataRaty = kredytBaza.getTermin_raty();
        this.dataNastepnejRaty = null;
        this.kwotaDoSplaty = kredytBaza.getIle_zostalo_splacic();
        this.pozostalaLiczbaRat = kredytBaza.getIle_zostalo_rat();
    }

    public static int przeksztalcanie(double wartosc){ // zmiana double na int pomnozony x100 w celu wykonania poprawnych obliczen
        String tekst = wartosc + "0";
        String[] tablica = tekst.split("(?<=\\.\\d{2})");
        tablica[0] = tablica[0].replace(".", "");
        return Integer.parseInt(tablica[0]);
    }

    public boolean stanKontaDodatni(Kredyt kredyt) { //sprawdza czy stan konta jest dodatni
        return kredyt.balance > 0;
    }

    public boolean weryfikacjaPrzychodow(Kredyt kredyt, double przychod) throws SQLException {
        List<Kredyt_DB> listaKredytow = sqlDriver.returnKlientaKredyty(kredyt.accountNo);
        double sumaKredytow = 0;
        for (Kredyt_DB i:listaKredytow){
            sumaKredytow = sumaKredytow + i.getKwota_raty();
        }
//        System.out.println(sumaKredytow);
        wysokoscRaty(kredyt);
        return (sumaKredytow + kredyt.wysokoscRaty) <= (0.4 * przychod);
    }

    public void kwotaDoSplaty(Kredyt kredyt) {    //wyliczenie calkowitej kwoty do splaty
        kredyt.kwotaDoSplaty = Math.round((kredyt.kwota * (1 + (kredyt.oprocentowanieKredytu/100)))*100)/100d;     //zaokraglenie do 2 mijsca po przecinku
    }

    public void dataNastepnejRaty(Kredyt kredyt) throws SQLException {   //ustalenie daty nastepnej raty
        kredyt.dataNastepnejRaty = sqlDriver.returnData();//kredyt.dataRaty; //nowy kredyt otrzyma date raty za miesiac od daty aktualnej
        int temp = kredyt.dataNastepnejRaty.getMonth() + 1;  //zamiast kredyt.dataRaty.getMonth +1;  //przy splacie raty data ustalana jest na nastepny miesiac tego samego dnia
        kredyt.dataNastepnejRaty.setMonth(temp);
    }

    public void wysokoscRaty(Kredyt kredyt) {   //obliczanie wysokosci raty
        kredyt.wysokoscRaty = Math.round((kredyt.kwotaDoSplaty/kredyt.pozostalaLiczbaRat)*100d)/100d; //zaokraglenie do 2 miejsca po przecinku
    }

    public double obliczRataPrzycisk(double kwota, int pozostalaLiczbaRat) throws SQLException {  //metoda uruchamiana przez przycisk w GUI do obliczenia raty kredytu
        return Math.round((kwota * (1 + (sqlDriver.returnKredytProcent()/100))/pozostalaLiczbaRat)*100d)/100d;
    }

    public void pomniejszenieKwotyDoSplaty(Kredyt kredyt) {   //obnizenie pozostalej kwoty do splaty o wysokosc raty
        kredyt.kwotaDoSplaty = (przeksztalcanie(kredyt.kwotaDoSplaty) - przeksztalcanie(kredyt.wysokoscRaty))/100d;
    }

    public String przyznanieKredytu(Customer customer, double kwota, int pozostalaLiczbaRat, double przychod) throws SQLException, DatabaseException, TransferException {
        Kredyt kredyt = new Kredyt(customer, kwota, pozostalaLiczbaRat);
        kwotaDoSplaty(kredyt);
        if (stanKontaDodatni(kredyt) && weryfikacjaPrzychodow(kredyt, przychod)) {
            Bank.transfer(kredyt.accountNo, kredyt.kwota, "Wypłata kredytu nr: " + (kredyt.ostatnieIDKredytu));
            dataNastepnejRaty(kredyt);
            int correctNewCredit = sqlDriver.insertNewKredyt(customer, sqlDriver.returnData() /*kredyt.dataRaty,*/, kredyt.kwotaDoSplaty, kredyt.pozostalaLiczbaRat, kredyt.kwotaDoSplaty, kredyt.dataNastepnejRaty, kredyt.wysokoscRaty);
            if (correctNewCredit != 1) {
                throw new DatabaseException("błąd zapisu w bazie danych");
            } else {
                kredyt.komunikatZwrotny = "przyznano kredyt";
            }
        } else {
            kredyt.komunikatZwrotny = "wniosek o kredyt został odrzucony";
        }
        return kredyt.komunikatZwrotny;
    }

    public String splataRaty(Customer customer, int id) throws SQLException, TransferException, DatabaseException { //dodac weryfikacje zmienionych rekordow
        Kredyt_DB kredytBaza = sqlDriver.returnOneKredyt(id);
        Kredyt splataKredyt = new Kredyt(customer,kredytBaza);
        int correctLoanPayment;

        if (splataKredyt.balance >= splataKredyt.wysokoscRaty) {
            customer.transfer(Bank.getBankNo(), splataKredyt.wysokoscRaty, "Spłata raty kredytu nr: " + id);
            dataNastepnejRaty(splataKredyt);
            pomniejszenieKwotyDoSplaty(splataKredyt);
            splataKredyt.pozostalaLiczbaRat -= 1;

            if (splataKredyt.pozostalaLiczbaRat == 0) {
                correctLoanPayment = sqlDriver.splacRate(null,0, splataKredyt.pozostalaLiczbaRat, splataKredyt.kwotaDoSplaty, id);
                if (correctLoanPayment != 1) {
                    throw new DatabaseException("błąd zapisu w bazie danych");
                } else {
                    return "kredyt został spłacony";
                }
            } else if (splataKredyt.pozostalaLiczbaRat == 1) {
                splataKredyt.wysokoscRaty = splataKredyt.kwotaDoSplaty;
            }

            correctLoanPayment = sqlDriver.splacRate(splataKredyt.dataNastepnejRaty, splataKredyt.wysokoscRaty, splataKredyt.pozostalaLiczbaRat, splataKredyt.kwotaDoSplaty, id);
            if (correctLoanPayment != 1){
                throw new DatabaseException("błąd zapisu w bazie danych");
            } else {
                return "rata została zapłacona";
            }

        } else {
            return "brak wystarczających środków na koncie";
        }
    }
}