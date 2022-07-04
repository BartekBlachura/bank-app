import bankapp.Runner;
import gui.MainFrame;

import java.awt.*;

public class BankApp {

    public static void main(String[] args) {
        Runner runner = new Runner();
        runApp(runner);
        runner.autoRefresh(5000);
    }

    public static void runApp(Runner runner) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame(runner);
            }
        });
    }
}