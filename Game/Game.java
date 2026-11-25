package Game;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private int gameID;
    private Board board;
    private Player[] players=new Player[2];
    private List<Move> moves=new ArrayList<>();
    //0=player turn, 1= computer turn
    private int playerIndex;
    //-1 nu a pierdut nimeni, altfel devine playerIndex
    private int loser=-1;

    private boolean Remise=false;
    private List<Move> playerRepetitiveMoves=new ArrayList<>();
    private List<Move> computerRepetitiveMoves=new ArrayList<>();
    public String messageToBeDisplayed="";
    private String endReason="";

    public String getEndReason() {
        return endReason;
    }

    private final List<GameObserver> observers=new ArrayList<>();

    public Game(int gameID, Board board, Colors playerColor, String name) {
        this.gameID = gameID;
        this.board = board;
        this.players[0]=new Player(name,playerColor);
        if(playerColor== Colors.WHITE){
            this.players[1]=new Player("Computer", Colors.BLACK);
        } else {
            this.players[1]=new Player("Computer", Colors.WHITE);
        }
    }

    public void addObserver(GameObserver observer){
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer){
        observers.remove(observer);
    }

    private void notifyMoveMade(Move m) {
        for (GameObserver o: observers)
            o.onMoveMade(m);
    }

    private void notifyPieceCaptured(Piece p) {
        for (GameObserver o : observers)
            o.onPieceCaptured(p);
    }

    private void notifyPlayerSwitch(Player p) {
        for (GameObserver o :observers)
            o.onPlayerSwitch(p);
    }

    //consideram players[0] uman
    public void setTurn(Colors playerColor){
        if(this.players[0].getColor()==playerColor){
            playerIndex=0;
        } else{
            playerIndex=1;
        }
    }

    public void start(){
        board.initialize();
        if(players[0].getColor()== Colors.WHITE){
            playerIndex=0;
        } else{
            playerIndex=1;
        }
    }

    //nu am utilizare ptr functia asta :)))
    public void resume(){
        Player currentPlayer=players[playerIndex];
        System.out.println("YOU ARE PLAYING" + players[0].getColor());
        System.out.println(""+currentPlayer.getColor()+"'s turn to play!");
        System.out.println("GOOD LUCK!");

    }

    //schimbam doar indexul
    public void switchPlayer(){
        if(playerIndex==0){
            playerIndex=1;
        } else{
            playerIndex=0;
        }

        notifyPlayerSwitch(players[playerIndex]);
    }

    //metoda care gestioneaza coresp .celor 2 liste de Mutari specifice
    //verificam la fiecare pas daca in cele 2 liste de mutari sunt mutare-invers-mutare
    public void reptitionHandlerer(Player player, Position from, Position to){
        Move newMove=new Move(player.getColor(),from,to);
        if(player.getColor()==players[0].getColor()){
            if(playerRepetitiveMoves.isEmpty()){
                playerRepetitiveMoves.add(newMove);
            } else{
                //adaugam doar daca mutariloe vor fi de genul from1 to1 to1 from1, adica from2-to1 si to2=from1
                Move lastMove=playerRepetitiveMoves.get(playerRepetitiveMoves.size()-1);
                if(from.equals(lastMove.getTo()) && to.equals(lastMove.getFrom())){
                    playerRepetitiveMoves.add(newMove);
                } else{
                    //nu mai e repetitiv si nu mai e mutare-inversulmutarii-mutare, golim
                    playerRepetitiveMoves.clear();
                    playerRepetitiveMoves.add(newMove);
                }
            }
        } else{
            if(computerRepetitiveMoves.isEmpty()){
                computerRepetitiveMoves.add(newMove);
            } else{
                Move lastMove=computerRepetitiveMoves.get(computerRepetitiveMoves.size()-1);
                if(from.equals(lastMove.getTo()) && to.equals(lastMove.getFrom())){
                    computerRepetitiveMoves.add(newMove);
                } else{
                    //nu mai e repetitiv si nu mai e mutare-inversulmutarii-mutare, golim
                    computerRepetitiveMoves.clear();
                    computerRepetitiveMoves.add(newMove);
                }
            }
        }
    }
    public boolean checkForCheckMate(){
        if(playerRepetitiveMoves.size()>=3 &&computerRepetitiveMoves.size()>=3){
            Remise=true;
            endReason="DRAW";
            messageToBeDisplayed="DRAW! Too many moves the same!";
            return true;
        }

        this.players[0].updateMyPieces(this.board);
        this.players[1].updateMyPieces(this.board);

        Player currentPlayer=this.players[playerIndex];
        boolean Mate=false;

        //cautam o piesa mutabila
        List<ChessPair<Position, Piece>> pieseCurente=currentPlayer.getOwnedPieces();
        for(ChessPair<Position, Piece> p:pieseCurente){
            List<Position> possibleMoves=p.getValoare().getPossibleMoves(this.board);
            Position from=p.getCheie();
            for(Position to:possibleMoves){
                if(board.isValidMove(from,to)){
                    Mate=true;
                    break;
                }
            }
            if(Mate) break;
        }

        //Mate=false inseamna si PAT si Checkmate-tratam separat
        if(!Mate){
            if(board.isKingInCheck(currentPlayer.getColor())){
                loser=playerIndex;
                messageToBeDisplayed="CHECKMATE!" +currentPlayer.getColor() + "has lost! NASOL";
            } else{
                loser=-1;
                Remise=true;
                endReason="PAT";
                messageToBeDisplayed="PAT! No legal moves left";
            }
            return true;
        }
        if(loser!=-1){
            return true;
        }

        return false;
    }

    //metoda noua ptr mutarea automata a computerului ca sa nu aglomerez Utils.Main
    public void automaticComputerMoves(){
        this.players[1].updateMyPieces(this.board);
        Player computer=this.players[1];

        computer.updateMyPieces(this.board);
        List<ChessPair<Position, Piece>> computerPieces=computer.getOwnedPieces();
        List<Move> ValidMoves=new ArrayList<>();
        for(ChessPair<Position, Piece> chessPair:computerPieces){
            Piece nowPiece=chessPair.getValoare();
            Position nowPosition=chessPair.getCheie();

            List<Position> potentialMoves=nowPiece.getPossibleMoves(this.board);
            for(Position to:potentialMoves){
                if(board.isValidMove(nowPosition,to)){
                    Piece potentialCapture=this.board.getPieceAt(to);

                    Move moveOption = new Move(computer.getColor(), nowPosition, to, potentialCapture);
                    ValidMoves.add(moveOption);
                }
            }
        }

        if(ValidMoves.isEmpty()==false){
            Random rand=new Random();
            Move theMove=ValidMoves.get(rand.nextInt(ValidMoves.size()));
            System.out.println("Computer: "+theMove.getFrom() + "-" + theMove.getTo());
            Piece capturedPiece = this.board.getPieceAt(theMove.getTo());
            //desi nu cred ca poate sa dea eroare, dar movePiece() da throw la erori
            try{
                computer.makeMove(theMove.getFrom(), theMove.getTo(), this.board);

                //istoricul
                addMove(computer, theMove.getFrom(), theMove.getTo(), capturedPiece);
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void addMove(Player p, Position from, Position to, Piece capturedPiece){
        Move m=new Move(p.getColor(), from, to, capturedPiece);
        moves.add(m);
        reptitionHandlerer(p, from, to);

        notifyMoveMade(m);

        if (capturedPiece != null){
            notifyPieceCaptured(capturedPiece);
        }
    }

    public void setMoves(List<Move> moves){
        this.moves=moves;
    }

    public int getGameID(){
        return gameID;
    }

    @Override
    public String toString() {
        return this.board.showThatBoard(players[0].getColor());
    }

    public List<Move> getMoves() {
        return moves;
    }

    public boolean Remise(){
        return Remise;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getLoser(){
        return this.loser;
    }

    public void setId(int gameID){
        this.gameID=gameID;
    }

    public String getMessageToBeDisplayed() {
        return messageToBeDisplayed;
    }
}
