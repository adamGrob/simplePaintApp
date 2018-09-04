package com.codecool.simplePaint;

import com.codecool.simplePaint.controller.LinePositionController;
import com.codecool.simplePaint.model.Line;
import com.codecool.simplePaint.model.Position;
import com.sun.xml.internal.bind.v2.TODO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SimplePaintPanel extends JPanel
        implements MouseListener, MouseMotionListener, ComponentListener {

    private List<Line> lineList = new ArrayList<Line>();
    private LinePositionController linePositionController = new LinePositionController();

    /**
     * Some constants to represent the color selected by the user.
     */
    private final static int BLACK = 0,
            RED = 1,
            GREEN = 2,
            BLUE = 3,
            CYAN = 4,
            MAGENTA = 5,
            YELLOW = 6;

    private int currentColor = BLACK;  // The currently selected drawing color,
    //   coded as one of the above constants.


      /* The following variables are used when the user is sketching a
       curve while dragging a mouse. */

    private int prevX, prevY;     // The previous location of the mouse.

    private int startX, startY;  // The location of the mouse when the use first clicked at a drawing event(for straight lines)

    private int endX, endY;

    private boolean dragging;      // This is set to true while the user is drawing.

    private boolean moving;  // This is set true while the use is moving the cursor

    private boolean straightLine = false;

    private Graphics graphicsForDrawing;  // A graphics context for the panel
    // that is used to draw the user's curve.


    /**
     * Constructor for SimplePaintPanel class sets the background color to be
     * white and sets it to listen for mouse events on itself.
     */
    SimplePaintPanel() {
        setBackground(new Color(0, 0, 0, 0));
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
    }


    /**
     * Draw the contents of the panel.  Since no information is
     * saved about what the user has drawn, the user's drawing
     * is erased whenever this routine is called.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);  // Fill with background color (white).

        int width = getWidth();    // Width of the panel.
        int height = getHeight();  // Height of the panel.

        int colorSpacing = (height - 56) / 7;
        // Distance between the top of one colored rectangle in the palette
        // and the top of the rectangle below it.  The height of the
        // rectangle will be colorSpacing - 3.  There are 7 colored rectangles,
        // so the available space is divided by 7.  The available space allows
        // for the gray border and the 50-by-50 CLEAR button.

         /* Draw a 3-pixel border around the applet in gray.  This has to be
          done by drawing three rectangles of different sizes. */

        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width-1, height-1);
        g.drawRect(1, 1, width-3, height-3);
        g.drawRect(2, 2, width-5, height-5);

         /* Draw a 56-pixel wide gray rectangle along the right edge of the applet.
          The color palette and Clear button will be drawn on top of this.
          (This covers some of the same area as the border I just drew. */

        g.fillRect(width - 56, 0, 56, height);

         /* Draw the "Clear button" as a 50-by-50 white rectangle in the lower right
          corner of the applet, allowing for a 3-pixel border. */

        g.setColor(Color.GRAY);
        g.fillRect(width-53,  height-53, 50, 50);
        g.setColor(Color.BLACK);
        g.drawRect(width-53, height-53, 49, 49);
        g.drawString("CLEAR", width-48, height-23);

        /* Draw the seven color rectangles. */

        g.setColor(Color.BLACK);
        g.fillRect(width-53, 3 + 0*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.RED);
        g.fillRect(width-53, 3 + 1*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.GREEN);
        g.fillRect(width-53, 3 + 2*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.BLUE);
        g.fillRect(width-53, 3 + 3*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.CYAN);
        g.fillRect(width-53, 3 + 4*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.MAGENTA);
        g.fillRect(width-53, 3 + 5*colorSpacing, 50, colorSpacing-3);
        g.setColor(Color.YELLOW);
        g.fillRect(width-53, 3 + 6*colorSpacing, 50, colorSpacing-3);

         /* Draw a 2-pixel white border around the color rectangle
          of the current drawing color. */

        g.setColor(Color.WHITE);
        g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
        g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);

    } // end paintComponent()


    /**
     * Change the drawing color after the user has clicked the
     * mouse on the color palette at a point with y-coordinate y.
     * (Note that I can't just call repaint and redraw the whole
     * panel, since that would erase the user's drawing!)
     */
    private void changeColor(int y) {
        int width = getWidth();           // Width of applet.
        int height = getHeight();         // Height of applet.
        int colorSpacing = (height - 56) /7;  // Space for one color rectangle.
        int newColor = y / colorSpacing;       // Which color number was clicked?

        if (newColor < 0 || newColor > 6)      // Make sure the color number is valid.
            return;

         /* Remove the hilite from the current color, by drawing over it in gray.
          Then change the current drawing color and draw a hilite around the
          new drawing color.  */

        Graphics g = getGraphics();
        g.setColor(Color.GRAY);
        g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
        g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);
        currentColor = newColor;
        g.setColor(Color.WHITE);
        g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
        g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);
        g.dispose();

    } // end changeColor()


    /**
     * This routine is called in mousePressed when the user clicks on the drawing area.
     * It sets up the graphics context, graphicsForDrawing, to be used to draw the user's
     * sketch in the current color.
     */
    private void setUpDrawingGraphics() {
        graphicsForDrawing = getGraphics();
        switch (currentColor) {
            case BLACK:
                graphicsForDrawing.setColor(Color.BLACK);
                break;
            case RED:
                graphicsForDrawing.setColor(Color.RED);
                break;
            case GREEN:
                graphicsForDrawing.setColor(Color.GREEN);
                break;
            case BLUE:
                graphicsForDrawing.setColor(Color.BLUE);
                break;
            case CYAN:
                graphicsForDrawing.setColor(Color.CYAN);
                break;
            case MAGENTA:
                graphicsForDrawing.setColor(Color.MAGENTA);
                break;
            case YELLOW:
                graphicsForDrawing.setColor(Color.YELLOW);
                break;
        }
    } // end setUpDrawingGraphics()


    /**
     * This is called when the user presses the mouse anywhere in the applet.
     * There are three possible responses, depending on where the user clicked:
     * Change the current color, clear the drawing, or start drawing a curve.
     * (Or do nothing if user clicks on the border.)
     */
    public void mousePressed(MouseEvent evt) {

        int x = evt.getX();   // x-coordinate where the user clicked.
        int y = evt.getY();   // y-coordinate where the user clicked.



        int width = getWidth();    // Width of the panel.
        int height = getHeight();  // Height of the panel.

        if (dragging)  // Ignore mouse presses that occur
            return;            //    when user is already drawing a curve.
        //    (This can happen if the user presses
        //    two mouse buttons at the same time.)

        if (x > width - 53) {
            // User clicked to the right of the drawing area.
            // This click is either on the clear button or
            // on the color palette.
            if (y > height - 53) {
                lineList.clear();
                repaint();       //  Clicked on "CLEAR button".
            } else {
                changeColor(y);  // Clicked on the color palette.
            }
        } else if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
            // The user has clicked on the white drawing area.
            // Start drawing a curve from the point (x,y).
            prevX = x;
            prevY = y;
            startX = x;
            startY = y;

            dragging = true;
            setUpDrawingGraphics();
        }

    } // end mousePressed()

    /**
     * Called whenever the user releases the mouse button. If the user was drawing
     * a curve, the curve is done, so we should set drawing to false and get rid of
     * the graphics context that we created to use during the drawing.
     */
    public void mouseReleased(MouseEvent evt) {

        if (straightLine) {
            graphicsForDrawing.drawLine(startX, startY, endX, endY);
            lineList.add(new Line(new Position(startX, startY, getWidth()-57, getHeight()),
                    new Position(endX, endY, getWidth() - 57, getHeight())));
        } else {
            if (dragging == false)
                return;  // Nothing to do because the user isn't drawing.
        }
        dragging = false;
        graphicsForDrawing.dispose();
        graphicsForDrawing = null;
    }


    /**
     * Called whenever the user moves the mouse while a mouse button is held down.
     * If the user is drawing, draw a line segment from the previous mouse location
     * to the current mouse location, and set up prevX and prevY for the next call.
     * Note that in case the user drags outside of the drawing area, the values of
     * x and y are "clamped" to lie within this area.  This avoids drawing on the color
     * palette or clear button.
     */
    public void mouseDragged(MouseEvent evt) {

        if (dragging == false)
            return;  // Nothing to do because the user isn't drawing.

        int x = evt.getX();   // x-coordinate of mouse.
        int y = evt.getY();   // y-coordinate of mouse.

        if (x < 3)                          // Adjust the value of x,
            x = 3;                           //   to make sure it's in
        if (x > getWidth() - 57)       //   the drawing area.
            x = getWidth() - 57;

        if (y < 3)                          // Adjust the value of y,
            y = 3;                           //   to make sure it's in
        if (y > getHeight() - 4)       //   the drawing area.
            y = getHeight() - 4;



        if (straightLine && dragging && moving) {
            graphicsForDrawing.setColor(Color.white);
            graphicsForDrawing.fillRect(0,0,getWidth() - 55,getHeight());
            setUpDrawingGraphics();
            ((Graphics2D) graphicsForDrawing).setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,    // End-cap style
                    BasicStroke.JOIN_ROUND));
            endX = x;
            endY = y;
            drawLines();
            graphicsForDrawing.drawLine(startX, startY, x, y);  // Draw the line.
            System.out.println(startX + " " +  startY + " "  + x +  " " + y);
        } else if (!straightLine) {
            ((Graphics2D) graphicsForDrawing).setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,    // End-cap style
                    BasicStroke.JOIN_ROUND));
            graphicsForDrawing.drawLine(prevX, prevY, x, y);  // Draw the line.
            lineList.add(new Line(new Position(prevX, prevY, getWidth()-57, getHeight()),
                    new Position(x, y, getWidth() - 57, getHeight())));
            prevX = x;  // Get ready for the next line segment in the curve.
            prevY = y;
        }
    } // end mouseDragged()

    private void updateLinePositions() {
        for (Line line: lineList) {
            Position updatedStartPostition = linePositionController.relativePositionConverter(line.startPoint, line.startPoint.panelWidthAtCreation, line.startPoint.paneHeightAtCreation, getWidth(), getHeight());
            Position updatedEndPosition = linePositionController.relativePositionConverter(line.endPoint, line.endPoint.panelWidthAtCreation, line.endPoint.paneHeightAtCreation, getWidth(), getHeight());

            line.startPoint = updatedStartPostition;
            line.endPoint = updatedEndPosition;
        }
    }

    private void drawLines() {
        for (Line line: lineList) {
            int startPointX = line.startPoint.x;
            int startPointY = line.startPoint.y;
            int endPointX = line.endPoint.x;
            int endPointY = line.endPoint.y;
            graphicsForDrawing.drawLine(startPointX, startPointY, endPointX, endPointY);
        }

    }


    public void mouseEntered(MouseEvent evt) { }   // Some empty routines.
    public void mouseExited(MouseEvent evt) {
    }    //    (Required by the MouseListener
    public void mouseClicked(MouseEvent evt) { }   //    and MouseMotionListener
    public void mouseMoved(MouseEvent evt) {
        moving = true;
        }     //    interfaces).
    public void componentResized(ComponentEvent e) {
        setUpDrawingGraphics();
        graphicsForDrawing.setColor(new Color(0, 0, 0, 0));
        graphicsForDrawing.fillRect(0,0,getWidth() - 55,getHeight());
        setUpDrawingGraphics();
        ((Graphics2D) graphicsForDrawing).setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,    // End-cap style
                BasicStroke.JOIN_ROUND));
        drawLines();
        updateLinePositions();
        /*graphicsForDrawing.dispose();
        graphicsForDrawing = null;*/
        System.out.println(e.getComponent().getClass().getName() + " --- Resized ");
    }
    public void componentMoved(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Moved");
    }
    public void componentShown(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Shown");
    }
    public void componentHidden(ComponentEvent e) {
        System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
    }



}  // End class SimplePaintPanel