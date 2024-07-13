package com.android.appforlanguage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;

public class FragmentCreate extends Fragment {

    private ImageButton buttonBack;
   // private ImageButton buttonCreateCard;
    private ImageButton buttonPlayGame;
    private ImageButton buttonNewWord;
    private ImageButton buttonDictionary;
    private Button createButtonCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        buttonBack = view.findViewById(R.id.imageButtonBack);
        buttonPlayGame = view.findViewById(R.id.imageButtonPlayGame);
        buttonNewWord = view.findViewById(R.id.imageButtonNewWord);
        buttonDictionary = view.findViewById(R.id.imageButtonDictionary);
        createButtonCard = view.findViewById(R.id.buttonCreateCard);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Повернення до попереднього фрагмента
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PlayGame());
            }
        });
        buttonNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new AddWord());
            }
        });
        buttonDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Dictionary());
            }
        });

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
