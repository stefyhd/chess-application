package Utils;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

public class ChessPair<K extends Comparable<K>, V> implements Comparable<ChessPair<K, V>>{
    private K cheie;
    private V valoare;

    public ChessPair(K cheie, V valoare){
        this.cheie = cheie;
        this.valoare = valoare;
    }

    public int compareTo(ChessPair<K, V> chessPiece){
        return this.cheie.compareTo(chessPiece.cheie);
    }

    public K getCheie() {
        return cheie;
    }

    public V getValoare() {
        return valoare;
    }

    @Override
    public String toString(){
        return cheie.toString() + "-" + valoare.toString();
    }
}
