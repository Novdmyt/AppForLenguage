package com.android.appforlanguage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;

public class FragmentHome extends Fragment {
    private ImageButton newCard;
    private ImageButton addWord;
    private ImageButton playGame;
    private ImageButton dictionary;
    private  ImageButton setting;
    private ImageButton newCardMenu;
    private ImageButton addWordMenu;
    private ImageButton playGameMenu;
    private ImageButton dictionaryMenu;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        newCard = view.findViewById(R.id.imageButtonCreate);
        addWord = view.findViewById(R.id.imageButtonNeuWorte);
        playGame = view.findViewById(R.id.imageButtonSpeilen);
        dictionary = view.findViewById(R.id.imageButtonKorrigieren);
        setting = view.findViewById(R.id.imageButtonSetting);
        newCardMenu = view.findViewById(R.id.imageButtonNeuGroupeMenu);
        addWordMenu = view.findViewById(R.id.imageButtonNeuesWorteMenu);
        playGameMenu = view.findViewById(R.id.imageButtonSpeilenMenu);
        dictionaryMenu =view.findViewById(R.id.imageButtonKorrigiernMenu);

        View.OnClickListener createClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openFragment(new FragmentCreate());
            }
        };
        newCard.setOnClickListener(createClickListener);
        newCardMenu.setOnClickListener(createClickListener);

        View.OnClickListener createAddWord = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new AddWord());
            }
        };
        addWord.setOnClickListener(createAddWord);
        addWordMenu.setOnClickListener(createAddWord);

        View.OnClickListener createPlayGame = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFragment(new PlayGame());
            }
        };
        playGame.setOnClickListener(createPlayGame);
        playGameMenu.setOnClickListener(createPlayGame);
        View.OnClickListener createDictionary = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFragment(new Dictionary());
            }
        };
        dictionary.setOnClickListener(createDictionary);
        dictionaryMenu.setOnClickListener(createDictionary);

       View.OnClickListener createSetting = new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               openFragment(new Setting());
           }
       };
        setting.setOnClickListener(createSetting);

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
