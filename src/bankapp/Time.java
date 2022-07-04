package bankapp;

import java.util.Calendar;
import java.util.Date;

//DOKUMENTACJA Klasa przeznaczona do wszystkich operacjach zwiazanych z czasem (dodawanie dni, miesiecy ect)

public class Time {
    public static Date addTime(Date Dzien, int ile_dni){
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(Dzien);
        c.add(Calendar.DATE, ile_dni);
        dt = c.getTime();
       // System.out.println(dt.toString());
        return dt;
    }

    public static Date addMonth(Date Dzien, int ile_miesiecy){
        Date dat = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(Dzien);
        c.add(Calendar.MONTH, ile_miesiecy);
        dat = c.getTime();
        // System.out.println(dt.toString());
        return dat;
    }
}
