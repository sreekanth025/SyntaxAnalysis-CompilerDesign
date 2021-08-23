import java.util.*;

/*
 * '$' - Epsilon
 * Non-terminals should be between 'A' and 'L' in the input file.
 * Terminal symbols should be lower-case english alphabets.
 * */
public class LeftFactoring {

    static int c = -1;
    public static String getNextChar() {
        return "" + ((char)('M' + (++c)));
    }

    public static void main(String[] args) throws Exception {
        Map<String, List<String>> input = Utils.readInput("q2.txt");
        Utils.showGrammar(input);
        System.out.println("");

        Map<String, List<String>> result = leftFactoring(input);
        Utils.showGrammar(result);
    }

    public static Map<String, List<String>> leftFactoring(Map<String, List<String>> input) {

        Map<String, List<String>> result = new HashMap<>();
        for(Map.Entry<String, List<String>> entry: input.entrySet()) {
            result = addProductions(result, process(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    public static Map<String, List<String>> process(String left, List<String> right) {

        Map<String, List<String>> result = new HashMap<>();
        String commonPrefix = longestPrefixCommonToMoreThanOneElement(right);

        while (commonPrefix.length() > 0) {

            if(result.containsKey(left)) right = result.get(left);

            String newLeft = getNextChar();
            List<String> newRight = new ArrayList<>();
            List<String> toBeRemoved = new ArrayList<>();

            for(int i=0; i<right.size(); i++) {

                if(right.get(i).startsWith(commonPrefix)) {
                    String str = right.get(i);
                    String notCommon = str.substring(commonPrefix.length());
                    newRight.add(notCommon);
                    toBeRemoved.add(str);
                }
            }

            right.add(commonPrefix + newLeft);

            result.put(left, right);
            result.put(newLeft, newRight);

            for(String s: toBeRemoved) {
                right.remove(s);
            }

            commonPrefix = longestPrefixCommonToMoreThanOneElement(right);
            if(right.size() <= 1) break;
        }

        return result;
    }

    public static String longestPrefixCommonToMoreThanOneElement(List<String> list) {
        String result = "";
        Integer resLen = 0;

        for(int i=0; i<list.size(); i++) {
            for(int j=i+1; j<list.size(); j++) {
                resLen = Math.max(resLen, commonPrefixLength(list.get(i), list.get(j)));
                if(resLen > result.length()) {
                    result = list.get(i).substring(0, resLen);
                }
            }
        }
        return result;
    }

    private static int commonPrefixLength(String s1, String s2) {
        int i=0;
        while(i < s1.length() && i < s2.length()) {
            if(s1.charAt(i) == s2.charAt(i)) i++;
            else break;
        }
        return i;
    }

    public static Map<String, List<String>> addProductions(Map<String, List<String>> a, Map<String, List<String>> b) {

        Map<String, List<String>> result = new HashMap<>();
        for(Map.Entry<String, List<String>> entry: a.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, List<String>> entry: b.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
