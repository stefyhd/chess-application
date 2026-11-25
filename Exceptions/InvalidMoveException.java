package Exceptions;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException() {
        super("Invalid move. Try again.");
    }
}
