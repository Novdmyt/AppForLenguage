package com.android.appforlanguage.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.appforlanguage.R;
import com.android.appforlanguage.database.DataBase;
import com.android.appforlanguage.database.Word;
import com.android.appforlanguage.util.WordAdapter;

import java.util.List;
import java.util.Locale;

public class Dictionary extends Fragment {
    private ImageButton buttonBack;
    private ImageButton buttonPlayGame;
    private ImageButton buttonAddWord;
    private ImageButton buttonCreateNew;
    private ImageButton buttonDictionary;
    private Spinner spinnerDataBase;
    private SearchView searchViewDic;
    private RecyclerView recyclerViewDic;
    private WordAdapter wordAdapter;
    private DataBase dbHelper;
    private TextToSpeech tts;
    private Locale selectedLanguage = Locale.GERMAN;
    private static final String PREFS_NAME = "app_preferences";
    private static final String PREF_LANGUAGE = "selected_language";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.fragment_dictionary, container, false);

      buttonBack = view.findViewById(R.id.imageButtonBackPlayGameDic);
      buttonPlayGame = view.findViewById(R.id.imageButtonSpeilenDic);
      buttonAddWord = view.findViewById(R.id.imageButtonNewWordDic);
      buttonCreateNew = view.findViewById(R.id.imageButtonNeuGroupeDic);
      buttonDictionary = view.findViewById(R.id.imageButtonDictionaryDic);
      spinnerDataBase = view.findViewById(R.id.spinnerDataBaseDic);
      searchViewDic = view.findViewById(R.id.searchViewDic);//настоить поиск
      recyclerViewDic = view.findViewById(R.id.recyclerViewDic);

        dbHelper = new DataBase(getActivity());

        loadSelectedLanguage();
        setupTextToSpeech();

        loadTableNames();
        refreshTableNames();
        spinnerDataBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = (String) parent.getItemAtPosition(position);
                loadWordsFromTable(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        searchViewDic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wordAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordAdapter.filter(newText);
                return false;
            }
        });

        recyclerViewDic.setLayoutManager(new LinearLayoutManager(getActivity()));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Повернення до попереднього фрагмента
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        buttonAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new AddWord());
            }
        });
        buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PlayGame());
            }
        });

        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentCreate());
            }
        });
        buttonDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new Dictionary());
            }
        });
        return view;
    }
    private void setupTextToSpeech() {
        tts = new TextToSpeech(getActivity(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(selectedLanguage);
            } else {
                Log.d("DictionaryWord", "TextToSpeech initialization failed.");
            }
        });
    }

    private void loadTableNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> tableNames = dbHelper.getTableNames(db);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataBase.setAdapter(adapter);
    }

    private void loadWordsFromTable(String tableName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Word> words = dbHelper.getWordsFromTable(db, tableName);
        wordAdapter = new WordAdapter(getActivity(), tts, selectedLanguage, tableName);
        wordAdapter.setWords(words);
        recyclerViewDic.setAdapter(wordAdapter);
    }

    private void loadSelectedLanguage() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = preferences.getString(PREF_LANGUAGE, "English");
        switch (language) {
            case "English":
                selectedLanguage = Locale.ENGLISH;
                break;
            case "German":
                selectedLanguage = Locale.GERMAN;
                break;
            case "French":
                selectedLanguage = Locale.FRENCH;
                break;
            case "Spanish":
                selectedLanguage = new Locale("es", "ES");
                break;
            case "Italian":
                selectedLanguage = Locale.ITALIAN;
                break;
        }
    }

    // Call this method whenever a new table is added to update the spinner
    public void refreshTableNames() {
        loadTableNames();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
