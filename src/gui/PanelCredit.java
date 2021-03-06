package gui;

import javax.swing.*;
import java.awt.*;

public class PanelCredit extends JPanel {
    private MainListener listener;
    private JButton but_Ok;
    private JButton but_Back;
    private JButton but_calculate;
    private JTextField textAmo_credit;
    private JTextField textInstallment;
    private JTextField textIncome;

    public JButton getBut_Ok() {
        return but_Ok;
    }

    public JButton getBut_Back() {

        return but_Back;
    }
    public JButton getBut_calculate() {
        return but_calculate;
    }

    public String getTextAmo_credit() {
        return textAmo_credit.getText();
    }
    public String getTextInstallment() {
        return textInstallment.getText();
    }
    public String getTextIncome() {
        return textIncome.getText();
    }



    public PanelCredit(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setPanelCredit(this);

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
        Inside_panel.setLayout(null);


        Top_panel.setBackground(new Color(57, 49, 133));
        Top_panel_pink.setBackground(new Color(229, 9, 127));
        Top_panel_title.setBackground(new Color(57, 49, 133));
        Center_panel.setBackground(Color.white);
        Down_panel.setBackground(Color.white);
        Right_panel.setBackground(Color.white);
        Left_panel.setBackground(Color.white);
        Inside_panel.setBackground(Color.white);

        Top_panel.setPreferredSize(new Dimension(500,70));
        Top_panel_title.setPreferredSize(new Dimension(500,70));
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
        label_top.setText("KREDYT"); // wy??wietlany tekst
        label_top.setIcon(top);// wy??wietlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(80);//odst??p tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setBounds(100, 200, 200, 40);
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);



//------------------------------Center Panel-----------------------------------


        JLabel information = new JLabel();
        information.setText("Aktualne oprocentowanie: "+listener.displayPercentageLoan()+ "%");
        information.setBounds(93, 10, 280, 20);
        information.setFont(new Font("Calibre", Font.BOLD, 13));
        Inside_panel.add(information);

        //----------------Textbox amount of credit------------------------

        JLabel Amo_credit = new JLabel("Podaj kwot?? kredytu PLN");
        Amo_credit.setBounds(120, 77, 200, 40);
        Amo_credit.setFont(new Font("Calibre", Font.BOLD, 13)); //ustawienia czcionki

        textAmo_credit = new JTextField();
        textAmo_credit.setBounds(85, 45, 230, 40);
        Inside_panel.add(textAmo_credit);
        Inside_panel.add(Amo_credit);

        //-------------------Textbox installment-------------------------

        JLabel installment= new JLabel("Podaj liczb?? rat");
        installment.setBounds(150, 147, 200, 40);
        installment.setFont(new Font("Calibre", Font.BOLD, 13));

        textInstallment = new JTextField();
        textInstallment.setBounds(85, 115, 230, 40);

        Inside_panel.add(textInstallment);
        Inside_panel.add(installment);

        //----------------Textbox monthly income---------------

        JLabel title = new JLabel("Miesi??czny doch??d PLN");
        title.setBounds(124, 217, 200, 40);
        title.setFont(new Font("Calibre", Font.BOLD, 13));

        textIncome = new JTextField();
        textIncome.setBounds(85, 185, 230, 40);

        Inside_panel.add(textIncome);
        Inside_panel.add(title);


        //----------------Akcept Button-------------------

        JLabel akcept = new JLabel();
        but_Ok = new JButton();
        but_Ok.setBounds(70, 345, 100, 35);
        but_Ok.setText("Akceptuj");
        but_Ok.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Ok.setHorizontalTextPosition(JButton.CENTER);
        but_Ok.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Ok.setFocusable(false); //usuwa ramk?? wok???? tekstu
        but_Ok.setBackground(new Color(229, 9, 127));
        but_Ok.setBorder(BorderFactory.createEtchedBorder());
        but_Ok.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Ok.setHorizontalAlignment(JButton.CENTER);
        // but_Ok.setPreferredSize(new Dimension(90,35));
        but_Ok.addActionListener(listener);
        but_Ok.setOpaque(true);
        Inside_panel.add(but_Ok,BorderLayout.CENTER);
        Inside_panel.add(akcept);

        //----------------Back Button-------------------

        JLabel back = new JLabel();
        but_Back = new JButton();
        but_Back.setBounds(230, 345, 100, 35);
        but_Back.setText("Cofnij");
        but_Back.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Back.setHorizontalTextPosition(JButton.CENTER);
        but_Back.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Back.setFocusable(false); //usuwa ramk?? wok???? tekstu
        but_Back.setBackground(new Color(229, 9, 127));
        but_Back.setBorder(BorderFactory.createEtchedBorder());
        but_Back.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Back.setHorizontalAlignment(JButton.CENTER);
        but_Back.addActionListener(listener);
        but_Back.setOpaque(true);
        Inside_panel.add(but_Back,BorderLayout.CENTER);
        Inside_panel.add(back);

        //----------------Back calculate-------------------

        JLabel calculate = new JLabel();
        but_calculate = new JButton();
        but_calculate.setBounds(70, 275, 260, 35);
        but_calculate.setText("Oblicz wysoko???? raty");
        but_calculate.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_calculate.setHorizontalTextPosition(JButton.CENTER);
        but_calculate.setFont(new Font("Calibre", Font.BOLD, 13));
        but_calculate.setFocusable(false); //usuwa ramk?? wok???? tekstu
        but_calculate.setBackground(new Color(229, 9, 127));
        but_calculate.setBorder(BorderFactory.createEtchedBorder());
        but_calculate.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_calculate.setHorizontalAlignment(JButton.CENTER);
        but_calculate.addActionListener(listener);
        but_calculate.setOpaque(true);

        Inside_panel.add(but_calculate,BorderLayout.CENTER);
        Inside_panel.add(calculate);

//-------------------------------------------------------------------------
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
