package caro.client;

import caro.common.GPos;
import caro.common.CPiece;
import caro.common.CPiece;
import caro.common.GPos;
import java.applet.*;
import java.awt.*;

public class CGoban extends Canvas {

    static final int TOP        = 10;
    static final int LEFT        = 10;
    static final int BOARDSIZE    = 25;
    static final int PIECESIZE    = 20;

    static final int MAXPIECENUM = BOARDSIZE * BOARDSIZE;

    static final int GC_OK         = 0;
    static final int GC_ILLEGAL = 1;
    static final int GC_CANNOT    = 2;
    static final int GC_FILLED    = 3;
    static final int GC_WIN        = 4;

    public boolean Sakiyomi;
    public boolean Kinjite;


    public CPiece Pieces[][];

    public int Area[][];
    static final int AREASIZE = 2;

    public int numPiece;

    //CComputer Computer;

    //Double buffer
    Graphics bufferGraphics;
    Image offscreen;
    Dimension dim; 
     
    public CGoban() {

        resize(310,310);

        Pieces = new CPiece[BOARDSIZE][BOARDSIZE];

        for(int i=0; i < BOARDSIZE; i++){
            for(int j=0; j < BOARDSIZE; j++){
                Pieces[i][j] = new CPiece();
            }
        }
        Area = new int[BOARDSIZE][BOARDSIZE];

        
    }


//    public void Initialize(CComputer com) {
     public void Initialize() {
        //Computer = com;

        for(int x=0; x < BOARDSIZE; x++){
            for(int y=0; y < BOARDSIZE; y++){
                Pieces[x][y].State = CPiece.EMPTY;
                Area[x][y] = 0;
            }
        }
        numPiece = 0;
    }
    
    public void init(int width, int height)
    {
        //dim = getSize();
        offscreen = createImage(width,height);
        bufferGraphics = offscreen.getGraphics();
    }


    public void Put(int color,int x,int y) {

        if(Pieces[x][y].State == CPiece.EMPTY) {
            Pieces[x][y].State = color;
            numPiece++;

            int x1,x2,y1,y2;
            x1 = (x-AREASIZE < 0) ? 0 : x-AREASIZE ;
            x2 = (x+AREASIZE >= BOARDSIZE) ? BOARDSIZE-1 : x+AREASIZE;
            y1 = (y-AREASIZE < 0) ? 0 : y-AREASIZE ;
            y2 = (y+AREASIZE >= BOARDSIZE) ? BOARDSIZE-1 : y+AREASIZE;
            for( ; x1 <= x2; x1++){
                for(y = y1 ; y <= y2; y++) {
                    Area[x1][y]++;
                }
            }

        }

    }


    public void Remove(int x,int y) {

        if(Pieces[x][y].State != CPiece.EMPTY) {

            Pieces[x][y].State = CPiece.EMPTY;
            numPiece--;

            int x1,x2,y1,y2;
            x1 = (x-AREASIZE < 0) ? 0 : x-AREASIZE ;
            x2 = (x+AREASIZE >= BOARDSIZE) ? BOARDSIZE-1 : x+AREASIZE;
            y1 = (y-AREASIZE < 0) ? 0 : y-AREASIZE ;
            y2 = (y+AREASIZE >= BOARDSIZE) ? BOARDSIZE-1 : y+AREASIZE;
            for( ; x1 <= x2; x1++){
                for( y = y1; y <= y2; y++) {
                    Area[x1][y]--;
                }
            }

        }

    }


    public int Check(int color,int x,int y) {

        int ret;

        if(Pieces[x][y].State != CPiece.EMPTY)
            return GC_CANNOT;

        Put(color,x,y);

//        ret = Computer.CheckIllegal(color,x,y);
//        if(ret != Computer.ILL_NOT){
//            Remove(x,y);
//            return GC_ILLEGAL;
//        }
//
//        ret = Computer.Find5Block(color,x,y);

        Draw();

        //if(ret == Computer.OK5)
        //    return GC_WIN;

        if(numPiece == MAXPIECENUM)
            return GC_FILLED;

        return GC_OK;
    }


    public boolean GetPos(int x,int y,GPos pos) {

        if(x < LEFT-(PIECESIZE/2) || x > LEFT+(PIECESIZE*(BOARDSIZE-1))+(PIECESIZE/2) )
            return false;
        if(y < TOP-(PIECESIZE/2) || y > TOP+(PIECESIZE*(BOARDSIZE-1))+(PIECESIZE/2) )
            return false;
        pos.x = ( x-(LEFT-(PIECESIZE/2)) ) / PIECESIZE;
        pos.y = ( y-(TOP -(PIECESIZE/2)) ) / PIECESIZE;
        return true;
    }


    public void Draw() {

        repaint();

    }

    @Override
    public void paint(Graphics g) {

        int x,y;

        
        bufferGraphics.setColor(Color.white);
        bufferGraphics.clearRect(0, 0, offscreen.getWidth(this), offscreen.getHeight(this));
        bufferGraphics.setColor(Color.black);
        for(x = 0; x < BOARDSIZE; x++) {
            bufferGraphics.drawLine(x*PIECESIZE+LEFT,TOP,x*PIECESIZE+LEFT,TOP+(BOARDSIZE-1)*PIECESIZE);
        }
        for(y = 0; y < BOARDSIZE; y++) {
            bufferGraphics.drawLine(LEFT,y*PIECESIZE+TOP,LEFT+(BOARDSIZE-1)*PIECESIZE,y*PIECESIZE+TOP);
        }

        /*
        for(x = 0; x < BOARDSIZE; x++) {
            for(y = 0; y < BOARDSIZE; y++) {
                switch(Pieces[x][y].State) {
                    case CPiece.WHITE:
                        g.setColor(Color.white);
                        g.fillOval(x*PIECESIZE,y*PIECESIZE,PIECESIZE-2,PIECESIZE-2);
                        g.setColor(Color.black);
                        g.drawOval(x*PIECESIZE,y*PIECESIZE,PIECESIZE-2,PIECESIZE-2);
                        break;
                    case CPiece.BLACK:
                        g.setColor(Color.black);
                        g.fillOval(x*PIECESIZE,y*PIECESIZE,PIECESIZE-2,PIECESIZE-2);
                        break;
                }
            }
        }*/
        
        //XO Drawing
        for(x = 0; x < BOARDSIZE; x++) {
            for(y = 0; y < BOARDSIZE; y++) {
                switch(Pieces[x][y].State) {
                    case CPiece.WHITE:
                        bufferGraphics.setColor(Color.red);
                        
                        bufferGraphics.drawLine(x*PIECESIZE+2, y*PIECESIZE+2, (x+1)*PIECESIZE-2, (y+1)*PIECESIZE-2);
                        bufferGraphics.drawLine(x*PIECESIZE+2, (y+1)*PIECESIZE-2, (x+1)*PIECESIZE-2, y*PIECESIZE+2);

                        break;
                    case CPiece.BLACK:
                        bufferGraphics.setColor(Color.blue);
                        bufferGraphics.drawOval(x*PIECESIZE,y*PIECESIZE,PIECESIZE-2,PIECESIZE-2);
                        break;
                }
            }
        }
        
        g.drawImage(offscreen, 0, 0, this);
    }

}  