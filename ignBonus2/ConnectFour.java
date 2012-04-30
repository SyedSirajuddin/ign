import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

public class ConnectFour extends Applet implements MouseMotionListener, MouseListener, ActionListener{
	
	// GAME SETTINGS
	public static final int EMPTY = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	
	public static final int XSIZE = 7;
	public static final int YSIZE = 6;
	
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;
	public static final int OFFSET = 10;
	public static final int BOTTOMTABLE = 100;
	
	public static final int BOARDHEIGHT = (YSIZE * HEIGHT) + OFFSET;
	
	private int player1 = 1;
	private int player2 = 2;
	private int winner = 0;
	
	private int DRAW = 3;
	
	int[][] board = new int [XSIZE][YSIZE];
	int playerTurn;
	
	boolean mouseClicked;
	int columnClicked;
	boolean boardClicked;
	
	int xpos;
	int ypos;
	
	// DEBUGGING
	int curxpos;
	int curypos;
	
	// USED FOR DOUBLE-BUFFERING
	Graphics bufferedGraphics;
	Image offScreen;
	Dimension dim;
	
	// Specify variables that will be needed everywhere, anytime here
	// The font variable
	Font bigFont;

	// The colors you will use
	Color player1Color;
	Color player2Color;
	     
	Color boardColor;
	Color bgColor; 
	 
	// Interface (GUI)
	Button newGameButton;
	CheckboxGroup playerGroup;
	
	Checkbox firstPlayer;
	Checkbox secondPlayer;
	
	Checkbox vsComp;
	     
	public void init() {
		// Player settings
		setLayout(null);
		newGameButton = new Button("New Game");
		playerGroup = new CheckboxGroup();
		
		firstPlayer = new Checkbox("Choose Player 1", playerGroup, true);
		secondPlayer = new Checkbox("Choose Player 2", playerGroup, false);
		
		vsComp = new Checkbox("vs Computer", true);
		
		int newGameWSize = 100;
		newGameButton.setBounds(OFFSET, (HEIGHT*YSIZE) + 2*OFFSET, newGameWSize,30);
		firstPlayer.setBounds(OFFSET + newGameWSize + 2*OFFSET, (HEIGHT*YSIZE) + 2*OFFSET, 150, 30);
		secondPlayer.setBounds(OFFSET + newGameWSize + 2*OFFSET + 150, (HEIGHT*YSIZE) + 2*OFFSET, 150, 30);
		vsComp.setBounds(OFFSET, (HEIGHT*YSIZE) + 2*OFFSET + 30, 100,30);

		
		add(newGameButton);
		add(firstPlayer);
		add(secondPlayer);
		add(vsComp);
		
		newGameButton.addActionListener(this);
		
		// Settings
		player1Color = Color.red;
		player2Color = Color.blue;
		boardColor = Color.gray;
		bgColor = Color.white;

		// this will set the backgroundcolor of the applet
		setBackground(bgColor); 
		
		addMouseMotionListener(this);
		addMouseListener(this);
		setSize(new Dimension((WIDTH * XSIZE) + 2*OFFSET, (HEIGHT*YSIZE) + 2*OFFSET + BOTTOMTABLE) );
		dim = getSize();
		
        offScreen = createImage(dim.width,dim.height);

        bufferedGraphics = offScreen.getGraphics();
        
        
        playerTurn = player1;
	}
	
	public void stop() {
		
	}
	
	public void paint(Graphics g) {
		
        // Wipe off everything that has been drawn before
        // Otherwise previous drawings would also be displayed.
        bufferedGraphics.clearRect(0,0,dim.width,dim.width);
        
		if(winner == 0) {
			drawBoard(g);
	
			/*if(firstPlayer.getState())
				playerTurn = player1;
			else
				playerTurn = player2;*/
	
			if (boardClicked){
				bufferedGraphics.drawString("You clicked on " + columnClicked + " column",20,700);
				boardClicked = false;
			}
			  // else this one
			else bufferedGraphics.drawString("You clicked outside of the rectangle",20,700);
			
			bufferedGraphics.drawString("x = " + curxpos + "; y = " + curypos, 310, 700);
		} else if (winner == DRAW) {
			drawBoard(g);
			bufferedGraphics.drawString("Draw!",30,50);
		} else {
			drawBoard(g);
		    int fontSize = 30;

		    Font originalFont = bufferedGraphics.getFont();
		    bufferedGraphics.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
		     
		    bufferedGraphics.setColor(Color.ORANGE);
			bufferedGraphics.drawString("Player " + winner + " is the winner!",30,50);
			
			bufferedGraphics.setFont(originalFont);
			bufferedGraphics.setColor(Color.black);
			
		}
        g.drawImage(offScreen,0,0,this);

		
	}
	
	/*public void insertToken(int Player, int column) {
		
		if(column != -1)
			for(int i = YSIZE-1; i >= 0; i-- ) {
				if(board[column][i] == EMPTY) {
					board[column][i] = Player;
					
					if(Player == player1)
						playerTurn = player2;
					else
						playerTurn = player1;
					break;
				}
			}
	}*/
	public void drawBoard(Graphics g) {
		
		int width = WIDTH;
		int height = HEIGHT;

		int offset = OFFSET;
		setSize(new Dimension((WIDTH * XSIZE) + 2*OFFSET, (HEIGHT*YSIZE) + 2*OFFSET + BOTTOMTABLE));
		bufferedGraphics.setColor(boardColor);
		
		// draw background board
		for(int i = 0; i < XSIZE; i++) {
			for(int j = 0; j < YSIZE; j++) {
				//g.setColor(boardColor);
				bufferedGraphics.fillRect((width*i) + offset, (height*j) +offset, width, height);
			}
		}
		
		bufferedGraphics.setColor(Color.black);
		for(int i = 0; i < XSIZE; i++) {
			for(int j = 0; j < YSIZE; j++) {
				bufferedGraphics.drawRect((width*i) + offset, (height*j) + offset, width, height);
			}
		}
		
		// Draw tokens
		for(int i = 0; i < XSIZE; i++) {
			for (int j = 0; j < YSIZE; j++) {
				if(board[i][j] == EMPTY) {
					bufferedGraphics.setColor(Color.WHITE);
				} else if (board[i][j] == RED) {
					bufferedGraphics.setColor(Color.RED);
				} else {
					bufferedGraphics.setColor(Color.BLUE);
				}
				bufferedGraphics.fillArc((width*i) + offset, (height*j) + offset, width, height, 0, 360);
			}
		}
		
		bufferedGraphics.setColor(Color.BLACK);
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if(winner != 0)
			return;
		xpos = me.getX();
		ypos = me.getY();
		// Check the columns, if it is between
		int curCol = getColumn(xpos, ypos);
		
		boolean inserted = insertToken(playerTurn, curCol, board);
		System.out.println("Player " + playerTurn);
		if(isWinner(board)) {
			// Record who the winner was
			winner = playerTurn;
			
			repaint();
			return;
			// Reset start game to player 1
			//playerTurn = player1;
			
		} else if(inserted) {  // Check if player actually inserted anything
			nextTurn();
			//System.out.println("Player " + playerTurn + " should choose "+ chooseCol());
			//System.out.println("Comp should choose column " + chooseCol());
		}
		
		// Computer
		if(vsComp.getState()) {
			insertToken(playerTurn, chooseCol(), board);
			//nextTurn();
			if(isWinner(board)) {
				winner = playerTurn;
				//playerTurn = player1;
			} else {
				nextTurn();
			}
		}


		repaint();
	}
	
	public void nextTurn() {
		if(playerTurn == player1)
			playerTurn = player2;
		else
			playerTurn = player1;	
	}
	
	/*public void checkBoard() {
		// Check board for a winning result
		for(int i = 0; i < XSIZE; i++) {
			for(int j = 0; j < YSIZE; j++){
				int chip = board[i][j];
				if(chip != EMPTY) {
					
					int chipNum = 1;
					//Check board by column
					for(int c = j+1; c < YSIZE; c++) {
						
						if(chip == board[i][c]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						winner = chip;
						return;
					}
					
					//Else check board by row
					chipNum = 1;
					for(int r = i+1; r < XSIZE; r++) {
						
						if(chip == board[r][j]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						winner = chip;
						return;
					}
					
					//Else Check board by diagonal up
					chipNum = 1;
					for(int r = i+1, c = j+1; r < XSIZE && c < YSIZE; ) {
						if(chip == board[r++][c++]) {
							chipNum++;
						} else
							break;
						
					}
					
					if(chipNum >= 4) {
						winner = chip;
						return;
					}
					
					chipNum = 1;
					for(int r = i+1, c = j-1; r < XSIZE && c >= 0; ) {
						if(chip == board[r++][c--]) {
							chipNum++;
						} else
							break;
					}
					
					if(chipNum >= 4) {
						winner = chip;
						return;
					}
				}
			}
		}
	}*/
	
	public void update(Graphics g) {
		paint(g);
	}
	
	private int getColumn(int x, int y) {
		boolean yLimit = false;
		boolean xLimit = false;
		
		if(y > OFFSET && y < BOARDHEIGHT) {
			yLimit = true;
		}
		
		int col = (x-10)/100;
		if(col >= 0 && col < XSIZE) {
			xLimit = true;
		}
		
		if(xLimit && yLimit) {
			columnClicked = col;
			boardClicked = true;
			return col;
		} else {
			return -1;
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		curxpos = me.getX();
		curypos = me.getY();
		
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent evnt) {
		if(evnt.getSource() == newGameButton) {
			reset();
			
			if(secondPlayer.getState() && vsComp.getState()) {
				insertToken(playerTurn, chooseCol(), board);
				nextTurn();

			}
		}
		
		repaint();
	}
	
	public void reset() {
		winner = 0;
		playerTurn = player1;
		for(int i = 0; i < XSIZE; i++)
			for(int j = 0; j < YSIZE; j++) {
				board[i][j] = EMPTY;
			}
	}
	
	public int chooseColumn() {
		// Check if there is a potential winning move for current turn
		// Probably use a simple recursive method
		
		// Create a priority cue so that the comp places the token into the most
		// likely spot
		
		//First check if there is a winning spot. If not, check if opponent has a chance to win
		
		// If not, check if opponent has a chance to win
		
		// else check for good placement
		int [][] cboard = board.clone();
		boolean [] badCol = new boolean[XSIZE];
		
		for(int b = 0; b < XSIZE; b++) 
			badCol[b] = false;
		
		// Check all possible placements
		for(int i = 0; i < XSIZE; i++) {
			for(int j = YSIZE-1; j >= 0; j-- ) {
				if(cboard[i][j] == EMPTY) {
					int chip;
					chip = cboard[i][j] = playerTurn;
					
					// Check for possible winning current player input
						
						int chipNum = 1;
						//Check board by column
						for(int c = j+1; c < YSIZE; c++) {
							
							if(chip == cboard[i][c]) {
								chipNum++;
							} else
								break;
						}
						if(chipNum >= 4) {
							return i;
						}
						
						//Else check board by row
						chipNum = 1;
						for(int r = i+1; r < XSIZE; r++) {
							
							if(chip == cboard[r][j]) {
								chipNum++;
							} else
								break;
						}
						if(chipNum >= 4) {
							return i;
						}
						
						//Else Check board by diagonal up
						chipNum = 1;
						for(int r = i+1, c = j+1; r < XSIZE && c < YSIZE; ) {
							if(chip == cboard[r++][c++]) {
								chipNum++;
							} else
								break;
							
						}
						
						if(chipNum >= 4) {
							return i;
						}
						
						chipNum = 1;
						for(int r = i+1, c = j-1; r < XSIZE && c >= 0; ) {
							if(chip == cboard[r++][c--]) {
								chipNum++;
							} else
								break;
						}
						
						if(chipNum >= 4) {
							return i;
						}
						

					
					/*// Next, check if the opponent's next turn they have a winning spot (can make
					// slightly more complicated by seeing if current input can affect opponent's input
					if(playerTurn == player1) {
						chip = cboard[i][j] = player2;
					} else {
						chip = cboard[i][j] = player1;
					}
					
					// Check if opponent has a possible winning move and try to block it
					//Check board by column
					for(int c = j+1; c < YSIZE; c++) {
						
						if(chip == cboard[i][c]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						return i;
					}
					
					//Else check board by row
					chipNum = 1;
					for(int r = i+1; r < XSIZE; r++) {
						
						if(chip == cboard[r][j]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						return i;
					}
					
					//Else Check board by diagonal up
					chipNum = 1;
					for(int r = i+1, c = j+1; r < XSIZE && c < YSIZE; ) {
						if(chip == cboard[r++][c++]) {
							chipNum++;
						} else
							break;
						
					}
					
					if(chipNum >= 4) {
						return i;
					}
					
					chipNum = 1;
					for(int r = i+1, c = j-1; r < XSIZE && c >= 0; ) {
						if(chip == cboard[r++][c--]) {
							chipNum++;
						} else
							break;
					}
					
					if(chipNum >= 4) {
						return i;
					}
					
					// then check if there is a move where you can chain a > number of tokens or more without helping
					// opponent win.
					
					cboard[i][j] = playerTurn;
					if(playerTurn == player1) {
						j--;
						chip = cboard[i][j] = player2;
					} else {
						j--;
						chip = cboard[i][j] = player1;
					}
					
					// Check if opponent has a possible winning move and try to block it
					//Check board by column
					for(int c = j+1; c < YSIZE; c++) {
						
						if(chip == cboard[i][c]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						badCol[i] = true;
						j++;
						break;
					}
					
					//Else check board by row
					chipNum = 1;
					for(int r = i+1; r < XSIZE; r++) {
						
						if(chip == cboard[r][j]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						badCol[i] = true;
						j++;
						break;
					}
					
					//Else Check board by diagonal up
					chipNum = 1;
					for(int r = i+1, c = j+1; r < XSIZE && c < YSIZE; ) {
						if(chip == cboard[r++][c++]) {
							chipNum++;
						} else
							break;
						
					}
					
					if(chipNum >= 4) {
						badCol[i] = true;
						j++;
						break;
					}
					
					chipNum = 1;
					for(int r = i+1, c = j-1; r < XSIZE && c >= 0; ) {
						if(chip == cboard[r++][c--]) {
							chipNum++;
						} else
							break;
					}
					
					if(chipNum >= 4) {
						badCol[i] = true;
						j++;
						break;
					}
					
					j++;*/
				} else
					break;
								
			}
		}
		
		Random generator = new Random();
		
		int num = generator.nextInt(XSIZE);
		int fill = 0;
		while(badCol[num]) {
			fill++;
			num = generator.nextInt(XSIZE);
			
			if(fill >= XSIZE)
				return -1;
		}
		
		return -1;
		
	}
	
	
	public boolean insertToken(int player, int column, int[][] gameBoard) {
		if(column != -1)
			for(int j = YSIZE-1; j >= 0; j-- ) {
				if(gameBoard[column][j] == EMPTY) {
					gameBoard[column][j] = player;
					
					return true;
				}
			}
		return false;
	}
	
	public boolean removeToken(int column, int[][] gameBoard) {
		for(int j = 0; j < YSIZE; j++ ) {
			if(gameBoard[column][j] != EMPTY) {
				gameBoard[column][j] = EMPTY;
				
				return true;
			}
		}
		return false;
	}
	
	public boolean isWinner(int[][] gameBoard) {
		// Check board for a winning result
		// ASSUMPTION: XSIZE and YSIZE equal gameBoard.lenght and gameBoard[i].lenght respectively
		
		// Check entire board. If there is a win is a winner, then return true. If not, return false 
		for(int i = 0; i < XSIZE; i++) {
			for(int j = 0; j < YSIZE; j++){
				int chip = gameBoard[i][j];
				
				if(chip != EMPTY) {
					int chipNum = 1;
					//Check board by column
					for(int c = j+1; c < YSIZE; c++) {
						
						if(chip == gameBoard[i][c]) {
							chipNum++;
						} else
							break;
					}
					
					if(chipNum >= 4) {
						return true;
					}
					
					//Else check board by row
					chipNum = 1;
					for(int r = i+1; r < XSIZE; r++) {
						
						if(chip == gameBoard[r][j]) {
							chipNum++;
						} else
							break;
					}
					if(chipNum >= 4) {
						return true;
					}
					
					//Else Check board by diagonal up
					chipNum = 1;
					for(int r = i+1, c = j+1; r < XSIZE && c < YSIZE; ) {
						if(chip == gameBoard[r++][c++]) {
							chipNum++;
						} else
							break;
						
					}
					
					if(chipNum >= 4) {
						return true;
					}
					
					chipNum = 1;
					for(int r = i+1, c = j-1; r < XSIZE && c >= 0; ) {
						if(chip == gameBoard[r++][c--]) {
							chipNum++;
						} else
							break;
					}
					
					if(chipNum >= 4) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	public int chooseCol() {
		int [][] cboard = new int[XSIZE][YSIZE];
		boolean[] isBadColumn = new boolean[XSIZE];
		int[] maxColumnValue = new int[XSIZE];
		int [] maxOpCol = new int[XSIZE];
		
		for(int i = 0; i < XSIZE; i++) {
			isBadColumn[i] = false;
			maxColumnValue[i] = 0;
			maxOpCol[i] = 0;
			for(int j = 0; j < YSIZE; j++)
				cboard[i][j] = board[i][j];
		}
		
		
		for(int i = 0; i < XSIZE; i++) {
			if(insertToken(playerTurn, i, cboard)) {
				if(isWinner(cboard)) {
					return i;
				}
				
				removeToken(i, cboard);
				
				int stopOpponentWin = 0;
				if(playerTurn == player1) {
					stopOpponentWin = player2;
				} else {
					stopOpponentWin = player1;
				}
				
				if(insertToken(stopOpponentWin, i, cboard)) {
					if(isWinner(cboard)) {
						return i;
					}
					
					removeToken(i, cboard);
				}
				
				// Check if there are any moves that the player shouldn't
				// that will allow the opponent to win
				insertToken(playerTurn, i, cboard);
				insertToken(stopOpponentWin, i, cboard);
				
				if(isWinner(cboard)) {
					isBadColumn[i] = true;
					System.err.println("BAD COLUMN: " + i);
				}
				
				removeToken(i, cboard);
				removeToken(i, cboard);
			} else
				break;			
		}
		

		
		for(int i = 0; i < XSIZE; i++) {
			if(isBadColumn[i]) {
				maxColumnValue[i] = -2;
			} else {
				//System.err.println("Column i = " + i + " Maximum Column" + maxVal(playerTurn, i, cboard));
				maxColumnValue[i] = maxVal(playerTurn, i, cboard);	
				if(playerTurn == player1) {
					maxOpCol[i] = maxVal(player2, i, cboard);
				} else
					maxOpCol[i] = maxVal(player1, i, cboard);
				
			}
		}
		
		int max = -2;
		int maxo = -2;
		Vector<Integer> maxVal = new Vector<Integer>();
		Random g = new Random();
		
		// Check own turn as well as opponents
		for(int i = 0; i < XSIZE; i++) {
			if(maxColumnValue[i] > max) {
				max = maxColumnValue[i];
			}
			
			if(maxOpCol[i] > maxo) {
				maxo = maxOpCol[i];
			}
		}
		
		// Check if the opponent has a choice that can guarantee a win and
		// block it if possible
		if(maxo >= 3) {
			for(int i = 0; i < XSIZE; i++) {
				if(maxo == maxOpCol[i]) {
					return i;
				}
			}
		}
		for(int i = 0; i < XSIZE; i++) {
			if(max == maxColumnValue[i]) {
				maxVal.add(new Integer(i));
			}
		}
		
		return maxVal.get(g.nextInt(maxVal.size())).intValue();

		
	}
	
	public int maxVal(int player, int column, int[][] gameBoard) {
		// Check board for a winning result
		// ASSUMPTION: XSIZE and YSIZE equal gameBoard.lenght and gameBoard[i].lenght respectively
		
		// Check entire board. If there is a win is a winner, then return true. If not, return false 
		int dcol = 0;
		
		int lrow = 0;
		boolean lrowc = false;
		int rrow = 0;
		boolean rrowc = false;
		
		// EXTRA
		int ldup = 0;
		int rddown = 0;
		boolean ldupc = false;
		boolean rddownc = false;
		
		int lddown = 0;
		int rdup = 0;
		boolean rdupc = false;
		boolean lddownc = false;
		
		//
		
		int playerYPos = 0;
		int j;
		int i;
		for(j = YSIZE-1; j >= 0; j--) {
			if(gameBoard[column][j] == EMPTY /*&& gameBoard[column][j] == player*/) {
				playerYPos = j;
				break;
			}
		}
		
		if(j == -1)
			return -2;
		// COLUMN CHECK
		for(j = playerYPos + 1; j < YSIZE; j++) {
			if(gameBoard[column][j] == player){
				dcol++;
			} else
				break;
		}
		
		if(playerYPos + dcol < 4)
			dcol = -1;
		// ROW CHECK
		// Check both rows on either column to figure out how many 
		
		for(i = column-1; i>= 0; i--) {
			if(gameBoard[i][playerYPos] == player) {
				// Check left
				lrow++;
			} else if (gameBoard[i][playerYPos] == EMPTY) {
				if(playerYPos < YSIZE-1) {
					if(gameBoard[i][playerYPos +1] != EMPTY)
						lrowc = true;
					else
						break;
				} else
					lrowc = true;
				break;
			} else {
				break;
			}
		}
		
		for(i = column + 1; i < XSIZE; i++) {
			if(gameBoard[i][playerYPos] == player) {
				// Check right
				rrow++;
			} else if (gameBoard[i][playerYPos] == EMPTY) {
				if(playerYPos < YSIZE-1 ) {
					if (gameBoard[i][playerYPos+1] != EMPTY)
						rrowc = true;
					else
						break;
				} else
					rrowc = true;
				break;
			} else {
				break;
			}
		}
		
		/*int comb  = 0;
		if(column < 3 && ((lrowc && lrow < 0) || lrow > 1)  && rrow < 1) {
			return dcol;
		}
		
		//return Math.max(dcol, rrow + lrow);*/
		
		/*if(lrowc || rrowc) {
			// Add the potential combination
			return lrow + rrow;
		}*/
		

		
		// DIAGONAL CHECK bl to tr
		for(j = playerYPos + 1, i = column+1 ; i < XSIZE && j < YSIZE; i++, j++) {
			//for(j = playerYPos +1; j < YSIZE; ) {
				if(gameBoard[i][j] == player) {
					rdup++;
				} else if (gameBoard[i][j] == EMPTY) {
					if(j < YSIZE-1) {
						if(gameBoard[i][j+1] != EMPTY)
							rdupc = true;
						break;
					} else
						rdupc = true;
						
					break;
				} else
					break;
			//}
		}
		
		for(i = column-1, j = playerYPos -1; i >= 0 && j>= 0; j--, i-- ) {
			//for(j = playerYPos -1; j >= 0; j--, i--) {
				if(gameBoard[i][j] == player) {
					lddown++;
				} else if (gameBoard[i][j] == EMPTY) {
					if(j+1 < XSIZE ) {
						if(gameBoard[i][j+1] != EMPTY)
							lddownc = true;
						break;
					} else
						lddownc = true;
					break;
				} else
					break;	
			//}
		}
		
		// tl to br
		for( i = column+1, j = playerYPos -1; i < XSIZE && j >= 0; i++, j--) {
			//for(j = playerYPos -1; j >= 0; i++, j--) {
				if(gameBoard[i][j] == player) {
					rddown++;
				} else if (gameBoard[i][j] == EMPTY) {
					if(j+ 1 < XSIZE) { 
						if(gameBoard[i][j+1] != EMPTY)
							rddownc = true;
						break;
					} else
						rddownc = true;
						break;
				} else
					break;
			//}
		}
		
		for( i = column-1, j = playerYPos +1; i >= 0 && j < YSIZE; j++, i-- ) {
			//for(j = playerYPos +1; j < YSIZE; j++, i--) {
				if(gameBoard[i][j] == player) {
					ldup++;
				} else if (gameBoard[i][j] == EMPTY) {
					if(j+1 < YSIZE ) { 
						if(gameBoard[i][j+1] !=  EMPTY)
							ldupc = true;
						break;
					} else
						ldupc = true;
					break;
				} else
					break;	
			//}
		}
		
		int combr = 0;
		int combdu = 0;
		int combdd = 0;
		if(lrow + rrow >= 2 && (lrowc && rrowc)) {
			combr =  lrow+rrow + 1;
		}
		if(rdup + lddown >= 2 && (rdupc && lddownc)) {
			combdu = rdup + lddown + 1;
		}
		if(rddown + ldup >= 2 && (rddownc && ldupc)) {
			combdu = rddown + ldup + 1;
		}
		
		int goodMove = Math.max(combr, Math.max(combdu, combdd));
		
		return Math.max(Math.max(goodMove, dcol), Math.max(Math.max(rdup, lddown),Math.max(Math.max(rddown, ldup), Math.max(lrow,rrow))));
		/*int comb  = 0;
		if(column < 3 && ((lrowc && lrow < 0) || lrow > 1)  && rrow < 1) {
			return dcol;
		}*/
		
		//return Math.max(dcol, rrow + lrow);
	}
	
	
	
}
