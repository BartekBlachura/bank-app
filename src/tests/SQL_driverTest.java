package tests;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class SQL_driverTest {

    public  SQL_driver driver = new SQL_driver();
    public double eps = 0.01;


    // ------------------------SEKCJA KLIENTA--------------------
    @Test
    void InsertAndReturnNewKlientTest(){
        Customer_DB klient = new Customer_DB();
        Customer_DB klient_back = new Customer_DB();
        boolean finished = false;
        Random random = new Random();
        StringBuilder accountNo = new StringBuilder();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String data_st = dateFormat.format(date).substring(11).replace(":","");

        klient.setPassHash("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");
        klient.setUserName("test_"+data_st);
        accountNo = new StringBuilder("55000");
        for (int i = 0; i < 5; i++){
            accountNo.append(random.nextInt(10));
        }
        accountNo = accountNo.append("1234567890123456");


        klient.setAccountNo(accountNo.toString());
        klient.setBalance(1000);
        try {
            int result = driver.insertNewClient(klient);
            assertEquals(1, result);
            klient_back = driver.returnClient("test_"+data_st);

        }catch (Exception e){assertTrue(false);}
        assertEquals(klient.getUserName(), klient_back.getUserName());
        assertEquals(klient.getAccountNo(),klient_back.getAccountNo());
        assertEquals(klient.getPassHash(),klient_back.getPassHash());
        assertEquals(klient.getBalance(),klient_back.getBalance(),0.0001);
    }

    @Test
    void UpdateClientBalanceTest(){
        try {
            Customer_DB klient_back = new Customer_DB();
            klient_back = driver.returnClient("test_085819");
            double stan_poczatkowy = klient_back.getBalance();
            double stan_wyjsciowy = stan_poczatkowy;
            double[] lista_kwot = new double[] {-500d, 2.33, 33.222, -0.222, 0, 15, 500, -2.33, -47};
            for (double kwota:lista_kwot) {
                stan_wyjsciowy = stan_wyjsciowy + kwota;
                assertEquals(1, driver.updateClientBalance(klient_back.getAccountNo(),kwota));
                klient_back = driver.returnClient("test_085819");
                assertEquals(stan_wyjsciowy, klient_back.getBalance(),eps);
            }
        }catch (Exception e){assertTrue(false);}
    }

//    @Test
//    void updateClientPassHashTest() throws NoSuchAlgorithmException, SQLException {
//        for (int ID = 14; ID < 25; ID++){
//            String newPassHash = Hasher.sha256(""+ ID + ID);
//            assertEquals(1, driver.updateClientPassHash(ID,newPassHash));
//        }
//        for (int ID = 30; ID < 34; ID++){
//            String newPassHash = Hasher.sha256(""+ ID + ID);
//            assertEquals(1, driver.updateClientPassHash(ID,newPassHash));
//        }
//        for (int ID = 35; ID < 74; ID++){
//            String newPassHash = Hasher.sha256(""+ ID + ID);
//            assertEquals(1, driver.updateClientPassHash(ID,newPassHash));
//        }
//        for (int ID = 75; ID < 81; ID++){
//            String newPassHash = Hasher.sha256(""+ ID + ID);
//            assertEquals(1, driver.updateClientPassHash(ID,newPassHash));
//        }
//        for (int ID = 75; ID < 90; ID++){
//            String newPassHash = Hasher.sha256(""+ ID + ID);
//            assertEquals(1, driver.updateClientPassHash(ID,newPassHash));
//        }
//    }

    @Test
    void IsExistAccountNoTest(){
        try{
            assertTrue(driver.isExistAccountNo("55500000001234567890123456"));
            assertFalse(driver.isExistAccountNo("55500060001234567890123456"));
        }catch(Exception e){ assertTrue(false); }
    }

    @Test
    void IsExistCustomerLogin(){
        try{
            assertTrue(driver.isExistCustomerLogin("test_085819"));
            assertFalse(driver.isExistCustomerLogin("test_NIEMAKONTA"));
        }catch(Exception e){ assertTrue(false); }
    }

    @Test
    void IsExistAdminLogin(){
        try{
            assertTrue(driver.isExistAdminLogin("Admin"));
            assertFalse(driver.isExistAdminLogin("test_NIEMAKONTA"));
        }catch(Exception e){ assertTrue(false); }
    }

    // ------------------------SEKCJA ADMINA--------------------
    @Test
    void ReturnAdminTest(){
        try{
            Customer_DB dane_admina = new Customer_DB();
            dane_admina = driver.returnAdmin("Admin");
            assertEquals("Admin",dane_admina.getUserName());
            assertEquals("3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4",dane_admina.getPassHash());
        }catch(Exception e){ assertTrue(false); }
    }


    // ------------------------SEKCJA PRZELEWOW--------------------
    @Test
    void NewPrzelewTestAndAktualizacjamiDniaIKont(){
        try{

            Customer_DB nadawca = driver.returnClient("test_085819");
            Customer_DB odbiorca = driver.returnClient("test_092157");
            double[] lista_kwot = new double[] {500, 2.33};
            Date dzis = new Date();
            for (double kwota_przelewu:lista_kwot) {
                dzis = driver.returnData();
                assertEquals(1, driver.newPrzelew(nadawca.getAccountNo(), odbiorca.getAccountNo(),
                        kwota_przelewu, "TestTytul", dzis));
                assertEquals(1, driver.updateClientBalance(nadawca.getAccountNo(), -kwota_przelewu));
                assertEquals(1, driver.updateClientBalance(odbiorca.getAccountNo(), kwota_przelewu));
                assertEquals(1, driver.insertNewDataPlusDay());
                dzis = driver.returnData();
                assertEquals(1, driver.newPrzelew(odbiorca.getAccountNo(), nadawca.getAccountNo(),
                        kwota_przelewu, "TestTytulZwrotny", dzis));
                assertEquals(1, driver.updateClientBalance(nadawca.getAccountNo(), kwota_przelewu));
                assertEquals(1, driver.updateClientBalance(odbiorca.getAccountNo(), -kwota_przelewu));
                assertEquals(1, driver.insertNewDataPlusDay());
            }
        }catch(Exception e){ assertTrue(false); }


    }

    @Test
    void ReturnAllPrzelewyTest(){
        List<Przelew_DB> ls = new ArrayList<Przelew_DB>();
        try {
            ls = driver.returnAllPrzelewy();
            assertTrue(ls.size()>0);
            assertTrue(ls.get(ls.size()-1).getKwota()>0);
        }catch (Exception e){ assertTrue(false); }
    }

    @Test
    void ReturnKlientaPrzelewy(){
        List<Przelew_DB> ls = new ArrayList<Przelew_DB>();
        try{
            ls = driver.returnKlientaPrzelewy("55504000001234567890123456");
            for (Przelew_DB przelew:ls) {
                if(przelew.getNr_odbiorcy().equals("55504000001234567890123456")){
                    assertTrue(przelew.getKwota()>0);
                } else{
                    assertTrue(przelew.getKwota()<0);
                }
            }
        }catch (Exception e){ assertTrue(false); }
    }



    // ------------------------SEKCJA LOKAT--------------------
    @Test
    void InsertNewLokataTest(){
        Customer klient = new Customer();
        try {
            klient.logIn("test_085819");
            Date start = driver.returnData();
            Date stop = Time.addMonth(start, 2);
            assertEquals(1,driver.insertNewLokata(klient, start, stop, 250d, start));

        }catch (Exception e){ assertTrue(false); }
    }

    @Test
    void ReturnLokataTest(){
        Lokata_DB lokata = new Lokata_DB();
        try {
            lokata = driver.returnLokata(9);
            assertEquals("55500000001234567890123456", lokata.getNr_konta_klienta());
            assertEquals(4.6, lokata.getProcent(),eps);
            lokata = driver.returnLokata(5);
            assertEquals("00000000001234567890123457", lokata.getNr_konta_klienta());
            assertEquals(2.3, lokata.getProcent(),eps);
        }catch (Exception e){ assertTrue(false); }
    }

    @Test
    void returnKlientaLokatyTest(){
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        Lokata_DB lokata = new Lokata_DB();
        try {
            ls = driver.returnKlientaLokaty("55500000001234567890123456");
            assertTrue(ls.size()>1);
            ls = driver.returnKlientaLokaty("55500002201234567890123456");
            assertFalse(ls.size()>0);
        }catch (Exception e){ assertTrue(false); }
    }

    @Test
    void returnAllLokatyAndLastIDTest(){
        List<Lokata_DB> ls = new ArrayList<Lokata_DB>();
        Lokata_DB lokata = new Lokata_DB();
        try {
            ls = driver.returnAllLokaty();
            int ostatni_id = driver.lastIDLokat();
            assertEquals(ostatni_id, ls.get(ls.size()-1).getID());
        }catch (Exception e){ assertTrue(false); }

    }

    @Test
    void CloseLokataTest(){
        Lokata_DB lokata = new Lokata_DB();
        try {
            assertEquals(1,driver.closeLokata(0d, driver.returnData(), 9));
            lokata = driver.returnLokata(9);
            assertEquals(0,lokata.getAktualne_srodki(),eps);
        }catch (Exception e){ assertTrue(false); }
    }
    // ------------------------SEKCJA KREDYTY--------------------


//    @Test
//    void SplacRateTest(){
//        try{
//            Date teraz = null;
//            driver.splacRate(teraz, 0, 0,0, 1);
//        }catch(Exception e){ assertTrue(false); }
//    }

    // ------------------------SEKCJA INNE--------------------

    @Test
    void InsertNewKredytProcentTest(){
        double kredyt_procent_new = 12.3;
      try {
          int result = driver.insertNewKredytProcent(kredyt_procent_new);
          assertEquals(1, result);
          assertEquals(kredyt_procent_new, driver.returnKredytProcent(),eps);
      }catch (Exception e) {
          assertTrue(false);
      }
    }

    @Test
    void InsertAndReturnNewLokataProcentTest(){
        double lokata_procent_new = 4.6;
        try {
            int result = driver.insertNewLokataProcent(lokata_procent_new);
            assertEquals(1, result);
            assertEquals(lokata_procent_new, driver.returnLokataProcent(),eps);
        }catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void ReturnDataTest(){
        try {
            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}", Pattern.CASE_INSENSITIVE);
            String data_wynik = driver.returnData().toString();
            System.out.println("Test Return data wynik: "+data_wynik);
            Matcher matcher = pattern.matcher(data_wynik);
            assertTrue(matcher.matches());
        }catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void InsertNewDataTestPlusDay(){
        try {
            Date stara = driver.returnData();
            assertEquals(1, driver.insertNewDataPlusDay());
            Date nowa = driver.returnData();
            stara = Time.addTime(stara,1);
            assertEquals(stara,nowa);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void InsertNewDataTest(){
        try {
            Date stara = driver.returnData();
            assertEquals(1, driver.insertNewData(Time.addTime(stara,4)));
            Date nowa = driver.returnData();
            Date oczekiwana = Time.addTime(stara,4);
            assertEquals(oczekiwana,nowa);
            assertEquals(0, driver.insertNewData(stara));
        }catch (Exception e){
            assertTrue(false);
        }
    }

}