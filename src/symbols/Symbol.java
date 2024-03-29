package symbols;

import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import core.Coordinates;

/**
 *
 * @author Felix Wohnhaas
 */
public class Symbol {
    
    private Coordinates position;
    private final Terminal.Color backgroundColor;
    private final Terminal.Color foregroundColor;
    private final char symbol;
    
    /**
     * Constructor with Background color
     * @param x the x position of the symbol
     * @param y the y position of the symbol
     * @param value the value of the symbol in the properties file
     * @param backgroundColor the background color of the symbol
     * @param foregroundColor the foreground color of the symbol
     * @param symbol the char the symbol represents
     */
    public Symbol(int x, int y, int value, Terminal.Color backgroundColor, Terminal.Color foregroundColor, char symbol)
    {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.symbol = symbol;
        position = new Coordinates(x, y);
    }
    
    /**
     * Constructor without Background color, uses black as default
     * @param x the x position of the symbol
     * @param y the y position of the symbol
     * @param value the value of the symbol in the properties file
     * @param foregroundColor the foreground color of the symbol
     * @param symbol the char the symbol represents
     */
    public Symbol(int x, int y, int value, Terminal.Color foregroundColor, char symbol)
    {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = Color.BLACK;
        this.symbol = symbol;
        position = new Coordinates(x, y);
    }
    
    public Terminal.Color getBackgroundColor()
    {
        return backgroundColor;
    }
    public Terminal.Color getForegroundColor()
	{
		return foregroundColor;
	}
    
    public char getSymbol()
    {
        return symbol;
    }
    
    public Coordinates getPosition()
    {
        return position;
    }
    @Override
    public String toString()
    {
    	// TODO Auto-generated method stub
    	return symbol+"";
    }
}
