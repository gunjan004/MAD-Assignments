package com.example.inclass08;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditExpense extends AppCompatActivity {

    private Spinner spinner;
    private EditText expenseName;
    private EditText expenseAmount;
    private Button btnSave;
    private Button btnCancel;
    Expense e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Edit Expense");
        spinner = findViewById(R.id.spinnerEditID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditExpense.this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        expenseName = findViewById(R.id.etExpNameID);
        expenseAmount = findViewById(R.id.etAmtID);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("expenses");

        if(getIntent() != null && getIntent().getExtras() != null){
            e = (Expense) getIntent().getExtras().getSerializable("edit_data");
            expenseName.setText(e.getExpenseName());
            expenseAmount.setText(e.getAmount());
            int myAdapPos = adapter.getPosition(e.getCategory());
            spinner.setSelection(myAdapPos);
        }

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("demo", "Save clicked");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                HashMap<String, Object> hm = new HashMap<>();

                hm.put("id", e.getId());
                hm.put("amount", expenseAmount.getText().toString());
                hm.put("category",spinner.getSelectedItem().toString());
                hm.put("expenseName", expenseName.getText().toString());
                hm.put("expenseDate", sdf.format(cal.getTime()));

                myRef.child(e.getId()).updateChildren(hm);

                Intent in = new Intent(EditExpense.this, MainActivity.class);
                startActivity(in);
            }
        });
    }
}
