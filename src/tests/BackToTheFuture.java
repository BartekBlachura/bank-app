package tests;

import bankapp.DatabaseException;
import bankapp.Lokata;
import bankapp.SQL_driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

public class BackToTheFuture {
    private static final SQL_driver sqlDriver = new SQL_driver();

    private static Date getDate() throws SQLException {
        return sqlDriver.returnData();
    }

    public static void addDays(int days) throws SQLException, DatabaseException {
        for (int i = 0; i < days; i++) {
            addDay();
        }
    }

    public static void addDay() throws SQLException, DatabaseException {
        Date date = getDate();
        date.setTime(date.getTime() + 86400000);
        sqlDriver.insertNewData(date);
        Lokata.sprawdzenieLokat();
    }

//-------------------------------------- GUI -------------------------------------

    BackToTheFuture() {
    }

//--------------------------------------------------------------------------------
    public static void main(String[] args) {
        new BackToTheFuture().runDeLorean();
    }

    public void runDeLorean() {
        EventQueue.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

//--------------------------------------------------------------------------------
    public class MainFrame extends JFrame {
        public MainFrame() throws SQLException {
            super();
            setPreferredSize(new Dimension(400, 550));

            MainListener listener = new MainListener(this);
            JPanel panel = new PanelTimeMachine(listener);
            add(panel);

            pack();
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

//--------------------------------------------------------------------------------
    public class MainListener  implements ActionListener {
        private final JFrame frame;
        private PanelTimeMachine panelTimeMachine;

        public void setPanelTimeMachine(PanelTimeMachine panelTimeMachine){
            this.panelTimeMachine = panelTimeMachine;
        }

        MainListener(JFrame frame) {

            this.frame = frame;
            ImageIcon icon = new ImageIcon("src/tests/logoB.png");
            frame.setIconImage(icon.getImage()); //ustawia nową ikone na pasku
            frame.setTitle("DeLorean time machine"); //górny pasek toogle bar
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                try {
                    addDays(Integer.parseInt(panelTimeMachine.monthsInput.getText()));

                    JPanel newPanel = new PanelTimeMachine(new MainListener(frame));
                    frame.getContentPane().removeAll();
                    frame.add(newPanel);
                    frame.validate();
                }
                catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.toString(), "Błąd logowania",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
        }
    }

//--------------------------------------------------------------------------------
    public class PanelTimeMachine extends JPanel {
        private final MainListener listener;
        private JTextField monthsInput;

        public PanelTimeMachine(MainListener listener) throws SQLException {
                super();
                this.listener = listener;
                this.listener.setPanelTimeMachine(this);
                createComponents();
            }

        private void createComponents() throws SQLException {
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

//--------------------------------------Top panel---------------------------------
            JLabel label_top = new JLabel();
            ImageIcon top = new ImageIcon("src/tests/tittle.png");
            label_top.setText("WEHIKUŁ CZASU");
            label_top.setIcon(top);
            label_top.setHorizontalTextPosition(JLabel.CENTER);
            label_top.setVerticalTextPosition(JLabel.CENTER);
            label_top.setForeground(new Color(255, 255, 255));
            label_top.setFont(new Font("Calibre", Font.BOLD, 20));
            label_top.setIconTextGap(5);
            label_top.setBackground(new Color(57, 49, 133));
            label_top.setOpaque(true);
            Top_panel_title.add(label_top);

//---------------------------------------Center Panel-----------------------------
            JLabel currentDateLabel = new JLabel("Aktualna data:");
            currentDateLabel.setBounds(156, 40, 300, 30);
            currentDateLabel.setFont(new Font("Calibre", Font.PLAIN, 15));
            Inside_panel.add(currentDateLabel);

            JLabel currentDate = new JLabel(getDate().toString());
            currentDate.setBounds(150, 70, 300, 40);
            currentDate.setFont(new Font("Calibre", Font.BOLD, 20));
            Inside_panel.add(currentDate);

            JLabel monthsLabel = new JLabel("Podaj liczbę dni:");
            monthsLabel.setBounds(142, 160, 230, 40);
            monthsLabel.setFont(new Font("Calibre", Font.BOLD, 15));
            Inside_panel.add(monthsLabel);

            monthsInput = new JTextField("1");
            monthsInput.setBounds(85, 200, 230, 40);
            Inside_panel.add(monthsInput);

            JButton buttonTimeTravel = new JButton();
            buttonTimeTravel.setBounds(85, 250, 230, 35);
            buttonTimeTravel.setText("Rozpocznij podróż!");
            buttonTimeTravel.setForeground(new Color(255, 255, 255));
            buttonTimeTravel.setHorizontalTextPosition(JButton.CENTER);
            buttonTimeTravel.setFont(new Font("Calibre", Font.BOLD, 15));
            buttonTimeTravel.setFocusable(false);
            buttonTimeTravel.setBackground(new Color(229, 9, 127));
            buttonTimeTravel.setBorder(BorderFactory.createEtchedBorder());
            buttonTimeTravel.setVerticalAlignment(JButton.CENTER);
            buttonTimeTravel.setHorizontalAlignment(JButton.CENTER);
            buttonTimeTravel.setOpaque(true);
            buttonTimeTravel.addActionListener(listener);
            Inside_panel.add(buttonTimeTravel);

//--------------------------------------------------------------------------------
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
}
