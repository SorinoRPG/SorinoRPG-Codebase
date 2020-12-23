package data.datareading;

import java.util.Scanner;

class MainReading {
    public static void main(String[] args) {
        FileCommand.CMD.action.action("");
        while (true) {
            System.out.print(">>");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            FileCommand fileCommand = FileCommand.getCmd(input);
            if(fileCommand.equals(FileCommand.ERROR)) return;
            fileCommand.action.action(input.substring(input.indexOf(" ")+1));
        }
    }
}
