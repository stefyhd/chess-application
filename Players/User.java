package Players;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String email, password;
    private List<Game> userGames=new ArrayList<>();
    private int totalPoints=0;

    public User(){
        this.userGames=new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addGame(Game game){
        userGames.add(game);
    }

    public void removeGame(Game game){
        userGames.remove(game);
    }

    public List<Game> getActiveGames(){
        List<Game> activeGames=new ArrayList<>();
        for(Game game:userGames){
            if(game.getLoser()==-1 && !game.Remise()){
                activeGames.add(game);
            }
        }

        return activeGames;
    }

    public int getPoints(){
        return totalPoints;
    }

    public void setPoints(int points){
        this.totalPoints+=points;
    }

    public List<Game> getALLGames(){
        return this.userGames;
    }
}
