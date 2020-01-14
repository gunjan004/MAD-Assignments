package com.example.hw5;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainExpenseAppFragment extends Fragment {

    public static String EXPENSES_LIST_KEY = "expenses_object_list";

    private OnExpenseAppFragmentInteractionListener mListener;
     private ExpenseAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;

    public MainExpenseAppFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.isOnMainScreen = true;
        getActivity().invalidateOptionsMenu();

        adapter.notifyDataSetChanged();

        if(adapter.getItemCount() != 0){
            getView().findViewById(R.id.message_textView).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.message_textView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Expense App");

        View view = inflater.inflate(R.layout.fragment_expense_app, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.expense_recyclerView);
        recyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        adapter = new ExpenseAdapter((ArrayList<Expense>) this.getArguments().getSerializable(EXPENSES_LIST_KEY));
        recyclerView.setAdapter(adapter);

        TextView totalCostTextView = view.findViewById(R.id.total_cost_display_textView);

        double totalCost = 0;

        for (Expense expense : adapter.myData) {
            totalCost += expense.amount;
        }

        totalCostTextView.setText(String.format("$%.2f", totalCost));

        view.findViewById(R.id.goTo_add_expense_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddExpenseFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseAppFragmentInteractionListener) {
            mListener = (OnExpenseAppFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpenseAppFragmentInteractionListener");
        }
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
        Log.d("demo", "after notify " + adapter.getItemCount());
    }

    public interface OnExpenseAppFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddExpenseFragment();
        void goToShowExpenseFragment(Expense expense, int index);

    }
}
