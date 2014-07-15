/* Code for COMP 102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import comp102.*;
import java.awt.Color;
import java.io.*;

/** Line represents a straight line.
Implements the Shape interface.
Needs fields to record the two ends of the line and the colour of the line.
May have additional fields if you want.

I DONT LIKE HAVING THIS PREWRITTEN CLASS CRAMPING MY STYLE WHEN CODING EVERYTHING ELSE SO I CHANGED IT.

 */

public class Line implements Shape{
    //fields
    private double x1;  // one end
    private double y1;
    private double x2;  // the other end
    private double y2;
    private double offX;
    private double offY;
    private Color col, opCol;  // the colour of the line
    private boolean select;
    private boolean end1 = true;
    private boolean left1 = true;

    /** Constructor with explicit values
    Arguments are the position (x1,y1) of one end and the position (x2,y2) of the other end,
    and the color. */
    public Line (double x1, double y1, double x2, double y2, Color colx){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        changeCol(colx);
    }

    public void changeCol(Color colx){
        col = colx;
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        opCol = new Color(255 - r, 255 - g, 255 - b);
    }
    
    public void align(double xx, double yy, String side){
        if (side.equals("top")){
            if (y1 < y2){
                y2 = y2 + (yy - y1);
                y1 = yy;
            } else {
                y1 = y1 + (yy - y2);
                y2 = yy;
            }
        } else if (side.equals("bot")){
            if (y1 < y2){
                y1 = y1 + (yy - y2);
                y2 = yy;
            } else {
                y2 = y2 + (yy - y1);
                y1 = yy;
            }
        } else if (side.equals("lef")){
            if (x1 < x2){
                x2 = x2 + (xx - x1);
                x1 = xx;
            } else {
                x1 = x1 + (xx - x2);
                x2 = xx;
            }
        } else if (side.equals("rig")){
            if (x1 < x2){
                x1 = x1 + (xx - x2);
                x2 = xx;
            } else {
                x2 = x2 + (xx - x1);
                x1 = xx;
            }
        }
    }

    /** Constructor which reads values from a file (scanner)
    The argument is a Scanner that contains the specification of the line.
    The next 7 integers should be the x and y of the first end
    the x and y of the other end, and three integers that specify the color.
     */
    public Line (Scanner data){
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        Color colx = new Color(red, green, blue);
        changeCol(colx);
        this.x1 = data.nextDouble();
        this.y1 = data.nextDouble();
        this.x2 = data.nextDouble();
        this.y2 = data.nextDouble();
    }

    public void setOff(double xo ,double yo){

        offX = x1 - xo;
        offY = y1 - yo;

    }

    /** Returns true if the point (u, v) is on top of the shape
     * The geometry of this is more tricky than for rectangles
     * or dots!
     */
    public boolean on(double u, double v){
        double threshold = 3;
        // first check if it is past the ends of the line...
        if (u < Math.min(this.x1,this.x2)-threshold ||     
        u > Math.max(this.x1,this.x2)+threshold ||
        v < Math.min(this.y1,this.y2)-threshold ||
        v > Math.max(this.y1,this.y2)+threshold) {
            return false;
        }
        // then check the distance from the point to the line
        double wd = this.x2-this.x1;
        double ht = this.y2-this.y1;
        return (Math.abs(((v-this.y1)*wd - (u-this.x1)*ht)/Math.hypot(wd, ht)) <= threshold);
        // distance of a point from a line, from linear algebra
    }

    public void select(boolean x){
        select = x;
    }

    public boolean isSelect(){
        return select;
    }

    /** Changes the position of the shape*/
    public void move(double dx, double dy) {
        // YOUR CODE HERE
        
        x2 = (x2 - x1) + dx + offX;
        y2 = (y2 - y1) + dy + offY;
        
        
        x1 = dx + offX;
        y1 = dy + offY;

    }

    /** Draws the line on the graphics pane. 
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.
     */
    public void redraw(){
        if (select) {
            UI.setColor(opCol);
            UI.drawRect(x1 - 1, y1 - 1, 3, 3, false);
            UI.drawRect(x2 - 1, y2 - 1, 3, 3, false);
        }

        UI.setColor(this.col);
        UI.drawLine(this.x1, this.y1, this.x2, this.y2, false);

    }

    /**
     * returns an integer with the corner represented by number as indicated
     * below
     *          4 | 1
     *          -----
     *          3 | 2
     */
    public int getCorner(double x3, double y3){
        double middleX, middleY;
        if (x1 < x2){
            middleX = ((x2 - x1) / 2) + x1;
        } else {
            middleX = ((x1 - x2) / 2) + x2;
        }
        if (y1 < y2){
            middleY = ((y1 - y2) / 2) + y1;
        } else {
            middleY = ((y2 - y1) / 2) + y2;
        }

        boolean top, left;
        if ( x3 >= middleX){
            left = false;
        } else {
            left = true;
        }
        if ( y3 >= middleY){
            top = false;
        } else {
            top = true;
        }

        end1 = Math.sqrt(((x1 - x3) * (x1 - x3)) + ((y1 - y3) * (y1 - y3))) <= Math.sqrt(((x2 - x3) * (x2 - x3)) + ((y2 - y3) * (y2 - y3)));
        left1 = x1 < x2;
        if (top && left) return 4;
        if (top && !left) return 1;
        if (!top && left) return 3;
        if (!top && !left) return 2;
        return 0;
    }

    /** Changes the width and height of the shape by the
    specified amounts.
    The amounts may be negative, which means that the shape
    should get smaller, at least in that direction.
    The shape should never become smaller than 1 pixel in width or height
    The center of the shape should remain the same.
     */
    public void resize(double changeWd, double changeHt, int corner){

        if (corner == 0){
            if (this.x1 < this.x2){
                this.x1 = this.x1 - changeWd/2;
                this.x2 = this.x2 + changeWd/2;
            }
            else{
                this.x1 = this.x1 + changeWd/2;
                this.x2 = this.x2 - changeWd/2;
            }
            if (this.y1 < this.y2){
                this.y1 = this.y1 - changeHt/2;
                this.y2 = this.y2 + changeHt/2;
            }
            else{
                this.y1 = this.y1 + changeHt/2;
                this.y2 = this.y2 - changeHt/2;
            }
        } else if (left1) {
            if (end1){
                x1 = x1 - changeWd;
                y1 = y1 + changeHt;
            } else if (!end1){
                x2 = x2 + changeWd;
                y2 = y2 + changeHt;
            }
        } else if (!left1) {
            if (end1){
                x1 = x1 + changeWd;
                y1 = y1 + changeHt;
            } else if (!end1){
                x2 = x2 - changeWd;
                y2 = y2 + changeHt;
            }
        }

    }

    /** Returns a string description of the line in a form suitable for
    writing to a file in order to reconstruct the line later */
    public String toString(){
        return ("Line "+col.getRed()+" "+col.getGreen()+" "+col.getBlue()+" "+this.x1+" "+this.y1+" "+this.x2+" "+this.y2);
    }

}
