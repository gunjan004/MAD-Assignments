package com.example.RecipePuppy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;



public class RecipeDisplayFragment extends Fragment {

    private RecyclerView.LayoutManager myLayoutManager;
    private String mParam1;
    private String mParam2;
    public static String RECIPES_LIST_KEY = "recipes_array_list";
    private ArrayList<Recipe> listDisplayRecipes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Button buttonFinish;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public RecipeDisplayFragment() {
    }

    public static RecipeDisplayFragment newInstance(String param1, String param2) {
        RecipeDisplayFragment fragment = new RecipeDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Recipes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_display, container, false);


        recyclerView = view.findViewById(R.id.recyclerView_RecipeList);
        recyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) myLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(myLayoutManager);

        adapter = new RecipeAdapter((ArrayList<Recipe>) this.getArguments().getSerializable(RECIPES_LIST_KEY));
        recyclerView.setAdapter(adapter);

        buttonFinish = view.findViewById(R.id.button_Finish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

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

}
