package com.example.hw5;

import java.io.Serializable;


public class Expense implements Serializable {
    String name, date, receiptUrl;
    int month, day, year;
    Double amount;

    public Expense() {
    }

    public Expense(String name, String date, String receiptUrl, int month, int day, int year, Double amount) {
        this.name = name;
        this.date = date;
        this.receiptUrl = receiptUrl;
        this.month = month;
        this.day = day;
        this.year = year;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
