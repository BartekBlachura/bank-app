package gui;

import bankapp.Runner;

import javax.swing.*;
import java.awt.*;

public class PanelLogin extends JPanel {
    private MainListener listener;
    private JButton but_Ok;
    private JButton but_registration;
    private JPasswordField textPassword;
    private JTextField textLogin;
    private JLabel messages;


    public JButton getBut_Ok() {
        return but_Ok;
    }
    public String getUserName() {
        return textLogin.getText();
    }
    public String getPassword() {
        return textPassword.getText();
    }
    public JButton getBut_registration() {return but_registration;}

    public PanelLogin(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setPanelLogin(this);
        createComponents();
    }
    public  String setMesseges(String message) {
       // this.message = message;
        //  createComponents();
        String mes = message;
        return mes;
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

//--------------------------------------Top panel-------------------------------------

        //--------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("LOGOWANIE"); // wyświetlany tekst
        label_top.setIcon(top);// wyświetlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(5);//odstęp tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);


//---------------------------------------Center Panel-----------------------------------

        //-----------------------------Textbox Login---------

        JLabel login = new JLabel("Login");
        login.setBackground(new Color(20, 100, 5));
        login.setBounds(180, 75, 150, 40);
        login.setFont(new Font("Calibre", Font.BOLD, 15)); //ustawienia czcionki

        textLogin = new JTextField();
        textLogin.setBounds(85, 45, 230, 40);

        Inside_panel.add(textLogin);
        Inside_panel.add(login);

        //----------------------------Textbox Password---------

        JLabel password = new JLabel("Hasło");
        password.setBounds(180, 150, 80, 40);
        password.setFont(new Font("Calibre", Font.BOLD, 15));

        textPassword = new JPasswordField();
        textPassword.setBounds(85, 120, 230, 40);

        Inside_panel.add(textPassword);
        Inside_panel.add(password);

        //System.out.println(mes);
        messages=new JLabel();
        messages.setBounds(100,270,400,25);
        messages.setFont(new Font("Calibre", Font.BOLD, 15));
        messages.setForeground(Color.RED); //kolor tekstu
       // messages.setText(setMesseges( message));


        Inside_panel.add(messages);


        //-------------------------------Akcept Button-------------------

        JLabel akcept = new JLabel();
        but_Ok = new JButton();
        but_Ok.setBounds(new Rectangle(150, 210, 100, 35));
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

        //---------------------------Registration Button--------------

        but_registration = new JButton();
        but_registration.setBounds(new Rectangle(25, 320, 350, 50));
        //but_registration.setPreferredSize(new Dimension(350,50));
        but_registration.setText("Załóż konto");
        but_registration.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_registration.setHorizontalTextPosition(JButton.CENTER);
        but_registration.setFont(new Font("Calibre", Font.BOLD, 20));
        but_registration.setFocusable(false); //usuwa ramkę wokół tekstu
        but_registration.setBackground(new Color(57, 49, 133));
        but_registration.setBorder(BorderFactory.createEtchedBorder());

        but_registration.setOpaque(true);

        but_registration.addActionListener(listener);

        Inside_panel.add(but_registration);
//---------------------------------------------------------------------------
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
