package uta.cse3310;

public class Game {
    public PlayerType Players;
    public PlayerType CurrentTurn;
    public PlayerType[] Button;
    public String[] Msg;
    public String[] bottomMsg;
    public int GameId;
    public Stats record;

    Game(Stats info) {
        record = info;
        Button = new PlayerType[9];
        // initialize it
        for (int i = 0; i < Button.length; i++) {
            Button[i] = PlayerType.NOPLAYER;
        }

        Msg = new String[2];
        bottomMsg = new String[2];
        Players = PlayerType.XPLAYER;
        CurrentTurn = PlayerType.NOPLAYER;
        Msg[0] = "Waiting for other player to join";
        Msg[1] = "";
        
        //0 is for X and 1 is for O
        //print the bottom message containing the win/loss/draw/# of games/$ of concurrent games
        bottomMsg[0] = "X wins: " + record.xwin + "/ X loss: " + record.owin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
        + "/ Number of concurrent game: " + record.concurrentgame;  

        bottomMsg[1] = "O wins: " + record.owin + "/ O loss: " + record.xwin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
        + "/ Number of concurrent game: " + record.concurrentgame;
        
    }

    public void StartGame() {
        // X player goes first. Because that is how it is.
        Msg[0] = "You are X. Your turn";
        Msg[1] = "You are O. Other players turn";
        CurrentTurn = PlayerType.XPLAYER;
        //increment the concurrent game when the second player enter the game
        record.concurrentgame = record.concurrentgame + 1;
        //update the number of concurrent game in the bottom message
        bottomMsg[0] = "X wins: " + record.xwin + "/ X loss: " + record.owin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
        + "/ Number of concurrent game: " + record.concurrentgame;  

        bottomMsg[1] = "O wins: " + record.owin + "/ O loss: " + record.xwin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
        + "/ Number of concurrent game: " + record.concurrentgame;
    }

    private boolean CheckLine(int i, int j, int k, PlayerType player) {
        return player == Button[i] && player == Button[j] && player == Button[k];
    }

    private boolean CheckHorizontal(PlayerType player) {
        return CheckLine(0, 1, 2, player) | CheckLine(3, 4, 5, player) | CheckLine(6, 7, 8, player);
    }

    private boolean CheckVertical(PlayerType player) {
        return CheckLine(0, 3, 6, player) | CheckLine(1, 4, 7, player) | CheckLine(2, 5, 8, player);
    }

    private boolean CheckDiagonal(PlayerType player) {
        return CheckLine(0, 4, 8, player) | CheckLine(2, 4, 6, player);
    }

    private boolean CheckBoard(PlayerType player) {
        return CheckHorizontal(player) | CheckVertical(player) | CheckDiagonal(player);
    }

    private boolean CheckDraw(PlayerType player) {
        // how to check for a draw?
        // Are all buttons are taken ?
        int count = 0;
        for (int i = 0; i < Button.length; i++) {
            if (Button[i] == PlayerType.NOPLAYER) {
                count = count + 1;
            }
        }

        return count == 0;
    }

    // This function returns an index for each player
    // It does not depend on the representation of Enums
    public int PlayerToIdx(PlayerType P) {
        int retval = 0;
        if (P == PlayerType.XPLAYER) {
            retval = 0;
        } else {
            retval = 1;
        }
        return retval;
    }

    public void Update(UserEvent U) {
        System.out.println("The user event is " + U.PlayerIdx + "  " + U.Button);

        if ((CurrentTurn == U.PlayerIdx) && (CurrentTurn == PlayerType.OPLAYER || CurrentTurn == PlayerType.XPLAYER)) {
            // Move is legitimate, lets do what was requested

            // Is the button not taken by X or O?
            if (Button[U.Button] == PlayerType.NOPLAYER) {
                System.out.println("the button was 0, setting it to" + U.PlayerIdx);
                Button[U.Button] = U.PlayerIdx;
                if (U.PlayerIdx == PlayerType.OPLAYER) {
                    CurrentTurn = PlayerType.XPLAYER;
                    Msg[1] = "Other Players Move.";
                    Msg[0] = "Your Move.";
                } else {
                    CurrentTurn = PlayerType.OPLAYER;
                    Msg[0] = "Other Players Move.";
                    Msg[1] = "Your Move.";
                }
            } else {
                Msg[PlayerToIdx(U.PlayerIdx)] = "Not a legal move.";
            }

            // Check for winners, losers, and a draw

            if (CheckBoard(PlayerType.XPLAYER)) {
                Msg[0] = "You Win!";
                Msg[1] = "You Lose!";
                //increment the X score and # of total game
                //decrement the number of concurrent game when a game is finished
                record.xwin = record.xwin + 1;
                record.totalgame = record.totalgame + 1;
                record.concurrentgame = record.concurrentgame - 1;
                CurrentTurn = PlayerType.NOPLAYER;
                //update the bottom message when X wins
                bottomMsg[0] = "X wins: " + record.xwin + "/ X loss: " + record.owin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;

                bottomMsg[1] = "O wins: " + record.owin + "/ O loss: " + record.xwin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;

            } else if (CheckBoard(PlayerType.OPLAYER)) {
                Msg[1] = "You Win!";
                Msg[0] = "You Lose!";
                //increment the O score and # of total game
                //decrement the number of concurrent game when a game is finished
                record.owin = record.owin + 1;
                record.totalgame = record.totalgame + 1;
                record.concurrentgame = record.concurrentgame - 1;
                CurrentTurn = PlayerType.NOPLAYER;
                 //update the bottom message when O wins
                bottomMsg[0] = "X wins: " + record.xwin + "/ X loss: " + record.owin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;

                bottomMsg[1] = "O wins: " + record.owin + "/ O loss: " + record.xwin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;

            } else if (CheckDraw(U.PlayerIdx)) {
                Msg[0] = "Draw";
                Msg[1] = "Draw";
                //increment the draw score and # of total game
                //decrement the number of concurrent game when a game is finished
                record.draw = record.draw + 1;
                record.totalgame = record.totalgame + 1;
                record.concurrentgame = record.concurrentgame - 1;
                CurrentTurn = PlayerType.NOPLAYER;
                // //update the bottom message when its a draw
                bottomMsg[0] = "X wins: " + record.xwin + "/ X loss: " + record.owin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;

                bottomMsg[1] = "O wins: " + record.owin + "/ O loss: " + record.xwin + "/ Draw: " + record.draw + "/ Number of games played: " + record.totalgame
                + "/ Number of concurrent game: " + record.concurrentgame;
            }
        }
    }

    public void Tick() {
        // this function can be called periodically if a
        // timer is needed.

    }
    
    
}
// In windows, shift-alt-F formats the source code
// In linux, it is ctrl-shift-I
