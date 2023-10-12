package caro.client;

import caro.common.GPos;
import caro.common.CPiece;
import caro.common.CPiece;
import caro.common.GPos;
import java.applet.*;
import java.awt.*;

public class CComputer {

    CGoban Goban;

    static final int EMPTY = 0;
    static final int WHITE = 1;
    static final int BLACK = 2;

    public boolean Space;
    public int Score;

    int numRecursion;
    static final int MAXRECURSION = 2;

    static final int THINK_OK        = 0;
    static final int THINK_FILLED    = 1;
    static final int THINK_WIN        = 2;

    static final int NOT5        = 0;
    static final int OK5        = 1;
    static final int ILL_CHOUREN= 2;
    static final int ILL_33        = 3;
    static final int ILL_44        = 4;
    static final int ILL_NOT    = 5;


    int Dx[];
    int Dy[];

    static final int D_UP        = 0;
    static final int D_UPRIGHT    = 1;
    static final int D_RIGHT    = 2;
    static final int D_DOWNRIGHT= 3;
    static final int D_DOWN        = 4;
    static final int D_DOWNLEFT    = 5;
    static final int D_LEFT        = 6;
    static final int D_UPLEFT    = 7;



    public CComputer(CGoban goban) {

        Goban = goban;

        Dx = new int[8];
        Dy = new int[8];

        Dx[0] =  0;  Dy[0] = -1;
        Dx[1] =  1;  Dy[1] = -1;
        Dx[2] =  1;  Dy[2] =  0;
        Dx[3] =  1;  Dy[3] =  1;
        Dx[4] =  0;  Dy[4] =  1;
        Dx[5] = -1;  Dy[5] =  1;
        Dx[6] = -1;  Dy[6] =  0;
        Dx[7] = -1;  Dy[7] = -1;


    }

    public int Think(int color,GPos Best) {

        int x,y;
        int BestScore =  0;
        int eBestScore = 0;
        int eBestX = 0;
        int eBestY = 0;
        int ecolor = (color==WHITE)? BLACK : WHITE;
        Best.x = 0;
        Best.y = 0;

        for(x=0;x<CGoban.BOARDSIZE;x++){
            for(y=0;y<CGoban.BOARDSIZE;y++){

                if( Goban.Pieces[x][y].State == CPiece.EMPTY  && Goban.Area[x][y] > 0 ){

                    Goban.Put(color,x,y);
                    if(GetScore(color,x,y) == ILL_NOT){
                        if(Score > BestScore) {
                            BestScore = Score;
                            Best.x = x;
                            Best.y = y;
                        }
                    }
                    Goban.Remove(x,y);

                    Goban.Put(ecolor,x,y);
                    if(GetScore(ecolor,x,y) == ILL_NOT){
                        if(Score > eBestScore) {
                            eBestScore = Score;
                            eBestX = x;
                            eBestY = y;
                        }
                    }
                    Goban.Remove(x,y);
                }
            }
        }

        if(BestScore >= SC_WIN){
            Goban.Put(color,Best.x,Best.y);
            Goban.Draw();
            return THINK_WIN;
        }

        if(eBestScore >= SC_WIN){
            Best.x = eBestX;
            Best.y = eBestY;
        } else if(eBestScore >= SC_WIN2) {
            if(BestScore < SC_WIN2) {
                Best.x = eBestX;
                Best.y = eBestY;
            }
        } else if(BestScore < SC_WIN2 && Goban.numPiece > 3 && Goban.Sakiyomi) {
            Best.x=-2; Best.y=-2;

            Perspective(color,ecolor,Best);
        } else {
            if(eBestScore > BestScore){
                Best.x=eBestX;
                Best.y=eBestY;
            }
        }

        Goban.Put(color,Best.x,Best.y);
        Goban.Draw();

        if(Goban.numPiece == Goban.MAXPIECENUM)
            return THINK_FILLED;

        return THINK_OK;
    }

    void Perspective(int color,int ecolor,GPos Best) {

        int x,y;
        int score=0;
        int BestScore = 0;

        numRecursion = 0;

        Best.x = 0;
        Best.y = 0;

        for(x=0;x<CGoban.BOARDSIZE;x++){
            for(y=0;y<CGoban.BOARDSIZE;y++){

                if( Goban.Pieces[x][y].State == CPiece.EMPTY  && Goban.Area[x][y] > 0 ){
                    Goban.Put(color,x,y);

                    ;score = GetGlobalScore(color);

                    score += PerspectiveIter( color,ecolor);

                    if(score > BestScore) {
                        BestScore = score;
                        Best.x = x; Best.y = y;
                    }

                    Goban.Remove(x,y);
                }
            }
        }

    }


    int PerspectiveIter(int color,int ecolor) {

        int score = 0;
        int num = 0;
        int x,y,x2,y2;

        for(x=0;x<CGoban.BOARDSIZE;x++){
            for(y=0;y<CGoban.BOARDSIZE;y++){
                if(Goban.Pieces[x][y].State == CPiece.EMPTY && Goban.Area[x][y] > 0 ){
                    Goban.Put(ecolor,x,y);

                    for(x2=0;x2<CGoban.BOARDSIZE;x2++){
                        for(y2=0;y2<CGoban.BOARDSIZE;y2++){
                            if(Goban.Pieces[x2][y2].State == CPiece.EMPTY && Goban.Area[x2][y2] > 0 ){
                                Goban.Put(color,x2,y2);
                                GetScore(color,x2,y2);
                                if(Score > 20){
                                     score += GetGlobalScore(color) - GetGlobalScore(ecolor);
                                    num++;
                                }
                                 Goban.Remove(x2,y2);
                             }
                         }
                     }
                    Goban.Remove(x,y);
                }
            }
        }
        if(num == 0)
            return 0;

        return score / num;
    }



    final int SC_WIN = 10000000;
    final int SC_WIN2 = 1000000;

    int GetScore(int color,int x,int y) {

        int d1,d2;
        int check2open=0;
        int check3open=0;
        int check3close=0;
        int check4open=0;
        int check4close=0;
        int check5 = 0;
        int checklong=0;
        int num;
        boolean space1;

        Score = 0;

        for(d1=D_UP,d2=D_DOWN; d1 <= D_DOWNRIGHT; d1++,d2++) {

            num = GetSequence(color,x,y,d1);
            space1 = Space;
            num += GetSequence(color,x,y,d2) - 1 ;

            switch (num) {

                case 2:
                    if( space1 && Space )
                        check2open++;
                    break;

                case 3:
                    if( space1 && Space )
                        check3open++;
                    else if( space1 || Space )
                        check3close++;
                    break;

                case 4:
                    if( space1 && Space )
                        check4open++;
                    else if (space1 || Space )
                        check4close++;
                    break;

                case 5:
                    check5++;
                    break;

                default:
                    if( num > 5 )
                        checklong++;
                    break;
            }
        }

        if(    color == CPiece.BLACK && Goban.Kinjite){

            if(check3open >= 2)
                return ILL_33;

            if(check4open + check4close >= 2)
                return ILL_44;

            if(checklong > 0)
                return ILL_CHOUREN;
        }

        if(check5 + checklong > 0) {
            Score = SC_WIN;
        } else if(check4open > 0) {
            Score = SC_WIN2;
        } else if(check4close >= 2) {
            Score = SC_WIN2;
        } else if( (check4open + check4close) > 0 && check3open > 0) {
            Score = SC_WIN2;
        } else if( check3open >= 2) {
            Score = 30000;
        } else if( check4close > 0 ) {
            Score = 100;
        } else if( check3open + check3close >= 2 ){
            Score = 1200;
        } else if( check3open > 0 ){
            Score = 1000*check3open;
        } else if( check3close >= 2 ){
            Score = 120;
        } else if( check2open > 0 ) {
            Score = 100*check2open;
        } else if( x==7 && y==7){
            Score = 10;
        } else {
            Score = 1;
        }
        return ILL_NOT;
    }


    public int CheckIllegal(int color,int x,int y) {

        if(color != CPiece.BLACK || Goban.Kinjite)
            return ILL_NOT;

        int d1,d2;
        int check33=0;
        int check44=0;
        int checklong=0;
        int num;
        boolean space1;

        for(d1=D_UP,d2=D_DOWN; d1 <= D_DOWNRIGHT; d1++,d2++) {

            num = GetSequence(CPiece.BLACK,x,y,d1);
            space1 = Space;
            num += GetSequence(CPiece.BLACK,x,y,d2) - 1 ;

            if( num == 3 && space1 && Space)
                check33++;
            if( num == 4 && ( space1 || Space ) )
                check44++;
            if( num > 5 )
                checklong++;
        }

        if(check33 >= 2)
            return ILL_33;

        if(check44 >= 2)
            return ILL_44;

        if(checklong > 0)
            return ILL_CHOUREN;

        return ILL_NOT;
    }



    public int Find5Block(int color,int x,int y) {

        int max,a;

        max = GetSequence(color,x,y,D_UP) + GetSequence(color,x,y,D_DOWN) - 1 ;
        a =GetSequence(color,x,y,D_LEFT) + GetSequence(color,x,y,D_RIGHT) - 1 ;
        max = Math.max(max,a);
        a = GetSequence(color,x,y,D_UPLEFT) + GetSequence(color,x,y,D_DOWNRIGHT) -1 ;
        max = Math.max(max,a);
        a = GetSequence(color,x,y,D_UPRIGHT) + GetSequence(color,x,y,D_DOWNLEFT) - 1 ;
        max = Math.max(max,a);

        if( max >= 5)
            return OK5;

        return NOT5;
    }


    public int GetSequence(int color,int x,int y,int direction) {

        int num = 0;
        int dx = Dx[direction];
        int dy = Dy[direction];

        Space = false;

        while( Goban.Pieces[x][y].State == color) {
            num++;
            x += dx; y += dy;
            if( x < 0 || x >= Goban.BOARDSIZE || y < 0 || y >= Goban.BOARDSIZE ) break;
            if(Goban.Pieces[x][y].State == CPiece.EMPTY) {
                Space = true;
                break;
            }
        }
        return num;
    }


    int num2open,num3open,num3close,num4open,num4close,num5;

    public int GetGlobalScore(int color) {

        int x,y;
        boolean spacecheck;

        num2open=0;
        num3open=0;
        num3close=0;
        num4open=0;
        num4close=0;
        num5=0;

        for(x=0; x < CGoban.BOARDSIZE; x++){
            y=0; spacecheck=false;
            do {
                if(Goban.Pieces[x][y].State == EMPTY) {
                    spacecheck=true;
                    y++;
                } else {
                    y += GetGlobalSequence(color,x,y,D_DOWN,spacecheck);
                    spacecheck=false;
                }
            } while(y < CGoban.BOARDSIZE);
        }

        for(y=0; y < CGoban.BOARDSIZE; y++){
            x=0; spacecheck=false;
            do {
                if(Goban.Pieces[x][y].State == EMPTY) {
                    spacecheck=true;
                    x++;
                } else {
                    x += GetGlobalSequence(color,x,y,D_RIGHT,spacecheck);
                    spacecheck=false;
                }
            } while(x < CGoban.BOARDSIZE);
        }

        for(int i=0; i < CGoban.BOARDSIZE*2-1; i++){
            int ret;
            if(i < CGoban.BOARDSIZE){
                x = 0; y = i;
            } else {
                x = i-CGoban.BOARDSIZE+1; y=0;
            }
            spacecheck = false;
            do {
                if(Goban.Pieces[x][y].State == EMPTY) {
                    spacecheck=true;
                    x++; y++;
                } else {
                    ret = GetGlobalSequence(color,x,y,D_DOWNRIGHT,spacecheck);
                    x+=ret; y+=ret;
                    spacecheck=false;
                }

            } while(x < CGoban.BOARDSIZE && y < CGoban.BOARDSIZE );
        }

        for(int i=0; i < CGoban.BOARDSIZE*2-1; i++){
            int ret;
            if(i < CGoban.BOARDSIZE){
                x = 0; y = i;
            } else {
                x = i-CGoban.BOARDSIZE+1; y = CGoban.BOARDSIZE-1;
            }
            spacecheck = false;
            do {
                if(Goban.Pieces[x][y].State == EMPTY) {
                    spacecheck=true;
                    x++; y--;
                } else {
                    ret = GetGlobalSequence(color,x,y,D_UPRIGHT,spacecheck);
                    x+=ret; y-=ret;
                    spacecheck=false;
                }
            } while(x < CGoban.BOARDSIZE && y >= 0 );
        }


        int score = num5 * 10000 + num4open * 5000 + num3open * 500
                        + num3close * 10 + num2open * 20 + 1;
        if(num4close >= 2)
            score += 4000;
        else
            score += num4close * 400;

        return score;

    }

    int GetGlobalSequence(int color,int x,int y,int direction,boolean spacecheck) {

        int num = GetSequence(color,x,y,direction);

        if(num==0)
            return 1;

        if(num==2){
            if(Space && spacecheck)
                num2open++;
        } else if(num==3) {
            if(Space && spacecheck)
                num3open++;
            else if(Space || spacecheck)
                num3close++;
        } else if(num==4){
            if(Space && spacecheck)
                num4open++;
            else if(Space || spacecheck)
                num4close++;
        } else if(num >= 5) {
            num5++;
        }

        return num;
    }

}  