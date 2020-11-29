package data.datareading;

import java.util.Scanner;

class MainReading {
    public static void main(String[] args) {
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.println(">>");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            FileCommand fileCommand = FileCommand.getCmd(input.substring(0, input.indexOf(" ")));

            fileCommand.action.action(input.substring(input.indexOf(" ")).trim());
        }
    }
}
