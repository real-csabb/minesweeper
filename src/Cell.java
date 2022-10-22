

/**
 * The Cell class defines a cell that can be set as hidden or revealed, and will either display
 * an hyphen, an M for mine, or a digit when revealed.
 * @author Chris Sabb
 * @version 3.0 Electric Boogaloo Edition
 * @since 2022-08-28
 */
public class Cell {

    private boolean containsMine;
    private boolean contentVisible;
    private boolean revealed;
    private boolean paddingAfter;
    private int digit;
    private String contentToDisplay;
    
    /**
     * The Cell class constructor sets whether or not a cell contains a mine and whether or not
     * there is a padding space after the display of the cell.
     * @param containsMine Boolean value whether or not the cell has a mine.
     * @param paddingAfter Boolean value whether or not the cell has padding.
     */
    public Cell(boolean containsMine, boolean paddingAfter) {
    	digit = -1;
    	contentToDisplay = "-";
        this.containsMine = containsMine;
        this.paddingAfter = paddingAfter;
    }

    /** 
     * This .getContainsMine function is a getter method that returns a boolean value whether
     * or not the cell has a mine or not.
     * @return boolean Boolean value whether or not the cell has a mine.
     */
    public boolean getContainsMine() {
        return this.containsMine;
    }
    
    /**
     * This .getDigit function is a getter method that returns an integer value of where on
     * the 8-by-8 Minesweeper board the cell is located, represented by a number between 0
     * and 63.
     * @return int Cell location on an 8-by-8 Minesweeper gameboard represented by a number between 0 and 63.
     */
    public int getDigit() {
        return this.digit;
    }
    
    /**
     * This .hideIfUnrevealed method hides the content of the cell if the cell has not already been set as revealed.
     * @param args Unused
     * @return Nothing
     */
    public void hideIfUnrevealed() {    
        if (!this.revealed) {
            this.contentVisible = false;
        }
    }
    
    /**
     * This .reveal method sets a cell as revealed.
     * @param args Unused
     * @return Nothing
     */
    public void reveal() {
        this.revealed = true;
    }
    
    /**
     * This .showContent method sets a cell's contents as visible on the gameboard.
     * @param args Unused
     * @return Nothing
     */
    public void showContent() {
        this.contentVisible = true;
    }
    
    /**
     * The .setDisplayToDigit method causes a cell to display a given digit when its
     * .toString method is called.
     * @param digit The digit that will be printed when the cell is displayed.
     */
    public void setDisplayToDigit(int digit) {
        if (0 <= digit && digit < 9) {
            this.digit = digit;
            this.contentToDisplay = "" + digit;
        }
    }
     
    /**
     * The .toString method provides the string that will be printed when a call to print
     * the cell is made.
     * @param args Unused
     * @return Nothing
     */
    public String toString() {
        if (this.containsMine) {
            if (this.contentVisible) {
                this.contentToDisplay = "M";
            } else {
                this.contentToDisplay = "-";
            }
        }
        return this.contentToDisplay + (this.paddingAfter ? " " : "");
    }   
}