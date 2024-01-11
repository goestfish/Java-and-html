import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

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
 * @author Bowei Kou
 */

public class GlossaryTest {

    /**
     * test with letter.
     */
    @Test
    public void testComparator1() {
        Comparator<String> c = new Glossary.StringLT();
        Queue<String> q = new Queue1L<>();
        q.enqueue("d");
        q.enqueue("b");
        q.enqueue("c");
        q.sort(c);
        assertEquals("b", q.dequeue());
        assertEquals("c", q.dequeue());
        assertEquals("d", q.dequeue());
    }

    /**
     * test with word.
     */
    @Test
    public void testComparator2() {
        Comparator<String> c = new Glossary.StringLT();
        Queue<String> q = new Queue1L<>();
        q.enqueue("zoo");
        q.enqueue("dog");
        q.enqueue("ant");
        q.sort(c);
        assertEquals("ant", q.dequeue());
        assertEquals("dog", q.dequeue());
        assertEquals("zoo", q.dequeue());
    }

    /**
     * test a file with one line description.
     */
    @Test
    public void testReadWord1() {
        SimpleReader in = new SimpleReader1L("test/test1.txt");
        Map<String, String> m = new Map1L<>();
        Queue<String> q = new Queue1L<>();
        Glossary.readWord(in, m, q);
        assertEquals("number1", m.value("1"));
        assertEquals("1", q.dequeue());
        in.close();
    }

    /**
     * test a file with multiple lines description.
     */
    @Test
    public void testReadWord2() {
        SimpleReader in = new SimpleReader1L("test/test2.txt");
        Map<String, String> m = new Map1L<>();
        Queue<String> q = new Queue1L<>();
        Glossary.readWord(in, m, q);
        assertEquals("number1", m.value("1"));
        assertEquals("number2", m.value("2"));
        assertEquals("1", q.dequeue());
        assertEquals("2", q.dequeue());
        in.close();
    }

    /**
     * test a empty file.
     */
    @Test
    public void testReadWord3() {
        SimpleReader in = new SimpleReader1L("test/test3.txt");
        Map<String, String> m = new Map1L<>();
        Queue<String> q = new Queue1L<>();
        Glossary.readWord(in, m, q);
        assertEquals(0, m.size());
        in.close();
    }

    /**
     * test if the queue restore and check the output file.
     */
    @Test
    public void testWriteIndex() {
        Queue<String> q = new Queue1L<>();
        SimpleWriter out = new SimpleWriter1L("test/index.html");
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue("c");
        Glossary.writeIndex(out, q);
        assertEquals("a", q.dequeue());
        assertEquals("b", q.dequeue());
        assertEquals("c", q.dequeue());
    }

    /**
     * test with no repeated characters.
     */
    @Test
    public void testGenerateElements1() {

        Set<Character> tem = new Set1L<>();
        Glossary.generateElements("123", tem);
        Set<Character> expect = new Set1L<>();
        expect.add('1');
        expect.add('2');
        expect.add('3');
        assertEquals(expect, tem);
    }

    /**
     * test with repeated characters.
     */
    @Test
    public void testGenerateElements2() {

        Set<Character> tem = new Set1L<>();
        Glossary.generateElements("033", tem);
        Set<Character> expect = new Set1L<>();
        expect.add('0');
        expect.add('3');
        assertEquals(expect, tem);
    }

    /**
     * test with whitespace character.
     */
    @Test
    public void testNextWordOrSeparator1() {

        Set<Character> s = new Set1L<>();
        Glossary.generateElements(" /t, ", s);
        String str = Glossary.nextWordOrSeparator("123 123", 0, s);
        assertEquals("123", str);
    }

    /**
     * test with commas and whitespace character.
     */
    @Test
    public void testNextWordOrSeparator2() {

        Set<Character> s = new Set1L<>();
        Glossary.generateElements(" /t, ", s);
        String str = Glossary.nextWordOrSeparator(", 123", 0, s);
        assertEquals(", ", str);
    }

    /**
     * test with one word.
     */
    @Test
    public void testWriteDes1() {
        Map<String, String> m = new Map1L<>();
        m.add("1", "num1");
        Glossary.writeDes(m, "1");
        assertEquals("num1", m.value("1"));
    }

    /**
     * test with two words.
     */
    @Test
    public void testWriteDes2() {
        Map<String, String> m = new Map1L<>();
        m.add("1", "num 1");
        m.add("2", "num1");
        Glossary.writeDes(m, "1");
        Glossary.writeDes(m, "2");
        assertEquals("num <a href=\"1.html\">1</a>", m.value("1"));
        assertEquals("num1", m.value("2"));
    }

    /**
     * test a sentence which contain multiple word in glossary.
     */
    @Test
    public void testWriteDes3() {
        Map<String, String> m = new Map1L<>();
        m.add("keep", "keep hard");
        m.add("hard", "hard");
        Glossary.writeDes(m, "keep");
        Glossary.writeDes(m, "hard");
        assertEquals("<a href=\"keep.html\">keep</a> "
                + "<a href=\"hard.html\">hard</a>", m.value("keep"));
        assertEquals("<a href=\"hard.html\">hard</a>", m.value("hard"));
    }

    /**
     * output the html file to check the formal
     */
    @Test
    public void testWritePage() {
        SimpleWriter out = new SimpleWriter1L("test/Page.html");
        Map<String, String> m = new Map1L<>();
        m.add("keep", "keep hard");
        m.add("hard", "hard");
        Glossary.writePage(out, m, "keep");

        out.close();
    }
}
