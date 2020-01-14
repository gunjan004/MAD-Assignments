package com.example.inclass08;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private ListView myListView;
    private ExpenseAdapter expenseAdapter;
    private TextView textView;
    ArrayList<Expense> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Expense App");

        btnAdd = findViewById(R.id.button_Add);
        textView = findViewById(R.id.textView_NoData);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(MainActivity.this, AddExpense.class);
                startActivity(intnt);
            }
        });

        myListView = findViewById(R.id.listViewID);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("expenses");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses.clear();
                for(DataSnapshot expenseSnap: dataSnapshot.getChildren()){

                    Expense expense = expenseSnap.getValue(Expense.class);
                    expenses.add(expense);
                    Log.d("demo", "MY DATA: " + expense.toString());
                }

                if(expenses.size() == 0){
                    textView.setVisibility(View.VISIBLE);
                } else {

                    expenseAdapter = new ExpenseAdapter(MainActivity.this, R.layout.list_item, expenses);
                    myListView.setAdapter(expenseAdapter);
                    myListView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("demo", "Failed to read value.", databaseError.toException());
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                myRef.getRef().child(expenses.get(position).getId()).removeValue();
                expenses.remove(position);
                expenseAdapter.notifyDataSetChanged();
                return false;
            }
        });

        if(getIntent() != null && getIntent().getExtras() != null) {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                        Expense expense = expenseSnap.getValue(Expense.class);

                        Log.d("demo", "MY DATA: " + expense.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("demo", "Failed to read value.", databaseError.toException());
                }
            });
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("demo", "Clicked: " + expenses.get(position));

                Intent i = new Intent(MainActivity.this, ShowExpense.class);
                i.putExtra("expense_data", expenses.get(position));
                startActivity(i);
            }
        });
    }
}
