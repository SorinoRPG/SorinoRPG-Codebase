import main.userinterface.Prefix;

public class InputTest {
    public static void main(String[] args) {
        String input = "-=Fight @aidsa";
        input = Prefix.removeFightPrefix(input);
        System.out.println(input.substring(0, input.indexOf(" ")) + "hi");
    }
}
