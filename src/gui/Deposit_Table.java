package gui;

import bankapp.Runner;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Deposit_Table extends JPanel{
    private Dimension tableDimension = new Dimension(770,100);
    private MainListener listener;

    private JTextField textDeposit_id;
    private JButton but_PayOut;
    private JButton but_Back;


    public String getIDDeposit() {
        return textDeposit_id.getText();
    }

    public JButton getBut_PayOut() {

        return but_PayOut;
    }

    public JButton getBut_Back() {

        return but_Back;
    }

    public Deposit_Table(MainListener listener){
        super();
        this.listener = listener;
        this.listener.setDepositTable(this);
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
        Inside_panel1.setPreferredSize(new Dimension(900,20));
        Inside_panel2.setPreferredSize(tableDimension);
        Inside_panel3.setPreferredSize(new Dimension(980,200));
   //-----------------------------Top panel-------------------------------------

         //--------Violet label with image and title on top-----

        JLabel label_top = new JLabel();
        ImageIcon top = new ImageIcon("src/gui/tittle.png");
        label_top.setText("LOKATY"); // wyświetlany tekst
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


        String[] columnNames = {"ID lokaty", "Aktualne środki PLN", "Oprocentowanie",
                "Data założenia", "Data zakończenia","Data ostatniej kapitalizacji"};


        Object[][] data = listener.displayDepositsTable();

        int[] columnsWidth = {70, 130, 100, 120, 120, 250};

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
        currentDateLabel.setBounds(180, 10, 300, 40);
        currentDateLabel.setFont(new Font("Calibre", Font.PLAIN, 12));
        Inside_panel1.add(currentDateLabel);


        JLabel dep_ID= new JLabel("Podaj ID Lokaty którą chcesz wypłacić");
        dep_ID.setBounds(375, 40, 300, 40);
        dep_ID.setFont(new Font("Calibre", Font.BOLD, 12));
        dep_ID.setVisible(true);

        textDeposit_id = new JTextField();
        textDeposit_id.setBounds(370, 90, 230, 40);






        //----------------Akcept PayOut-------------------

        JLabel PayOut = new JLabel();
        but_PayOut = new JButton();
        but_PayOut.setBounds(370, 150, 100, 35);
        but_PayOut.setText("Wypłać");
        but_PayOut.setForeground(new Color(255, 255, 255)); //kolor tekstu
        but_PayOut.setHorizontalTextPosition(JButton.CENTER);
        but_PayOut.setFont(new Font("Calibre", Font.BOLD, 15));
        but_PayOut.setFocusable(false); //usuwa ramkę wokół tekstu
        but_PayOut.setBackground(new Color(229, 9, 127));
        but_PayOut.setBorder(BorderFactory.createEtchedBorder());
        but_PayOut.setVerticalAlignment(JButton.CENTER); //pozycja obrazka z tekstem
        but_PayOut.setHorizontalAlignment(JButton.CENTER);
        but_PayOut.addActionListener(listener);
        but_PayOut.setOpaque(true);
        Inside_panel3.add(but_PayOut,BorderLayout.CENTER);
        Inside_panel3.add(PayOut);

        //----------------Back Button-------------------

        JLabel back = new JLabel();
        but_Back = new JButton();
        but_Back.setBounds(500, 150, 100, 35);
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
        Inside_panel3.add(textDeposit_id);
        Inside_panel3.add(dep_ID);

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
