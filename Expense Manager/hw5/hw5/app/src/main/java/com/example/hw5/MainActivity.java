package com.example.hw5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements MainExpenseAppFragment.OnExpenseAppFragmentInteractionListener, AddEditExpenseFragment.OnAddExpenseFragmentInteractionListener {

    ArrayList<Expense> expenses = new ArrayList<>();

    public boolean isOnMainScreen = true;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenses");
    MainExpenseAppFragment mainExpenseAppFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Expense App");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expenses.clear();

                if(dataSnapshot.exists()) {
                    for (DataSnapshot expenseSnap : dataSnapshot.getChildren()) {
                        Expense expense = expenseSnap.getValue(Expense.class);
                        expenses.add(expense);
                        Log.d("demo", expense.toString());
                    }
                }

                sortExpensesByDate();
                mainExpenseAppFragment = new MainExpenseAppFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(MainExpenseAppFragment.EXPENSES_LIST_KEY, expenses);
                mainExpenseAppFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mainExpenseAppFragment, "expense_app_fragment")
                        .commitAllowingStateLoss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("demo", "onCancelled: databaseError "  + databaseError);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isOnMainScreen) {
            inflater.inflate(R.menu.menu_with_dropdown, menu);
        } else {
            super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_cost_item:
                Collections.sort(expenses, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense o1, Expense o2) {
                        if(o1.amount > o2.amount) {
                            return 1;
                        } else if (o1.amount < o2.amount) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                mainExpenseAppFragment.notifyAdapter();
                break;
            case R.id.sort_by_date_item:
                sortExpensesByDate();
                mainExpenseAppFragment.notifyAdapter();
                break;
            case R.id.reset_all_item:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reset Expenses")
                        .setMessage("Are you sure you want to reset all expenses?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                expenses.clear();
                                myRef.setValue(expenses);
                                Log.d("demo", "before notify ");
                                mainExpenseAppFragment.notifyAdapter();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortExpensesByDate() {
        Collections.sort(expenses, new Comparator<Expense>() {
            @Override
            public int compare(Expense o1, Expense o2) {
                if(o1.year < o2.year) {
                    return 1;
                } else if (o1.year == o2.year) {
                    if(o1.month < o2.month) {
                        return 1;
                    } else if(o1.month == o2.month) {
                        if(o1.day < o2.day) {
                            return 1;
                        } else if(o1.day == o2.day) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        });
    }

    public void goToEditExpenseFragment(Expense expense, int index) {
        getSupportFragmentManager().popBackStack();

        AddEditExpenseFragment addEditExpenseFragment = new AddEditExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        bundle.putInt(ShowExpenseFragment.INDEX_KEY, index);
        addEditExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, addEditExpenseFragment, "add_or_edit_expense_fragment")
                .addToBackStack(null)
                .commit();
        }

    @Override
    public void goToAddExpenseFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddEditExpenseFragment(), "add_or_edit_expense_fragment")
                .addToBackStack(null)
                .commit();
        }

    @Override
    public void goToShowExpenseFragment(Expense expense, int index) {
        ShowExpenseFragment showExpenseFragment = new ShowExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowExpenseFragment.EXPENSE_KEY, expense);
        bundle.putInt(ShowExpenseFragment.INDEX_KEY, index);
        showExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, showExpenseFragment, "show_expense_fragment")
                .addToBackStack(null)
                .commit();
        }

    @Override
    public void addExpenseToList(Expense expense) {
        expenses.add(expense);
        myRef.setValue(expenses);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void updateExpenseInList(Expense expense, int index) {
        expenses.set(index, expense);
        myRef.setValue(expenses);
        mainExpenseAppFragment.notifyAdapter();
        getSupportFragmentManager().popBackStack();
    }
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
