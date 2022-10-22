/**
 * This Minesweeper program runs the Minesweeper game defined in the gameboard class as "run()".
 * 
 * @author Chris Sabb
 * @version 3.0 Electric Boogaloo Edition
 * @since 2022-08-28
 */

public class Minesweeper {

	/**
	 * This main method of the Minesweeper program creates a new gameboard and runs the Minesweeper
	 * program defined therein as "run()".
	 * @param args Unused
	 * @return Nothing
	 */
	
    public static void main(String[] args) {
        
        Gameboard gameboard = new Gameboard();
        gameboard.run();

    }

}