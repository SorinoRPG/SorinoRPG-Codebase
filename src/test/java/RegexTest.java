import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestOnly
public class RegexTest {
    @TestOnly
    public static void main(String[] args) {
        String move = "Honi Hold";
        System.out.println(move.substring(0, move.indexOf(" "))  + ": hello");
    }
}
