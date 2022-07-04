package gui;

import javax.swing.*;
import java.awt.*;

public class PanelRegistration extends JPanel {
    private MainListener listener;
    private JButton but_Ok;
    private JButton but_Back;
    private JTextField textLogin;
    private JTextField textPassword;
    private JTextField textPassword2;

    public JButton getBut_Ok() {
        return but_Ok;
    }

    public JButton getBut_Back() {
        return but_Back;
    }
    public String newUserName() {
        return textLogin.getText();
    }
    public String newPassword() {
        return textPassword.getText();
    }
    public String repeatPassword() {
        return textPassword2.getText();
    }


    public PanelRegistration(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setPanelRegistration(this);

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

//-----------------------------------------------Top panel-------------------------------------

        //----------------------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("REJESTRACJA"); // wyświetlany tekst
        label_top.setIcon(top);// wyświetlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(5);//odstęp tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);


//-----------------------------------------------Center Panel-----------------------------------

        JLabel information = new JLabel();
        JLabel information2 = new JLabel();
        information.setText("Login oraz hasło powinno składać się");
        information2.setText("z minimum 4 znaków");
        information.setBounds(95, 6, 230, 40);
        information2.setBounds(135, 24, 230, 40);
        information.setForeground(Color.darkGray);
        information2.setForeground(Color.darkGray);
        information.setFont(new Font("Calibre", Font.BOLD, 12));
        information2.setFont(new Font("Calibre", Font.BOLD, 12));
        Inside_panel.add(information);
        Inside_panel.add(information2);

        //------------------------------------Textbox Login---------

        JLabel login = new JLabel("Login");
        login.setBackground(new Color(20, 100, 5));
        login.setBounds(180, 104, 150, 40);
        login.setFont(new Font("Calibre", Font.BOLD, 15)); //ustawienia czcionki

        textLogin = new JTextField();
        textLogin.setBounds(85, 75, 230, 40);

        Inside_panel.add(textLogin);
        Inside_panel.add(login);

        //---------------------------------Textbox Password---------

        JLabel password = new JLabel("Hasło");
        password.setBounds(180, 174, 80, 40);
        password.setFont(new Font("Calibre", Font.BOLD, 15));

        textPassword = new JPasswordField();
        textPassword.setBounds(85, 145, 230, 40);

        Inside_panel.add(textPassword);
        Inside_panel.add(password);

        //---------------------------------Textbox Password2---------

        JLabel password2 = new JLabel("Powtórz hasło");
        password2.setBounds(150, 244, 100, 40);
        password2.setFont(new Font("Calibre", Font.BOLD, 15));

        textPassword2 = new JPasswordField();
        textPassword2.setBounds(85, 215, 230, 40);

        Inside_panel.add(textPassword2);
        Inside_panel.add(password2);


        //-----------------------------------Akcept Button-------------------

        JLabel akcept = new JLabel();
        but_Ok = new JButton();
        but_Ok.setBounds(70, 340, 100, 35);
        but_Ok.setText("Akceptuj");
        but_Ok.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Ok.setHorizontalTextPosition(JButton.CENTER);
        but_Ok.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Ok.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Ok.setBackground(new Color(229, 9, 127));
        but_Ok.setBorder(BorderFactory.createEtchedBorder());
        but_Ok.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Ok.setHorizontalAlignment(JButton.CENTER);
        but_Ok.setOpaque(true);
        but_Ok.addActionListener(listener);

        Inside_panel.add(but_Ok,BorderLayout.CENTER);
        Inside_panel.add(akcept);

        //-------------------------------------Back Button-------------------

        JLabel back = new JLabel();
        but_Back = new JButton();
        but_Back.setBounds(230, 340, 100, 35);
        but_Back.setText("Cofnij");
        but_Back.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Back.setHorizontalTextPosition(JButton.CENTER);
        but_Back.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Back.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Back.setBackground(new Color(229, 9, 127));
        but_Back.setBorder(BorderFactory.createEtchedBorder());
        but_Back.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Back.setHorizontalAlignment(JButton.CENTER);
        but_Back.setOpaque(true);
        but_Back.addActionListener(listener);


        Inside_panel.add(but_Back,BorderLayout.CENTER);
        Inside_panel.add(back);

//-----------------------------------------------------------------

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


