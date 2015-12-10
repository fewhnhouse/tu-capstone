package core;

import java.io.IOException;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

import menus.PauseMenu;
import symbols.*;

/**
 * @author Felix Wohnhaas
 *
 */
public class Core extends Window {
	// private List<DynamicEnemy> dynamicEnemyList = new ArrayList<>();

	final char idWall = '0';
	final char idIn = '1';
	final char idOut = '2';
	final char idStaticTrap = '3';
	final char idDynamicTrap = '4';
	final char idKey = '5';
	final char empty = '6';
	final char idPlayer = '7';
	final char idCollectable = '8';
	private char[][] lvl;
	KeyListener listener = new KeyListener(getScreen());

	public Core(Screen screen, int resolutionX, int resolutionY, String filename) {
		super(resolutionX, resolutionY, screen);
		Game.io.loadProperties(filename);
		Game.io.createLevelData();
		lvl = Game.io.getLvl();
	}

	public void start() {
		drawLevel(resizeArray(lvl));
		getScreen().refresh();
		update();
	}

	public char[][] resizeArray(char[][] lvl) {
		char[][] level = new char[getResolutionX()][getResolutionY()];
		for (int i = 0; i < getResolutionX(); i++) {
			for (int ii = 0; ii < getResolutionY(); ii++) {
				level[i][ii] = lvl[i + getResolutionX() * Game.io.getRegionX()][ii
						+ getResolutionY() * Game.io.getRegionY()];
			}
		}
		return level;
	}

	public void drawLevel(char[][] level) {
		Entry entry = null;
		int posX = 0;
		int posY = 0;
		for (int x = 0; x < getResolutionX(); x++) {
			for (int y = 0; y < getResolutionY(); y++) {
				/*
				 * int levelX = x + getResolutionX() * Game.io.getRegionX(); int
				 * levelY = y + getResolutionY() * Game.io.getRegionY(); if
				 * (levelY < Game.io.getHeight() && levelX < Game.io.getWidth())
				 * {
				 */
				switch (level[x][y]) {
				case idWall:
					drawSymbol(new Wall(x, y), x, y);
					break;
				case idIn:
					entry = new Entry(x, y);
					posX = x;
					posY = y;
					drawSymbol(entry, x, y);
					break;
				case idOut:
					drawSymbol(new Exit(x, y), x, y);
					break;
				case idStaticTrap:
					drawSymbol(new StaticEnemy(x, y), x, y);
					break;
				case idDynamicTrap:
					// Don�t loop this yet, will add new enemy every
					// time
					DynamicEnemy enemy = new DynamicEnemy(x, y);
					drawSymbol(enemy, x, y);
					// dynamicEnemyList.add(enemy);
					break;
				case idKey:
					drawSymbol(new Key(x, y), x, y);
					break;
				case empty:
					drawSymbol(new Path(x, y), x, y);
					break;
				/*
				 * case idPlayer: Game.player = new Player(levelX, levelY);
				 * drawSymbol(Game.player); break;
				 */
				case idCollectable:
					drawSymbol(new Collectable(x, y), x, y);
					break;
				default:
					drawSymbol(new Path(x, y), x, y);
					break;
				}
			}
		}
		// }
		if (entry == null && Game.player == null) {
			if(Game.io.getRegionX() < 10)
			{
				Game.io.addRegionX(1);
			}
			else
			{
				Game.io.addRegionX(-Game.io.getRegionX());
				Game.io.addRegionY(1);
			}
			drawLevel(resizeArray(lvl));
		}
		/*
		 * if(!entryFound && !load){ if(regionX*terminalHeight>=levelHeight){
		 * regionY++; regionX--; } regionX++; setLab((terminalWidth) * regionX,
		 * (terminalHeight-3) * regionY);
		 *
		 * if (Game.io.getWidth() > Game.io.getRegionX() * getResolutionX())
		 * System.out.println("Y"); Game.io.addRegionY(1); }
		 */
		if (Game.player == null)

		{
			System.out.println(posX + ", " + posY);
			Game.player = new Player(posX, posY);
		}

		getScreen().setCursorPosition(Game.player.getPosition().getX(), Game.player.getPosition().getY());
		drawSymbol(Game.player, Game.player.getPosition().getX(), Game.player.getPosition().getY());
		getScreen().refresh();
	}

	private void drawSymbol(Symbol symbol, int x, int y) {
		char character = symbol.getSymbol();
		Terminal.Color background = symbol.getBackgroundColor();
		Terminal.Color foreground = symbol.getForegroundColor();
		drawColoredString(character + "", foreground, background, null, x, y);
		getScreen().getTerminal().applyBackgroundColor(Color.BLACK);
		getScreen().getTerminal().applyForegroundColor(Color.BLACK);
	}

	public void update() {
		getScreen().getTerminal().setCursorVisible(true);
		boolean game = true;
		while (game) {
			game = move();
			game = check();
		}
		System.out.println("Lost.");
		// Movement
		// Game Logic
		// Update Lab
		// Menu Invoke
	}

	private void setPos(int x, int y) throws InterruptedException, IOException {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		if ((lvl[playerX + x][playerY + y] != idWall
				|| (lvl[playerX + x][playerY + y] == idOut && Game.player.getHasKey()))
				&& lvl[playerX + x][playerY + y] != idIn) {

			Terminal terminal = getScreen().getTerminal();
			terminal.setCursorVisible(false);
			terminal.moveCursor(playerX, playerY);
			if (lvl[playerX][playerY] == idIn) {
				terminal.applyForegroundColor(Color.GREEN);
				terminal.putCharacter('\u2691');
				terminal.applyForegroundColor(Color.WHITE);
			} else {
				terminal.putCharacter(' ');
			}

			Game.player.getPosition().setX(playerX + x);
			Game.player.getPosition().setY(playerY + y);
			terminal.applyBackgroundColor(Terminal.Color.BLACK);
			terminal.applyForegroundColor(Terminal.Color.WHITE);
			terminal.moveCursor(playerX + x, playerY + y);
			if (x == -1 && y == 0) {
				terminal.putCharacter('\u25C4');
			} else if (x == 1 && y == 0) {
				terminal.putCharacter('\u25BA');
			} else if (x == 0 && y == -1) {
				terminal.putCharacter('\u25B2');
			} else if (x == 0 && y == 1) {
				terminal.putCharacter('\u25BC');
			} else {
				terminal.putCharacter('~');
			}
		}

	}

	public boolean check() {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		if (lvl[playerX][playerY] == idStaticTrap || lvl[playerX][playerY] == idDynamicTrap) {
			lvl[playerX][playerY] = empty;
			System.out.println("Enemy");
			Game.player.died();
			System.out.println(Game.player.getLives());
			if (Game.player.getLives() <= 0) {
				return false;
			}
			// writer.drawString(10 + 2 * lives, terminalHeight - 2, " ");
		} else if (lvl[playerX][playerY] == idKey) {
			System.out.println("Key");
			lvl[playerX][playerY] = empty;
			Game.player.setHasKey(true);
		}
		return true;
	}

	public boolean move() {
		Kind kind = listener.getKey();

		try {
			if (kind != null) {
				switch (kind) {
				case ArrowDown:
					setPos(0, 1);
					break;
				case ArrowUp:
					setPos(0, -1);
					break;
				case ArrowLeft:
					setPos(-1, 0);
					break;
				case ArrowRight:
					setPos(1, 0);
					break;
				case Escape:
					PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), this);
					pause.interact(null);
					return false;
				default:
					setPos(0, 0);
					break;
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		} catch (IOException e1) {
			System.err.println("IOException");
		}
		return true;
	}

	public void end() {

	}

}
