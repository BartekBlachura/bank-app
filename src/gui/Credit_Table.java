package gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Credit_Table extends JPanel {
    private Dimension tableDimension = new Dimension(580,100);
    private MainListener listener;
    private JTable tab_Credit;
    private JTextField textCredit_id;
    private JButton but_PayOff;
    private JButton but_Back;

    public String getIDCredit() {
        return textCredit_id.getText();
    }
    public JButton getBut_PayOff() {

        return but_PayOff;
    }

    public JButton getBut_Back() {

        return but_Back;
    }

    public Credit_Table(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setCreditTable(this);
        createComponents();
    }
    private void createComponents(){this.setLayout(new BorderLayout());

        JPanel Top_panel = new JPanel();
        JPanel Top_panel_title = new JPanel();
        JPanel Top_panel_pink = new JPanel();
        JPanel Center_panel = new JPanel();
        JPanel Down_panel = new JPanel();
        JPanel Right_panel = new JPanel();
        JPanel Left_panel = new JPanel();
        Top_panel.setLayout(new BorderLayout());
        JPanel Inside_panel1 = new JPanel();
        JPanel Inside_panel2 = new JPanel();
        JPanel Inside_panel3 = new JPanel();

        Inside_panel2.setLayout((null));
        Inside_panel3.setLayout((null));


        Top_panel.setBackground(new Color(57, 49, 133));
        Top_panel_pink.setBackground(new Color(229, 9, 127));
        Top_panel_title.setBackground(new Color(57, 49, 133));
        Center_panel.setBackground(Color.white);
        Down_panel.setBackground(Color.white);
        Right_panel.setBackground(Color.white);
        Left_panel.setBackground(Color.white);
        Inside_panel1.setBackground(Color.white);
        Inside_panel2.setBackground(Color.white);
        Inside_panel3.setBackground(Color.white);

        Top_panel.setPreferredSize(new Dimension(500,70));
        Top_panel_pink.setPreferredSize(new Dimension(500,8));
        Center_panel.setPreferredSize(tableDimension);
        Down_panel.setPreferredSize(new Dimension(500,20));
        Right_panel.setPreferredSize(new Dimension(10,410));
        Left_panel.setPreferredSize(new Dimension(10,410));
        Inside_panel1.setPreferredSize(new Dimension(1000,20));
        Inside_panel2.setPreferredSize(tableDimension);
        Inside_panel3.setPreferredSize(new Dimension(980,200));

        //-----------------------------Top panel-------------------------------------

        //--------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("KREDYTY"); // wyświetlany tekst
        label_top.setIcon(top);// wyświetlany obraz
        label_top.setHorizontalTextPosition(JLabel.CENTER); //USTAWIENIE TEKSTU WZGLEDEM OBRAZKA LEFT CENTER RIGHT
        label_top.setVerticalTextPosition(JLabel.CENTER);
        label_top.setForeground(new Color(255, 255, 255)); //kolor tekstu
        label_top.setFont(new Font("Calibre", Font.BOLD, 20)); //ustawienia czcionki
        label_top.setIconTextGap(80);//odstęp tekstu od obrazka
        label_top.setBackground(new Color(57, 49, 133));
        label_top.setBounds(150, 200, 200, 40);
        label_top.setOpaque(true);
        Top_panel_title.add(label_top);


        //----------------------------tabel-----


        String[] columnNames = {"ID kredytu", "Cała kwota do spłaty", "Kwota raty",
                "Termin raty", "Ile zostało do spłaty"};


        Object[][] data = listener.displayLoansTable();

        int[] columnsWidth = {80, 150, 100, 100, 150};

        JTable table = new JTable(data, columnNames);

        int i = 0;
        for (int width : columnsWidth) {
            TableColumn column = table.getColumnModel().getColumn(i++);
            column.setMinWidth(width);
            column.setMaxWidth(width);
            column.setPreferredWidth(width);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        Inside_panel2.setLayout(new BorderLayout());
        Inside_panel2.add(scrollPane, BorderLayout.CENTER);

        //------------------text field------------------

        JLabel currentDateLabel = new JLabel("Aktualna data:"+listener.displayLocalDate());
        currentDateLabel.setBounds(580, 10, 300, 40);
        currentDateLabel.setFont(new Font("Calibre", Font.PLAIN, 12));
        Inside_panel1.add(currentDateLabel);


        JLabel cre_ID= new JLabel("Podaj ID Kredytu którego ratę chcesz spłacić");
        cre_ID.setBounds(365, 40, 300, 40);
       // cre_ID.setPreferredSize(new Dimension(300,40));
        cre_ID.setFont(new Font("Calibre", Font.BOLD, 12));
        cre_ID.setVisible(true);

        textCredit_id = new JTextField();
        textCredit_id.setBounds(375, 90, 230, 40);


        //----------------Akcept PayOff-------------------

        JLabel Pay = new JLabel();
        but_PayOff = new JButton();
        but_PayOff.setBounds(375, 150, 100, 35);
        but_PayOff.setText("Spłać");
        but_PayOff.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_PayOff.setHorizontalTextPosition(JButton.CENTER);
        but_PayOff.setFont(new Font("Calibre", Font.BOLD, 15));
        but_PayOff.setFocusable(false); //usuwa ramkę wokół tekstu
        but_PayOff.setBackground(new Color(229, 9, 127));
        but_PayOff.setBorder(BorderFactory.createEtchedBorder());
        but_PayOff.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_PayOff.setHorizontalAlignment(JButton.CENTER);
        but_PayOff.addActionListener(listener);
        but_PayOff.setOpaque(true);
        Inside_panel3.add(but_PayOff,BorderLayout.CENTER);
        Inside_panel3.add(Pay);

        //----------------Back Button-------------------

        JLabel back = new JLabel();
        but_Back = new JButton();
        but_Back.setBounds(503, 150, 100, 35);
        but_Back.setText("Cofnij");
        but_Back.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_Back.setHorizontalTextPosition(JButton.CENTER);
        but_Back.setFont(new Font("Calibre", Font.BOLD, 15));
        but_Back.setFocusable(false); //usuwa ramkę wokół tekstu
        but_Back.setBackground(new Color(229, 9, 127));
        but_Back.setBorder(BorderFactory.createEtchedBorder());
        but_Back.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_Back.setHorizontalAlignment(JButton.CENTER);
        but_Back.addActionListener(listener);
        but_Back.setOpaque(true);
        Inside_panel3.add(but_Back,BorderLayout.CENTER);
        Inside_panel3.add(back);


        Top_panel.add(Top_panel_title,BorderLayout.CENTER);
        Top_panel.add(Top_panel_pink,BorderLayout.SOUTH);
        Inside_panel3.add(textCredit_id);
        Inside_panel3.add(cre_ID);

        //this.setPreferredSize(new Dimension(380, 60));
        Center_panel.add(Inside_panel1,BorderLayout.CENTER);
        Center_panel.add(Inside_panel2,BorderLayout.CENTER);
        Center_panel.add(Inside_panel3,BorderLayout.CENTER);

        Top_panel.add(Top_panel_title,BorderLayout.CENTER);
        Top_panel.add(Top_panel_pink,BorderLayout.SOUTH);

        this.add(Top_panel,BorderLayout.NORTH);
        this.add(Center_panel,BorderLayout.CENTER);
        this.add(Down_panel,BorderLayout.SOUTH);
        this.add(Right_panel,BorderLayout.EAST);
        this.add(Left_panel,BorderLayout.WEST);

    }


}
