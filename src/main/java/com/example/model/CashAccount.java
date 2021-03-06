package com.example.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class CashAccount extends BaseEntity {
    public static final String CURRENCY = "USD";

    private Money balance;
    private Person person;

    public CashAccount() {
        setBalance(Money.of(CurrencyUnit.of(CashAccount.CURRENCY), 0));

    }

    public void addCash(Money amount) {
        this.setBalance(getBalance().plus(amount));
    }

    public boolean removeCash(Money amount) {
        if (balanceAvailabilityFor(amount)) {
            setBalance(getBalance().minus(amount));
            return true;
        } else {
            return false;
        }
    }

    public boolean balanceAvailabilityFor(Money checkAmount) {
        return !getBalance().isLessThan(checkAmount);
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "CashAccount{" +
                "id=" + getId() +
                ", balance=" + balance +
                ", person=" + person +
                '}';
    }
}
