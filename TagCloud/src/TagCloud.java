import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This Java program will generate a tag cloud from a given input text. The
 * input file can be an arbitrary text file, The output shall be a single
 * well-formed HTML file displaying the name of the input file in a heading
 * followed by a tag cloud of the N words with the highest count in the input.
 *
 * @author Bowei Kou
 *
 */

public final class TagCloud {

    /**
     * total number of font size.
     */
    static final int SIZE = 37;
    /**
     * basic font size.
     */
    static final int BASIC = 11;

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloud() {
    }

    /**
     * Compare Map's key in number order.
     */
    private static class Sort1
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue() - o1.getValue();
        }
    }

    /**
     * Compare Map's key in lexicographic order.
     */
    private static class Sort2
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            String s1 = o1.getKey().toLowerCase();
            String s2 = o2.getKey().toLowerCase();
            int r = s1.compareTo(s2);
            if (r == 0) {
                r = o1.getKey().compareTo(o2.getKey());
            }
            return r;
        }

    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @requires charSet is empty
     * @ensures charSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";
        assert charSet.size() != 0 : "Violation of: charSet is empty";

        for (int i = 0; i < str.length(); i++) {
            if (!charSet.contains(str.charAt(i))) {
                charSet.add(str.charAt(i));
            }
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int i = position; //check the length of word or separator
        String str = ""; //result
        StringBuilder s = new StringBuilder(str);
        if (separators.contains(text.charAt(position))) {
            while (i < text.length() && separators.contains(text.charAt(i))) {
                s.append(text.charAt(i));
                i++;
            }
        } else {
            while (i < text.length() && !separators.contains(text.charAt(i))) {
                s.append(text.charAt(i));
                i++;
            }
        }
        str = s.toString();
        return str;
    }

    /**
     * Store the word into the map's key and then store the count of word into
     * map's value.
     *
     * @param input
     *            input stream with the input file
     * @param m
     *            map which store the word and number of word in input file
     * @replace m
     * @requires input is opened
     * @ensures m's keys are words and values are count of words
     */
    private static void readWord(BufferedReader input, Map<String, Integer> m) {
        assert input != null : "Violation of: output must open";
        /*
         * Define separator characters
         */
        final String sep = " \t\n\r,-.!?[]';:/()"; // separator string
        Set<Character> s = new HashSet<>(); // the set store the separator
        generateElements(sep, s);
        try {
            String sen = input.readLine();
            while (sen != null) {
                int position = 0;
                while (position < sen.length()) {
                    String tokenWithUpper = nextWordOrSeparator(sen, position,
                            s);
                    String token = tokenWithUpper.toLowerCase();
                    if ((!s.contains(token.charAt(0)))
                            && !m.containsKey(token)) {
                        m.put(token, 1);
                    } else if (!s.contains(token.charAt(0))
                            && m.containsKey(token)) {
                        m.replace(token, m.get(token) + 1);
                    }
                    position += token.length();
                }
                try {
                    sen = input.readLine();
                } catch (IOException e) {
                    System.err.println("Error reading from file " + e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file " + e);
        }
    }

    /**
     * Store the Map pair into a sortingMachine and return the maximum counts.
     *
     * @param map
     *            map which store the word and number of word in input file
     * @param n
     *            the number of words to be included in the generated tag cloud
     * @param maxAndMin
     *            The list which contains the maximum counts of words and the
     *            minimum counts of words.(index 0 is maximum and index1 is
     *            minimum)
     * @return the maximum counts of words
     * @require map /= null;
     * @replace maxAndMin
     * @ensure sortMap contains all the needs values and follow the order
     */
    private static List<Map.Entry<String, Integer>> sortMap(
            Map<String, Integer> map, int n, List<Integer> maxAndMin) {
        assert map != null : "Violation of: map /= null";
        Comparator<Map.Entry<String, Integer>> c1 = new Sort1();
        Comparator<Map.Entry<String, Integer>> c2 = new Sort2();

        Set<Map.Entry<String, Integer>> s = map.entrySet();
        List<Map.Entry<String, Integer>> l1 = new ArrayList<>();
        List<Map.Entry<String, Integer>> l2 = new ArrayList<>();

        for (Entry<String, Integer> x : s) {
            l1.add(x);
        }
        l1.sort(c1);
        maxAndMin.add(l1.get(0).getValue());
        maxAndMin.add(l1.get(n - 1).getValue());
        for (int i = 0; i < n; i++) {
            l2.add(l1.get(i));
        }
        l2.sort(c2);

        return l2;

    }

    /**
     * Print the header of the html file.
     *
     * @param out
     *            Output stream for the output file
     *
     * @param s
     *            input file name
     *
     * @param n
     *            the number of words to be included in the generated tag cloud
     *
     * @require out is opened
     */
    private static void printUp(PrintWriter out, String s, int n) {
        assert out != null : "Violation of: out must open";

        out.println("<html>");
        out.println("<head>");
        out.print("<title>Top " + n + " words in " + s);
        out.println("</title>");
        out.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/"
                        + "assignments/projects/tag-cloud-generator/data/tagcloud.css\" "
                        + "rel=\"stylesheet\" type=\"text/css\">");
        out.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Top " + n + " words in " + s + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");
    }

    /**
     * Get the font Size of the counts.
     *
     * @param count
     *            counts of this word
     * @param maxAndMin
     *            The list which contains the maximum counts of words and the
     *            minimum counts of words.(index 0 is maximum and index1 is
     *            minimum)
     * @return the font size of the counts
     */
    private static int fontSize(int count, List<Integer> maxAndMin) {
        int r = SIZE * (count - maxAndMin.get(1))
                / (maxAndMin.get(0) - maxAndMin.get(1));
        return BASIC + r;
    }

    /**
     * Print the table of the html file and the footer.
     *
     * @param out
     *            Output stream for the output file
     * @param s
     *            SortingMachine which store all values
     * @param maxAndMin
     *            The list which contains the maximum counts of words and the
     *            minimum counts of words.(index 0 is maximum and index1 is
     *            minimum)
     * @require out is opened
     */
    private static void printUn(PrintWriter out,
            List<Map.Entry<String, Integer>> s, List<Integer> maxAndMin) {
        assert out != null : "Violation of: out must open";

        while (s.size() > 0) {
            Map.Entry<String, Integer> m = s.remove(0);
            int i = fontSize(m.getValue(), maxAndMin);
            out.println("<span style=\"cursor:default\" class=\"f" + i
                    + "\" title=\"count: " + m.getValue() + "\">" + m.getKey()
                    + "</span>");
        }
        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {

        BufferedReader in;
        BufferedReader input;
        PrintWriter output;

        String s;
        Map<String, Integer> m = new HashMap<>();
        int n = 0;

        System.out.println(
                "please enter the name of the output file(html file): ");
        try {
            in = new BufferedReader(new InputStreamReader(System.in));
            //data/importance.txt
            s = in.readLine();
            input = new BufferedReader(new FileReader(s));
            try {
                System.out.println(
                        "please enter the name of the input file(text file): ");
                //test/importanceOut.html
                output = new PrintWriter(
                        new BufferedWriter(new FileWriter(in.readLine())));
                try {
                    System.out.println(
                            "please enter the number of words to be included "
                                    + "in tag cloud: ");
                    //n=100
                    n = Integer.parseInt(in.readLine());
                    assert n >= 0 : "The number of words should be positive";
                    readWord(input, m);
                    List<Integer> maxAndMin = new ArrayList<>();
                    List<Map.Entry<String, Integer>> sort = sortMap(m, n,
                            maxAndMin);
                    printUp(output, s, n);
                    printUn(output, sort, maxAndMin);
                    try {
                        input.close();
                        output.close();
                        in.close();
                    } catch (IOException e) {
                        System.err.println("Error closing file " + e);
                    }

                } catch (IOException e) {
                    System.err.println("Error opening file " + e);
                }
            } catch (IOException e) {
                System.err.println("Error reading from file " + e);
            }
        } catch (IOException e) {
            System.err.println("Error opening file " + e);

        }

    }

}
