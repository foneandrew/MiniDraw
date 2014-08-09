/* Code for COMP102 Assignment 10
 * Name:    Andrew Fone
 * Usercode:    foneandr
 * ID:  300224127
 */

import comp102.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.swing.JColorChooser;

/** The MiniDraw program allows the user to create, save, and reload files
specifying drawings consisting of a list of simple shapes.
The program allows the user to
- add a new shape to the drawing
- remove a shape from the drawing
- move a shape to a different position
- set the colour for the next shape
- save the current drawing to a file
- load a previous drawing from a file.
The shapes include lines, rectangles, ovals, and dots

Classes
The MiniDraw class handles all the user interaction:
buttons, mouse actions, file opening and closing.
It stores the current drawing in an array of Shape .

The Shape interface specifies the Shape type
The shape classes all implement Shape and represent different kinds of shapes.

Files:
A drawing is stored in a file containing one line for each shape,
Each line has the name of the type of shape,
followed by a specification of the shape,
including the position (x and y) and the
colour (three integers for red, blue, and green).
The other values will depend on the shape.

User Interface:
There are buttons for dealing with the whole drawing (New, Load, Save),
buttons for specifying the next shape to draw, and
buttons for moving and removing shapes, and setting the color.
 */

public class MiniDraw implements UIButtonListener, UIMouseListener{

    // Fields
    private ArrayList<Shape> shapes = new ArrayList<Shape>();    // the collection of shapes

    // suggested fields to store mouse positions, current action, current shape, current colour, etc

    private double pressedX;                 //where the mouse was pressed
    private double pressedY;  
    private String currentAction = "Line";   // current action ("Move", etc) or shape ("Line" etc)
    private Shape currentShape = null;      //  the current shape (or null if no shape)
    private Color currentColor = Color.black;
    private Polygon currentPolygon;          // The polygon (if any) that is currently being constructed.

    //UI stuff
    private int uiW = 120;
    private ArrayList<Button> uiB = new ArrayList<Button>();
    private boolean uiPane = false;
    private boolean saved = true;

    //currently draged shape
    private Shape drag = null;
    private boolean dragged = false;

    //resizing
    private int corner = 0;

    /** Constructor sets up the GUI:
     *        sets the mouse listener and adds all the buttons
     */
    public MiniDraw(){
        // start listeners
        UI.setMouseListener(this);
        UI.setMouseMotionListener(this);

        //add UI buttons
        UI.addButton("Line", this);
        UI.addButton("Rectangle", this);
        UI.addButton("Polygon", this);
        UI.addButton("Dot", this);
        UI.addButton("Oval", this);
        UI.addButton("Tree", this);
        UI.addButton("New", this);
        UI.addButton("Save", this);
        UI.addButton("Load", this);

        //add my awesome toggle buttons
        uiB.add(new Button("Select"));
        uiB.add(new Button("Move"));
        uiB.add(new Button("Resize"));
        uiB.add(new Button("Delete"));
        uiB.add(new Button("Colour"));
        //set colour button colour
        int colourButton = uiB.size();
        uiB.get(colourButton - 1).newCol(currentColor);
        uiB.add(new Button("Shift Up"));
        uiB.add(new Button("Shift Down"));
        uiB.add(new Button("Align Top"));
        uiB.add(new Button("Align Bottom"));
        uiB.add(new Button("Align Left"));
        uiB.add(new Button("Align Right"));

        drawDrawing();
    }

    /** Respond to button presses
     * For New, Open, Save, and Color, call the appropriate method (see below)
     *  to perform the action immediately.
     * For other buttons, store the button name in the currentAction field
     */
    public void buttonPerformed(String button){
        // YOUR CODE HERE
        if (button.equals("Line")){
            currentAction = button;
        } else if (button.equals("Rectangle")){
            currentAction = button;
        } else if (button.equals("Polygon")){
            currentAction = button;
        } else if (button.equals("Dot")){
            currentAction = button;
        } else if (button.equals("Oval")){
            currentAction = button;
        } else if (button.equals("Tree")){
            currentAction = button;
        } else if (button.equals("New")){
            if (saved) {
                newD();
            } else {
                save();
                newD();
            }
        } else if (button.equals("Save")){

            save();
        } else if (button.equals("Load")){
            if (saved) {
                load();
            } else {
                save();
                newD();
                load();
            }
        }
        //deselect objects
        deselect();
        //deselect pane buttons
        for (Button z : uiB){
            z.select(false);
        }
        //save polygon (if any)
        if (currentPolygon != null){
            shapes.add(currentPolygon);
        }
        currentPolygon = null;
        drawDrawing();
    }

    /**
     * new drawing
     */
    public void newD(){
        shapes =  new ArrayList<Shape>();
        currentShape = null;
        currentPolygon = null;
        drag = null;
        dragged = false;
    }

    /**
     * save drawing to file
     */
    public void save(){
        if (currentPolygon != null){
            shapes.add(currentPolygon);
        }
        currentPolygon = null;
        String dest = UIFileChooser.save();
        if (dest == null) { return; }
        try {
            PrintStream out = new PrintStream(new File(dest));
            for (Shape s : shapes){
                out.println(s.toString());
            }
        } catch (IOException e) {Trace.println("ERROR: " + e);}
        //set save state
        saved = true;
    }

    /**
     * load image from file
     */
    public void load(){

        boolean cancel;
        //needed for polygons and trees
        String poly;
        Scanner polyScan;

        try {
            String open = UIFileChooser.open();
            if (open == null){return;}
            
            //reset drawing
            newD();
            Scanner scan = new Scanner(new File(open));
            String type;
            while (scan.hasNext()){
                type = scan.next();
                cancel = false;
                if (type.equals("Line")){
                    shapes.add(new Line(scan));
                } else if (type.equals("Rectangle")){
                    shapes.add(new Rectangle(scan));
                } else if (type.equals("Dot")){
                    shapes.add(new Dot(scan));
                } else if (type.equals("Oval")){
                    shapes.add(new Oval(scan));
                } else if (type.equals("Polygon")){
                    
                    // polygons and trees require a seperate scanner as a whole line must be passed
                    
                    cancel = true;
                    poly = scan.nextLine();
                    polyScan = new Scanner(poly);
                    shapes.add(new Polygon(polyScan));
                } else if (type.equals("Tree")){
                    cancel = true;
                    poly = scan.nextLine();
                    polyScan = new Scanner(poly);
                    shapes.add(new Tree(polyScan));
                }
                if (!cancel){ scan.nextLine();}
            }
            //set save state
            saved = true;
        } catch (IOException e) {Trace.println("ERROR: " + e);}
    }

    // Respond to mouse events 

    /** When mouse is pressed, remember the position in fields
    and also find the shape it is on (if any), and store
    the shape in a field (use the findShape(..) method)
     *  When the Mouse is released, depending on the currentAction,
     *  - perform the action (move, delete, or resize).
     *    move and resize are done on the shape  where the mouse was pressed,
     *    delete is done on the shape where the mouse was released 
     *  - construct the shape and add to the shapes array.
     *    (though the polygon is more complicated)
     *  It is easiest to call other methods (see below) to actually do the work,
     *  otherwise this method gets too big!
     *  
     *  yeah..... i didnt really think about making a whole bunch of methods for some reason... so this is a total mess. If this was for realsies I would definately go back through and fix this up
     */
    public void mousePerformed(String mouseAction, double x, double y) {
        boolean cancel= false;
        if (mouseAction.equals("pressed")){
            
            
            //if mouse is in the drawing area
            if (x > uiW) {
                pressedX = x;
                pressedY = y;
                dragged = true;

                //not in togglebutton pane
                uiPane = false; 
                
                //are we dealing with a polygon??
                if (currentAction.equals("Polygon") && currentPolygon == null){
                    currentPolygon = new Polygon(x, y, currentColor);
                } else if (currentAction.equals("Polygon")){
                    currentPolygon.addPoint(x, y, true);
                }

                //gets shape (if any)
                currentShape = findShape(x, y);

                //is a tool currently selected??
                if (currentAction.equals("Move") && currentShape != null){
                    //move tool. determine whether we are moving selected shapes or moving just the shape under the mouse
                    
                    //if shape under mouse is not selected, unselect everything
                    if (!currentShape.isSelect()){
                        deselect();
                    }
                    moveShape(x, y, true);

                } else if (currentAction.equals("Delete") && currentShape != null){
                    //nothing
                } else if (currentAction.equals("Select")){
                    //find shapes and add them or remove them to the selection.
                    
                    //deselect all shapes if no shape clicked on
                    if (currentShape == null) {
                        deselect();
                        drawDrawing();
                        return;
                    }
                    //otherwise add the shape to the selection or remove if already selected
                    currentShape.select(!currentShape.isSelect());
                } else if (currentAction.equals("Resize")){
                    //resize, need to first set offset
                    
                    //sets new offset position used to calculate resize
                    resizeShape(x, y, true);
                }
            } else {
                
                //mouse in toggle button area
                uiPane = true;
            }
            
            //redisplay
            drawDrawing();

        } else if (mouseAction.equals("released")){
            
            
            if (!uiPane && (x > uiW || dragged)) {
                //mouse in drawing area
                
                
                if (currentAction.equals("Move") && currentShape != null){
                    //move the shape
                    
                    moveShape(x, y, false);
                    saved = false;
                } else if (currentAction.equals("Delete") && currentShape != null){
                    //delete shape
                    
                    //removes shape that was clicked on
                    deleteShape(currentShape);
                    saved = false;
                } else if (currentAction.equals("Select") && currentShape != null){
                    //nothing
                } else if (currentAction.equals("Resize") && currentShape != null){
                    //resize shape
                    
                    resizeShape(x, y, false);
                    saved = false;
                } else if (currentAction.equals("Polygon") && currentPolygon != null) {
                    //add final point to polygon
                    currentPolygon.addPoint(x, y, false);
                    drag = null;
                    dragged = false;
                    drawDrawing();
                    return;
                } else if (currentAction.equals("Tree")) {
                    //create new tree
                    shapes.add(new Tree(x, y, 0, 0, currentColor));
                    saved = false;
                } else if (currentAction.equals("Align Top")){
                    //align all selected shapes
                    
                    align(x, y, "top");
                } else if (currentAction.equals("Align Bottom")){
                    align(x, y, "bot");
                } else if (currentAction.equals("Align Left")){
                    align(x, y, "lef");
                } else if (currentAction.equals("Align Right")){
                    align(x, y, "rig");
                } 

                //remove temporary dragged shape
                drag = null;
                //add new shape (method will determine if and what shape needs to be created)
                addShape(pressedX, pressedY, x, y, false);

            } else {
                //user is pressing a button in the toggle button area
                
                
                for (Button b : uiB){
                    //checks if button is being pressed
                    
                    
                    if (b.on(x, y)){
                        //found a button!
                        
                        //save polygon (if any)
                        if (currentAction.equals("Polygon") && currentPolygon != null){
                            shapes.add(currentPolygon);
                        }
                        currentPolygon = null;
                        
                        //get name of button
                        String but = b.name();
                        
                        //if colour button is pressed
                        if (but.equals("Colour")){
                            //change colour
                            currentColor = JColorChooser.showDialog(null, "Pick a colour", this.currentColor);
                            b.newCol(currentColor);

                            //if shapes are selected they will all be changed to this color straight away
                            for (Shape s : shapes){
                                if (s.isSelect()){
                                    s.changeCol(currentColor);
                                }
                            }

                        } else if (but.equals("Shift Up")){
                            //move the shape up in the order
                            
                            int count = 0;
                            int current = 0;
                            
                            //find selected shape. must haave a single shape selected else wont do anything
                            for (Shape s : shapes){
                                if (s.isSelect()){
                                    count++;
                                    current = shapes.indexOf(s);
                                }
                            }
                            if (count != 1){break;}
                            
                            //make sure not at end of array
                            if (current == shapes.size() - 1){break;}
                            
                            //swap shapes
                            Shape temp = shapes.get(current);
                            shapes.set(current, shapes.get(current + 1));
                            shapes.set(current + 1, temp);
                        } else if (but.equals("Shift Down")){
                            //move the shape down in the order
                            
                            int count = 0;
                            int current = 0;
                            
                            //find selected shape. must haave a single shape selected else wont do anything
                            for (Shape s : shapes){
                                if (s.isSelect()){
                                    count++;
                                    current = shapes.indexOf(s);
                                }
                            }
                            if (count != 1){break;}
                            
                            //make sure not at end of array
                            if (current == 0){break;}
                            
                            //swap shapes
                            Shape temp = shapes.get(current);
                            shapes.set(current, shapes.get(current - 1));
                            shapes.set(current - 1, temp);
                        } else {
                            //if other button pressed set new action
                            
                            currentAction = but;
                            if (but.equals("Select")){
                                //select is pressed so deselect everything
                                deselect();
                            }
                            
                            //manage highighting of my toggle buttons to display which on is currently toggled:
                            //deselect all buttons
                            for (Button z : uiB){
                                z.select(false);
                            }
                            //select current button
                            b.select(true);
                        }
                        if (but.equals("Delete")){
                            //deletes any selected shapes
                            deleteShape(null);
                        }

                    }
                }

            }
            dragged = false;
            drawDrawing();
        } else if (mouseAction.equals("dragged")){
            
            //mouse on drawing area:
            if (!uiPane){
                //draw shapes but passing argument to ensure shape is not permanently saved (rubberbanding)
                if (currentAction.equals("Move") && currentShape != null){
                    moveShape(x, y, false);
                } else if (currentAction.equals("Delete") && currentShape != null){
                    //do nothing
                } else if (currentAction.equals("Select") && currentShape != null){
                    //do nothing
                } else if (currentAction.equals("Resize") && currentShape != null){
                    resizeShape(x, y, false);
                    pressedX = x;
                    pressedY = y;
                } else if (currentAction.equals("Polygon")){
                    currentPolygon.addPoint(x, y, true);
                } else {
                    //adds temp shape for 'rubberbanding' effect
                    addShape(pressedX, pressedY, x, y, true);            

                }

                drawDrawing();

            }
        }
    }
    
    /**
     * align all selected shapes based on the type of string argument
     */
    public void align(double x, double y, String side){
        for (Shape s : shapes){
            if (s.isSelect()){
                s.align(x, y, side);
            }
        }
    }

    /**
     * deselect all shapes
     */
    public void deselect(){
        for ( Shape s : shapes){
            //deselect all
            s.select(false);
        }
    }

    /** Draws all the shapes in the list on the graphics pane
    First clears the graphics pane, then draws each shape,
    Finally repaints the graphics pane
     */
    public void drawDrawing(){
        //wipe graphics
        UI.clearGraphics(false);

        //draw all shapes
        for (Shape s : shapes){
            s.redraw();
        }

        //draw any shapes currently being drawn (rubberbanding)

        if (currentPolygon != null){
            currentPolygon.redraw();
        } else if (drag != null){
            drag.redraw();
        }

        //draw my toggle button pane
        UI.setColor(Color.white);
        UI.fillRect(0,0,uiW,5000, false);
        UI.setColor(Color.black);
        UI.drawLine(uiW,0,uiW,5000, false);
        UI.drawLine(0,765,uiW,765, false);

        //draw all the toggle buttons
        for (Button b : uiB) {
            b.draw();
        }

        //push to screen
        UI.repaintGraphics();
    }   

    /** Checks each shape in the list to see if the point (x,y) is on the shape.
    It returns the topmost shape for which this is true.
    Returns null if there is no such shape.
     */
    public Shape findShape(double x, double y){
        Shape s;
        Trace.println("shapes " + shapes.size());

        //works through arraylist of shapes backwards (in order to select first shapes that are on top)
        for (int i = shapes.size() - 1; i >= 0; i = i - 1){
            Trace.println("checking shape " + i);

            s = shapes.get(i);
            //returns the shape it finds if any
            if (s.on(x, y)) return s;
        }
        // no shape found return null
        return null;  
    }
    
    /** Construct a new Shape object of the appropriate kind
    (depending on currentAction) using the appropriate constructor
    of the Line, Rectangle, Oval, or Dot classes.
    Adds the shape to the end of the collection of shapes in the drawing, and
    Re-draws the drawing */
    public void addShape(double x1, double y1, double x2, double y2, boolean dragged){
        Trace.printf("Drawing shape %s, at (%.2f, %.2f)-(%.2f, %.2f)\n",
            this.currentAction, x1, y1, x2, y2);  //for debugging
        Shape s = null;

        //based on what the current action is, will create new object of right type
        if (currentAction.equals("Line")){
            s = new Line(x1, y1, x2, y2, currentColor);
        } else if (currentAction.equals("Rectangle")){
            s = new Rectangle(x1, y1, x2, y2, currentColor);
        } else if (currentAction.equals("Polygon")){

        } else if (currentAction.equals("Dot")){
            //cannot be drawn before mouse released
            if (!dragged) s = new Dot(x2, y2, currentColor);
        } else if (currentAction.equals("Oval")){
            s = new Oval(x1, y1, x2, y2, currentColor);
        }

        //failsafe in case no shape is created
        if (s == null) {
            return;
        }

        //set save state
        saved = false;
        if (dragged){
            //adds to temp shape if dragged
            drag = s;
        } else {
            //adds to shape array if not dragged
            shapes.add(s);
        }
    }

    /** Moves the current shape (if there is one)
    to where the mouse was released.
     */
    public void moveShape(double x, double y, boolean first){
        Trace.printf("Moving shape to (%.2f, %.2f)\n", x, y);  //for debugging
        // YOUR CODE HERE
        boolean cancel = false;
        //if multiple shaped selected move them all (by searching array first for any selected shapes, if found a flag is set)
        for (Shape s : shapes){
            if (s.isSelect()){
                cancel = true;
                if (first){
                    //get the offset of mouse to shapes
                    s.setOff(x, y); 
                } else {
                    s.move(x,y);
                }
            }
        }

        if (!cancel){
            if (first){
                //if only one shape selected then move it
                currentShape.setOff(x, y); 
            } else {
                currentShape.move(x,y);
            }
        }
    } 

    /** 
     * deletes the passed shape or deletes all selected shapes if no shape is given
     */
    public void deleteShape(Shape target){

        // YOUR CODE HERE
        if (target == null){
            //if there are selected objects pressing delete will delete them straight away
            for (int i = shapes.size() - 1; i >= 0; i = i - 1){
                if (shapes.get(i).isSelect()){
                    shapes.remove(i);
                }
            }
        } else {
            shapes.remove(target);
        }
    }

    // METHODS FOR THE COMPLETION PART
    /** Resizes the current shape. A simple way of doing it is to
    resize the shape by the amount that the mouse was moved
    (ie from (fromX, fromY) to (toX, toY)). 
    If the mouse is moved to the right, the shape should
    be made that much wider on each side; if the mouse is moved to
    the left, the shape should be made that much narrower on each side
    If the mouse is moved up, the shape should be made
    that much higher top and bottom; if the mouse is moved down, the shape 
    should be made that much shorter top and bottom.
    The effect is that if the user drags from the top right corner of
    the shape, the shape should be resized to whereever the dragged to.
    
    polygons and trees dont support this
    
     */
    public void resizeShape(double x, double y, boolean first){
        Trace.printf("Changing size of shape by (%.2f, %.2f) \n", x, y);  //for debugging
        // YOUR CODE HERE
        boolean cancel = false;

        //get corner that is being clicked in order to determine anchor point
        if (first) {
            corner = currentShape.getCorner(x, y);
            return;
        }
        
        //get distance moved
        x = x - pressedX;
        y = y - pressedY;
        
        //modify based on corner pressed
        if (corner == 1){
            //top right
            y = 0 - y;
        } else if (corner == 2){
            //bottom right
        } else if (corner == 3){
            //bottom left
            x = 0 - x;
        } else if (corner == 4){
            //top left
            y = 0 - y;
            x = 0 - x;
        }

        //pass to shapes
        
        //if many shapes selected use centre anchor resize
        for (Shape s : shapes){
            if (s.isSelect()){
                cancel = true;
                s.resize(x, y, 0);
            }
        }

        //else just use opposite corner weighted resize on single shape
        if (!cancel){
            currentShape.resize(x, y, corner);
        }
    }

    public static void main(String args[]){
        new MiniDraw();
    }

}

