package gui;

import bankapp.Runner;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static Dimension standardDimension = new Dimension(400,550);

//    zmienna refreshable wraz z metodami służy set i get do automatycznego odświażania panelów klas:
//    1 - PanelClient
//    2 - PanelAdministrator
//    3 - PanelHistory
//    4 - Deposit_Table
//    5 - Credit_Table
//    7 - PanelDeposit
//    8 - PanelCredit

    private static int refreshable = 0;
    public static void setRefreshable(int panelNo){
        refreshable = panelNo;
    }
    public static int getRefreshable() {
        return refreshable;
    }

    public MainFrame(Runner runner) {
        super();
        setPreferredSize(standardDimension);

        MainListener listener = new MainListener(this, runner);
        JPanel panel = new PanelLogin(listener);
        add(panel);
        addMouseMotionListener(new MainListener(this, runner));

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
