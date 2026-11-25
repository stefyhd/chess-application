package Utils;

import Game.*;
import Pieces.*;
import Players.*;
import Points.*;
import Exceptions.*;
import UIs.*;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {
    private List<User> allUsers=new ArrayList<User>();
    private Map<Long, Game> allGames=new HashMap<Long, Game>();
    User currentUser=null;

    private final Scanner sc=new Scanner(System.in);
    private static Main instance;

    //Singleton design pattern
    private Main(){
        ;
    }

    public void setCurrentUser(User user){
        currentUser=user;
    }

    public User getCurrentUser(){
        return currentUser;
    }
    public static Main getInstance(){
        if(instance==null){
            instance=new Main();
        }
        return instance;
    }

    public Map<Long, Game> getAllGames() {
        return allGames;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }


    //citeste tot din JSON
    public void read(){
        try{
           System.out.println("Preparing game...");
            System.out.println("Loading games...");
            this.allGames= JsonReaderUtil.readGamesAsMap(Path.of("input/games.json"));

            System.out.println("Loading users...");
            this.allUsers=JsonReaderUtil.readUsers(Path.of("input/accounts.json"), this.allGames);
            System.out.println("Loading...");
        } catch(IOException| ParseException e){
            System.err.println("Error "+e.getMessage());
            System.exit(1);
        }
    }

    //scrie tot in JSON (pray it works)
    public void write(){
        System.out.println("Saving games...");
        JsonWriterUtil.writeGame(this.allGames, "input/games.json");
        JsonWriterUtil.writeUsers(this.allUsers, "input/accounts.json");
        System.out.println("Done! Everything is in order!");
    }

    public User login(String email, String password){
        for(User user:allUsers){
            if(user.getEmail().equals(email) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public User newAccount(String email, String password){
        User newUser=new User(email, password);
        this.allUsers.add(newUser);
        return newUser;
    }

    public boolean userExists(String email){
        for(User user:allUsers){
            if(user.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    //sa alegem ce strategie e bune
    public ConditionPointsStrategy chooseStrategy(String result){
        switch(result){
            case "WIN":
               return new WinConditionStrategy();
            case "LOSE":
               return new LossConditionStrategy();
            case "DRAW":
                return new DrawResultStrategy();
            case "RESIGN":
                return new ResignConditionStrategy();
            case "PAT":
                return new PatResultStrategy();
            default:
                return null;
        }
    }

    //a cam devenit meniu -> play face gameplay-ul
    public void run(){
        boolean isPlaying=true;
        while(isPlaying){
            if(currentUser==null){
                System.out.print("Please enter your email: ");
                String email=sc.nextLine();
                System.out.print("Please enter your password: ");
                String password=sc.nextLine();

                this.currentUser=login(email, password);

                if(userExists(email)){
                    if(this.currentUser!=null){
                        System.out.println("Welcome back, Players.Player "+currentUser.getEmail() +"!");
                        System.out.println("Your points: "+currentUser.getPoints());
                    } else{
                        System.out.println("Wrong Password!");
                        continue;
                    }
                } else if(this.currentUser==null){
                    System.out.println("New account will be generated shortly");
                    this.currentUser=newAccount(email, password);
                    System.out.println("New account has been created");
                    System.out.println("Welcome, Players.Player " + currentUser.getEmail() +"!");
                    System.out.println("Your points: " + currentUser.getPoints());
                }
            //jocul in sine
            } else{
                System.out.println("What do you want to do, today?");
                System.out.println("1. New Game.Game?");
                System.out.println("2. Continue a Game.Game?");
                System.out.println("3. Log out?");
                System.out.println("Choose: (1|2|3)");

                String choice=sc.nextLine();
                switch(choice){
                    case "1":
                        startNewGame();
                        break;
                    case "2":
                        resumeGame();
                        break;
                    case "3":
                        currentUser=null;
                        System.out.println("Want to play again? (Y/N)");

                        while(true){
                            String playAgain=sc.nextLine().toUpperCase();
                            if(playAgain.equals("Y")){
                                break;
                            } else if(playAgain.equals("N")){
                                isPlaying=false;
                                break;
                            } else{
                                System.out.println("Invalid choice");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice");

                }
            }
        }
        //scriem doar cand se incheie programul
        write();
    }

    //in general alegerea culorii si apelarea functiei joc(joc nou)
    public void startNewGame(){
        System.out.println("Starting New Game.Game...");

        Colors playerColor = Colors.GRAY;
        while(playerColor == Colors.GRAY){
            System.out.println("Choose your color (WHITE - W/ BLACK- B):");
            String colorInput=sc.nextLine();
            if(colorInput.equals("WHITE") || colorInput.equals("W") || colorInput.equals("w") || colorInput.equals("white")){
                playerColor=Colors.WHITE;
            } else if(colorInput.equals("BLACK") || colorInput.equals("B") || colorInput.equals("b") || colorInput.equals("black")){
                playerColor=Colors.BLACK;
            } else{
                System.out.println("Invalid color");
            }
        }

        long maxId=0;
        for(Long id:allGames.keySet()){
            if(id>maxId){
                maxId=id;
            }
        }

        int newId=(int) (maxId+1);
        Board newBoard=new Board();
        Game newGame=new Game(newId, newBoard, playerColor, this.currentUser.getEmail());
        newGame.start();

        allGames.put((long)newId, newGame);
        currentUser.addGame(newGame);

        play(newGame);

    }

    //sa am si varianta in terminal daca se cere
    public Game startNewGameForUI(Colors playerColor){
        long maxId=0;
        for(Long id:allGames.keySet()){
            if(id>maxId){
                maxId=id;
            }
        }

        int newId=(int) (maxId+1);
        Board newBoard=new Board();
        Game newGame=new Game(newId, newBoard, playerColor, this.currentUser.getEmail());
        newGame.start();

        allGames.put((long)newId, newGame);
        currentUser.addGame(newGame);

        return newGame;
    }

    public void resumeGame(){
        if(currentUser==null){
            return;
        }

        System.out.println("What game do you want to resume?");

        List<Game> activeGames=currentUser.getActiveGames();

        if(activeGames.isEmpty()) {
            System.out.println("There are no active games! You have to start a new game!");
            System.out.println("NewGame(1)|Exit(2)");

            String option = sc.nextLine();
            if (option.equals("1")) {
                startNewGame();
            } else if (option.equals("2")){
                return;
            } else{
                System.out.println("Invalid option");
            }
        }
        while(true) {
            System.out.println("Your active games: ");
            for (Game game : activeGames) {
                String myColor = game.getPlayers()[0].getColor().toString();
                System.out.println("ID: " + game.getGameID() + " | YOUR COLOR:" + myColor);
            }

            System.out.println("Enter game ID or type exit to exit: ");
            String choice = sc.nextLine();
            if(choice.equals("exit") || choice.equals("Exit") || choice.equals("EXIT")) {
                break;
            }
            try {
                int gameId = Integer.parseInt(choice);
                Game gameToResume = null;
                for (Game game : activeGames) {
                    if (game.getGameID() == gameId) {
                        gameToResume = game;
                        break;
                    }
                }
                if (gameToResume == null) {
                    System.out.println("Invalid GameID");
                } else {
                    System.out.println("Resuming...");
                    gameToResume.resume();
                    play(gameToResume);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Enter a number");
            }
        }
    }

    //punctele pe capturi nu se mai fac in clasa player! (update)
    public void pointsCalculator(Game game, String gameResult){
        Player humanPlayer=game.getPlayers()[0];

        CapturePointsStrategy cap=new CapturePoints();
        int capturePoints=cap.calculateCapturePoints(humanPlayer);

        ConditionPointsStrategy cond=this.chooseStrategy(gameResult);
        int adaos=cond.calcutePoints(capturePoints);

        currentUser.setPoints(adaos);
    }

    //efectiv ce se intampla in joc
    public void play(Game game){
        boolean isPlaying=true;
        Player[] players=game.getPlayers();
        System.out.println("Bafta!!");
        boolean showBoard=true;
        while(isPlaying){
            if(showBoard){
                System.out.println(game.toString());
                if(game.getPlayerIndex()==0){
                    System.out.println("YOUR TURN!! \n Select the piece you want to move (FORMAT (A-H)(1-8)) \n Type your move: (FORMAT (A-H)(1-8)-(A-H)(1-8)) \n Type exit to exit: \n ");
                } else{
                    System.out.println("COMPUTERS TURN");
                }
                showBoard=false;
            }


            //ptr jucatir
            if(game.getPlayerIndex()==0){

                if(game.getBoard().isKingInCheck(players[0].getColor())){
                    System.out.println("YOU ARE IN CHECK");
                }
                String comand=sc.nextLine();
                if(comand.equals("exit")){
                    isPlaying=false;
                    break;
                }

                if(comand.equals("resign")){
                    System.out.println("You resigned from the game!:(");
                    pointsCalculator(game,"RESIGN");

                    allGames.remove((long)game.getGameID());
                    currentUser.getALLGames().remove(game);
                    isPlaying=false;
                    break;
                }

                //verificari in plus, ptr orice situatie, mai bine mai tratez odata
                try{
                    if(comand.length()==2){
                        char x=comand.charAt(0);
                        int y=Integer.parseInt(comand.substring(1));
                        Position pos=new Position(x, y);

                        if(x<'A' || x>'H' || y<1 || y>8){
                            System.out.println("Invalid coordinate");
                            continue;
                        }

                        Piece p=game.getBoard().getPieceAt(pos);
                        if(p==null){
                            System.out.println("That chess square is empty " +pos.toString());

                        } else if(p.getColor()!=players[0].getColor()){
                            System.out.println("That piece doesn't belong to you");
                        } else{
                            System.out.println("Selected piece: "+p.type() + " at "+pos.toString());
                            System.out.println("Possible moves: ");

                            List<Position> possibleMoves=p.getPossibleMoves(game.getBoard());
                            boolean found=false;

                            for(Position to:possibleMoves){
                                if(game.getBoard().isValidMove(pos, to)){
                                    System.out.print(pos.toString()+ "-" + to.toString()+ " ,");
                                    found=true;
                                }
                            }

                            if(!found){
                                System.out.println("That piece has nowhere to be moved.");
                            }
                            System.out.println("\n");
                        }
                        continue;
                    }

                    if(comand.length()==5){
                        char from1=comand.charAt(0);
                        int from2=Integer.parseInt(comand.substring(1,2));
                        char separator=comand.charAt(2);
                        char to1=comand.charAt(3);
                        int to2=Integer.parseInt(comand.substring(4,5));

                        if(from1<'A' || from1>'H' || from2<1 || from2>8){
                            System.out.println("FROM Utils.Position is invalid. Pieces.Piece not on board! \n Try again:(FORMAT (A-H)(1-8)-(A-H)(1-8))");
                            continue;
                        }
                        if(separator!='-'){
                            System.out.println("Invalid input. Add '-' symbol!!");
                            continue;
                        }
                        if(to1<'A' || to1>'H' || to2<1 || to2>8){
                            System.out.println("TO Utils.Position is invalid. Pieces.Piece not on board! \n Try again:(FORMAT (A-H)(1-8)-(A-H)(1-8))");
                            continue;
                        }

                        Position from=new Position(from1,from2);
                        Position to=new Position(to1,to2);

                        Piece pieceToMove=game.getBoard().getPieceAt(from);

                        if(pieceToMove==null){
                            System.out.println("There's no piece on" + from.toString());
                            continue;
                        }

                        if(pieceToMove.getColor()!= players[0].getColor()){
                            System.out.println("That piece is not yours! Can't move opponents pieces");
                            continue;
                        }

                        if (!game.getBoard().isValidMove(from, to)) {
                            System.out.println("Not a valid Game.Move");
                            continue;
                        }

                        //luam piesa capturata
                        Piece capturedPiece = game.getBoard().getPieceAt(to);
                        game.getPlayers()[0].makeMove(from, to, game.getBoard());

                        //poate sa fie si null captured
                        game.addMove(game.getPlayers()[0], from, to, capturedPiece);
                        game.switchPlayer();
                        showBoard=true;
                    } else{
                        System.out.println("STICK TO THE FORMATS!!");
                    }
                } catch(InvalidMoveException e){
                    System.out.println("Rejected!" + e.getMessage());
                } catch(InvalidCommandException e){
                    System.out.println("Comanda gresita" + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error!" + e.getMessage());
                }
            } else{
                //randul computerului
                game.automaticComputerMoves();
                game.switchPlayer();
                showBoard=true;
            }
            //schema de calcul final al punctelor
            //chiar n am idee de ce if-ul ala nu e aliniat bine
            game.checkForCheckMate();
           if(game.Remise() || game.getLoser()!=-1){
               System.out.println("GAME OVER!");
               System.out.println(game.messageToBeDisplayed);
               if(game.Remise()){
                   pointsCalculator(game,"DRAW");
               } else if(game.getLoser()==1){
                   System.out.println("YOU WON!");
                   pointsCalculator(game,"WIN");
               } else{
                   System.out.println("YOU LOST!");
                   pointsCalculator(game,"LOSE");
               }

               allGames.remove((long)game.getGameID());
               currentUser.getALLGames().remove(game);

               isPlaying=false;
               break;
           }
        }
    }

//    //main-ul ;)
//    public static void main(String[] args){
//        Main main = Main.getInstance();
//        main.read();
//        main.run();
//    }

    public static void main(String[] args) {
        Main.getInstance().read();
        new LoginUI().setVisible(true);
    }
}
