/* Code for COMP102 Assignment 10
 * Name:
 * Usercode:
 * ID:
 */

import comp102.*;
import java.awt.Color;


/** The interface for all Shape objects
    Provides four methods 
    
    I HEAVILY MODIFIED THE SHAPE OBJECTS.
    EXPLANATIONS WILL BE GIVEN HERE AND NOT IN THE ACTUAL CLASSES THEMSELVES UNLESS IMPORTANT
    
    THE RECTANGLE CLASS WILL ALSO BE FULLY COMMENTED DUE TO THE SIMILARITY BETWEEN ALL SHAPE OBJECTS AND THE RECTANGLE CLASS
 */

public interface Shape{

  /** Returns true if the point (x, y) is on top of the shape
   */
  public boolean on(double x, double y);

  /**
   * moves shape to new location. shapes determine where to move by figuring it out by being given the location to move to, and the side that should move there.
   */
  public void align(double x, double y, String side);
  
  /** Changes the position of the shape
  */
  public void move(double dx, double dy);
  
  /**
   * sets the state of selection to x
   */
  public void select(boolean x);
  
  /**
   * checks if shape is selected
   */
  public boolean isSelect();
  
  /**
   * changes color and determines the opposite colour for when shape is selected
   */
  public void changeCol(Color colx);

  /** Draws the shape on the graphics pane.
      It uses the drawing methods with the extra last argument of "false"
      so that the shape will not actually appear until the 
      graphics pane is redisplayed later. This gives much smoother redrawing.
  */
  public void redraw();

  /**
   * get offset position so that shapes move around nicely without first reseting position to the mouse
   */
  public void setOff(double xo ,double yo);

  /** Changes the width and height of the shape by the
      specified amounts.
      The amounts may be negative, which means that the shape
      should get smaller, at least in that direction.
      The shape should never become smaller than 1 pixel in width or height
      The center of the shape should remain the same.
      
      ONE CORNER WILL BE ANCHORED WHEN THIS IS HAPPENING UNLESS CORNER = 0 IN WHICH CASE IT WILL BE CENTER ANCHORED.
      CENTER ANCHORING IS NOT PERFECT AS THE SHAPES CAN BE MOVED BY THIS
  */
  public void resize(double changeWd, double changeHt, int corner);

  /**
     * returns an integer with the corner represented by number as indicated
     * below
     *          4 | 1
     *          -----
     *          3 | 2
     */
    public int getCorner(double x2, double y2);

  /** Returns a string description of the shape in a form suitable for
      writing to a file in order to reconstruct the shape later
  */
  public String toString();
      
}
