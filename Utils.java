import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utils {

    public static Map<String, List<String>> readInput(String fileName) throws IOException {

        Map<String, List<String>> input = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("input/" + fileName));

        String str = "";
        while((str = br.readLine()) != null) {

            int l = str.indexOf("->");

            String left = str.substring(0, 1).trim();
            String right = str.substring(l+2).trim();

            String[] tokens = right.split("[|]");
            input.put(left, new ArrayList<>(Arrays.asList(tokens)));
        }
        
        br.close();
        return input;
    }

    public static void showGrammar(Map<String, List<String>> grammar) {

        for(Map.Entry<String, List<String>> entry: grammar.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
