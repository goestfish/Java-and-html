import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

public class TestModel {

    @Test
    public void testModel1() {
        NNCalcModel m = new NNCalcModel1();
        NaturalNumber top = new NaturalNumber2();
        NaturalNumber bot = new NaturalNumber2();
        assertEquals(top, m.top());
        assertEquals(bot, m.top());
    }

    @Test
    public void testModel2() {
        NNCalcModel m = new NNCalcModel1();
        NaturalNumber t = new NaturalNumber2(3);
        NaturalNumber b = new NaturalNumber2(6);
        m.top().add(t);
        m.bottom().add(b);
        assertEquals(t, m.top());
        assertEquals(b, m.bottom());
    }

}
