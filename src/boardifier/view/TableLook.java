package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;

public class TableLook extends ContainerLook {

    protected int borderWidth;
    protected final String PURPLE = "\u001B[35m";
    protected final String RESET = "\u001B[0m";

    /**
     * constructor to get a flexible cell size, with inners in 0,0
     * @param containerElement
     * @param depth
     * @param borderWidth 
     */
    public TableLook(ContainerElement containerElement, int depth, int borderWidth) {
        this(containerElement, -1, -1, depth, 0,0, borderWidth);
    }

    /**
     * constructor to get a flexible cell size, with inners given as parameters
     * @param containerElement
     * @param depth
     * @param innersTop
     * @param innersLeft
     * @param borderWidth
     */
    public TableLook(ContainerElement containerElement, int depth, int innersTop, int innersLeft, int borderWidth) {
        
        this(containerElement, -1,  -1, depth, innersTop, innersLeft, borderWidth);
    }

    /**
     * constructor to get either flexible or fixed cell size
     * @param containerElement
     * @param rowHeight
     * @param colWidth
     * @param depth
     * @param innersTop
     * @param innersLeft
     * @param borderWidth* 
     */
    public TableLook(ContainerElement containerElement, int rowHeight, int colWidth, int depth, int innersTop, int innersLeft, int borderWidth) {

        super(containerElement, rowHeight,  colWidth, depth, innersTop, innersLeft);
        this.borderWidth = borderWidth;
        if (borderWidth > 0) {
            setPaddingLeft(1);
            setPaddingTop(1);
        }
    }


    @Override
    public int getHeight() {
        int h = super.getHeight();
        // if there are borders, it needs +1 in height to be drawn
        if ((borderWidth > 0) && (getGridHeight() > 0)) h++;
        return h;
    }

    @Override
    public int getWidth() {
        int w = super.getWidth();
        // if there are borders, it needs +1 in width to be drawn
        if ((borderWidth > 0) && (getGridWidth() > 0)) w++;
        return w;
    }

    /**
     * overrides default method that does nothing, because as soon as the TableLook is created,
     * if there are some row/cols that have a fixed size, they must be "visible" on screen
     */
    protected void render() {
        Logger.trace("called", this);
        // create & clear the viewport if needed
        setSize(getWidth(), getHeight());
        // clear the viewport => if there are more than inners looks to render (e.g. borders), must override this method
        clearShape();
        renderBorders();
        renderInners();
    }

    protected void renderBorders() {
        if (borderWidth < 1) return;
        // prevent displaying something if grid width or gridHeight = 0
        if ((getGridHeight() == 0) || (getGridWidth() == 0)) return;
        Logger.trace("update borders", this);
        // start by drawing the border of each cell, which will be change after
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                int x = getCellLeft(i,j);
                if (x == -1) continue;
                int xx = getCellRight(i,j)+1; // +1 because border is at the right of the real position
                int y = getCellTop(i,j);
                int yy = getCellBottom(i,j)+1; // +1 because border is below of the real position
                //top-left corner
                if (y==0) {
                    if (x==0) shape[innersTop+y][innersLeft+x] = PURPLE + "\u2554" + RESET; // draw ╔
                    else shape[innersTop+y][innersLeft+x] = PURPLE + "\u2566" + RESET; // draw ╦
                }
                else {
                    if (x==0) shape[innersTop+y][innersLeft+x] = PURPLE + "\u2560" + RESET; // draw ╠
                    else shape[innersTop+y][innersLeft+x] = PURPLE + "\u256c" + RESET; // draw ╬
                }

                // top-right corner
                if (y==0) {
                    if (xx==getGridWidth()) shape[innersTop+y][innersLeft+xx] = PURPLE + "\u2557" + RESET; // draw ╗
                    else shape[ innersTop+y][innersLeft+xx] = PURPLE + "\u2566" + RESET; // draw ╦
                }
                else {
                    if (xx==getGridWidth()) shape[ innersTop+y][innersLeft+xx] = PURPLE + "\u2563" + RESET; // draw ╣
                    else shape[ innersTop+y][innersLeft+xx] = PURPLE + "\u256c" + RESET; // draw ╬
                }

                //bottom-left corner
                if (yy==getGridHeight()) {
                    if (x==0) shape[ innersTop+yy][innersLeft+x] = PURPLE + "\u255A" + RESET; // draw ╚
                    else shape[ innersTop+yy][innersLeft+x] = PURPLE + "\u2569" + RESET; // draw ╩
                }
                else {
                    if (x==0) shape[ innersTop+yy][innersLeft+x] = PURPLE + "\u2560" + RESET; // draw ╠
                    else shape[ innersTop+yy][innersLeft+x] = PURPLE + "\u256c" + RESET; // draw ╬
                }

                // bottom-right corner
                if (yy==getGridHeight()) {
                    if (xx==getGridWidth()) shape[ innersTop+yy][innersLeft+xx] = PURPLE + "\u255d" + RESET; // draw ╝
                    else shape[ innersTop+yy][innersLeft+xx] = PURPLE + "\u2569" + RESET; // draw ╩
                }
                else {
                    if (xx==getGridWidth()) shape[ innersTop+yy][innersLeft+xx] = PURPLE + "\u2563" + RESET; // draw ╣
                    else shape[ innersTop+yy][innersLeft+xx] = PURPLE + "\u256c" + RESET; // draw ╬
                }

                // draw top/bottom horizontal lines
                for (int k = x+1; k < xx; k++) {
                    shape[ innersTop+y][innersLeft+k] = PURPLE + "\u2500" + RESET;
                    shape[ innersTop+yy][innersLeft+k] = PURPLE + "\u2500" + RESET;
                }
                // draw left/right vertical lines
                for (int k = y+1; k < yy; k++) {
                    shape[innersTop+k][innersLeft+x] = PURPLE + "\u2502" + RESET;
                    shape[innersTop+k][innersLeft+xx] = PURPLE + "\u2502" + RESET;
                }
            }
        }
    }
}
