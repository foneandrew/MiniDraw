
/* Code for COMP102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import comp102.*;
import java.awt.Color;
import java.io.*;

/** Rectangle represents a solid rectangle shape
Implements the Shape interface.
Needs fields to record the position, size, and colour*/

public class Rectangle implements Shape {
    //fields
    // YOUR CODE HERE
    double x;
    double y;
    double wd;
    double ht;
    double offX;
    double offY;
    Color col, opCol;
    boolean select = false;

    /** Constructor with explicit values
    Arguments are the x and y of the top left corner,
    the width and height, and the color. */
    public Rectangle(double x1, double y1, double x2, double y2, Color colx) {
        // YOUR CODE HERE
        
        //determine left most point so that can be drawn starting from any corner
        if (x2 < x1) {
            x = x2;
            wd = x1 - x2;
        } else {
            x = x1;
            wd = x2 - x1;
        }
        if (y2 < y1) {
            y = y2;
            ht = y1 - y2;
        } else {
            y = y1;
            ht = y2 - y1;
        }
        changeCol(colx);
    }

    public void changeCol(Color colx){
        //set colour
        col = colx;
        
        //get colour components
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        
        //make new inverted colour
        opCol = new Color(255 - r, 255 - g, 255 - b);
    }

    /** [Completion] Constructor which reads values from a file (scanner)
    The argument is a Scanner that contains the specification of the 
    Rectangle. The next 7 integers specify the position of the top
    left corner, and the width and height, and three ints specifying the 
    color. */
    public Rectangle(Scanner data) {
        //read data from passed scanner
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        Color colx = new Color(red, green, blue);
        changeCol(colx);
        x = data.nextDouble();
        y = data.nextDouble();
        wd = data.nextDouble();
        ht = data.nextDouble();
    }
    
    public void align(double xx, double yy, String side){
        //sets the anchor point to whatever based the side given
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
        //set selection state
        select = x;
    }

    public boolean isSelect(){
        //returns selection state
        return select;
    }

    public void setOff(double xo ,double yo){
        //stores offset value
        offX = x - xo;
        offY = y - yo;
    }

    /** Returns true if the point (u, v) is on top of the shape */
    public boolean on(double u, double v) {
        //determines whether given point lies within points of shape
        
        if (u >= x && u <= x + wd){
            if (v >= y && v <= y + ht){
                Trace.println("shape is on");
                return true;
            }
        }
        Trace.println("shape is not on");
        return false;
    }

    /** Changes the position of the shape*/
    public void move(double dx, double dy) {
        // move shape using offset value so that shape doesnt 'jump'
        x = dx + offX;
        y = dy + offY;
    }

    /** Draws the rectangle on the graphics pane. It draws a black border and
    fills it with the color of the rectangle.
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.*/
    public void redraw() {
        // set colour
        UI.setColor(col);
        //draw shape
        UI.fillRect(x, y, wd, ht, false);
        
        //if selecteed set opposite colour and then draw inside border
        if (select) {
            UI.setColor(opCol);
            UI.drawRect(x + 1, y + 1, wd - 2, ht - 2, false);
            UI.drawRect(x + 2, y + 2, wd - 4, ht - 4, false);
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
        //determins which quadrant the given point is on and returns a number to represent that information, or 0 if not on the shape
        if ( x2 >= x + (wd/2)){
            left = false;
        } else {
            left = true;
        }
        if ( y2 >= y + (ht/2)){
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
        
        //remember orignal values just in case
        double tempX = x;
        double tempY = y;
        double tempX2 = x + wd;
        double tempY2 = y + ht;
        
        //adjust top left anchor if needed
        //checks is size goes to small and if it does replaces new values with the saved ones above
        if (corner == 0){
            //centred at centre of shape
            y = y - (y2/2);
            x = x - (x2/2);
            if (x >= (wd / 2) + tempX  - 5) x = tempX ;
            if (y >= (ht / 2) + tempY  - 5) y = tempY ;
        } else if (corner == 1){
            //top right
            y = y - y2;
            if (y >= tempY2  - 5) y = tempY2 - 5;
        } else if (corner == 2){
            //bottom right

        } else if (corner == 3){
            //bottom left
            x = x - x2;
            if (x >= tempX2  - 5) x = tempX2 - 5;
        } else if (corner == 4){
            //top left
            y = y - y2;
            x = x - x2;
            if (y >= tempY2  - 5) y = tempY2 - 5;
            if (x >= tempX2  - 5) x = tempX2 - 5;
        }
        
        //increase size
        ht = ht + y2;
        wd = wd + x2;
        //make sure never go size below 1
        if (wd < 5) wd = 5;
        if (ht < 5) ht = 5;
        
        
    }

    /** Returns a string description of the rectangle in a form suitable for
    writing to a file in order to reconstruct the rectangle later
    The first word of the string must be Rectangle */
    public String toString() {
        // write all custom data to a string in a format that can later be read by this class
        return ("Rectangle "+col.getRed()+" "+col.getGreen()+" "+col.getBlue() + " " + x + " " + y + " " + wd + " " + ht);
    }

}
