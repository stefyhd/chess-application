package Utils;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading JSON documents using JSON.simple ("simple-json").
 *
 * ###### IMPORTANT: This is just an example of how to read JSON documents using the library.
 * Your classes might differ slightly, so don’t hesitate to update this class as needed.
 *
 * Expected structures:
 * - accounts.json: an array of objects with fields: email (String), password (String), points (Number), games (array of numbers)
 * - games.json: an array of objects with fields matching the JSON provided:
 *   id (Number), players (array of {email, color}), currentPlayerColor (String),
 *   board (array of {type, color, position}), moves (array of {playerColor, from, to})
 */
public final class JsonReaderUtil {

    private JsonReaderUtil() {
    }

    /**
     * Reads the accounts from the given JSON file path.
     *
     * @param path path to accounts.json
     * @return list of Account objects (empty list if file not found or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static List<User> readUsers(Path path, Map<Long, Game> existingGames) throws IOException, ParseException {
        if (path == null || !Files.exists(path)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            List<User> result = new ArrayList<>();

            if (arr == null) {
                return result;
            }

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }

                User acc = new User();
                acc.setEmail(asString(obj.get("email")));
                acc.setPassword(asString(obj.get("password")));
                acc.setPoints(asInt(obj.get("points"), 0));

                JSONArray games = asArray(obj.get("games"));
                if (games != null) {
                    for (Object gidO : games) {
                        long gid=asLong(gidO, -1);
                        if(existingGames.containsKey(gid)) {
                            //gasim jocurile ptr fiecare user/account si le adaugam la citirea accounturilor
                            Game foundGame=existingGames.get(gid);
                            acc.addGame(foundGame);
                        }
                    }
                }
                result.add(acc);
            }
            return result;
        }
    }

    /**
     * Reads the games from the given JSON file path and returns them as a map by id.
     * The structure strictly follows games.json as provided (no title/genre).
     *
     * @param path path to games.json
     * @return map id -> Game.Game (empty if file missing or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static Map<Long, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Long, Game> map = new HashMap<>();
        //mic debugging ca nu-mi gasea fieiserul
        if (path == null || !Files.exists(path)) {
            System.err.println("Fisierul nu a fost gasit");
            if (path != null) {
                System.err.println("Asta e patul la car el-am cautat " + path.toAbsolutePath());
            }
            return map;
        } else {
            System.out.println("Asta e pathul la care l-am cautat: " + path.toAbsolutePath());
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);

            if (arr == null)
                return map;

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }

                long id = asLong(obj.get("id"), -1);
                if (id < 0) {
                    continue;
                }// skip invalid

                Board board = new Board();
                JSONArray boardArr = asArray(obj.get("board"));

                if (boardArr != null) {
                    for (Object bItem : boardArr) {
                        JSONObject bObj = asObject(bItem);
                        if (bObj == null) {
                            continue;
                        }

                        PieceCreator pieceCreator = new PieceCreator();
                        String typeS = asString(bObj.get("type"));
                        String colorS = asString(bObj.get("color"));
                        String positionS = asString(bObj.get("position"));
                        char type=typeS.charAt(0);
                        Colors color;
                        if(colorS.equals("WHITE")) {
                            color=Colors.WHITE;
                        } else{
                            color=Colors.BLACK;
                        }
                        Position position=new Position(positionS.charAt(0), Integer.parseInt(positionS.substring(1)));

                        Piece p=pieceCreator.getPiece(position,color,type);
                        board.modifyBoard(p);
                    }
                }

                // players array
                String playerName="KAHSIOH";
                String playerColor="GFSHDA";
                Colors pColor=Colors.WHITE;
                JSONArray playersArr = asArray(obj.get("players"));

                if (playersArr != null) {
                    for (Object pItem : playersArr) {
                        JSONObject pObj = asObject(pItem);
                        if (pObj == null) {
                            continue;
                        }

                        String email = asString(pObj.get("email"));
                        String colorS = asString(pObj.get("color"));

                        if(!email.equals("computer")) {
                            playerName=email;
                            playerColor=colorS;

                            if(playerColor.equals("WHITE")){
                                pColor=Colors.WHITE;
                            } else {
                                pColor=Colors.BLACK;
                            }
                            break;
                        }
                    }
                }

                Game g = new Game((int) id, board, pColor, playerName);
                g.setId((int) id);



                // currentPlayerColor
                String currentPlayer=asString(obj.get("currentPlayerColor"));
                Colors currentColor;
                if(currentPlayer.equals("WHITE")) {
                    currentColor=Colors.WHITE;
                } else{
                    currentColor=Colors.BLACK;
                }
                g.setTurn(currentColor);


                // Parse optional moves array
                JSONArray movesArr = asArray(obj.get("moves"));
                if (movesArr != null) {
                    List<Move> moves = new ArrayList<>();
                    for (Object mItem : movesArr) {
                        JSONObject mObj = asObject(mItem);
                        if (mObj == null) {
                            continue;
                        }

                        String playerColors = asString(mObj.get("playerColor"));
                        String fromString = asString(mObj.get("from"));
                        String toString = asString(mObj.get("to"));

                        Position from=new Position(fromString.charAt(0), Integer.parseInt(fromString.substring(1)));
                        Position to=new Position(toString.charAt(0), Integer.parseInt(toString.substring(1)));

                        Colors moveColor;
                        if(playerColors.equals("WHITE")) {
                            moveColor=Colors.WHITE;
                        } else{
                            moveColor=Colors.BLACK;
                        }
                        Piece capturedPiece = null;
                        JSONObject capObj = asObject(mObj.get("captured"));

                        if(capObj != null) {
                            String capTypeS = asString(capObj.get("type"));
                            String capColorS = asString(capObj.get("color"));

                            if(capTypeS != null && capColorS != null) {
                                char capType = capTypeS.charAt(0);
                                Colors capColor = capColorS.equals("WHITE") ? Colors.WHITE : Colors.BLACK;

                                PieceCreator pc = new PieceCreator();
                                capturedPiece = pc.getPiece(to, capColor, capType);
                            }
                        }
                        Move m=new Move(moveColor, from, to, capturedPiece);
                        moves.add(m);
                    }
                    g.setMoves(moves);
                }
                map.put(id, g);
            }
        }
        return map;
    }

    // -------- helper converters --------

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static long asLong(Object o, long def) {
        if (o instanceof Number) return ((Number) o).longValue();
        try {
            return o != null ? Long.parseLong(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
