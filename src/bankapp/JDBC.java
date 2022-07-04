package bankapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBC {

    //DOKUMENTACJA Modul odpowiedzialny za stworzenie singletonu do komunikacji z baza danych,
    // zawiera wszystkie niezbedne parametry do komunikacji tj: adres serwera, nazwe bazy, uzytkownik, haslo
    // zawiera rowniez driver z biblioteki mysql-connector-java-8.0.29.jar
    //Uwaga dla projektu uruchamianego w InteliiJ nalezy w File-> Project Structure dodac ww. biblioteke, dla VS Code
    // nalezy w settings.json dodac w sourcePaths "lib"

    static Connection conn = null;

    private JDBC() {
    }

    public static Connection getConnection() {
        String polaczenieURL = "jdbc:mysql://mysql49.mydevil.net/m1063_dmt?user=m1063_dmt&password=";
        // nazwa_serweru/nazwaBD?user=test&password=test // adres bazy i login, haslo

        try (Scanner scanner = new Scanner(new File("pass.txt"))) {
            polaczenieURL += scanner.nextLine();

        } catch (FileNotFoundException e) {
            CreateFile.toFile(e.getMessage());
        }

        try {
            if (conn == null) {
                Class.forName("com.mysql.cj.jdbc.Driver"); // wybor sterownika (tu mysql)
                conn = DriverManager.getConnection(polaczenieURL);
                // conn = DriverManager.getConnection(url); // metoda odpowiadajaca
                // za nawiazanie
                // polaczenia z BD
            }
        } catch (ClassNotFoundException e) {
            //Error wyrzucany gdy jest zly driver, nie ma go wogole, brak komunikacji
            CreateFile.toFile("Zly driver, BD nie znaleziona \n" + e.getMessage());
        } // BD nie znaleziona
        catch (SQLException e) {
            //Error wyrzucany gdy jest znaleziona bibioteka i znaleziony serwer ale dane logowania do BD sa zle
            CreateFile.toFile("Zle dane logowania/inny problem z dostepem do bazy \n" + e.getMessage()+
                    "\n Error Code: "+e.getErrorCode()+ "\n SQL State: "+ e.getSQLState());
        }
        catch (Exception e) {
            CreateFile.toFile(e.getMessage());
        } // inne bledy - nie spotkalem

        return conn;
    }
}