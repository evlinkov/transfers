package service;

import org.junit.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        //in this case we guess, that users had always enough money to send them
        Bank.getBank().addUserIdentifier(1, 10000.);
        Bank.getBank().addUserIdentifier(2, 10000.);
        Bank.getBank().addUserIdentifier(3, 10000.);
        double expectedValues[] = new double[4];
        expectedValues[1] = 10000.;
        expectedValues[2] = 10000.;
        expectedValues[3] = 10000.;

        List<Transfer> transfers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            int from = random.nextInt(3) + 1;
            int to = random.nextInt(3) + 1;
            int amount = random.nextInt(5) + 1;
            if (from == to) {
                continue;
            }
            transfers.add(new Transfer(from, to, amount));
            expectedValues[from] -= amount;
            expectedValues[to] += amount;
        }

        transfers.parallelStream().forEach(transfer -> {
            try {
                Bank.getBank().transfer(transfer.from, transfer.to, transfer.amount);
            } catch(NotEnoughMoneyException error) {
                assert(true);
            }
        });

        assertEquals(expectedValues[1], Bank.getBank().getAmount(1), delta);
        assertEquals(expectedValues[2], Bank.getBank().getAmount(2), delta);
        assertEquals(expectedValues[3], Bank.getBank().getAmount(3), delta);
    }

    private static class Transfer {
        private int from;
        private int to;
        private double amount;

        private Transfer(int from, int to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

    }

}