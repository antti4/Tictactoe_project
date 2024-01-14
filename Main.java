import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
/**
 * This is the main class of the project that asks the needed information to start the tictactoe game.
 * 
 * @author Antti Hokkinen
 */

public class Main{
    static int inaRow=0;
    static Gamescreen GS1;
/**
 * The main method that asks the user for the required parameters to creating the tictactoe gamescreen.
 * @param args Command line parameters, Not used.
 */
    public static void main(String [] args){
        int gridsize=0;
        do{
            try{
                gridsize = Integer.parseInt(JOptionPane.showInputDialog("Please input the number that will be used as base of the grid"));
            }catch(Exception e){}
        }while (gridsize < 3);
        
        boolean Isitplayable = false;
        do{
            try{
                inaRow = Integer.parseInt(JOptionPane.showInputDialog("please input how many characters you have to get in a row to win"));
            }catch(Exception e){}
            if(gridsize<10 && inaRow>=3 && inaRow<5 && inaRow<=gridsize){
                Isitplayable = true;
            }
            else if(gridsize>=10 && inaRow>=5 && inaRow<=gridsize){
                Isitplayable = true;
            }
            else{
                JOptionPane.showMessageDialog(null,"There seems to be a problem with your inputs. You either inputted a too large or a too small number for the gridsize. Please refer to the rules of tictactoe and try again","Inane error",JOptionPane.ERROR_MESSAGE);
            }
        }while(!Isitplayable);
        GS1 = new Gamescreen(gridsize , gridsize);
        GS1.setVisible(true);
    }
    /**
     * A method in the Main class that asks the code for the game's winner and displays it also givin the user a chnche to play again.
     */
    public static void again(){
        String Winner = Victorycheck.getWinner();
        int reply = JOptionPane.showConfirmDialog(null, Winner+", do you want to play again?", "Again?", JOptionPane.YES_NO_OPTION);
        if(reply == JOptionPane.YES_OPTION){
            GS1.reset();
        }
        else if(reply == JOptionPane.NO_OPTION){
            GS1.dispose();
        }
    }
}

/**
 * A class that extends the Jframe and creates an object for a grafical gamescreen of the tictactoe game.
 */
class Gamescreen extends JFrame implements ActionListener, Runnable{
    private static int Grid;
    private static int Gridsqr;
    private static JPanel [][] PanelArray;
    private static JButton [][] ButtonArray;
/**
 * The modified constructor for the class: Gamescreen
 * 
 * It creates a dynamic GUI with a gridlayout and summons a method to insert a button and a panel to every grid
 * @param rows the inputted amount of rows on the grid
 * @param columns the inputted amount of columns on the grid
 */
    public Gamescreen(int rows, int columns){
        Grid = rows*columns;
        Gridsqr = rows;
        PanelArray = new JPanel [rows][columns];
        ButtonArray = new JButton [rows][columns];
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation((int)(getScreenwidth()/2-this.getWidth()/2),(int)(getScreenheigth()/2-this.getHeight()/2));
        this.setLayout(new GridLayout(rows, columns));
        this.setResizable(false);
        formButtonGrid(rows, columns);
    }
/**
 * A method to form a grid of JButtons and JPanels on a gridlayout.
 * @param rows The integer that defines the amount of rows on the gridlayout
 * @param columns The integer that defines the amount of columns on the gridlayout
 */

    public void formButtonGrid(int rows, int columns){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
            PanelArray[i][j] = new JPanel();
            PanelArray[i][j].setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            PanelArray[i][j].setLayout(new GridLayout());
            ButtonArray[i][j] = new JButton();
            ButtonArray[i][j].addActionListener(this);
            ButtonArray[i][j].setFocusPainted(false);
            PanelArray[i][j].add(ButtonArray[i][j]);
            this.add(PanelArray[i][j]);
            }
        }
    }
/**
 * A getter method to obtain the memory location address of the Buttonarray used in the GUI.
 * @return The memory location address of the parameter named ButtonArray.
 */
    public static JButton[][] getButtonArray(){
        return ButtonArray;
    }
/**
 *  A getter method to obtain the size of the grid that has been inputted by the user.
 * @return The parameter called Grid
 */
    public static int getGrid(){
        return Grid;
    }
/**
 * A method that ask for the width of the user's screen so the game's GUI can be dynamic.
 * @return a double value of the used screen's current width.
 */
    public double getScreenwidth(){
        Dimension screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.getWidth();
    }
/**
 * A method that ask for the height of the user's screen so the game's GUI can be dynamic.
 * @returna double value of the used screen's current width.
 */
    public double getScreenheigth(){
        Dimension screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.getHeight();
    }
/**
 * A method that the interface called ActionListener requires.
 * 
 * This method is activated when a button on the GUI grid is pushed. it disables the pushed button and activates threads for computer move and victory check.
 */
    public void actionPerformed(ActionEvent e){
        for(int i = 0; i<Gridsqr; i++){
            for(int j = 0; j<Gridsqr; j++){
                if(e.getSource() == ButtonArray[i][j]){
                    ButtonArray[i][j].setText("X");
                    ButtonArray[i][j].setEnabled(false);
                }
            }
        }
        Thread enemymove = new Thread(this);
        enemymove.start();
        Thread resultcheck = new Thread(new Victorycheck());
        resultcheck.start();
    }
/**
 * A method that the interface called Runnable requires.
 * 
 * This method is used when a thread of this class is activated. It Places the computer's move on the GUI.
 */
    public void run(){
        int randomnum = (int) (Math.random()) * (int) (Math.sqrt((double)Gridsqr));
        int randomnum2 = (int) (Math.random()) * (int) (Math.sqrt((double)Gridsqr));
        int x = 0;
        do{
            if(ButtonArray[randomnum][randomnum2].getText().equalsIgnoreCase("X") || ButtonArray[randomnum][randomnum2].getText().equalsIgnoreCase("O")){
                randomnum = (int) ((Math.random()) * Gridsqr);
                randomnum2 = (int) ((Math.random()) * Gridsqr);
            }
            else{
                ButtonArray[randomnum][randomnum2].setEnabled(false);
                ButtonArray[randomnum][randomnum2].setText("O");
                x++;
            }
        }while(x == 0);
    }
/**
 * A method for resetting the gameboard so the game can be played again.
 */
    public void reset(){
        for(int i=0; i<Gridsqr; i++){
            for(int j=0; j<Gridsqr; j++){
                ButtonArray[i][j].setText("");
                ButtonArray[i][j].setEnabled(true);
            }
        }
        Victorycheck.reset();
    }
}

/**
 * A class that is used in a tictactoe game as a checker to see if the user has won. It implements Runnable so it can use threads.
 * 
 * It has parameters from different classes that are used to determine the victory conditions for the game.
 */

class Victorycheck implements Runnable{

    private static JButton[][] ButtonArray = Gamescreen.getButtonArray();
    private static int Grid = Gamescreen.getGrid();
    private static int Gridsqr = (int) Math.sqrt((double) Grid);
    private static boolean victory = false;
    private static String Winner = "";
    private static int wincondit = Main.inaRow;
/**
 * A method that the interface called Runnable requires.
 * 
 * When this method is activated it looks for a marked spot on the GUI and starts the threads that check to see if the user or computer had won the game. It creates a new thread for every marked spot so they are running in the background continuously.
 */
    public void run(){
        for(int i =0; i<Gridsqr; i++){
            for(int j =0; j<Gridsqr; j++){
                int rows = i;
                int columns = j;
                if(ButtonArray[i][j].getText().equalsIgnoreCase("x")){
                    Thread Checker = new Thread(()->{do{nearbyCheck(rows, columns, "x");}
                                                    while(!victory);});
                    Checker.start();
                }
                else if(ButtonArray[i][j].getText().equalsIgnoreCase("o")){
                    Thread Checker = new Thread(()->{do{nearbyCheck(rows, columns, "o");}
                                                    while(!victory);});
                    Checker.start();
                }
            }
        }
    }
/**
 * A method that decides based on the given user inputs what kind of check should be done on the grid so errors won't appear.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void nearbyCheck(int rows, int columns, String player){
        if(wincondit/Gridsqr == 1){
            checkMiddle(rows, columns, player);
        }
        else if(wincondit % 2 == 1){
            checkOdd(rows, columns, player);
        }
        else if(wincondit %2 == 0){
            checkEven(rows, columns, player);
        }
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
    }
/**
 * A method to turn all the buttons on the ButtonArray off.
 */
    public static void disableButtons(){
        for(int i=0; i<Gridsqr; i++){
            for(int j=0;j<Gridsqr; j++){
                ButtonArray[i][j].setEnabled(false);
            }
        }
    }
/**
 * A method to turn the Gamescreen off, set the winner of the game and to call a method to ask for a reset.
 * @param player the mark of the computer or user who won the game
 */
    public static void setWinner(String player){
        if(!victory){
        victory = true;
        Winner = player;
        disableButtons();
        Main.again();
        }
    }
/**
 * A method that checks which player won and returns a String telling which one it was.
 * @return A String of either "Computer won" or "Player won"
 */
    public static String getWinner(){
        return Winner.equalsIgnoreCase("o") ? "Computer won": "Player won";
    }
/**
 * A method to check those game areas which sides are as large as the marks needed to win the game.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkMiddle(int rows, int columns, String player){
        int win =0;
        for(int i=0; i<Gridsqr; i++){
            for(int j=0; j<Gridsqr; j++){
                if(ButtonArray[i][j].getText().equalsIgnoreCase(player)){
                    win++;
                }
                if(win == Gridsqr){
                    setWinner(player);
                }
            }
            win=0;
        }
        for(int i=0; i<Gridsqr; i++){
            for(int j=0; j<Gridsqr; j++){
                if(ButtonArray[j][i].getText().equalsIgnoreCase(player)){
                    win++;
                }
                if(win == Gridsqr){
                    setWinner(player);
                }
            }
            win=0;
        }
        win =0;
        for(int i=0; i<Gridsqr; i++){
            if(ButtonArray[i][i].getText().equalsIgnoreCase(player)){
                win++;
            }
            if(win==Gridsqr){
                setWinner(player);
            }
        }
        win=0;
        for(int i=0; i<Gridsqr; i++){
            if(ButtonArray[Gridsqr-1-i][i].getText().equalsIgnoreCase(player)){
                win++;
            }
            if(win==Gridsqr){
                setWinner(player);
            }
        }
    }
/**
 * A method used for victorychecking if the grids's side is an odd number.
 * 
 * It summons three different methods to check the game area in order to not try to check outside the grid's area
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkOdd(int rows, int columns, String player){
        if( rows < wincondit/2 && columns < wincondit/2 || rows < wincondit/2 && columns > Gridsqr-1-wincondit/2
        || rows > Gridsqr-1-wincondit/2 && columns < wincondit/2 || rows > Gridsqr-1-wincondit/2 && columns > Gridsqr-1-wincondit/2){
        }
        else if(rows<wincondit/2 || rows>Gridsqr-1-wincondit/2){
            checkoddVerticalRows(rows, columns, player);
        }
        else if(columns<wincondit/2 || columns>Gridsqr-1-wincondit/2){
            checkoddHorizontalRows(rows, columns, player);
        }
        else{
            checkoddVerticalRows(rows, columns, player);
            checkoddHorizontalRows(rows, columns, player);
            checkoddDiagonalRows(rows, columns, player);
        }
    }
/**
 * A method that checks the vertical rows for any wins when the grid's side is an odd number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkoddVerticalRows(int rows, int columns, String player){
        int win = 0;
        for(int i=1; i<=wincondit/2; i++){
            if(ButtonArray[rows][columns+i].getText().equalsIgnoreCase(player) && ButtonArray[rows][columns-i].getText().equalsIgnoreCase(player)){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method that checks the horizontal rows for any wins when the grid's side is an odd number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkoddHorizontalRows(int rows, int columns, String player){
        int win=0;
        for(int i=1; i<=wincondit/2; i++){
            if(ButtonArray[rows+i][columns].getText().equalsIgnoreCase(player) && ButtonArray[rows-i][columns].getText().equalsIgnoreCase(player)){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method that checks the diagonal rows for any wins when the grid's side is an odd number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkoddDiagonalRows(int rows, int columns, String player){
        int win=0;
        for(int i=1; i<=wincondit/2; i++){
            if(ButtonArray[rows+i][columns+i].getText().equalsIgnoreCase(player) && ButtonArray[rows-i][columns-i].getText().equalsIgnoreCase(player)
            ||ButtonArray[rows+i][columns-i].getText().equalsIgnoreCase(player) && ButtonArray[rows-i][columns+i].getText().equalsIgnoreCase(player)){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method used for victorychecking if the grids's side is an even number.
 * 
 * It summons three different methods to check the game area in order to not try to check outside the grid's area
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkEven(int rows, int columns, String player){
        if( rows < wincondit/2 && columns < wincondit/2 || rows < wincondit/2 && columns > Gridsqr-1-wincondit/2
        || rows > Gridsqr-1-wincondit/2 && columns < wincondit/2 || rows > Gridsqr-1-wincondit/2 && columns > Gridsqr-1-wincondit/2){
        }
        else if(rows<wincondit/2 || rows>Gridsqr-1-wincondit/2){
            checkEvenVerticalRows(rows, columns, player);
        }
        else if(columns<wincondit/2 || columns>Gridsqr-1-wincondit/2){
            checkEvenHorizontalRows(rows, columns, player);
        }
        else{
            checkEvenVerticalRows(rows, columns, player);
            checkEvenHorizontalRows(rows, columns, player);
            checkEvenDiagonalRows(rows, columns, player);
        }
    }
/**
 * A method that checks the vertical rows for any wins when the grid's side is an even number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkEvenVerticalRows(int rows, int columns, String player){
        int win = 0;
        for(int i=1; i<=wincondit/2; i++){
            if(ButtonArray[rows][columns+i].getText().equalsIgnoreCase(player) && ButtonArray[rows][columns-i].getText().equalsIgnoreCase(player)&& i!= wincondit/2){
                win++;
            }
            else if((ButtonArray[rows][columns+i].getText().equalsIgnoreCase(player) || ButtonArray[rows][columns-i].getText().equalsIgnoreCase(player))&& i==wincondit/2){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method that checks the horizontal rows for any wins when the grid's side is an even number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkEvenHorizontalRows(int rows, int columns, String player){
        int win = 0;
        for(int i=1; i<=wincondit/2; i++){
            if(ButtonArray[rows+i][columns].getText().equalsIgnoreCase(player) && ButtonArray[rows-1][columns].getText().equalsIgnoreCase(player)&& i!= wincondit/2){
                win++;
            }
            else if((ButtonArray[rows+i][columns].getText().equalsIgnoreCase(player) || ButtonArray[rows-i][columns].getText().equalsIgnoreCase(player))&& i==wincondit/2){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method that checks the diagonal rows for any wins when the grid's side is an even number.
 * @param rows The y position of the marker on the grid
 * @param columns The x position of the marker on the grid
 * @param player the identity of the marker
 */
    public static void checkEvenDiagonalRows(int rows, int columns, String player){
        int win=0;
        for(int i=1; i<=wincondit/2; i++){
            if((ButtonArray[rows+i][columns+i].getText().equalsIgnoreCase(player) && ButtonArray[rows-i][columns-i].getText().equalsIgnoreCase(player))
            ||(ButtonArray[rows+i][columns-i].getText().equalsIgnoreCase(player) && ButtonArray[rows-i][columns+i].getText().equalsIgnoreCase(player)) 
            && i!=wincondit/2){
                win++;
            }
            else if(win == wincondit/2-1 && ((ButtonArray[rows+i][columns+i].getText().equalsIgnoreCase(player) || ButtonArray[rows-i][columns-i].getText().equalsIgnoreCase(player))
            ||(ButtonArray[rows+i][columns-i].getText().equalsIgnoreCase(player) || ButtonArray[rows-i][columns+i].getText().equalsIgnoreCase(player)))){
                win++;
            }
            if(win == wincondit/2){
                setWinner(player);
            }
        }
    }
/**
 * A method that resets the parameters needed to play the game again back to their starting values.
 */
    public static void reset(){
        victory = false;
        Winner = "";
    }
}

