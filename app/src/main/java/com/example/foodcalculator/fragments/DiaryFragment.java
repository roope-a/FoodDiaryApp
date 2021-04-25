package com.example.foodcalculator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalculator.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiaryFragment extends BaseFragment implements View.OnClickListener {

    private MainActivityViewModel mainActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        FloatingActionButton fab = view.findViewById(R.id.addButton);
        fab.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.addButton) {
            mainActivityViewModel.diaryAddDataChanged("", "", 0, 0, 0, 0, 0, 0, 0, 0);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragmentAdd()).addToBackStack(null).commit();
        }



    }
}
