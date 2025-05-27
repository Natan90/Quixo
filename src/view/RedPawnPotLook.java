package view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Red pot inherits from GridLook but overrides the renderBorders() method
 * so that a special look is given to borders copared to the default look defined
 * in GridLook. Moreover, cells have a fixed size, meaning that if an element is
 * too big to fit within a cell, it will overlap neighbors cells.
 * The default alignment is also changed and set to the middle of the cells.
 */
public class RedPawnPotLook extends GridLook {

    private Rectangle[] cells;

    public RedPawnPotLook(int height, int width,  ContainerElement element) {
        super(height/4, width, element, -1, 1, Color.BLACK);

    }

    protected void render() {
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
        cells = new Rectangle[4];
        // create the rectangles.
        for(int i=0;i<25;i++) {
            cells[i] = new Rectangle(colWidth, rowHeight, Color.WHITE);
            cells[i].setStrokeWidth(3);
            cells[i].setStrokeMiterLimit(10);
            cells[i].setStrokeType(StrokeType.CENTERED);
            cells[i].setStroke(Color.valueOf("0x333333"));
            cells[i].setX(borderWidth);
            cells[i].setY(i*rowHeight+borderWidth);
            addShape(cells[i]);
        }
    }
}