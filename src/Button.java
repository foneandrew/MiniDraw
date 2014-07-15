import comp102.*;
import java.awt.Color;
/**
 * My own button class that has the ability to highlight which button is currently toggled. AAAAANNNNNDDDD it can change colour too (for the colour button).
 * Its pretty awesome
 */
public class Button
{
    private static int id = -1;
    private int top;
    private String name;
    private boolean selected = false;
    Color col = Color.black;
    Color opCol;
    boolean coloured = false;
    /**
     * New Button
     */
    public Button(String namex)
    {
        id++;
        top = (42 * id) + 10;
        name = namex;
    }

    public void draw(){
        UI.drawImage("button.png", 10, top, false);
        if(coloured) {
            UI.setColor(col);
            UI.fillRect(12, top + 2, 96, 28, false);
            UI.setColor(opCol);
            UI.drawString(name, 20, top + 20, false);
        } else {
            UI.setColor(Color.black);
            UI.drawString(name, 20, top + 20, false);
        }
        if (selected){
            UI.setColor(Color.red);
            UI.drawRect(10, top, 100, 32, false);
            UI.drawRect(11, top + 1, 98, 30, false);
        }

    }

    public void newCol(Color colx){
        col = colx;
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        opCol = new Color(255 - r, 255 - g, 255 - b);
        coloured = true;
    }

    public void select(boolean x){
        selected = x;
    }

    public String name(){
        return name;
    }

    public boolean on(double x, double y){
        if ( x >= 10 && x <= 110) {
            if (y >= top && y <= top + 32){
                Trace.println(name + " is on");
                return true;
            }
        }
        Trace.println(name + " is not on");
        return false;
    }

}
