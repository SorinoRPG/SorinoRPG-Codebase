package main.userinterface.parser;

public class StringParseException extends Exception{
    String properFormat;

    StringParseException(String properFormat){
        this.properFormat = properFormat;
    }

    public String getProperFormat(){
        return this.properFormat;
    }
}
