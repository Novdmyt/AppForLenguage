package com.android.appforlanguage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.appforlanguage.R;
public class PlayGame extends Fragment {

    private ImageButton createMenu;
   // private ImageButton playGameMenu;
    private ImageButton addNewWordMenu;
    private ImageButton dictionaryMenu;
    private ImageButton backPlayGame;
    private Spinner spinnerDataBase;
    private Switch aSwitchMix;
    private ImageButton soundHelp;
    private Button buttonCheck;
    private Button buttonHelp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_play_game, container, false);


        return view;
    }
}