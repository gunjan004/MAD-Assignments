package com.example.inclass08;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddExpense extends AppCompatActivity {


    private Button btnAdd;
    private Spinner spinner;
    private Button btnCancel;
    static String EXPENSE_LIST = "EXPENSE_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Expense");

        btnAdd = findViewById(R.id.btnAddExpense);
        btnCancel = findViewById(R.id.btnCancel);
        spinner = findViewById(R.id.spinnerCategory);
        final Expense expenseData = new Expense();
        final ArrayList<Expense> expenses= new ArrayList();
        final String id = UUID.randomUUID().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("expenses");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Expense expense = expenseSnap.getValue(Expense.class);
                    expenses.add(expense);
                    Log.d("demo", "MY DATA: " + expense.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("demo", "Failed to read value.", databaseError.toException());
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddExpense.this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText expenseName = findViewById(R.id.editTextAddExpense);
                EditText expenseAmount = findViewById(R.id.editText_Amount);
                Spinner category = findViewById(R.id.spinnerCategory);

                if(expenseName.getText().toString().length() <= 0){
                    Toast.makeText(AddExpense.this, "Please enter all the details before adding.", Toast.LENGTH_LONG).show();
                } else if( expenseAmount.getText().toString().length() <=0){
                    Toast.makeText(AddExpense.this, "Please enter all the details before adding.", Toast.LENGTH_LONG).show();
                } else if(category.getSelectedItem() == null){
                    Toast.makeText(AddExpense.this, "Please enter all the details before adding.", Toast.LENGTH_LONG).show();
                } else {
                    expenseData.setId(id);
                    expenseData.setExpenseName(expenseName.getText().toString());
                    expenseData.setAmount(expenseAmount.getText().toString());

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                    expenseData.setExpenseDate(sdf.format(cal.getTime()));
                    expenseData.setCategory(category.getSelectedItem().toString());

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("expenses/" + id);

                    myRef.setValue(expenseData);

                    Intent in = new Intent(AddExpense.this, MainActivity.class);
                    in.putExtra(EXPENSE_LIST, expenseData);
                    startActivity(in);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "Cancel clicked");

                if(getFragmentManager().getBackStackEntryCount() == 0) {
                    onBackPressed();
                }
                else {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }
}
