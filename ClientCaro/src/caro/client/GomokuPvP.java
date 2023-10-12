package caro.client;

import caro.common.GPos;
import java.applet.*;
import java.awt.*;

public class GomokuPvP extends Applet {

    Panel ButtonArea;
    Button Buttons[];
    Label MessageLabel;
    Checkbox Check1,Check2;

    CGoban Goban;
    CComputer Computer;

    int Player[];
    final int PLAYER1 = 1;
    final int PLAYER2 = 2;

    int CurrentPlayer;
    final int WHITE = 1;
    final int BLACK = 2;

    int GameState;
    final int GS_END = 0;
    final int GS_PLAYUSER = 1;
    //final int GS_PLAYCOM = 2;
    final int GS_READY = 3;


    public void init() {

        this.setSize(500, 600);
        ButtonArea = new Panel();
        setLayout(new BorderLayout());
        add("South",ButtonArea);

        MessageLabel = new Label("Welcome to Gomoku-Narabe Game !");
        add("North",MessageLabel);

        Goban = new CGoban();
        add("Center",Goban);

        Buttons  = new Button[4];
        Buttons[0] = new Button("First");
        Buttons[1] = new Button("Second");
        Buttons[2] = new Button("Clear");

        Buttons[3] = new Button("Check");

        ButtonArea.setLayout(new FlowLayout());
        for(int i=0;i<3;i++)
            ButtonArea.add(Buttons[i]);

        Check1 = new Checkbox("Sakiyomi");
        Check2 = new Checkbox("Kinjite");

        ButtonArea.add(Check1);
        ButtonArea.add(Check2);

        Check1.setState(true);
        Check2.setState(true);
        Goban.Sakiyomi = true;
        Goban.Kinjite=true;

        Computer = new CComputer(Goban);

        Player = new int[3];
        
        Goban.init(500,600);
    }

    public void start() {

        InitGame();

    }


    public boolean action(Event e,Object o) {

        String label = o.toString();

        if(e.target instanceof Button){

            if(label.equals("First")){

                StartGame(PLAYER1);

            } else if (label.equals("Second")) {

                StartGame(PLAYER2);

            } else if (label.equals("Clear")) {

                InitGame();

            }

        } else if(e.target instanceof Checkbox) {

            Goban.Sakiyomi = Check1.getState();
            Goban.Kinjite = Check2.getState();
        }

        return true;
    }


    void InitGame() {

        //Goban.Initialize(Computer);
        Goban.Initialize();
        Goban.Draw();
        repaint();
        GameState = GS_READY;

        for(int i=0;i<2;i++)
            Buttons[i].enable();

        PutMessage("Click 'First' or 'Second' to start game.");
    }


    void StartGame(int FirstPlayer) {

        if(GameState!=GS_READY)
            return;

        Player[BLACK] = FirstPlayer;
        Player[WHITE] = (FirstPlayer==PLAYER2) ? PLAYER1 : PLAYER2;
        CurrentPlayer = BLACK;

        for(int i=0;i<2;i++)
            Buttons[i].disable();


        GameState = GS_PLAYUSER;
        
        PutMessage("Player " + Player[CurrentPlayer] + " Turn");

    }


    public boolean mouseDown(Event evt,int x,int y) {

        GPos pos = new GPos();
        int offetX = Goban.getX();
        int offetY = Goban.getY();
        
        if( !Goban.GetPos(x-offetX,y-offetY,pos) )
            return true;
        
        putStone(Player[CurrentPlayer], pos);

        return true;
    }
    
    public boolean putStone(int player, GPos pos)
    {
        if (GameState==GS_PLAYUSER && player == Player[CurrentPlayer])
        {
            switch(Goban.Check(CurrentPlayer, pos.x, pos.y)) {

//                case CGoban.GC_OK:
//
//                    CurrentPlayer = (CurrentPlayer == WHITE) ? BLACK : WHITE;
//                    PutMessage("Player " + Player[CurrentPlayer] + " Turn");
//                    break;
//
//                case CGoban.GC_ILLEGAL:
//
//                    PutMessage("Illegal postion ! Can't put");
//                    break;
//
//                case CGoban.GC_CANNOT:
//
//                    PutMessage("Aleady exist! Can't put");
//                    break;
//
//                case CGoban.GC_FILLED:
//
//                    PutMessage("Board is filled ! Draw game.");
//                    GameState = GS_END;
//                    break;
//
//                case CGoban.GC_WIN:
//                    if (CurrentPlayer==BLACK)
//                        PutMessage("BLACK Win !!!");
//                    else
//                        PutMessage("WHITE Win !!!");
//
//                    
//                    GameState = GS_END;
//                    break;
            }
        }
        return true;
    }

    public boolean mouseUp(Event evt,int x,int y) {

        return true;
    }



    public void PutMessage(String s) {

        MessageLabel.setText(s);
    }


    public void paint(Graphics g) {

        Goban.Draw();
    }
}  