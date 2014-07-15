/* Code for COMP 102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import comp102.*;
import java.awt.Color;
import java.io.*;

/** Polgon represents a polygon made of a sequence of straight lines.
Implements the Shape interface.
Has a field to record the colour of the line and two fields to store
lists of the x coordinates and the y coordinates of all the vertices
 */

public class Polygon implements Shape{
    // YOUR CODE HERE
    ArrayList<String> point = new ArrayList<String>();
    double x;
    double y;
    Color col, opCol;
    boolean select = false;
    double offY, offX;

    //arrays for values
    double xPoint[];
    double yPoint[]; 

    //boxing values
    double yMin, yMax, xMin, xMax;

    public Polygon(double x1, double y1, Color colx){
        point.add("0 0");
        x = x1;
        y = y1;
        changeCol(colx);
    }

    public Polygon(Scanner data){
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        Color colx = new Color(red, green, blue);
        changeCol(colx);
        x = data.nextDouble();
        y = data.nextDouble();
        //reads until end of passed scanner(string)
        while(data.hasNext()){
            point.add("" + data.nextDouble() + " " + data.nextDouble());
        }
    }
    
    public void align(double x, double y, String side){
        //cannot be aligned yet
    }

    public void addPoint(double x1, double y1, boolean drag){
        //if being draged it will overwrite the previous point
        if (drag) {point.remove(point.size() - 1);}
        x1 = x1 - x;
        y1 = y1 - y;
        point.add("" + x1 + " " + y1);
    }

    public void changeCol(Color colx){
        col = colx;
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        opCol = new Color(255 - r, 255 - g, 255 - b);
    }

    public void setOff(double xo ,double yo){
        offX = x - xo;
        offY = y - yo;
    }

    public void select(boolean x){
        select = x;
    }

    public boolean isSelect(){
        return select;
    }

    /** Returns true if the point (x, y) is on top of the shape
     * doesnt work at all sometimes for some reason
     */
    public boolean on(double u, double v){
        //determines if point is in box
        getCo();
        getBox();
        if (u >= xMin + x && u <= x + xMax){
            if (v >= y + yMin && v <= y + yMax){
                Trace.println("shape is on");
                return true;
            }
        }
        Trace.println("shape is not on");
        return false;
    }

    public void getBox(){
        //gets a box that fits exaclty around the polygon becuase i dont know how to triangulate a polygon so this is how i fins if the shape is under a point
        getCo();
        yMin = 0;
        yMax = 0;
        xMin = 0;
        xMax = 0;
        for (int i = 1; i < point.size(); i++){
            if (xPoint[i] > xMax){xMax = xPoint[i];}
            if (xPoint[i] < xMin){xMin = xPoint[i];}
            if (yPoint[i] > yMax){yMax = yPoint[i];}
            if (yPoint[i] < yMin){xMin = yPoint[i];}
        }
    }

    /** Changes the position of the shape
     */
    public void move(double dx, double dy){
        x = dx + offX;
        y = dy + offY;
    }

    public void getCo() {
        //polygon is written as locations referenced from the anchor point. this method writes them as real point locations as they appear on the screen
        String temp;
        xPoint = new double[point.size()];
        yPoint = new double[point.size()];        
        for (int i = 0; i < point.size(); i++){
            temp = point.get(i);
            Scanner read = new Scanner(temp);
            xPoint[i] = read.nextDouble();
            yPoint[i] = read.nextDouble();
        }
    }

    /** Draws the shape on the graphics pane.
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.
     */
    public void redraw(){
        getCo();
        for (int i = 0; i < point.size(); i++){
            xPoint[i] = xPoint[i] + x;
            yPoint[i] = yPoint[i] + y;
        }
        UI.setColor(col);
        UI.fillPolygon(xPoint, yPoint, point.size(), false);
        if (select) {
            UI.setColor(opCol);
            UI.drawPolygon(xPoint, yPoint, point.size(), false);
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
        getBox();
        boolean top, left;
        if ( x2 >= x + ((xMax + xMin) / 2)){
            left = false;
        } else {
            left = true;
        }
        if ( y2 >= y + ((yMax + yMin) / 2)){
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

    /** Changes the width and height of the shape by the
    specified amounts.
    The amounts may be negative, which means that the shape
    should get smaller, at least in that direction.
    The shape should never become smaller than 1 pixel in width or height
    The center of the shape should remain the same.
    
    DOESNT WORK!!!
    and i dont know why and i dont have any more time to spend on it
    
     */
    public void resize(double x2, double y2, int corner){
        //move anchor
        getBox();
        //make sure not too small
        if (xMax - xMin < 5 || yMax - yMin < 5){
            return;
        }
        double xCent = (xMax - xMin) / 2;
        double yCent = (yMax - yMin) / 2;

        //adjust top left anchor if needed
        //if (corner == 0){
        //centred at centre of shape
        for (String s : point){
            Trace.println(s);
        }
        for (int i = 0; i < point.size(); i++){
            if (xPoint[i] <= xCent){
                //left
                xPoint[i] = xPoint[i] - ( x2 * ( ( xCent - xPoint[i] ) / ( xCent - xMin ) ) );
            } else {
                //right
                xPoint[i] = xPoint[i] + ( x2 * ( ( xPoint[i] - xCent ) / ( xMin - xCent ) ) );
            }
            if (yPoint[i] <= yCent){
                //top
                yPoint[i] = yPoint[i] - ( y2 * ( ( yCent - yPoint[i] ) / ( yCent - yMin ) ) );
            } else {
                //bottom
                yPoint[i] = yPoint[i] + ( y2 * ( ( yPoint[i] - yCent ) / ( yMin - yCent ) ) );
            }
            point.set(i, xPoint + " " + yPoint);

        }
        for (String s : point){
            Trace.println(s);
        }
        //} 

        
    }
    /** Returns a string description of the shape in a form suitable for
    writing to a file in order to reconstruct the shape later
     */
    public String toString(){
        String line = "Polygon " + col.getRed() + " " + col.getGreen() + " " + col.getBlue() + " " + x + " " + y;
        for (String p : point){
            line = line + " " + p;
        }
        return line;
    }
}
