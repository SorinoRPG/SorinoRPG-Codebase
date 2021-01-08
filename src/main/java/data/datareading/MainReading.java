package data.datareading;

import java.util.Scanner;

class MainReading {
    public static void main(String[] args) {
        while (true) {
            System.out.print(">>");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if(input.equals("END")) return;

            FileCommand fileCommand = FileCommand.getCmd(input);
            fileCommand.action.action(input.substring(input.indexOf(" ")+1));
        }
    }
}
