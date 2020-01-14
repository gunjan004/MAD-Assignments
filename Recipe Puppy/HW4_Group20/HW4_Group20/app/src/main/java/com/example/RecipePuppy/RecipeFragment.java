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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class RecipeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<String> List_ingredients = new ArrayList<>();
    private RecipeSearchFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    Button buttonSearch;
    TextView textViewDish;
    EditText searcheditText;


    public RecipeFragment() {

    }

    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
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
        getActivity().setTitle("Recipe Puppy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_IngridientsList);
        recyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        adapter = new IngredientsAdapter(List_ingredients);
        recyclerView.setAdapter(adapter);

        if(List_ingredients.size() == 0) {
            List_ingredients.add("");
            adapter.notifyDataSetChanged();
        }

        searcheditText = view.findViewById(R.id.editText_Search);
        buttonSearch = view.findViewById(R.id.button_Search);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searcheditText.getText().length() > 0)
                {
                    RequestParams url = new RequestParams();
                    StringBuilder stringBuilder = new StringBuilder();
                    if(List_ingredients.get(0).length() > 0)
                    {
                        stringBuilder.append(List_ingredients.get(0));
                    }
                    for(int i = 1; i < List_ingredients.size(); i++)
                    {
                        if(List_ingredients.get(i).length() > 0)
                        {
                            stringBuilder.append("," + List_ingredients.get(i));
                        }
                    }

                    url.addParameter("q", searcheditText.getText().toString());
                    url.addParameter("i", stringBuilder.toString());
                    String urlStr = url.getEncodedUrl("http://www.recipepuppy.com/api/");
                    mListener.recipeLoadFragmentFunction(urlStr);
                }
                else{
                    Toast.makeText(getActivity(), "Please enter the values.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeSearchFragmentInteractionListener) {
            mListener = (RecipeSearchFragmentInteractionListener) context;
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


    public interface RecipeSearchFragmentInteractionListener {
        void recipeLoadFragmentFunction(String url);
    }
}
