package com.example.expanseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,AddExpenseFragment.OnFragmentInteractionListener{

    final ArrayList<Expense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment(), "AddExpenses").commit();
    }

    @Override
    public void gotoAddExpenseFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, AddExpenseFragment.setData(expenseList), "AddExpenses").commit();
    }

    @Override
    public void onAddItemClick(Expense expense) {
        expenseList.add(expense);
        Log.d("demo", "test1 :" + expense.toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.setData(expenseList), "AddExpenses").commit();
    }
}
