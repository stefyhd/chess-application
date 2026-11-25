package Exceptions;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(){
        super("Invalid command");
    }
}
