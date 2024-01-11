import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A glossary facility.
 *
 * @author Bowei Kou
 *
 */
public final class Glossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
    }

    /**
     * Compare {@code String}s in lexicographic order.
     */
    public static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Store the word and its description into the map m.
     *
     * @param input
     *            input stream with the input file
     * @param m
     *            map which store the words and descriptions
     * @param q
     *            queue which contain the name of words
     * @updates m
     * @requires input is open, file is correct formal
     * @ensures m has all of the words and descriptions
     */
    public static void readWord(SimpleReader input, Map<String, String> m,
            Queue<String> q) {
        while (!input.atEOS()) {
            String word = input.nextLine(); //name of word
            StringBuilder des = new StringBuilder(input.nextLine()); //description of word
            if (!input.atEOS()) {
                String tep = input.nextLine(); //check if nextline is whitespace
                while (!tep.equals("")) {
                    des.append(tep);
                    if (!input.atEOS()) {
                        tep = input.nextLine();
                    } else {
                        tep = "";
                    }
                }
            }
            m.add(word, des.toString());
            q.enqueue(word);
        }
    }

    /**
     * Print the index page by html formal.
     *
     * @param output
     *            output stream to print the targeted html file
     * @param q
     *            queue which store the words in lexicographic order
     * @requires output is open, q is in lexicographic order
     * @ensures print the index page with correct formal
     */
    public static void writeIndex(SimpleWriter output, Queue<String> q) {
        //print the header
        output.println("<html>");
        output.println("<head>");
        output.println("<title>Glossary</title>");
        output.println("</head>");
        output.println("<body>");
        output.println("<h2>Glossary</h2>");
        output.println("<hr />");
        output.println("<h3>Index</h3>");
        output.println("<hr />");
        output.println("<ul>");
        //print the list
        Queue<String> tep = q.newInstance(); //temporary set
        tep.transferFrom(q);
        while (tep.length() != 0) {
            String s = tep.dequeue();
            output.print("<li><a href=\"" + s + ".html\">");
            output.println(s + "</a></li>");
            q.enqueue(s);
        }
        //print the footer
        output.println("</ul>");
        output.println("</body>");
        output.println("</html>");
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        Set<Character> tem = charSet.newInstance(); //temporary set
        for (int i = 0; i < str.length(); i++) {
            if (!tem.contains(str.charAt(i))) {
                tem.add(str.charAt(i));
            }
        }
        charSet.transferFrom(tem);
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
    public static String nextWordOrSeparator(String text, int position,
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
     * Update the description with link.
     *
     * @param m
     *            Map which store the words and description
     * @param word
     *            The name of word
     * @updates m
     *
     * @ensures Description has link to the Corresponding page when a word
     *          exists in the glossary
     */
    public static void writeDes(Map<String, String> m, String word) {
        /*
         * Define separator characters
         */
        final String sep = " \t, "; // separator string
        Set<Character> s = new Set1L<>(); // the set store the separator
        generateElements(sep, s);

        final String s1 = "<a href=\"";
        final String s2 = ".html\">";
        final String s3 = "</a>";
        StringBuilder result = new StringBuilder(m.value(word));
        int position = 0;
        while (position < result.length()) {
            String token = nextWordOrSeparator(result.toString(), position, s);
            if ((!s.contains(token.charAt(0))) && m.hasKey(token)) {
                result.replace(position, position + token.length(),
                        s1 + token + s2 + token + s3);
                position += s1.length() + s2.length() + s3.length();
            }
            position += token.length();
        }
        m.replaceValue(word, result.toString());
    }

    /**
     * Print the word page by html formal.
     *
     * @param output
     *            output stream to print the targeted html file
     * @param m
     *            Map which store the words and description
     * @param word
     *            The name of word
     * @requires output is open
     * @ensures print the word page with the correct formal
     */
    public static void writePage(SimpleWriter output, Map<String, String> m,
            String word) {
        output.println("<html>");
        output.println("<head>");
        output.println("<title>" + word + "</title>");
        output.println("</head>");
        output.println("<body>");
        output.print("<h2><b><i><font color=\"red\">");
        output.println(word + "</font></i></b></h2>");
        output.println("<hr/>");
        output.print("<blockquote>");
        output.print(m.value(word));
        output.println("</blockquote>");
        output.println("<hr/>");
        output.println("<p>Return to <a href=\"index.html\">index</a>.</p>");
        output.println("</body>");
        output.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L(); //input stream for console
        SimpleWriter out = new SimpleWriter1L(); //output stream for console
        SimpleReader input; //input stream to read input file
        SimpleWriter output; //output stream to print the html file

        //(use terms.txt as final test)
        out.println("please enter the name of the input file: ");
        input = new SimpleReader1L(in.nextLine());
        //(use finalTest folder as final test)
        out.println("please enter the name of the output folder: ");
        String name = in.nextLine(); //name of folder

        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>(); // map store the word and description
        readWord(input, m, q);
        Comparator<String> c = new StringLT();
        q.sort(c);
        //print index html file
        output = new SimpleWriter1L(name + "/index.html");
        writeIndex(output, q);
        // print word html file
        while (q.length() != 0) {
            String word = q.dequeue();
            output = new SimpleWriter1L(name + "/" + word + ".html");
            writeDes(m, word);
            writePage(output, m, word);
        }

        in.close();
        out.close();
        input.close();
        output.close();
    }

}
