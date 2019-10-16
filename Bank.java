package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.*;


/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */

public class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts = 0;
    private final int initialBalance;
    private final int numAccounts;
    private static ReentrantLock lock = new ReentrantLock();
    private boolean temp;
//    ExecutorService executor = Executors.newFixedThreadPool(2);

    public Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
//        accounts[from].waitForAvailableFunds(amount);
//          lock.lock();
          lock.lock();
        try{

            temp = accounts[from].withdraw(amount);
            Thread.sleep(2000);

        }

        finally{
            
            lock.unlock();
//            lock.unlock();
            
        }

        if(temp == true){
//            accounts[to].deposit(amount);
//               lock.lock();
                lock.lock();
            try{
                
                accounts[to].deposit(amount);
                Thread.sleep(2000);

            }

            finally{
//                lock.unlock();
                lock.unlock();
                

            }

        }

        // if (accounts[from].withdraw(amount)) {
        //     accounts[to].deposit(amount);
        // }
        if (shouldTest()) test();
    }

    public void test() {
        int sum = 0;
        for (Account account : accounts) {
            System.out.printf("%s %s%n", 
                    Thread.currentThread().toString(), account.toString());
            sum += account.getBalance();
        }
        System.out.println(Thread.currentThread().toString() + 
                " Sum: " + sum);
        if (sum != numAccounts * initialBalance) {
            System.out.println(Thread.currentThread().toString() + 
                    " Money was gained or lost");
            System.exit(1);
        } else {
            System.out.println(Thread.currentThread().toString() + 
                    " The bank is in balance");
        }
    }

    public int size() {
        return accounts.length;
    }
    
    
    public boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }

}
