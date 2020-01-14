package com.example.expanseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddExpenseFragment extends Fragment {

    Expense expense = new Expense();
    private OnFragmentInteractionListener mListener;
    private ArrayList<Expense> expenseNew;
    static  String ARG_PARAM="ItemList";

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static Fragment setData(ArrayList<Expense> expense) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            expenseNew = (ArrayList<Expense>) getArguments().getSerializable(ARG_PARAM);
        }
        getActivity().setTitle("Add Expense");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinner = getActivity().findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        getActivity().findViewById(R.id.btnAddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText expenseName = getActivity().findViewById(R.id.editTextAddExpense);
                EditText expenseAmount = getActivity().findViewById(R.id.editText_Amount);
                Spinner category = getActivity().findViewById(R.id.spinnerCategory);
                if(expenseName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please Enter Expense Name", Toast.LENGTH_SHORT).show();
                }else if(expenseAmount.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please Enter Expense Amount", Toast.LENGTH_SHORT).show();
                }else if(category.getSelectedItem().toString().equalsIgnoreCase("Select Category")){
                    Toast.makeText(getActivity(), "Please Select Expense Category", Toast.LENGTH_SHORT).show();
                }else {
                    expense.setExpenseName(expenseName.getText().toString());
                    expense.setAmount(expenseAmount.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    expense.setExpenseDate(sdf.format(cal.getTime()).toString());
                    expense.setCategory(category.getSelectedItem().toString());
                    Log.d("demo", "Expense " + expense.toString());
                    mListener.onAddItemClick(expense);
                }
            }
        });
        getActivity().findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, MainFragment.setData(expenseNew), "CancelButtonClick").commit();
                //getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddItemClick(Expense expense);
    }
}
