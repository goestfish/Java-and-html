import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

public class TestController {

    @Test
    public void testProcessEnterEvent() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(3);
        NaturalNumber b = new NaturalNumber2(6);

        m.top().add(t);
        m.bottom().add(b);
        c.processEnterEvent();
        assertEquals(m.top(), m.bottom());
    }

    @Test
    public void testProcessAddEvent() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(3);
        NaturalNumber b = new NaturalNumber2(6);
        NaturalNumber expectTop = new NaturalNumber2();
        NaturalNumber expectBottom = new NaturalNumber2(9);

        m.top().add(t);
        m.bottom().add(b);
        c.processAddEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    @Test
    public void testProcessSubtractEvent() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(6);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(3);

        m.top().add(t);
        m.bottom().add(b);
        c.processSubtractEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    @Test
    public void testProcessMultiplyEvent() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(6);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(18);

        m.top().add(t);
        m.bottom().add(b);
        c.processMultiplyEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test without reminder
    @Test
    public void testProcessDivideEvent1() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(6);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(2);

        m.top().add(t);
        m.bottom().add(b);
        c.processDivideEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test with reminder
    @Test
    public void testProcessDivideEvent2() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(7);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(1);
        NaturalNumber expectBottom = new NaturalNumber2(2);

        m.top().add(t);
        m.bottom().add(b);
        c.processDivideEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test with normal number
    @Test
    public void testProcessPowerEvent1() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(6);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(216);

        m.top().add(t);
        m.bottom().add(b);
        c.processPowerEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test with 0 power
    @Test
    public void testProcessPowerEvent2() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(6);
        NaturalNumber b = new NaturalNumber2(0);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(1);

        m.top().add(t);
        m.bottom().add(b);
        c.processPowerEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test with integer result
    @Test
    public void testProcessRootEvent1() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(9);
        NaturalNumber b = new NaturalNumber2(2);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(3);

        m.top().add(t);
        m.bottom().add(b);
        c.processRootEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    //test with other result
    @Test
    public void testProcessRootEvent2() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber t = new NaturalNumber2(9);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectTop = new NaturalNumber2(0);
        NaturalNumber expectBottom = new NaturalNumber2(2);

        m.top().add(t);
        m.bottom().add(b);
        c.processRootEvent();
        assertEquals(expectTop, m.top());
        assertEquals(expectBottom, m.bottom());
    }

    @Test
    public void testProcessAddNewDigitEvent() {
        NNCalcModel m = new NNCalcModel1();
        NNCalcView v = new NNCalcView1();
        NNCalcController c = new NNCalcController1(m, v);
        NaturalNumber b = new NaturalNumber2(3);
        NaturalNumber expectBottom = new NaturalNumber2(33);

        m.bottom().add(b);
        c.processAddNewDigitEvent(3);
        assertEquals(expectBottom, m.bottom());
    }
}
