import java.io.IOException;
import java.util.*;

/*
* '$' - Epsilon
* Non-terminals should be between 'A' and 'L' in the input file.
* Terminal symbols should be lower-case english alphabets.
* */
public class LeftRecursion {

    static int c = -1;
    public static String getNextChar() {
        return "" + ((char)('M' + (++c)));
    }

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> input = Utils.readInput("q1.txt");
        Utils.showGrammar(input);
        System.out.println("");

        Map<String, List<String>> result = eliminateLR(input);
        Utils.showGrammar(result);
    }

    public static Map<String, List<String>> eliminateLR(Map<String, List<String>> input) {

        List<String> keys = new ArrayList<>(input.keySet());
        keys = arrangeKeys(keys);
        Map<String, List<String>> processed = new HashMap<>();


        for(int k=0; k<keys.size(); k++) {

            String left = keys.get(k);
            List<String> right = input.get(left);

            /*
            * Replacing each production of the form Ai -> AjX with
            * Ai -> d1X | d2X | d3X ..... | dkX
            * where Aj -> d1 | d2 | d3 ... | dk
            * (Aj is already processed)
            * */
            for(int i=0; i<right.size(); i++) {

                if(processed.get("" + right.get(i).charAt(0)) != null) {
                    String result = right.get(i);
                    right.remove(result);

                    for(String s: processed.get("" + result.charAt(0))) {
                        right.add(s + result.substring(1));
                    }
                }
            }


            /*
            * Lists for storing productions with
            * immediate left recursion and others respectively
            * */
            List<String> leftRecursion = new ArrayList<>();
            List<String> noLeftRecursion = new ArrayList<>();

            for(int i=0; i<right.size(); i++) {
                if(left.charAt(0) == right.get(i).charAt(0)) {
                    leftRecursion.add(right.get(i).substring(1));
                }
                else {
                    if(right.get(i).charAt(0) == '$')
                        noLeftRecursion.add("");
                    else
                        noLeftRecursion.add(right.get(i));
                }
            }


            int ilrSize = leftRecursion.size();
            String newChar = getNextChar();

            if(ilrSize != 0) {

                String nextChar = newChar;
                List<String> newRight = new ArrayList<>();

                for(String s: noLeftRecursion) {
                    s += nextChar;
                    if(s.charAt(0) == '$') s = s.substring(1); // Removing epsilon
                    newRight.add(s);
                }

                processed.put(left, newRight);

                newRight = new ArrayList<>();
                for(String s: leftRecursion) {
                    s += nextChar;
                    if(s.charAt(0) == '$') s = s.substring(1); // Removing epsilon
                    newRight.add(s);
                }

                newRight.add("$");
                processed.put(nextChar, newRight);
            }
            else {
                processed.put(left, noLeftRecursion);
            }

        }

        return processed;
    }

    public static List<String> arrangeKeys(List<String> keys) {
//        Natural order of keys
        return keys;
    }
}