package data.datareading;

import java.util.Scanner;

class MainReading {
    public static void main(String[] args) {
        while (true) {
            System.out.println(">>");

            Scanner scanner = new Scanner(System.in);
            FileCommand fileCommand = FileCommand.getCmd(scanner.next());

            fileCommand.action.action();
        }
    }
}
