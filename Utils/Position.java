package Utils;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

public class Position implements Comparable<Position> {
    private char x;
    private int y;

    public Position(char x, int y) {
        if(x>='A' && x<='H'){
            this.x = x;
        } else{
            throw new IllegalArgumentException("x gresit");
        }

        if(y>=1 && y<=8){
            this.y=y;
        } else {
            throw new IllegalArgumentException("y prost");
        }
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(o instanceof Position){
            Position p = (Position)o;
            if(p.x==this.x && p.y==this.y){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "" +this.x+this.y;
    }

    @Override
    public int compareTo(Position p) {
        if(this.y<p.y){
            return -1;
        } else if(this.y>p.y){
            return 1;
        } else if(this.x<p.x){
            return -1;
        } else if(this.x>p.x){
            return 1;
        }
        return 0;
    }
}
