import java.util.Scanner;

public class Game {
	public static int BOARD_SIZE = 10;
	final static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		

		System.out.println();
		final Board board = new Board(BOARD_SIZE);
		board.update_board0();
		board.print_board();
		System.out.println("\n-------------------\n\n");
		board.generateRandomBoard();
		board.print_board();

		while (true) {
			System.out.println("insert coordinate 1");
			int r = input.nextInt();
			System.out.println("Insert coordinate 2");
			int c = input.nextInt();

			System.out.println("Insert direction: 0-UP, 1-DOWN, 2-LEFT, 3-RIGHT");
			int dir = input.nextInt();
			switch (dir) {
			case 0:
				System.out.println("The move is: " + board.checkLegalMove(r, c, Directions.UP));
				break;
			case 1:
				System.out.println("The move is: " + board.checkLegalMove(r, c, Directions.DOWN));
				break;
			case 2:
				System.out.println("The move is: " + board.checkLegalMove(r, c, Directions.LEFT));
				break;
			case 3:
				System.out.println("The move is: " + board.checkLegalMove(r, c, Directions.RIGHT));
				break;
			}
		}
	}
}
