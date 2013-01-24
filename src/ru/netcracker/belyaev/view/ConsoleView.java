package ru.netcracker.belyaev.view;

import ru.netcracker.belyaev.controller.OneCellOnBoard;
import ru.netcracker.belyaev.controller.OnePlayer;
import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;

public class ConsoleView extends View {
	
	private char drawDirection(Direction direction) {
		if(direction == Direction.DOWN) {
			return 'v';
		}
		else if(direction == Direction.LEFT) {
			return '<';
		}
		else if(direction == Direction.RIGHT) {
			return '>';
		}
		else {
			return '^';
		}
	}
	
	private char[][] getPureBoard(int sizeX, int sizeY) {
		char[][] pureBoard = new char[sizeY][sizeX];
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				pureBoard[j][i] = '.';
			}
		}
		return pureBoard;
	}
	
	public char[][] getBoardWithRiverWallAndArch(OneCellOnBoard[][] snapshot) {
		int sizeY = snapshot.length;
		int sizeX = snapshot[0].length;
		char[][] board = this.getPureBoard(sizeX, sizeY);
		for(int y = sizeY - 1; y >= 0; y--) {
			for(int x = 0; x < sizeX; x++) {
				OneCellOnBoard point = snapshot[y][x];
				Direction direction = point.getRiverDirection();
				if(direction != null) {
					board[y][x] = drawDirection(direction);
				}
				else if(point.isWall()) {
					board[y][x] = 'w';
				}
				else if(point.isArch()) {
					board[y][x] = 'A';
				}
			}
		}
		return board;
	}
	
	public char[][] getBoardWithPlayers(OneCellOnBoard[][] snapshot) {
		int sizeY = snapshot.length;
		int sizeX = snapshot[0].length;
		char[][] board = this.getPureBoard(sizeX, sizeY);
		for(int y = sizeY - 1; y >= 0; y--) {
			for(int x = 0; x < sizeX; x++) {
				OneCellOnBoard point = snapshot[y][x];
				for(OnePlayer player : point.getPlayers()) {
					if(player.isAlive()) {
						board[y][x] = player.getName().charAt(0);
					}
				}
			}
		}
		return board;
	}
	
	public char[][] getBoardWithMageTreasureAndExit(OneCellOnBoard[][] snapshot) {
		int sizeY = snapshot.length;
		int sizeX = snapshot[0].length;
		char[][] board = this.getPureBoard(sizeX, sizeY);
		for(int y = sizeY - 1; y >= 0; y--) {
			for(int x = 0; x < sizeX; x++) {
				OneCellOnBoard point = snapshot[y][x];
				if(point.isExit()) {
					board[y][x] = 'E';
				}
				else if(point.isMage()) {
					board[y][x] = 'M';
				}
				else if(point.getRealTreasureCount() > 0) {
					board[y][x] = 'R';
				}
				else if(point.getFakeTreasureCount() > 0) {
					board[y][x] = 'F';
				}
			}
		}
		return board;
	}
	
	public void drawBoard(char[][] board) {
		int sizeY = board.length;
		int sizeX = board[0].length;
		for(int y = sizeY - 1; y >= 0; y--) {
			System.out.print(y);
			for(int x = 0; x < sizeX; x++) {
				System.out.print(board[y][x]);
			}
			System.out.println();
		}
		System.out.print(" ");
		for(int x = 0; x < sizeX; x++) {
			System.out.print(x);
		}
	}

	@Override
	public void refreshBoard(OneCellOnBoard[][] snapshot) {
		char[][] board = this.getBoardWithRiverWallAndArch(snapshot);
		
		System.out.println("Rivers, Walls and Arches");
		System.out.println("----------------------------------");
		drawBoard(board);
		System.out.println();
		System.out.println();
		
		board = getBoardWithPlayers(snapshot);
		System.out.println("Players (first letter of name)");
		System.out.println("----------------------------------");
		drawBoard(board);
		System.out.println();
		System.out.println();
		
		board = getBoardWithMageTreasureAndExit(snapshot);
		System.out.println("Mage, treasure(Real and Fake) and Exit");
		System.out.println("----------------------------------");
		drawBoard(board);
		System.out.println();
	}

	@Override
	public void informAboutAction(GameCases action, int startX, int startY, int destX, int destY, OnePlayer player) {
		Direction direction = Direction.recognizeDirection(startX, startY, destX, destY);
		switch (action) {
			case GO:
				System.out.println("Player " + player.getName() + " goes " + direction);
				break;
			case INSIDE_WALL: case OUTSIDE_WALL:
				System.out.println("Player " + player.getName() + " run into a wall");
				break;
			case RIVER:
				int distinctionX = destX - startX;
				int distinctionY = destY - startY;
				StringBuffer message = new StringBuffer("Player " + player.getName() + " rafts");
				if(distinctionX != 0) {
					message.append(" on " + Math.abs(distinctionX) + " cells " + (distinctionX > 0 ?
							"right" : "left"));
				}
				if(distinctionY != 0) {
					if(distinctionX != 0) {
						message.append(" and");
					}
					message.append(" on " + Math.abs(distinctionY) + " cells " + (distinctionY > 0 ?
							"up" : "down"));
				}
				System.out.println(message);
				break;
			case SHOT:
				System.out.println("Player " + player.getName() + " shoots " + direction);
				break;
			default:
				return;
		}
	}

	@Override
	public void informAboutAction(GameCases action, int startX, int startY, OnePlayer player, int... colourID) {
		switch (action) {
			case FIND_TREASURE:
				int num = colourID.length;
				System.out.println("Player " + player.getName() + " find " + num + " treasure" + (num > 1 ? "s:" : ":"));
				for(int i = 0; i < colourID.length; i++) {
					System.out.println(Colors.values()[colourID[i]]);
				}
				break;
			case UP_TREASURE:
				System.out.println("Player " + player.getName() + " picks up " + Colors.values()[colourID[0]] + " treasure");
				break;
			case DROP_TREASURE:
				System.out.println("Player " + player.getName() + " drops treasure");
				break;
			default:
				return;
		}
		
	}

	@Override
	public void informAboutAction(GameCases action, int x, int y, OnePlayer player) {
		switch (action) {
			case FIND_MAGE:
				System.out.println("Player " + player.getName() + " on one cell with mage");
				break;
			case UNDER_ARCH:
				System.out.println("Player " + player.getName() + " is under the arch");
				break;
			case PREDICTION_REAL:
				System.out.println("Player " + player.getName() + " has real treasure");
				break;
			case PREDICTION_FAKE:
				System.out.println("Player " + player.getName() + " has fake treasure");
				break;
			case EXIT_FROM_MAZE:
				System.out.println("Player " + player.getName() + " exit from maze");
				break;
			case EXIT_WITH_REAL_TREASURE:
				System.out.println("Player " + player.getName() + " exit from maze with real treasure! Congratulation!");
				break;
			case EXIT_POINT:
				System.out.println("Player " + player.getName() + " is on exit");
				break;
			case KILLED:
				System.out.println("Player " + player.getName() + " was shot");
				break;
			case INJURED:
				System.out.println("Player " + player.getName() + " is injured");
				break;
			case CURRENT_PLAYER_INFORMATION:
				System.out.println("Current player is " + player.getName());
				break;
			default:
				return;
		}
	}

	@Override
	public void notifyUser(Notification notification) {
		switch (notification) {
			case HAVE_NO_TREASURE:
				System.out.println("You haven't treasure!");
				break;
			case NO_SUCH_TREASURE:
				System.out.println("There is no such treasure on your cell");
				break;
			case NO_MAGE:
				System.out.println("There is no mage on this point");
				break;
			case NO_EXIT:
				System.out.println("There is no exit here");
				break;
			case ALREADY_HAS_TREASURE:
				System.out.println("You have already picked up some treasure. Drop your treasure " +
						"to take another one");
				break;
			case BOARD_IS_ALREADY_CREATED:
				System.out.println("Game is already started");
				break;
			case BOARD_IS_NOT_CREATED:
				System.out.println("You should generate board first");
				break;
			case NOT_YOUR_MOVE:
				System.out.println("This is not your move now");
				break;
			case BOARD_GENERATION:
				System.out.println("Board is successfully generated! Game starts right now");
				break;
			case NO_BULLET:
				System.out.println("You haven't bullets");
				break;
		}
	}
	
	@Override
	public void informAboutAction(GameCases action) {
		switch (action) {
			case GAME_OVER:
				System.out.println("Game is over");
				break;
			default:
				return;
		}
	}

}
