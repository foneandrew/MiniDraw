/* Code for COMP102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.util.*;
import java.awt.Color;
import java.io.*;

/** Dot represents a small circle shape of a fixed size (5 pixels)
Implements the Shape interface.
Needs fields to record the position, and colour

SAME AS OVAL EXCEPT THE SIZE (WD, HT) ARE CONSTANT GIVEN BY CLASS DEFINITION

 */
public class Dot implements Shape{
    // YOUR CODE HERE

    double x;
    double y;
    double wd = 10;
    double ht = 10;
    double offX;
    double offY;
    Color col, opCol;
    boolean select = false;
    
    public Dot(double x1, double y1, Color colx) {
        x = x1;
        y = y1;
        changeCol(colx);
    }
    
    public void changeCol(Color colx){
        col = colx;
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        opCol = new Color(255 - r, 255 - g, 255 - b);
    }
    
    public Dot(Scanner data) {
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        Color colx = new Color(red, green, blue);
        changeCol(colx);
        x = data.nextDouble();
        y = data.nextDouble();
    }    
    
    public void align(double xx, double yy, String side){
        if (side.equals("top")){
            y = yy;
        } else if (side.equals("bot")){
            y = yy - ht;
        } else if (side.equals("lef")){
            x = xx;
        } else if (side.equals("rig")){
            x = xx - wd;
        }
    }
    
    public void select(boolean x){
        select = x;
    }
    
    public boolean isSelect(){
        return select;
    }
    

    public void setOff(double xo ,double yo){

        offX = x - xo;
        offY = y - yo;

    }

    /** Returns true if the point (x, y) is on top of the shape
     */
    public boolean on(double u, double v){
        u = u - wd;
        v = v - ht;
        double rx = wd / 2;
        double ry = ht / 2;
        
        double ans = (((u - x + rx)*(u - x + rx)) / (rx * rx)) + (((v - y + ry)*(v - y + ry)) / (ry * ry));
        Trace.println("dot " + ans);
        if ( ans <= 1){
            return true;
        }
        return false;
            
        
    }

    /** Changes the position of the shape*/
    public void move(double dx, double dy) {
        x = dx + offX;
        y = dy + offY;
    }

    /** Draws the shape on the graphics pane.
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.
     */
    public void redraw(){
        UI.setColor(col);
        UI.fillOval(x, y, wd, ht, false);
        if (select) {
            UI.setColor(opCol);
            UI.drawOval(x + 1, y + 1, wd - 2, ht - 2, false); 
            UI.drawOval(x + 2, y + 2, wd - 4, ht - 4, false); 
        }
    }
    
    /**
     * returns an integer with the corner represented by number as indicated
     * below
     *          4 | 1
     *          -----
     *          3 | 2
     */
    public int getCorner(double x2, double y2){
        boolean top, left;
        if ( x2 >= x + 5){
            left = false;
        } else {
            left = true;
        }
        if ( y2 >= y + 5){
            top = false;
        } else {
            top = true;
        }
        if (top && left) return 4;
        if (top && !left) return 1;
        if (!top && left) return 3;
        if (!top && !left) return 2;
        return 0;
    }
    
    /** [Completion] Changes the width and height of the shape by the
    specified amounts.
    The amounts may be negative, which means that the shape
    should get smaller, at least in that direction.
    The shape should never become smaller than 1 pixel in width or height
    The center of the shape should remain the same.*/
    public void resize (double x2, double y2, int corner) {
        // YOUR CODE HERE
        //cannot be resized so does nothing
        
    }

    /** Returns a string description of the shape in a form suitable for
    writing to a file in order to reconstruct the shape later
     */
    public String toString(){
        return ("Dot "+col.getRed()+" "+col.getGreen()+" "+col.getBlue() + " " + x + " " + y);
    }
}