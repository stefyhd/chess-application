package Players;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    private final String name;
    private final Colors colorType;
    private List<Piece> capturedPieces=new ArrayList<Piece>();
    private TreeSet<ChessPair<Position, Piece>> remainingPieces=new TreeSet<>();
    private int points;
    boolean isHuman=false;

    public Player(String name, Colors colorType) {
        this.name=name;
        this.colorType=colorType;
        this.points=0;
        if(!name.equals("computer") && !name.equals("Computer") && !name.equals("COMPUTER")) {
            isHuman=true;
        }
    }

    //metoda noua de a sincroniza ce am pe board si ce cunoaste playerul
    public void updateMyPieces(Board board){
        this.remainingPieces.clear();

        TreeSet<ChessPair<Position,Piece>> boardList=board.getList();
        for(ChessPair<Position,Piece> chessPair:boardList){
            if(chessPair.getValoare().getColor()==this.colorType){
                this.remainingPieces.add(chessPair);
            }
        }
    }

    //facem o tona de verificari in functia asta ca sa ne asiguram de tot basically
    public void makeMove(Position from, Position to, Board board) throws InvalidMoveException, InvalidCommandException {
        Piece p= board.getPieceAt(from);
        if(p==null){
            throw new InvalidCommandException("Pieces.Piece not found!");
        }

        if(p.getColor()!=colorType){
            throw new InvalidCommandException("Can't move enemies pieces!");
        }

        Piece targetPiece=board.getPieceAt(to);

        if(p.type()!='K'&&p.type()!='Q'&&p.type()!='R'&&p.type()!='B'&&p.type()!='N'&&p.type()!='P'){
            throw new InvalidCommandException("Not a valid piece"+from);
        }

        if(p.getPosition().getY()<1 || p.getPosition().getY()>8 || p.getPosition().getX()<'A' || p.getPosition().getX()>'H'){
            throw new InvalidCommandException("Not chess square"+ p.getPosition());
        }

        if(to.getY()<1 || to.getY()>8 || to.getX()<'A' || to.getX()>'H'){
            throw new InvalidCommandException("Not chess square"+ to);
        }

        try{
            board.movePiece(from, to, isHuman);
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        } catch (InvalidCommandException e2) {
            System.out.println(e2.getMessage());
        }

        if(targetPiece!=null){
            this.capturedPieces.add(targetPiece);
            //aici calculam si punctele
//            this.points+=calculatePoints(targetPiece);
        }

        //updatam ca sa ne asiguram ca are piesele bune si sa sincronizam player cu game
        updateMyPieces(board);
    }

    public void makeMoveForUI(Position from, Position to, Board board, char promotionType) throws InvalidMoveException, InvalidCommandException {
        Piece p= board.getPieceAt(from);
        if(p==null){
            throw new InvalidCommandException("Pieces.Piece not found!");
        }

        if(p.getColor()!=colorType){
            throw new InvalidCommandException("Can't move enemies pieces!");
        }

        Piece targetPiece=board.getPieceAt(to);

        if(p.type()!='K'&&p.type()!='Q'&&p.type()!='R'&&p.type()!='B'&&p.type()!='N'&&p.type()!='P'){
            throw new InvalidCommandException("Not a valid piece"+from);
        }

        if(p.getPosition().getY()<1 || p.getPosition().getY()>8 || p.getPosition().getX()<'A' || p.getPosition().getX()>'H'){
            throw new InvalidCommandException("Not chess square"+ p.getPosition());
        }

        if(to.getY()<1 || to.getY()>8 || to.getX()<'A' || to.getX()>'H'){
            throw new InvalidCommandException("Not chess square"+ to);
        }

        //alta functie ca sa folosim asta
        try{
            board.movePieceForUI(from, to, isHuman, promotionType);
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        } catch (InvalidCommandException e2) {
            System.out.println(e2.getMessage());
        }

        if(targetPiece!=null){
            this.capturedPieces.add(targetPiece);
            //aici calculam si punctele
//            this.points+=calculatePoints(targetPiece);
        }

        //updatam ca sa ne asiguram ca are piesele bune si sa sincronizam player cu game
        updateMyPieces(board);
    }

    public List<Piece> getCapturedPieces(){
        return capturedPieces;
    }

    public List<ChessPair<Position, Piece>> getOwnedPieces(){
        return new ArrayList<>(remainingPieces);
    }

    public int getPoints(){
        return points;
    }

    public Colors getColor(){
        return colorType;
    }

    public void setPoints(int points){
        this.points=points;
    }

    //metoda de calcul al punctajului sa fie mai finut
//    public int calculatePoints(Piece piece){
//        switch(piece.type()){
//            case 'Q': return 90;
//            case 'R': return 50;
//            case 'B': return 30;
//            case 'N': return 30;
//            case 'P': return 10;
//            default: return 0;
//        }
//    }

    public String getName() {
        return name;
    }
}
