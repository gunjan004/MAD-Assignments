package com.example.expanseapp;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {

    private String expenseName, category, amount, expenseDate;

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseName='" + expenseName + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                '}';
    }
}
