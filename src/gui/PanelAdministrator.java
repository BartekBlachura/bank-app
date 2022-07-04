package gui;

import javax.swing.*;
import java.awt.*;

public class PanelAdministrator extends JPanel  {
    private MainListener listener;
    private JButton but_Change_Cre;
    private JButton but_Change_Dep;
    private JButton but_Transfers;
    private JButton but_Log_out;
    private JButton but_Tran_history;
    private JTextField textCredit_P;
    private JTextField textDeposit_P;

    public JButton getBut_Change_Cre() {
        return but_Change_Cre;
    }

    public JButton getBut_Change_Dep() {
        return but_Change_Dep;
    }
    public JButton getBut_Tran_history() {
        return  but_Tran_history;
    }
    public JButton getBut_log_out() {
        return  but_Log_out;
    }
    public JButton getBut_Transfers() {
        return  but_Transfers;
    }
    public String getCredit_percent() {
        return textCredit_P.getText();
    }
    public String getDeposit_percent() {
        return textDeposit_P.getText();
    }

    public PanelAdministrator(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setPanelAdministrator(this);

        createComponents();
    }

    private void createComponents() {
        this.setLayout(new BorderLayout());

        JPanel Top_panel = new JPanel();
        JPanel Top_panel_title = new JPanel();
        JPanel Top_panel_pink = new JPanel();
        JPanel Center_panel = new JPanel();
        JPanel Down_panel = new JPanel();
        JPanel Right_panel = new JPanel();
        JPanel Left_panel = new JPanel();
        Top_panel.setLayout(new BorderLayout());
        JPanel Inside_panel = new JPanel();
        Inside_panel.setLayout((null));

        Top_panel.setBackground(new Color(57, 49, 133));
        Top_panel_pink.setBackground(new Color(229, 9, 127));
        Top_panel_title.setBackground(new Color(57, 49, 133));
        Center_panel.setBackground(Color.white);
        Down_panel.setBackground(Color.white);
        Right_panel.setBackground(Color.white);
        Left_panel.setBackground(Color.white);
        Inside_panel.setBackground(Color.white);

        Top_panel.setPreferredSize(new Dimension(500,70));
        Top_panel_pink.setPreferredSize(new Dimension(500,8));
        Center_panel.setPreferredSize(new Dimension(480,490));
        Down_panel.setPreferredSize(new Dimension(500,20));
        Right_panel.setPreferredSize(new Dimension(50,410));
        Left_panel.setPreferredSize(new Dimension(50,410));
        Inside_panel.setPreferredSize(new Dimension(400,400));

//-----------------------------Top panel-------------------------------------

        //--------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("ADMINISTRATOR"); // wyświetlany tekst
        label_top.setIcon(top);// wyświetlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(5);//odstęp tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);


//------------------------------Center Panel-----------------------------------


        JLabel information = new JLabel();
        information.setText("AKTUALNE OPROCENTOWANIE");
        information.setBounds(95, 0, 230, 40);
        information.setForeground(Color.darkGray);
        information.setFont(new Font("Calibre", Font.BOLD, 14));
        Inside_panel.add(information);

        JLabel information2 = new JLabel();
        information2.setText("Kredyt: " + listener.displayPercentageLoan()+"     Lokata: "
                +listener.displayPercentageDeposit());
        information2.setBounds(110, 40, 280, 20);
        information2.setFont(new Font("Calibre", Font.BOLD, 15));
        Inside_panel.add(information2);


        JLabel information3 = new JLabel();

        information3.setText("NOWE OPROCENTOWANIE");
        information3.setBounds(110, 60, 230, 40);
        information3.setForeground(Color.darkGray);
        information3.setFont(new Font("Calibre", Font.BOLD, 14));
        Inside_panel.add(information3);

        //----------------Textbox change_D---------

        JLabel credit = new JLabel("Kredyt");
        credit.setBackground(new Color(20, 100, 5));
        credit.setBounds(120, 95, 150, 40);
        credit.setFont(new Font("Calibre", Font.BOLD, 12)); //ustawienia czcionki

        textCredit_P = new JTextField();
        textCredit_P.setBounds(90, 130, 100, 40);

        Inside_panel.add(textCredit_P);
        Inside_panel.add(credit);

        //----------------Textbox change2---------

        JLabel deposit = new JLabel("Lokata");
        deposit.setBounds(238, 95, 80, 40);
        deposit.setFont(new Font("Calibre", Font.BOLD, 12));

        textDeposit_P = new JTextField();
        textDeposit_P.setBounds(210, 130, 100, 40);

        Inside_panel.add(textDeposit_P);
        Inside_panel.add(deposit);


        //----------------Button change_Credit-------------------

        JLabel change_C = new JLabel();
        but_Change_Cre = new JButton();
        but_Change_Cre.setBounds(90, 185, 100, 35);
        but_Change_Cre.setText("Zmień");
        but_Change_Cre.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Change_Cre.setHorizontalTextPosition(JButton.CENTER);
        but_Change_Cre.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Change_Cre.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Change_Cre.setBackground(new Color(229, 9, 127));
        but_Change_Cre.setBorder(BorderFactory.createEtchedBorder());
        but_Change_Cre.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Change_Cre.setHorizontalAlignment(JButton.CENTER);
        but_Change_Cre.addActionListener(listener);
        getBut_Change_Cre().setOpaque(true);
        Inside_panel.add(but_Change_Cre,BorderLayout.CENTER);
        Inside_panel.add(change_C);

        //----------------Button change_Deposit-------------------

        JLabel change_D = new JLabel();
        but_Change_Dep = new JButton();
        but_Change_Dep.setBounds(210, 185, 100, 35);
        but_Change_Dep.setText("Zmień");
        but_Change_Dep.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Change_Dep.setHorizontalTextPosition(JButton.CENTER);
        but_Change_Dep.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Change_Dep.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Change_Dep.setBackground(new Color(229, 9, 127));
        but_Change_Dep.setBorder(BorderFactory.createEtchedBorder());
        but_Change_Dep.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Change_Dep.setHorizontalAlignment(JButton.CENTER);
        but_Change_Dep.addActionListener(listener);
        but_Change_Dep.setOpaque(true);
        Inside_panel.add(but_Change_Dep,BorderLayout.CENTER);
        Inside_panel.add(change_D);

        //----------------Button Transaction history-------------------

        JLabel tran_history = new JLabel();
        but_Tran_history = new JButton();
        but_Tran_history.setBounds(70, 300, 260, 35);
        but_Tran_history.setText("Historia Transakcji");
        but_Tran_history.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Tran_history.setHorizontalTextPosition(JButton.CENTER);
        but_Tran_history.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Tran_history.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Tran_history.setBackground(new Color(229, 9, 127));
        but_Tran_history.setBorder(BorderFactory.createEtchedBorder());
        but_Tran_history.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Tran_history.setHorizontalAlignment(JButton.CENTER);
        but_Tran_history.addActionListener(listener);
        but_Tran_history.setOpaque(true);
        Inside_panel.add(but_Tran_history,BorderLayout.CENTER);
        Inside_panel.add(tran_history);


        //----------------Button Transfers-------------------

        JLabel transfer = new JLabel();
        but_Transfers = new JButton();
        but_Transfers.setBounds(70, 250, 260, 35);
        but_Transfers.setText("Nowy przelew");
        but_Transfers.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Transfers.setHorizontalTextPosition(JButton.CENTER);
        but_Transfers.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Transfers.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Transfers.setBackground(new Color(229, 9, 127));
        but_Transfers.setBorder(BorderFactory.createEtchedBorder());
        but_Transfers.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Transfers.setHorizontalAlignment(JButton.CENTER);
        but_Transfers.addActionListener(listener);
        but_Transfers.setOpaque(true);
        Inside_panel.add(but_Transfers,BorderLayout.CENTER);
        Inside_panel.add(transfer);

        //------------------------------Button log out--------------------------

        JLabel log_out = new JLabel();
        but_Log_out = new JButton();
        but_Log_out.setBounds(230, 360, 100, 35);
        but_Log_out.setText("Wyloguj");
        but_Log_out.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Log_out.setHorizontalTextPosition(JButton.CENTER);
        but_Log_out.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Log_out.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Log_out.setBackground(new Color(229, 9, 127));
        but_Log_out.setBorder(BorderFactory.createRaisedBevelBorder());
        but_Log_out.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Log_out.setHorizontalAlignment(JButton.CENTER);
        but_Log_out.addActionListener(listener);
        but_Log_out.setOpaque(true);
        Inside_panel.add(but_Log_out,BorderLayout.CENTER);
        Inside_panel.add(log_out);

//--------------------------------------------------

        Top_panel.add(Top_panel_title,BorderLayout.CENTER);
        Top_panel.add(Top_panel_pink,BorderLayout.SOUTH);
        Center_panel.add(Inside_panel,BorderLayout.CENTER);
        this.add(Top_panel,BorderLayout.NORTH);
        this.add(Center_panel,BorderLayout.CENTER);
        this.add(Down_panel,BorderLayout.SOUTH);
        this.add(Right_panel,BorderLayout.EAST);
        this.add(Left_panel,BorderLayout.WEST);

    }
}
