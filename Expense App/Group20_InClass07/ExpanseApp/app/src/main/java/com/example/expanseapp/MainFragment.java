package com.example.expanseapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView myListView;
    //private ExpenseAdapter expenseAdapter;
    private TextView textView;
    private ArrayList<Expense> expenseNew = new ArrayList<>();
    static  String ARG_PARAM="ItemList";
    private View view;

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment setData(ArrayList<Expense> expense) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM,expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view=inflater.inflate(R.layout.fragment_main, container,false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        myListView = view.findViewById(R.id.listViewID);
        //myListView.setAdapter(expenseAdapter);
        myListView.setVisibility(View.VISIBLE);
        textView = view.findViewById(R.id.textView_NoData);
        textView.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.button_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoAddExpenseFragment();
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

    @Override
    public void onStart() {

        super.onStart();
        if (getArguments() != null) {
            expenseNew = (ArrayList<Expense>) getArguments().getSerializable(ARG_PARAM);
            final ExpenseAdapter expenseAdapter = new ExpenseAdapter(view.getContext(), R.layout.list_item, expenseNew);
            myListView.setAdapter(expenseAdapter);
            myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Expense expenseClick = expenseAdapter.getItem(position);
                    expenseNew.remove(expenseClick);
                    if(expenseNew.isEmpty()){
                        myListView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }else {
                        myListView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                    expenseAdapter.notifyDataSetChanged();
                    return false;
                }
            });
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Expense expenseClick = expenseAdapter.getItem(position);
                    getFragmentManager().beginTransaction().replace(R.id.container, ShowExpenseFragement.setData(expenseClick,expenseNew), "ShowExpenses").commit();
                }
            });
            getActivity().setTitle("Expense Add");
        }
        if(expenseNew.isEmpty()){
            myListView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            myListView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
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
        void gotoAddExpenseFragment();
    }
}
