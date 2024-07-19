package com.android.appforlanguage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private static final long DOUBLE_BACK_PRESS_DURATION = 2000; // 2 seconds
    private long lastBackPressTime = 0;
    private Toast backPressToast;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        newCard = view.findViewById(R.id.imageButtonCreate);
        addWord = view.findViewById(R.id.imageButtonNeuWorte);
        playGame = view.findViewById(R.id.imageButtonSpeilen);
        dictionary = view.findViewById(R.id.imageButtonKorrigieren);
        setting = view.findViewById(R.id.imageButtonSetting);
        newCardMenu = view.findViewById(R.id.imageButtonNeuGroupeDic);
        addWordMenu = view.findViewById(R.id.imageButtonNewWord);
        playGameMenu = view.findViewById(R.id.imageButtonSpeilenDic);
        dictionaryMenu =view.findViewById(R.id.imageButtonKorrigiernSett);


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

        hideSystemUI();

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void hideSystemUI() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }
    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getAction() == android.view.KeyEvent.ACTION_UP) {
                    handleBackPress();
                    return true;
                }
                return false;
            });
        }
    }

    private void handleBackPress() {
        if (lastBackPressTime < System.currentTimeMillis() - DOUBLE_BACK_PRESS_DURATION) {
            backPressToast = Toast.makeText(getActivity(), "Натисніть двічі, щоб вийти з програми", Toast.LENGTH_SHORT);
            backPressToast.show();
            lastBackPressTime = System.currentTimeMillis();
        } else {
            if (backPressToast != null) {
                backPressToast.cancel();
            }
            getActivity().finishAffinity(); // Close the app
        }
    }
}
