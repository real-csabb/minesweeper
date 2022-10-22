import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * The Gameboard program defines an 8-by-8 Minesweeper grid as well as methods to manipulate and display the grid. It also
 * contains a run method which will enable the user to interact with the board and play Minesweeper.
 * @author Chris Sabb
 * @version 3.0 Electric Boogaloo Edition
 * @since 2022-08-28
 */
public class Gameboard {

	private Cell[][] cells;
	private int numberOfMines;
	private int[] minePlacements;
	
	/**
	 * This is the constructor method for the Gameboard class. Before it, the cells 2D array, numberOfMines int object,
	 * and minePlacement int objects are initialized. In the constructor method, the cells object has its cells elements
	 * created using the .createCells method and the mines are placed within them by the .initializedMinePlacements method,
	 * of which minePlacements is then updated to reflect this.
	 * @param args Unused
	 * @return Nothing
	 */
	public Gameboard() {
		cells = new Cell[8][8];
		numberOfMines = 10;
		minePlacements = new int[numberOfMines];
		
		this.initializeMinePlacements();
		this.createCells();
	}
	
	/**
	 * The run method contains the main execution of the Gameboard program. It prompts the user for input and displays the
	 * Minesweeper board and then determines whether the play has won or lost and allows them to continue playing.
	 * @param args Unused
	 * @return Nothing
	 */
	public void run() {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome to Minesweeper!");

		int row = 0;
		int column = 0;
				
		int minesFound = 0;
		
		String stage = "start";
		String nextStageOnY = "";
		String nextStageOnN = "";
		String invalidInputMessage = "Invalid input, please try again.";
		
		while (true) {
			switch (stage) {
				case "start":
					System.out.print("Would you like to play a game? (y/n): ");
					nextStageOnY = "display";
					nextStageOnN = "end";
					break;
				case "restart":
					minesFound = 0;
					this.initializeMinePlacements();
					this.createCells();
				case "display":
					this.displayBoard();
					stage = "peekPrompt";
				case "peekPrompt":
					System.out.print("Would you like to peek? (y/n): ");
					nextStageOnY = "peek";
					nextStageOnN = "guessRow";
					break;
				case "peek":
					this.showAllCells();
					this.displayBoard();
					this.hideCells();
					stage = "guessRow";
				case "guessRow":
					System.out.print("Please enter a row number: ");
					try {
						row = Integer.parseInt(scan.nextLine()) - 1;
						if (row < 0 || row > 7) {
							throw new NumberFormatException();
						}
						stage = "guessColumn";
					} catch (NumberFormatException e) {
						System.out.println(invalidInputMessage);
					}
					continue;
				case "guessColumn":
					System.out.print("Please enter a column number: ");
					try {
						column = Integer.parseInt(scan.nextLine()) - 1;
						if (column < 0 || column > 7) {
							throw new NumberFormatException();
						}
						stage = "guessMine";
					} catch (NumberFormatException e) {
						System.out.println(invalidInputMessage);
					}
					continue;
				case "guessMine":
					System.out.print("Does row " + (row + 1) + " and column " + (column + 1) + " contain a mine? (y/n): ");
					boolean containsMine = this.containsMine(row, column);
					if (containsMine) {
						this.revealMine(row, column);
						minesFound++;
						nextStageOnY = minesFound == this.numberOfMines ? "win" : "display";
						nextStageOnN = "lose";
					} else {
						this.expand(row, column);
						nextStageOnY = "lose";
						nextStageOnN = "display";
					}
					break;
				case "lose":
					System.out.println("Boom! You lose.");
					stage = "thankYouMessage";
					continue;
				case "win":
					System.out.println("You win!");
					stage = "thankYouMessage";
					continue;
				case "thankYouMessage":
					System.out.println("Thank you for playing Minesweeper.");
					stage = "playAgainPrompt";
				case "playAgainPrompt":
					System.out.print("Would you like to play again? (y/n): ");
					nextStageOnY = "restart";
					nextStageOnN = "end";
					break;
				case "end":
					System.out.println("Goodbye!");
					return;
			}
			

			String response = scan.nextLine();
			
			if (response.equals("y")) {
				stage = nextStageOnY;
			} else if (response.equals("n")) {
				stage = nextStageOnN;
			} else {
				System.out.println(invalidInputMessage);
			}			
		}
	}
		
	/**
	 * The displayBoard method iterates through each cell and prints its current string representation leaving a space between
	 * each cell and the next.
	 * @param args Unused
	 * @return Nothing
	 */
	private void displayBoard() {
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				System.out.print(cell);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * The expand method takes in a row and column of a cell and assigns a digit to that cell based on how many mines
	 * are adjacent to it if an digit hasn't already been assigned. If there's zero mines adjacent to that cell, the 
	 * expand method is called again on all adjacent cells.
	 * @param row This is a cell's row value. 
	 * @param column This is a cell's column value.
	 * @return Nothing
	 */
	private void expand(int row, int column) {
		Stack<int[]> positions = new Stack<int[]>();
		int[] pos = {row, column};
		positions.push(pos);
		
		while (!positions.isEmpty()) {
			pos = positions.pop();
			row = pos[0];
			column = pos[1];

			if (this.cells[row][column].getDigit() < 0) {
				this.changeCellToDigit(row, column);
				
				if (this.cells[row][column].getDigit() == 0) {
					for (int[] position : this.getPositionsAround(row, column)) {
						positions.push(position);
					}
				}
			}
		}
	}
	
	/**
	 * This changeCellToDigit method looks at all the cells adjacent to a cell, counts how many mines are adjacent to
	 * that cell, and assigns a digit to that cell based on how many mines are adjacent thereof.
	 * @param row This is a cell's row value. 
	 * @param column This is a cell's column value.
	 * @return Nothing
	 */
	private void changeCellToDigit(int row, int column) {
		int digit = 0;
		
		for (int[] pos : this.getPositionsAround(row, column)) {
			int i = pos[0];
			int j = pos[1];
			
			if (this.cells[i][j].getContainsMine()) {
				digit++;
			}
		}
		
		this.cells[row][column].setDisplayToDigit(digit);
	}
	
	/**
	 * The .revealMine method sets a cell to reveal its content and if there is a mine on that cell.
	 * @param row This is a cell's row value. 
	 * @param column This is a cell's column value.
	 * @return Nothing
	 */
	private void revealMine(int row, int column) {
		this.cells[row][column].showContent();
		this.cells[row][column].reveal();
	}
	
	/**
	 * The .showAllCells method shows the entire gameboard and each cell's contents.
	 * @param args Unused
	 * @return Nothing
	 */
	private void showAllCells() {
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				cell.showContent();
			}
		}
	}
		
	/**
	 * The .hideCells method hides all the cells that have not had all their contents revealed yet.
	 * @param args Unused
	 * @return Nothing
	 */
	private void hideCells() {
		for (Cell[] row : this.cells) {
			for (Cell cell : row) {
				cell.hideIfUnrevealed();
			}
		}
	}
	
	/**
	 * The .createCells method assigns a cell to each cell in the cells 2D-array and also puts a mine in the cell if
	 * there is a mine placement at that location.
	 * @param args Unused
	 * @return Nothing
	 */
	private void createCells() {
		for (int i = 0; i < 64; i++) {
			this.cells[i / 8][i % 8] = new Cell(this.isMinePlacement(i), true);//i % 8 != 7);
		}
	}
	
	/**
	 * The .initializeMinePlacements method randomly assigns ten mine placements on the gameboard in the cells 2d-array. 
	 */
	private void initializeMinePlacements() {
		Random rand = new Random();
		
		for (int i = 0; i < this.numberOfMines; i++) {
			this.minePlacements[i] = -1;
		}
		
		for (int i = 0; i < this.numberOfMines; i++) {
			int placement = rand.nextInt(64);
			while (this.isMinePlacement(placement)) {
				placement = rand.nextInt(64);
			}
			this.minePlacements[i] = placement;
		}
	}

	/**
	 * The .getPositionAround method pushes the positions of all cells adjacent to a given cell onto a stack and returns
	 * that stack.
	 * @param row This is a cell's row value. 
	 * @param column This is a cell's column value.
	 * @return Stack<int[]> This is an integer stack array that contains the positions of all the cells adjacent to a given cell.
	 */
	private Stack<int[]> getPositionsAround(int row, int column) {
		Stack<int[]> positions = new Stack<int[]>();
		
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = column - 1; j < column + 2; j++) {
				if (0 <= i && i < 8 && 0 <= j && j < 8 && (i != row || j != column)) {
					int[] pos = {i, j};
					positions.push(pos);
				}
			}
		}
		
		return positions;
	}
		
	/**
	 * This .containsMine method returns a boolean value whether or not a cell[row][column] contains a mine.
	 * @param row This is a cell's row value. 
	 * @param column This is a cell's column value.
	 * @return boolean This is a boolean value as to whether or not this cell contains a mine.
	 */
	private boolean containsMine(int row, int column) {
		return this.cells[row][column].getContainsMine();
	}
	
	/**
	 * The .isMinePlacement method checks whether or not a given placement within the grid of cells is
	 * a mine placement.
	 * @param num A placement in an 8-by-8 grid represented by a number between 0 and 63. 
	 * @return boolean Whether or not the the cell placement has a mine or not.
	 */
	private boolean isMinePlacement(int num) {
		for (int placement : this.minePlacements) {
			if (placement == num) {
				return true;
			}
		}
		return false;
	}
	
}
