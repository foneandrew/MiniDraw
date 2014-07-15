/* Code for COMP 102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import java.util.*;
import comp102.*;
import java.awt.Color;
import java.io.*;

/** Tree represents a nice-looking tree with branches.
The base of its trunk is at (x, y), and its size field
governs the size of the tree.
Since each tree is different, in a random way, it has
has a seed, which starts the random number generator in the same
place every time it is drawn.
Implements the Shape interface.
Needs fields, constructors, and all the methods specified in the interface.
 */

// YOUR CODE HERE
public class Tree implements Shape{

    double x;
    double y;
    double offX;
    double offY;
    Color col, opCol;
    Color brown = new Color(139, 69, 19);
    boolean select = false;
    int leaf = 30;

    //arrays for holding point values
    double[][] branchX = new double[3][4];
    double[][] branchY = new double[3][4];
    public Tree (double x1, double y1, double x2, double y2, Color colx){
        x = x1;
        y = y1;
        changeCol(colx);
        branch();

    }

    /**
     * silly named method that infact creates entire tree. first 3 branches and then the 3 branches that come off of them. 
     */
    public void branch(){
        double x1, x2, x3, y1, y2, y3;

        x1 = 25 * Math.random() - 25;
        x2 = 25 * Math.random() - (25 / 2);
        x3 = 25 * Math.random();
        
        //the point of the trigonometry here is to make all branches equal in size and so tree should have a rounded top
        branchY[0][0] = (0 - Math.sqrt((25 * 25) - (x1 * x1))) - 50;
        branchY[1][0] = (0 - Math.sqrt((25 * 25) - (x2 * x2))) - 50;
        branchY[2][0] = (0 - Math.sqrt((25 * 25) - (x3 * x3))) - 50;

        branchX[0][0] = x1;
        branchX[1][0] = x2;
        branchX[2][0] = x3;
        
        //gets branches of branches
        for (int a = 0; a < 3; a++){
            x1 = 50 * Math.random() - 50;
            x2 = 50 * Math.random() - 25;
            x3 = 50 * Math.random();

            branchY[a][1] = (0 - Math.sqrt((50 * 50) - (x1 * x1))) + branchY[a][0];
            branchY[a][2] = (0 - Math.sqrt((50 * 50) - (x2 * x2))) + branchY[a][0];
            branchY[a][3] = (0 - Math.sqrt((50 * 50) - (x3 * x3))) + branchY[a][0];

            branchX[a][1] = x1 + branchX[a][0];
            branchX[a][2] = x2 + branchX[a][0];
            branchX[a][3] = x3 + branchX[a][0];
        }

        //--------------------------------

    }

    public void changeCol(Color colx){
        col = colx;
        int r = colx.getRed();
        int g = colx.getGreen();
        int b = colx.getBlue();
        opCol = new Color(255 - r, 255 - g, 255 - b);
    }

    public Tree (Scanner data) {
        int red = data.nextInt();
        int green = data.nextInt();
        int blue = data.nextInt();
        Color colx = new Color(red, green, blue);
        changeCol(colx);
        x = data.nextDouble();
        y = data.nextDouble();
        for (int a = 0; a < 3; a++){
            for (int b = 0; b < 4; b++){
                branchX[a][b] = data.nextDouble();
                branchY[a][b] = data.nextDouble();
            }
        }
    }

    /**
     * tree will only align to the bottom.
     */
    public void align(double xx, double yy, String side){
        if (side.equals("bot")){
            y = yy;
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
     * uses only the leaves (circles on top). this is done by translating the values here into the ones that the oval class uses so that I can just reuse that code here with no modification
     */
    public boolean on(double u, double v){
        
        double x1, y1;
        double wd = leaf;
        double ht = leaf;
        u = u - wd;
        v = v - ht;
        double rx = wd / 2;
        double ry = ht / 2;
        double ans;
        for (int i = 0; i < 3; i++){
            for (int a = 1; a < 4; a++){
                x1 = x+branchX[i][a] - (leaf / 2);
                y1 = y+branchY[i][a] - (leaf / 2);

                ans = ans = (((u - x1 + rx)*(u - x1 + rx)) / (rx * rx)) + (((v - y1 + ry)*(v - y1 + ry)) / (ry * ry));

                Trace.println("leaf " + ans);
                if ( ans <= 1){
                    return true;
                }
            }
        }

        return false;
    }

    /** Changes the position of the shape*/
    public void move(double dx, double dy) {
        // YOUR CODE HERE
        x = dx + offX;
        y = dy + offY;
    }

    /** Draws the shape on the graphics pane.
    It uses the drawing methods with the extra last argument of "false"
    so that the shape will not actually appear until the 
    graphics pane is redisplayed later. This gives much smoother redrawing.
     */
    public void redraw(){

        UI.setColor(brown);
        UI.drawLine(x, y, x, y - 20, false);
        for (int i = 0; i < 3; i++){
            UI.drawLine(x, y - 20, x + branchX[i][0], y + branchY[i][0], false);
            for (int a = 1; a < 4; a++){
                UI.drawLine(x+branchX[i][0], y+branchY[i][0], x+branchX[i][a], y+branchY[i][a], false);
            }
        }
        
        //draw leaves
        UI.setColor(col);
        for (int i = 0; i < 3; i++){
            for (int a = 1; a < 4; a++){
                UI.fillOval(x+branchX[i][a] - (leaf / 2), y+branchY[i][a] - (leaf / 2), leaf, leaf, false);
            }
        }
        if (select) {
            UI.setColor(opCol);
            for (int i = 0; i < 3; i++){
                for (int a = 1; a < 4; a++){
                    UI.drawOval(x + branchX[i][a] - (leaf / 2) + 2, y + branchY[i][a] - (leaf / 2) + 2, leaf - 4, leaf - 4, false);
                }
            }
        }
    }

    /**
     * cannot be resized
     */
    public int getCorner(double x2, double y2){
        return 0;
    }

    /** 
    cannot be resized
     */
    public void resize(double changeWd, double changeHt, int corner){

    }

    /** Returns a string description of the shape in a form suitable for
    writing to a file in order to reconstruct the shape later
     */
    public String toString(){
        String line;
        line = "Tree " + col.getRed() + " " + col.getGreen() + " " + col.getBlue() + " " + x + " " + y;
        for (int a = 0; a < 3; a++){
            for (int b = 0; b < 4; b++){
                line = line + " " + branchX[a][b] + " " + branchY[a][b];
            }
        }
        return line;
    }
}