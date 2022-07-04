package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import java.awt.*;



public class PanelClient extends JPanel {
    private MainListener listener;
    private JButton but_Transfer;
    private JButton but_Credits;
    private JButton but_Deposit;
    private JButton but_History;
    private JButton but_log_out;
    private JButton but_TableDeposit;
    private JButton but_TableCredit;


    public JButton getBut_Transfer() {

        return but_Transfer;
    }
    public JButton getBut_Credits() {

        return but_Credits;
    }
    public JButton getBut_Deposit() {

        return but_Deposit;
    }
    public JButton getBut_History() {

        return but_History;
    }
    public JButton getBut_TableDeposit() {

        return but_TableDeposit;
    }
    public JButton getBut_TableCredit() {

        return but_TableCredit;
    }
    public JButton getBut_log_out() {

        return but_log_out;
    }

    public PanelClient(MainListener listener) {
        super();
        this.listener = listener;
        this.listener.setPanelClient(this);

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
        JPanel Inside_panel_1a = new JPanel();
        JPanel Inside_panel_1b = new JPanel();
        JPanel Inside_panel_1c = new JPanel();
        JPanel Inside_panel_1d = new JPanel();
        JPanel Inside_panel_1e = new JPanel();
        JPanel Inside_panel_2 = new JPanel();
        JPanel Inside_panel_3a = new JPanel();
        JPanel Inside_panel_3b = new JPanel();
        JPanel Inside_panel_4 = new JPanel();
        Inside_panel_4.setLayout((null));
        //Inside_panel_1.setLayout((null));
        //Inside_panel_3.setLayout((null));

        Top_panel.setBackground(new Color(57, 49, 133));
        Top_panel_pink.setBackground(new Color(229, 9, 127));
        Top_panel_title.setBackground(new Color(57, 49, 133));
        Center_panel.setBackground(Color.white);
        Down_panel.setBackground(Color.white);
        Right_panel.setBackground(Color.white);
        Left_panel.setBackground(Color.white);
        Inside_panel_1a.setBackground(Color.white);
        Inside_panel_1b.setBackground(Color.white);
        Inside_panel_1c.setBackground(Color.white);
        Inside_panel_1d.setBackground(Color.white);
        Inside_panel_1e.setBackground(Color.white);
        Inside_panel_2.setBackground(Color.white);
        Inside_panel_3a.setBackground(Color.white);
        Inside_panel_3b.setBackground(Color.white);
        Inside_panel_4.setBackground(Color.white);


        Top_panel.setPreferredSize(new Dimension(400,70));
        Top_panel_pink.setPreferredSize(new Dimension(400,8));
        Center_panel.setPreferredSize(new Dimension(400, 450));
        Down_panel.setPreferredSize(new Dimension(400, 20));
        Right_panel.setPreferredSize(new Dimension(10, 410));
        Left_panel.setPreferredSize(new Dimension(10, 410));
        Inside_panel_1a.setPreferredSize(new Dimension(800, 20));
        Inside_panel_1b.setPreferredSize(new Dimension(800, 20));
        Inside_panel_1c.setPreferredSize(new Dimension(800, 20));
        Inside_panel_1d.setPreferredSize(new Dimension(800, 30));
        Inside_panel_1e.setPreferredSize(new Dimension(800, 5));

        Inside_panel_2.setPreferredSize(new Dimension(800, 50));
        Inside_panel_3a.setPreferredSize(new Dimension(800, 82));
        Inside_panel_3b.setPreferredSize(new Dimension(800, 82));
        Inside_panel_4.setPreferredSize(new Dimension(800, 50));

//------------------------------------Top panel-------------------------------------


        //--------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("PANEL KLIENTA"); // wyświetlany tekst
        label_top.setIcon(top);// wyświetlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(5);//odstęp tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);


        //--------------------------------Center Panel 1-----------------------------------

        JLabel information1 = new JLabel();
        information1.setText("NUMER KONTA");
        information1.setBounds(101, 10, 280, 10);
        information1.setFont(new Font("Calibre", Font.BOLD, 10));

        JLabel information2 = new JLabel();
        information2.setText(listener.displayAccountNo());
        information2.setBounds(101, 0, 280, 20);
        information2.setFont(new Font("Calibre", Font.BOLD, 12));

        JLabel information3 = new JLabel();
        information3.setText("DOSTĘPNE ŚRODKI PLN:");
        information3.setBounds(85, 0, 280, 20);
        information3.setFont(new Font("Calibre", Font.BOLD, 12));

        JLabel information4 = new JLabel();
        information4.setText(listener.displayBalance());
        information4.setBounds(90, 0, 280, 25);
        information4.setFont(new Font("Calibre", Font.BOLD, 20));

        //----------------------Button transfer --------------------

        but_Transfer = new JButton();
        but_Transfer.setText("Nowy Przelew");
        but_Transfer.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Transfer.setHorizontalTextPosition(JButton.CENTER);
        but_Transfer.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Transfer.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Transfer.setBackground(new Color(57, 49, 133));
        //but_Transfer.setBorder(BorderFactory.createEtchedBorder());
        but_Transfer.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Transfer.setHorizontalAlignment(JButton.CENTER);
        but_Transfer.setPreferredSize(new Dimension(180, 40));
        but_Transfer.setOpaque(true);
        but_Transfer.addActionListener(listener);
        Inside_panel_2.add(but_Transfer,BorderLayout.SOUTH);

        //----------------------Button history --------------------

        but_History = new JButton();
        but_History.setText("Historia");
        but_History.setForeground(new Color(255, 255, 255)); //kolor tekstu

        but_History.setHorizontalTextPosition(JButton.CENTER);
        but_History.setFont(new Font("Calibre", Font.BOLD, 15));
        but_History.setFocusable(false); //usuwa ramkę wokół tekstu
        but_History.setBackground(new Color(57, 49, 133));
        but_History.setBorder(BorderFactory.createEtchedBorder());
        but_Transfer.setBorder(BorderFactory.createLineBorder(new Color(57, 49, 133), 3));
        but_History.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_History.setHorizontalAlignment(JButton.CENTER);
        but_History.setPreferredSize(new Dimension(100, 40));
        but_History.addActionListener(listener);
        but_History.setOpaque(true);
        Inside_panel_2.add(but_History,BorderLayout.SOUTH);

        //----------------------Button Deposit table--------------------

        but_TableDeposit = new JButton();
        but_TableDeposit.setText("Lokaty");
        but_TableDeposit.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_TableDeposit.setHorizontalTextPosition(JButton.CENTER);
        but_TableDeposit.setFont(new Font("Calibre", Font.BOLD, 15));
        but_TableDeposit.setFocusable(false); //usuwa ramkę wokół tekstu
        but_TableDeposit.setBackground(new Color(229, 9, 127));
        but_TableDeposit.setBorder(BorderFactory.createEtchedBorder());
        but_TableDeposit.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_TableDeposit.setHorizontalAlignment(JButton.CENTER);
        but_TableDeposit.setPreferredSize(new Dimension(140, 80));
        but_TableDeposit.addActionListener(listener);
        but_TableDeposit.setOpaque(true);
        Inside_panel_3a.add(but_TableDeposit);

        //----------------------Button Credit table--------------------

        but_TableCredit = new JButton();
        but_TableCredit.setText("Kredyty");
        but_TableCredit.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_TableCredit.setHorizontalTextPosition(JButton.CENTER);
        but_TableCredit.setFont(new Font("Calibre", Font.BOLD, 15));
        but_TableCredit.setFocusable(false); //usuwa ramkę wokół tekstu
        but_TableCredit.setBackground(new Color(229, 9, 127));
        but_TableCredit.setBorder(BorderFactory.createEtchedBorder());
        but_TableCredit.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_TableCredit.setHorizontalAlignment(JButton.CENTER);
        but_TableCredit.setPreferredSize(new Dimension(140, 80));
        but_TableCredit.addActionListener(listener);
        but_TableCredit.setOpaque(true);
        Inside_panel_3a.add(but_TableCredit);

        //----------------------Button deposit on top--------------------

        but_Deposit = new JButton();
        but_Deposit.setText("Nowa Lokata");
        but_Deposit.setForeground(new Color(255,255,255)); //kolor tekstu
        but_Deposit.setHorizontalTextPosition(JButton.CENTER);
        but_Deposit.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Deposit.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Deposit.setBackground(new Color(229, 9, 127));
        but_Deposit.setBorder(BorderFactory.createEtchedBorder());
        but_Deposit.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Deposit.setHorizontalAlignment(JButton.CENTER);
        but_Deposit.setPreferredSize(new Dimension(140, 80));
        //but_Transfer.setBounds(270, 00, 130, 40);
        but_Deposit.addActionListener(listener);
        but_Deposit.setOpaque(true);
        Inside_panel_3b.add(but_Deposit);

        //----------------------Button credits on top--------------------

        but_Credits = new JButton();
        but_Credits.setText("Nowy Kredyt");
        but_Credits.setForeground(new Color(255,255,255)); //kolor tekstu
        but_Credits.setHorizontalTextPosition(JButton.CENTER);
        but_Credits.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Credits.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Credits.setBackground(new Color(229, 9, 127));
        but_Credits.setBorder(BorderFactory.createEtchedBorder());
        but_Credits.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Credits.setHorizontalAlignment(JButton.CENTER);
        but_Credits.setPreferredSize(new Dimension(140, 80));
        //but_Transfer.setBounds(140, 00, 130, 40);
        but_Credits.setOpaque(true);
        but_Credits.addActionListener(listener);
        Inside_panel_3b.add(but_Credits );



        //----------------------Button log out--------------------------

        JLabel log_out = new JLabel();
        but_log_out = new JButton();
        but_log_out.setBounds(443, 10, 100, 35);
        but_log_out.setText("Wyloguj");
        but_log_out.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_log_out.setHorizontalTextPosition(JButton.CENTER);
        but_log_out.setFont(new Font("Calibre", Font.BOLD, 15));
        but_log_out.setFocusable(false); //usuwa ramkę wokół tekstu
        but_log_out.setBackground(new Color(229, 9, 127));
        but_log_out.setBorder(BorderFactory.createRaisedBevelBorder());
        but_log_out.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_log_out.setHorizontalAlignment(JButton.CENTER);
        but_log_out.setPreferredSize(new Dimension(100, 35));
        but_log_out.addActionListener(listener);
        but_log_out.setOpaque(true);
       // Inside_panel.add(but_log_out, BorderLayout.CENTER);
        Inside_panel_4.add(but_log_out);





//---------------------------------------------------------------------
        Inside_panel_1a.add(information1,BorderLayout.CENTER);
        Inside_panel_1b.add(information2, BorderLayout.CENTER);
        Inside_panel_1c.add(information3,BorderLayout.CENTER);
        Inside_panel_1d.add(information4,BorderLayout.SOUTH);

        Top_panel.add(Top_panel_title,BorderLayout.CENTER);
        Top_panel.add(Top_panel_pink,BorderLayout.SOUTH);

        Center_panel.add(Inside_panel_1a,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_1b,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_1c,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_1d,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_1e,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_2,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_3a,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_3b,BorderLayout.CENTER);
        Center_panel.add(Inside_panel_4,BorderLayout.CENTER);


        this.add(Top_panel,BorderLayout.NORTH);
        this.add(Center_panel,BorderLayout.CENTER);
        this.add(Down_panel,BorderLayout.SOUTH);
        this.add(Right_panel,BorderLayout.EAST);
        this.add(Left_panel,BorderLayout.WEST);

    }
}
