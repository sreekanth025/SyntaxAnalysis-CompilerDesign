import java.util.*;

/*
 * '$' - Epsilon
 * Non-terminals should be upper-case english alphabets.
 * Starting symbol must be 'E'. (Can be changed by modifying the list 'dfaSources' in the code)
 * Terminal symbols should be lower-case english alphabets.
 * */
public class TopDownParsing {

    public static List<List<String>> steps = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        Map<String, List<String>> input = Utils.readInput("q3.txt");
        Utils.showGrammar(input);
        List<String> examples = new ArrayList<>(Arrays.asList(
                "i", "(i)", "i++", "i+++i", "i+i+i", "(i)*i"
        ));

        System.out.println("");

        for(String example: examples) {
            steps = new ArrayList<>();
            System.out.println("Checking if '" + example + "' is acceptable by the given grammar: ");
            Boolean result = isAcceptable(input, example);

            Collections.reverse(steps);
            steps.forEach(step -> System.out.printf("%-10s %-2s -> %s\n", step.get(0), step.get(1), step.get(2)));
            System.out.println("\n" + example + " : " + result + "\n\n");
        }
    }

    public static Boolean isAcceptable(Map<String, List<String>> productionRules, String inputString) {

        List<String> keys = new ArrayList<>(productionRules.keySet());
        List<String> dfsSources = new ArrayList<>();
        dfsSources.add("E");

        /*
        * First set is for storing all the symbols that leads to epsilon.
        * Others will go into nonEpsilonAble set.
        * */
        Set<String> epsilonAble = new HashSet<>();
        Set<String> notEpsilonAble = new HashSet<>();


        /*
        * Rule 1: If it has an epsilon production, it is epsilonAble
        * Rule 2: If it has a production with all non-terminals
        *   and each of them are epsilonAble, then the Symbol is epsilonAble
        * */

        for(int i=0; i<keys.size(); i++) {

            /*
            * All the symbols are classified into either of the sets
            * */
            if(epsilonAble.size() + notEpsilonAble.size() == productionRules.size())
                break;

            String left = keys.get(i);
            List<String> right = productionRules.get(left);
            Boolean finished = false;
            Integer NotEpsilonAbleCount = 0;

            for(String s: right) {

                if(s.equals("$")) {
                    epsilonAble.add(left);
                    finished = true;
                    break;
                }
                else if (containsTerminal(s)) {
                    NotEpsilonAbleCount++;
//                    notEpsilonAble.add(left);
                    continue;
                }
                else {
                    // All the characters are non-terminals

                    int epsilonCount = 0;
                    for(Character c: s.toCharArray()) {
                        if(epsilonAble.contains(c.toString())) {
                            epsilonCount++;
                        }
                        else if (notEpsilonAble.contains(c.toString())) {
                            NotEpsilonAbleCount++;
//                            notEpsilonAble.add(left);
                            break;
                        }
                    }

                    if(epsilonCount == s.length()) {
                        epsilonAble.add(left);
                        finished = true;
                        break;
                    }
                }
            }

            if(NotEpsilonAbleCount == right.size()) {
                finished = true;
                notEpsilonAble.add(left);
            }

            /*
            * If there exists a rule A -> BC, for which
            * we don't know yet whether B is epsilonAble or not,
            * then we will check the rule later
            * */
            if(!finished) {
                keys.add(left);
            }
        }

        for(String s: dfsSources) {
            if(dfs(inputString, s, productionRules, new HashSet<String>(), epsilonAble)) {
                return true;
            }
        }

        return false;
    }

    public static Boolean dfs(
            String target,
            String formed,
            Map<String, List<String>> productionRules,
            Set<String> visited,
            Set<String> epsilonAble) {

//        System.out.println("Debug: continuing dfs, formed: " + formed);
        if(target.equals(formed)) return true;

        visited.add(formed);
        for(int i=0; i<formed.length(); i++) {
            Character ch = formed.charAt(i);
            if(isNonTerminal(ch)) {

                List<String> right = productionRules.get(ch.toString());
                for(String s: right) {
                    if(s.equals("$"))
                        s = "";

                    String possible = formed.substring(0, i) + s + formed.substring(i+1);

                    if(isValid(possible, target, epsilonAble) && !visited.contains(possible)) {
                        visited.add(possible);
                        if(dfs(target, possible, productionRules, visited, epsilonAble)) {
                            if(s=="") s = "$";
                            steps.add(new ArrayList<>(Arrays.asList(formed, ch.toString(), s)));
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Boolean isNonTerminal(Character c) {
        return ('A' <= c && c <= 'Z');
    }

    public static Boolean containsTerminal(String s) {

        for(Character c: s.toCharArray()) {
//            if('a' <= c && c <= 'z')
            if(!isNonTerminal(c))
                return true;

        }

        return false;
    }

    public static Boolean isValid(
            String s,
            String target,
            Set<String> epsilonAble) {

        int k=0;
        while (k<s.length() && k<target.length()) {
            if(isNonTerminal(s.charAt(k)))
                break;

            if(s.charAt(k) != target.charAt(k))
                return false;

            k++;
        }

        int count=0;
        for(int i=0; i<s.length(); i++) {
            if(isNonTerminal(s.charAt(i)) && epsilonAble.contains("" + s.charAt(i)))
                count++;
        }

        /*
        * count -> Total number of epsilonAble non-terminals in s.
        * If there are more than required number of terminals, then it is not valid.
        * */
        if(target.length() < s.length()-count) {
            return false;
        }

        return true;
    }
}
