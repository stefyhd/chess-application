package Utils;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonWriterUtil{

    private JsonWriterUtil() {
        ;
    }

    public static void writeUsers(List<User> users, String path){
        JSONArray usersArr = new JSONArray();

        for (User user : users){
            // folosim chestia asta cu hash ca pastreaza ordinea in care pui datele
            Map<String, Object> userObj = new LinkedHashMap<>();

            userObj.put("email", user.getEmail());
            userObj.put("password", user.getPassword());
            userObj.put("points",user.getPoints());

            JSONArray gamesIDs =new JSONArray();
            List<Game> userGames =user.getALLGames();

            if (userGames != null) {
                for (Game game : userGames) {
                    gamesIDs.add(game.getGameID());
                }
            }
            userObj.put("games", gamesIDs);

            usersArr.add(userObj);
        }

        writeToFile(usersArr, path);
    }

    public static void writeGame(Map<Long, Game> gamesMap, String path) {
        JSONArray gamesList = new JSONArray();

        for (Game game : gamesMap.values()) {
            Map<String, Object> gameObj = new LinkedHashMap<>();

            gameObj.put("id", game.getGameID());

            // playeri
            JSONArray playersArr = new JSONArray();
            for (Player p : game.getPlayers()){
                Map<String, Object> pObj =new LinkedHashMap<>();

                if (p.getName().equalsIgnoreCase("Computer")){
                    pObj.put("email", "computer");
                }else{
                    pObj.put("email", p.getName());
                }
                pObj.put("color", p.getColor().toString());

                playersArr.add(pObj);
            }
            gameObj.put("players", playersArr);

            //al cui e randul
            if (game.getPlayers() !=null && game.getPlayerIndex() >= 0 && game.getPlayerIndex() <game.getPlayers().length){
                gameObj.put("currentPlayerColor", game.getPlayers()[game.getPlayerIndex()].getColor().toString());
            }else{
                gameObj.put("currentPlayerColor", "WHITE");
            }

            //BOARDUL
            JSONArray boardArr =new JSONArray();
            if (game.getBoard()!=null && game.getBoard().getList()!=null){
                for (ChessPair<Position, Piece> pair : game.getBoard().getList()) {
                    Map<String, Object> pieceObj = new LinkedHashMap<>();
                    Piece piece = pair.getValoare();

                    pieceObj.put("type", String.valueOf(piece.type()));
                    pieceObj.put("color", piece.getColor().toString());
                    pieceObj.put("position", piece.getPosition().toString());

                    boardArr.add(pieceObj);
                }
            }
            gameObj.put("board", boardArr);

            //MUTARI
            JSONArray movesArr = new JSONArray();
            if (game.getMoves() != null) {
                for (Move m : game.getMoves()) {
                    Map<String, Object> moveObj = new LinkedHashMap<>();
                    moveObj.put("playerColor", m.getPlayerColor().toString());
                    moveObj.put("from", m.getFrom().toString());
                    moveObj.put("to", m.getTo().toString());

                    if (m.getCapturedPiece() != null) {
                        Map<String, Object> capObj = new LinkedHashMap<>();
                        capObj.put("type", String.valueOf(m.getCapturedPiece().type()));
                        capObj.put("color", m.getCapturedPiece().getColor().toString());
                        moveObj.put("captured", capObj);
                    }
                    movesArr.add(moveObj);
                }
            }
            gameObj.put("moves", movesArr);

            gamesList.add(gameObj);
        }

        writeToFile(gamesList, path);
    }

    private static void writeToFile(JSONArray jsonArray, String path) {
        try (FileWriter file = new FileWriter(path)) {
            JSONValue.writeJSONString(jsonArray, file);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}