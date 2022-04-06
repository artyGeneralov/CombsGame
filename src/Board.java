import java.util.concurrent.ThreadLocalRandom;

public class Board {
	public char[][] board = null;
	public final char EMPTY_CHAR = '=';
	public final char[] signs = { '1', '*', '@' };
	public final int BOARD_SIZE;

	public Board() throws Exception {
		throw new Exception("Board is empty, wtf.");
	}

	public Board(int board_size) {
		BOARD_SIZE = board_size;
		update_board0();
	}

	public void print_board() {
		if (board == null)
			System.out.println("Null board");
		else {
			System.out.print("  ");
			for(int i = 0; i < 10; i++)
				System.out.print(" "+i + " ");
			System.out.println();
			for (int i = 0; i < board.length; i++) {
				System.out.print(i + ":");
				for (int j = 0; j < board[i].length; j++) {
					System.out.print(" " + String.valueOf(board[i][j]) + " ");
				}
				System.out.println();
			}
		}
	}

	public void update_board0() {
		if (board == null) {
			board = new char[BOARD_SIZE][BOARD_SIZE];
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					board[i][j] = EMPTY_CHAR;
				}
			}
		}
	}

	public char getRandomChar() {
		return signs[ThreadLocalRandom.current().nextInt(0, signs.length)];

	}

	public boolean isLegalLoc(int r, int c) {
		if (r >= BOARD_SIZE || c >= BOARD_SIZE || r < 0 || c < 0)
			return false;
		return true;
	}

	public void generateRandomBoard() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				boolean flag = false;
				do {
					char newChar = getRandomChar();
					if (checkTrioAddedChar(newChar, row, col)) {
						board[row][col] = newChar;
						flag = true;
					}
				} while (!flag);
			}
		}
	}

	public boolean checkLegalMove(int r, int c, Directions direction) {
		// Illegal initial location
		String err = "Illegal move";
		int[] dest = new int[2];
		if (!isLegalLoc(r, c))
			return false;

		switch (direction) {
		case UP:
			// Check position is ok
			if (!isLegalLoc(r - 1, c)) {
				System.out.println(err);
				return false;
			}
			// Where we move next
			dest = getDest(r, c, Directions.UP);
			break;
		case DOWN:
			if (!isLegalLoc(r + 1, c)) {
				System.out.println(err);
				return false;
			}
			dest = getDest(r, c, Directions.DOWN);
			break;
		case LEFT:
			if (!isLegalLoc(r, c - 1)) {
				System.out.println(err);
				return false;
			}
			dest = getDest(r, c, Directions.LEFT);
			break;
		case RIGHT:
			if (!isLegalLoc(r, c + 1)) {
				System.out.println(err);
				return false;
			}
			dest = getDest(r, c, Directions.RIGHT);
			break;
		}
		System.out.printf("Swap coords: %d, %d -> %d, %d\n", r,c,dest[0],dest[1]);
		performSwap(r, c, dest[0], dest[1]);
		System.out.println("After swap: ");
		print_board();
		System.out.println();
		if (countTriosInLocation(dest[0], dest[1]) > 0 || countTriosInLocation(r, c) > 0) {
			performSwap(r, c, dest[0], dest[1]);
			return true;
		}
		performSwap(r, c, dest[0], dest[1]);
		return false;
	}

	// Where we move
	private int[] getDest(int r, int c, Directions direction) {
		int[] arr = { r, c };
		switch (direction) {
		case UP:
			arr[0]--;
			break;
		case DOWN:
			arr[0]++;
			break;
		case LEFT:
			arr[1]--;
			break;
		case RIGHT:
			arr[1]++;
			break;
		}
		return arr;
	}

	private void performSwap(int r1, int c1, int r2, int c2) {
		char temp = board[r1][c1];
		board[r1][c1] = board[r2][c2];
		board[r2][c2] = temp;
	}

	// check if number is part of a trio
	public int countTriosInLocation(int r, int c) {
		return upDown(r, c) + leftRight(r, c);
	}

	private int upDown(int r, int c) {
		int counter = 0;
		// if in two top rows
		for (int row = r - 2; row < Math.min(r, BOARD_SIZE-2); row++) {
			boolean isTriple = true;
			if (!isLegalLoc(row, c))
				continue;
			for (int cur = 0; cur < 2; cur++) {
				if (isTriple && board[row + cur][c] == board[row + cur + 1][c])
					isTriple = true;
				else
					isTriple = false;
			}
			if (isTriple)
				counter++;
		}
		
		return counter;
	}

	private int leftRight(int r, int c) {

		int counter = 0;
		for (int col = c - 2; col <Math.min(c, BOARD_SIZE-2); col++) {
			boolean isTriple = true;
			if (!isLegalLoc(r, col))
				continue;

			for (int cur = 0; cur < 2; cur++) {
				if (isTriple && board[r][col + cur] == board[r][col + cur + 1])
					isTriple = true;
				else
					isTriple = false;
			}
			if (isTriple)
				counter++;
		}
		
		return counter;

	}

	public boolean checkTrioAddedChar(char ch, int r, int c) {
		if (!isLegalLoc(r, c))
			return false;
		if (board[r][c] != EMPTY_CHAR)
			return false;

		// add to board
		board[r][c] = ch;

		// check its columns
		for (int col = c - 2; col < c && col < BOARD_SIZE - 2; col++) {
			boolean isTriple = true;
			if (!isLegalLoc(r, col))
				continue;
			for (int cur = 0; cur < 2; cur++) {
				if (isTriple && board[r][col + cur] == board[r][col + cur + 1])
					isTriple = true;
				else
					isTriple = false;
			}
			if (isTriple) {
				board[r][c] = EMPTY_CHAR;
				return false;
			}
		}
		// check its rows
		for (int row = r - 2; row < r && row < BOARD_SIZE - 2; row++) {
			boolean isTriple = true;
			if (!isLegalLoc(row, c))
				continue;
			for (int cur = 0; cur < 2; cur++) {
				if (isTriple && board[row + cur][c] == board[row + cur + 1][c])
					isTriple = true;
				else
					isTriple = false;
			}
			if (isTriple) {
				board[r][c] = EMPTY_CHAR;
				return false;
			}
		}
		board[r][c] = EMPTY_CHAR;
		return true;
	}
}
