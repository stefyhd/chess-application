package Game;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class Board {
    private TreeSet<ChessPair<Position, Piece>> list=new TreeSet<>();

    //am nevoie de lista pentru Players.Player
    public TreeSet<ChessPair<Position, Piece>> getList(){
        return list;
    }

    //initializam cu valorile normale
    public void initialize(){
        PieceCreator pc = new PieceCreator();

        this.list.add(new ChessPair<>(new Position('A', 1), pc.getPiece(new Position('A', 1), Colors.WHITE, 'R')));
        this.list.add(new ChessPair<>(new Position('H', 1), pc.getPiece(new Position('H', 1), Colors.WHITE, 'R')));

        this.list.add(new ChessPair<>(new Position('B', 1), pc.getPiece(new Position('B', 1), Colors.WHITE, 'N')));
        this.list.add(new ChessPair<>(new Position('G', 1), pc.getPiece(new Position('G', 1), Colors.WHITE, 'N')));

        this.list.add(new ChessPair<>(new Position('C', 1), pc.getPiece(new Position('C', 1), Colors.WHITE, 'B')));
        this.list.add(new ChessPair<>(new Position('F', 1), pc.getPiece(new Position('F', 1), Colors.WHITE, 'B')));

        this.list.add(new ChessPair<>(new Position('D', 1), pc.getPiece(new Position('D', 1), Colors.WHITE, 'Q')));
        this.list.add(new ChessPair<>(new Position('E', 1), pc.getPiece(new Position('E', 1), Colors.WHITE, 'K')));

        for(char c='A'; c<='H';c++) {
            this.list.add(new ChessPair<>(new Position(c, 2), pc.getPiece(new Position(c, 2), Colors.WHITE, 'P')));
        }

        list.add(new ChessPair<>(new Position('A', 8), pc.getPiece(new Position('A', 8), Colors.BLACK, 'R')));
        list.add(new ChessPair<>(new Position('H', 8), pc.getPiece(new Position('H', 8), Colors.BLACK, 'R')));

        list.add(new ChessPair<>(new Position('B', 8), pc.getPiece(new Position('B', 8), Colors.BLACK, 'N')));
        list.add(new ChessPair<>(new Position('G', 8), pc.getPiece(new Position('G', 8), Colors.BLACK, 'N')));

        list.add(new ChessPair<>(new Position('C', 8), pc.getPiece(new Position('C', 8), Colors.BLACK, 'B')));
        list.add(new ChessPair<>(new Position('F', 8), pc.getPiece(new Position('F', 8), Colors.BLACK, 'B')));

        list.add(new ChessPair<>(new Position('D', 8), pc.getPiece(new Position('D', 8), Colors.BLACK, 'Q')));
        list.add(new ChessPair<>(new Position('E', 8), pc.getPiece(new Position('E', 8), Colors.BLACK, 'K')));

        for(char c='A'; c<='H'; c++) {
            list.add(new ChessPair<>(new Position(c, 7), pc.getPiece(new Position(c, 7), Colors.BLACK, 'P')));
        }
    }

    public void modifyBoard(Piece p){
        this.list.add(new ChessPair<>(p.getPosition(), p));
    }

    public void movePiece(Position from, Position to, boolean isHuman) throws InvalidMoveException, InvalidCommandException {
        if(!isValidMove(from, to)){
            throw new InvalidMoveException();
        } else{
            Piece p=getPieceAt(from);
            Piece provizoriu=getPieceAt(to);

            //le dam remove
            for(ChessPair<Position, Piece> chessPair : list){
                if(chessPair.getCheie().equals(from)){
                    list.remove(chessPair);
                    break;
                }
            }

            //scoatem piesa capturata
            if(provizoriu!=null){
                for(ChessPair<Position, Piece> chessPair : list){
                    if(chessPair.getCheie().equals(to)){
                        list.remove(chessPair);
                        break;
                    }
                }
            }

            //tratam pionul
            if(p.type()=='P'){
                ((Pawn)p).setFirstMove();
            }

            //adaugam piesa mutata
            if(p.type()!='P'){
                p.setPosition(to);
                list.add(new ChessPair<>(to,p));
            } else{
                //tratare pion+promovare
                int y=(int)to.getY();

                if((p.getColor()== Colors.WHITE && y==8)||(p.getColor()== Colors.BLACK && y==1)){
                    char type='Q';

                    if(isHuman){
                        System.out.println("You promoted the pawn!");

                        Scanner scanner=new Scanner(System.in);

                        while(true){
                            System.out.println("Choose the piece(Q|R|B|N)");
                            String input=scanner.nextLine().toUpperCase();
                            if(input.length()==1){

                                char ch=input.charAt(0);
                                if(ch=='Q' || ch=='R' || ch=='B' || ch=='N'){
                                    type=ch;
                                    break;
                                }
                            }

                            System.out.println("Invalid command. CHOOSE FROM THE FOLLOWING (Q|R|B|N)");

                        }

                    }


                    PieceCreator pc = new PieceCreator();
                    Piece promotedPiece=pc.getPiece(to, p.getColor(), type);
                    list.add(new ChessPair<>(to,promotedPiece));
                } else{
                    //mutam pionul normal
                    p.setPosition(to);
                    list.add(new ChessPair<>(to,p));
                }
            }

        }
    }

    public void movePieceForUI(Position from, Position to, boolean isHuman, char promotionType)  throws InvalidMoveException, InvalidCommandException{
        if(!isValidMove(from, to)){
            throw new InvalidMoveException();
        } else{
            Piece p=getPieceAt(from);
            Piece provizoriu=getPieceAt(to);

            //le dam remove
            for(ChessPair<Position, Piece> chessPair : list){
                if(chessPair.getCheie().equals(from)){
                    list.remove(chessPair);
                    break;
                }
            }

            //scoatem piesa capturata
            if(provizoriu!=null){
                for(ChessPair<Position, Piece> chessPair : list){
                    if(chessPair.getCheie().equals(to)){
                        list.remove(chessPair);
                        break;
                    }
                }
            }

            //tratam pionul
            if(p.type()=='P'){
                ((Pawn)p).setFirstMove();
            }

            //adaugam piesa mutata
            if(p.type()!='P'){
                p.setPosition(to);
                list.add(new ChessPair<>(to,p));
            } else{
                //tratare pion+promovare
                int y=(int)to.getY();

                if((p.getColor()== Colors.WHITE && y==8)||(p.getColor()== Colors.BLACK && y==1)){
                    char type=promotionType;

                    if(!isHuman){
                        type='Q';
                    } else{
                        if (type !='Q' &&type != 'R'&& type!= 'B' && type != 'N') {
                            type = 'Q'; //pune regina DEFENSIV
                        }
                    }

                    PieceCreator pc = new PieceCreator();
                    Piece promotedPiece=pc.getPiece(to, p.getColor(), type);
                    list.add(new ChessPair<>(to,promotedPiece));
                } else{
                    //mutam pionul normal
                    p.setPosition(to);
                    list.add(new ChessPair<>(to,p));
                }
            }

        }
    }

    //ia piesa de la pozitia position, parcurgand toate pozitiile
    public Piece getPieceAt(Position position){
        for(ChessPair<Position, Piece> chessPair : list){
            if(chessPair.getCheie().equals(position)){
                return chessPair.getValoare();
            }
        }
        return null;
    }

    public boolean isValidMove(Position from, Position to){
        Piece p=getPieceAt(from);
        Piece King=null;
        char x=(to.getX());
        int y=(int)(to.getY());

        if(x<'A' || x>'H' || y<1 || y>8){
            return false;
        }

        if(p==null) return false;

        List<Position> possibleMoves=new ArrayList<Position>();
        possibleMoves=p.getPossibleMoves(this);

        if(p!=null){
            for(Position pos:possibleMoves){
                if(pos.equals(to)){
                    //(ONLY GOD KNOWS IF THIS WORK)

                    //iau kingul de culoarea piesei pe care vreau sa o mut (regele propriu)
                    for(ChessPair<Position, Piece> chessPair : list){
                        if(chessPair.getValoare().type()=='K' && chessPair.getValoare().getColor()== p.getColor()){
                            King=chessPair.getValoare();
                            break;
                        }
                    }

                    //mut piesa provizoriu, scot in caz de capturare
                    //provizoriu=null daca nu capturam piesa
                    Piece provizoriu=getPieceAt(to);

                    //simulam un remove, la pozitia from si to
                    for(ChessPair<Position, Piece> chessPair : list){
                        if(chessPair.getCheie().equals(from)){
                            list.remove(chessPair);
                            break;
                        }
                    }

                    if(provizoriu!=null){
                        for(ChessPair<Position, Piece> chessPair : list){
                            if(chessPair.getCheie().equals(to)){
                                list.remove(chessPair);
                                break;
                            }
                        }
                    }

                    //simulam mutarea
                    p.setPosition(to);
                    list.add(new ChessPair<>(to,p));

                    boolean isCheck=false;
                    for(ChessPair<Position, Piece> chessPair : list){
                        if(chessPair.getValoare().getColor()!=p.getColor()){
                            if(chessPair.getValoare().checkForCheck(this, King.getPosition())){
                                isCheck=true;
                                break;
                            }
                        }
                    }

                    //nu facem mutarea, deci le bagam la loc
                    for(ChessPair<Position, Piece> chessPair : list){
                        if(chessPair.getCheie().equals(to)){
                            list.remove(chessPair);
                            break;
                        }
                    }

                    p.setPosition(from);
                    list.add(new ChessPair<>(from,p));

                    if(provizoriu!=null){
                        list.add(new ChessPair<>(to,provizoriu));
                    }

                    //returnam in functie de valoarea sahului
                    if(isCheck==true){
                        return false;
                    } else{
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingInCheck(Colors color){
        Position kingPos=null;

        for(ChessPair<Position, Piece> chessPair : list){
            if(chessPair.getValoare().getColor()==color && chessPair.getValoare().type()=='K'){
                kingPos=chessPair.getCheie();
                break;
            }
        }

        //verificam toate piesele inamicului
        for(ChessPair<Position, Piece> chessPair : list){
            Piece inamic= chessPair.getValoare();
            if(inamic.getColor()!=color){
                if(inamic.checkForCheck(this, kingPos)){
                    return true;
                }
            }
        }
        return false;
    }

    //nu merge toString ptr ca vreau sa vad din perspectiva jucatorului mereu
    public String showThatBoard(Colors playerColor){
        StringBuilder s=new StringBuilder();

        if(playerColor== Colors.WHITE){
            s.append("-------------------------------------\n");

            for(int y=8; y>=1; y--){
                s.append(y).append(" |");
                for(char x='A'; x<='H'; x++){
                    Piece p=getPieceAt(new Position(x,y));
                    if(p!=null){
                        s.append(p.toString()).append(" ");
                    } else{
                        s.append("... ");
                    }
                }
                s.append("|\n");
            }
            s.append("-------------------------------------\n");
            s.append("    A   B   C   D   E   F   G   H\n");
        } else {
            s.append("-------------------------------------\n");

            for(int y=1; y<=8; y++){
                s.append(y).append(" |");
                for(char x='H'; x>='A'; x--){
                    Piece p=getPieceAt(new Position(x,y));
                    if(p!=null){
                        s.append(p.toString()).append(" ");
                    } else{
                        s.append("... ");
                    }
                }
                s.append("|\n");
            }
            s.append("-------------------------------------\n");
            s.append("    H   G   F   E   D   C   B   A\n");
        }

        return s.toString();
    }
}
