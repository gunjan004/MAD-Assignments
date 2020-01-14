package com.example.expanseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowExpenseFragement extends Fragment {

    //private OnFragmentInteractionListener mListener;
    static  String ARG_PARAM1="1";
    static  String ARG_PARAM2="2";
    private View view;
    private TextView tvExpenseName, tvCategory, tvAmount, tvDate;
    private Expense expense;
    private Button closeButton;
    private ArrayList<Expense> expenseNew;

    public ShowExpenseFragement() {
        // Required empty public constructor
    }

    public static Fragment setData(Expense expenseClick, ArrayList<Expense> expense) {
        ShowExpenseFragement fragment = new ShowExpenseFragement();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,expenseClick);
        args.putSerializable(ARG_PARAM2,expense);
        fragment.setArguments(args);
        Log.d("demo", "setData" );
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null)
        {
            view=inflater.inflate(R.layout.fragment_show_expense, container,false);
        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        tvAmount = view.findViewById(R.id.tvAmount);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvExpenseName = view.findViewById(R.id.tvExpenseName);
        tvDate = view.findViewById(R.id.tvExpenseDate);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            expense = (Expense) getArguments().getSerializable(ARG_PARAM1);
            expenseNew = (ArrayList<Expense>) getArguments().getSerializable(ARG_PARAM2);
            tvExpenseName.setText(expense.getExpenseName());
            tvCategory.setText(expense.getCategory());
            tvDate.setText(expense.getExpenseDate());
            tvAmount.setText(expense.getAmount());
        }
        getActivity().findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.setData(expenseNew), "AddExpenses").commit();
            }
        });
        getActivity().setTitle("Show Expense");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
