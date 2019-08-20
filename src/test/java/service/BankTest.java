package service;

import org.junit.*;

import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class BankTest {

    private final double delta = 0.0000001;

    @After
    public void clear() throws Exception {
        //not really elegant
        Field privateStaticField = Bank.class.getDeclaredField("bank");
        privateStaticField.setAccessible(true);
        privateStaticField.set(null, null);
    }

    @Test
    public void testSimpleBankFunctions() throws Exception {
        Bank.getBank().addUserIdentifier(13, 100.);
        assertEquals(100., Bank.getBank().getAmount(13), delta);
        assertEquals(.0, Bank.getBank().getAmount(1), delta);
        Bank.getBank().transfer(13, 1, 40.);
        assertEquals(60., Bank.getBank().getAmount(13), delta);
        assertEquals(40., Bank.getBank().getAmount(1), delta);
        try {
            Bank.getBank().transfer(13, 1, 70.);
        } catch (NotEnoughMoneyException error) {
        }
        assertEquals(60., Bank.getBank().getAmount(13), delta);
        assertEquals(40., Bank.getBank().getAmount(1), delta);
    }

    @Test
    public void stressTest() {

    }

}