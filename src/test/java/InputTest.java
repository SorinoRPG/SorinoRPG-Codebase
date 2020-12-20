public class InputTest {
    public static void main(String[] args) {
        String input = "AWARD_COINS 759068859348025405 672883208811184166 200";
        String newInput = input.substring(input.indexOf(" ")+1);
        String guildID = newInput.substring(0, newInput.indexOf(" "));
        String userID = newInput.substring(newInput.indexOf(" ")+1, newInput.lastIndexOf(" "));
        int coins = Integer.parseInt(newInput.substring(newInput.lastIndexOf(" ")+1));
        System.out.println(guildID + "/" + userID + "/" + coins);
    }
}
