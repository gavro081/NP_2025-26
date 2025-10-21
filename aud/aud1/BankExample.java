package aud.aud1;

import java.util.ArrayList;
import java.util.List;

class Bank{
    private List<Account> accounts = new ArrayList<>();

    public Bank(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public double totalAssets() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public void addInterest(){
        accounts.forEach(acc -> {
            if (acc instanceof InterestBearingAccount){
                ((InterestBearingAccount) acc).addInterest();
            }
        });
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}

abstract class Account{
    private String name;
    private int acc_number;
    static int acc_number_counter = 0;
    private double balance;

    public Account(){
        name = "";
        acc_number = acc_number_counter++;
        balance = 0;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.acc_number = acc_number_counter++;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void addAmount(double amount){
        balance += amount;
    }

    public void withdrawAmount(double amount){
        balance -= amount;
    }
}

class NonInterestCheckingAccount extends Account{
    public NonInterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }
}

class InterestCheckingAccount extends Account implements InterestBearingAccount {
    public static final double INTEREST = 0.03;

    public InterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void addInterest() {
        addAmount(getBalance() * INTEREST);
    }
}

class PlatinumCheckingAccount extends InterestCheckingAccount{
    public PlatinumCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void addInterest(){
        addAmount(getBalance() * 2 * INTEREST);
    }
}

interface InterestBearingAccount{
    void addInterest();
}

public class BankExample {
    public static void main(String[] args) {
    }
}
