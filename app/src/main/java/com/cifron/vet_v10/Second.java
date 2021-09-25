package com.cifron.vet_v10;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Second#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Second extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rv;
    Context context;
    FormAdapter formAdapter;

    public Second() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Second.
     */
    // TODO: Rename and change types and number of parameters
    public static Second newInstance(String param1, String param2) {
        Second fragment = new Second();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<NameCity> cityList = new ArrayList<>();
        cityList.add(new NameCity("Форма 1", 23, "", 0));
        cityList.add(new NameCity("Форма 2", 20, "", 0));
        cityList.add(new NameCity("Форма 3", 11, "", 0));

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        rv = view.findViewById(R.id.formRecycler);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setHasFixedSize(true);
        formAdapter = new FormAdapter(view.getContext(), cityList);
        rv.setAdapter(formAdapter);
        return view;
    }
}