package gui;

import bankapp.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MainListener  implements ActionListener, MouseMotionListener {
    private final JFrame frame;
    private PanelLogin panelLogin;
    private PanelRegistration panelRegistration;
    private PanelClient panelClient;
    private PanelTransfers panelTransfers;
    private PanelCredit panelCredit;
    private Credit_Table credit_table;
    private PanelDeposit panelDeposit;
    private Deposit_Table deposit_table;
    private PanelHistory panelHistory;
    private PanelAdministrator panelAdministrator;
    private Runner runner;

    private Dimension historyDimension = new Dimension(950,550);
    private Dimension creditsTableDimension = new Dimension(830,550);
    private Dimension depositsTableDimension = new Dimension(830,550);
    private Dimension standardDimension = MainFrame.standardDimension;

    public void setPanelLogin(PanelLogin panelLogin){
        this.panelLogin = panelLogin;
        MainFrame.setRefreshable(0);
    }
    public void setPanelRegistration(PanelRegistration panelRegistration) {
        this.panelRegistration = panelRegistration;
        MainFrame.setRefreshable(0);
    }
    public void setPanelClient(PanelClient panelClient){
        this.panelClient= panelClient;
        MainFrame.setRefreshable(1);
        runner.wasRefreshed();
    }
    public void setPanelTransfers(PanelTransfers panelTransfers){
        this.panelTransfers= panelTransfers;
        MainFrame.setRefreshable(6);
    }
    public void setPanelCredit(PanelCredit panelCredit){
        this.panelCredit= panelCredit;
        MainFrame.setRefreshable(8);
    }
    public void setCreditTable(Credit_Table credit_table){
        this.credit_table= credit_table;
        MainFrame.setRefreshable(5);
    }
    public void setPanelDeposit(PanelDeposit panelDeposit){
        this.panelDeposit= panelDeposit;
        MainFrame.setRefreshable(7);
    }
    public void setDepositTable(Deposit_Table deposit_table){
        this.deposit_table= deposit_table;
        MainFrame.setRefreshable(4);
    }
    public void setPanelHistory(PanelHistory panelHistory){
        this.panelHistory= panelHistory;
        MainFrame.setRefreshable(3);
    }

    public void setPanelAdministrator(PanelAdministrator panelAdministrator){
        this.panelAdministrator= panelAdministrator;
        MainFrame.setRefreshable(2);
        runner.wasRefreshed();
    }

//-------metody służące do wyświetlania w GUI dancyh pobieranych klasy Runner------------

    public String displayBalance() {
        return String.valueOf(runner.getCustomerBalance());
    }
    public String displayAccountNo() {
        return String.valueOf(runner.getCustomerAccountNo());
    }
    public String[][] displayTransactionsTable() {
        return runner.getTransactionsTable();
    }
    public String[][] displayLoansTable() {
        return runner.getLoansTable();
    }
    public String[][] displayDepositsTable() {
        return runner.getDepositsTable();
    }
    public String displayPercentageLoan() {
        return runner.getPercentageLoan();
    }
    public String displayPercentageDeposit() {
        return runner.getPercentageDeposit();
    }
    public String displayProfit() {
        return runner.getProfit();
    }
    public String displayAmountOfInstallment() {
        return runner.getAmountOfInstallment();
    }
    public String displayLocalDate(){
        return runner.getLocalDate();
    }


    MainListener(JFrame frame, Runner runner) {
        this.frame = frame;
        this.runner = runner;
        ImageIcon icon = new ImageIcon("src/gui/logoB.png");
        frame.setIconImage(icon.getImage()); //ustawia nową ikone na pasku
        frame.setTitle("aplikacja bankowa"); //górny pasek toogle bar
    }

    @Override
    public void actionPerformed(ActionEvent e) {

//-----------------------------------------Panel Login-----------------------------------------------

        if (panelLogin != null) {

            //---------------------- Panel Login switch to Panel Registration --------------------

            if (e.getSource() == panelLogin.getBut_registration()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelRegistration(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //----------------------------- Login to Panel Client / Panel Administrator -----------------------

            if (e.getSource() == panelLogin.getBut_Ok()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.logIn(panelLogin.getUserName(), panelLogin.getPassword())) {
                            JPanel newPanel;
                            if (runner.isLoggedInAsAdmin()) {
                                newPanel = new PanelAdministrator(new MainListener(frame, runner));
                            }
                            else {
                                newPanel = new PanelClient(new MainListener(frame, runner));
                            }
                            frame.getContentPane().removeAll();
                            frame.add(newPanel);
                            frame.validate();

                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd logowania",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//------------------------------------------- Panel Registration  --------------------------------------------

        if (panelRegistration != null) {

            //------------------------ Panel Registration Switch to panel Login ----------------------------

            if (e.getSource() == panelRegistration.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelLogin(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //-------------------------------- Creating a new account -----------------------------

            if (e.getSource() == panelRegistration.getBut_Ok()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.newAccount(panelRegistration.newUserName(), panelRegistration.newPassword(),
                                panelRegistration.repeatPassword())) {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                            JPanel newPanel = new PanelLogin(new MainListener(frame, runner));
                            frame.getContentPane().removeAll();
                            frame.add(newPanel);
                            frame.validate();
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd logowania",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//-------------------------------------------- Panel Client --------------------------------------------------

        if (panelClient != null) {

            //-------------------------- Panel Client switching to Panel Transfer-----------------

            if (e.getSource() == panelClient.getBut_Transfer()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelTransfers(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //-----------------Panel Client switching to Panel Credit-----------------

            if (e.getSource() == panelClient.getBut_Credits()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelCredit(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //-----------------Panel Client switching to Panel Table Credit-----------------

            if (e.getSource() == panelClient.getBut_TableCredit()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new Credit_Table(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(creditsTableDimension);
                    }
                });
            }

            //-----------------Panel Client switching to Panel Deposit-----------------

            if (e.getSource() == panelClient.getBut_Deposit()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelDeposit(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //-----------------Panel Client switching to Panel Table Deposit-----------------

            if (e.getSource() == panelClient.getBut_TableDeposit()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new Deposit_Table(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(depositsTableDimension);
                    }
                });
            }

            //-----------------Panel Client switching to Panel History-----------------

            if (e.getSource() == panelClient.getBut_History()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelHistory(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(historyDimension);
                    }
                });
            }


            //-----------------------Panel Client Logout------------------------

            if (e.getSource() == panelClient.getBut_log_out()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        runner.logOut();
                        JPanel newPanel = new PanelLogin(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }
        }

//----------------------------------- Panel Transfer ------------------------------------------

        if (panelTransfers != null) {

            // ------- Panel Transfer switch to Panel Client / Panel Administrator ----------------

            if (e.getSource() == panelTransfers.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel;
                        if (runner.isLoggedInAsAdmin()) {
                            newPanel = new PanelAdministrator(new MainListener(frame, runner));
                        }
                        else {
                            newPanel = new PanelClient(new MainListener(frame, runner));
                        }
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            // ------------------------- Panel Transfer - new transfer -----------------------------

            if (e.getSource() == panelTransfers.getBut_Ok()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.transfer(panelTransfers.getAccount(), panelTransfers.getAmount(),
                                panelTransfers.getTitle())) {
                            String message = "pieniądze zostały przelane";
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//------------------------------------------ Panel Deposit ----------------------------------

        if (panelDeposit != null) {

            //----------------------------Panel Deposit - New Deposit ----------------------

            if (e.getSource() == panelDeposit.getBut_Ok()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.newDeposit(panelDeposit.getDep_Amount(), panelDeposit.getDep_Time())) {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }

            //--------------------- Panel Deposit switch to Panel Client ----------------------

            if (e.getSource() == panelDeposit.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelClient(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //--------------------- Panel Deposit calculate profit ----------------------

            if (e.getSource() == panelDeposit.getBut_calculate()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if(runner.calculateProfit(panelDeposit.getDep_Amount(), panelDeposit.getDep_Time())){
                            JOptionPane.showMessageDialog(null,
                                    displayProfit() + " PLN", "potencjalny zysk z lokaty",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//------------------------------------- Panel Deposit Table ---------------------------------------

        if (deposit_table != null) {

            //--------------------- Panel Deposit Table  switch to Panel Client ----------------------

            if (e.getSource() == deposit_table.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelClient(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(standardDimension);
                    }
                });
            }

            // ---------------------------- Panel Deposit Table - Pay Out ----------------------

            if (e.getSource() == deposit_table.getBut_PayOut()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.withdrawDeposit(deposit_table.getIDDeposit())) {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//-------------------------------------------- Panel Credit --------------------------------------------

        if (panelCredit != null) {
            if (e.getSource() == panelCredit.getBut_Ok()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.newLoan(panelCredit.getTextAmo_credit(), panelCredit.getTextInstallment(),
                                panelCredit.getTextIncome())) {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                            panelLogin.setMesseges(message);

                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);

                        }
                    }
                });
            }

            //--------------------- Panel Credit switch to Panel Client ----------------------

            if (e.getSource() == panelCredit.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelClient(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //--------------------- Panel Credit calculate installment ----------------------

            if (e.getSource() == panelCredit.getBut_calculate()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if(runner.calculateAmountOfInstallment(panelCredit.getTextAmo_credit(),
                                panelCredit.getTextInstallment())){
                            JOptionPane.showMessageDialog(null,
                                    displayAmountOfInstallment() + " PLN", "wyokość raty",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//---------------------------------------Panel Credit Table ---------------------------------

        if (credit_table != null) {

            //------------------- Panel Credit Table switch to panel Client -----------------

            if (e.getSource() == credit_table.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelClient(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(standardDimension);
                    }
                });
            }

            //--------------------- Panel Credit Table - Pay Off -----------------

            if (e.getSource() == credit_table.getBut_PayOff()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        if (runner.payInstallment(credit_table.getIDCredit())) {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }
        }

//--------------------------------------Panel History--------------------------------

        if (panelHistory != null) {

            //-------------Panel History switch to Panel Client / Panel Administrator ----------------

            if (e.getSource() == panelHistory.getBut_Back()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel;
                        if (runner.isLoggedInAsAdmin()) {
                            newPanel = new PanelAdministrator(new MainListener(frame, runner));
                        }
                        else {
                            newPanel = new PanelClient(new MainListener(frame, runner));
                        }
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(standardDimension);
                    }
                });
            }
        }

//-------------------------------Panel Administrator------------------------------

        if (panelAdministrator != null) {

            //----------------Panel Administrator switch to Panel Transfers-----------------

            if (e.getSource() == panelAdministrator.getBut_Transfers()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelTransfers(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }

            //----------------Panel Administrator switch to Panel History -----------------

            if (e.getSource() == panelAdministrator.getBut_Tran_history()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JPanel newPanel = new PanelHistory(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                        frame.setSize(historyDimension);
                    }
                });
            }

            //----------------Panel Administrator change Deposit percent -----------------

            if (e.getSource() == panelAdministrator.getBut_Change_Dep()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.setPercentageDeposit(panelAdministrator.getDeposit_percent())) {

                            JPanel newPanel = new PanelAdministrator(new MainListener(frame, runner));
                            frame.getContentPane().removeAll();
                            frame.add(newPanel);
                            frame.validate();
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }

            //----------------Panel Administrator change Credit percent -----------------

            if (e.getSource() == panelAdministrator.getBut_Change_Cre()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (runner.setPercentageLoan(panelAdministrator.getCredit_percent())) {
                            JPanel newPanel = new PanelAdministrator(new MainListener(frame, runner));
                            frame.getContentPane().removeAll();
                            frame.add(newPanel);
                            frame.validate();
                        }
                        else {
                            String message = runner.getMessages();
                            JOptionPane.showMessageDialog(null, message, "błąd",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
            }

            //----------------Panel Administrator - Log Out -----------------

            if (e.getSource() == panelAdministrator.getBut_log_out()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        runner.logOut();
                        JPanel newPanel = new PanelLogin(new MainListener(frame, runner));
                        frame.getContentPane().removeAll();
                        frame.add(newPanel);
                        frame.validate();
                    }
                });
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        JPanel newPanel = null;
        if (runner.isNeedsRefresh()) {
            if (MainFrame.getRefreshable() == 1) {
                newPanel = new PanelClient(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 2) {
                newPanel = new PanelAdministrator(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 3) {
                newPanel = new PanelHistory(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 4) {
                newPanel = new Deposit_Table(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 5) {
                newPanel = new Credit_Table(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 6) {
                newPanel = new PanelTransfers(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 7) {
                newPanel = new PanelDeposit(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            if (MainFrame.getRefreshable() == 8) {
                newPanel = new PanelCredit(new MainListener(frame, runner));
                runner.wasRefreshed();
            }
            frame.getContentPane().removeAll();
            frame.add(newPanel);
            frame.validate();
        }
    }
}