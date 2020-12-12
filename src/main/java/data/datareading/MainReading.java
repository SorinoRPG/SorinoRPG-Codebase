package data.datareading;

import java.util.Scanner;

class MainReading {
    public static void main(String[] args) {
        while (true) {
            System.out.println(">>");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            FileCommand fileCommand = FileCommand.getCmd(input);
            if(fileCommand.equals(FileCommand.ERROR)) return;
            fileCommand.action.action(input);
        }
    }
}
