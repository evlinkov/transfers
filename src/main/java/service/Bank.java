package service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private static Bank bank;

    private static final int NUMBER_OF_LOCKS = 100;

    private ConcurrentHashMap<Integer, Double> userAccounts;

    private List<Lock> lock;

    public Double getAmount(Integer userIdentifier) {
        return this.userAccounts.getOrDefault(userIdentifier, .0);
    }

    public void transfer(Integer userIdentifierFrom, Integer userIdentifierTo, Double amount) throws NotEnoughMoneyException {

    }

    public synchronized static Bank getBank() {
        if (bank == null) {
            bank = new Bank();
        }
        return bank;
    }

    private int getLockIndexByUserIdentifier(Integer userIdentifier) {
        return (userIdentifier.toString() + "LOCK_INDEX").hashCode() % NUMBER_OF_LOCKS;
    }

    private Bank() {
        this.userAccounts = new ConcurrentHashMap<>();
        this.lock = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_LOCKS; ++i) {
            this.lock.add(new ReentrantLock());
        }
    }

    public void addUserIdentifier(Integer userIdentifier, Double amount) {
        this.userAccounts.put(userIdentifier, amount);
    }
}
