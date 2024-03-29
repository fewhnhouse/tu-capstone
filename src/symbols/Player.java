package symbols;

import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author Felix Wohnhaas
 */
public class Player extends Symbol {
	private int lives = 3;
	private int score = 0;
	private boolean hasKey = false;
	private final static Terminal.Color color = Terminal.Color.WHITE;
	private static final int value = 7;
	public static final char symbol = '\u25A0';

	public Player(int x, int y) {
		super(x, y, value, color, symbol);
	}

	public void died() {
		lives--;
	}
	
	public void addScore() {
		score+=10;
	}

	public void setHasKey(boolean hasKey) {
		this.hasKey = hasKey;
	}

	public boolean hasKey() {
		return hasKey;
	}

	public int getLives() {
		return lives;
	}

	public int getScore() {
		return score;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
