package com.android.appforlanguage.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;
import com.android.appforlanguage.database.DataBase;
import com.android.appforlanguage.database.Word;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlayGame extends Fragment {

    private ImageButton createMenu;
    private ImageButton addNewWordMenu;
    private ImageButton dictionaryMenu;
    private ImageButton backPlayGame;
    private Spinner spinnerDataBase;
    private Switch switchMix;
    private ImageButton soundHelp;
    private Button buttonCheck;
    private Button buttonHelp;
    private EditText wordTextView;
    private EditText translateEditText;
    private DataBase dbHelper;
    private SQLiteDatabase db;
    private List<Word> words;
    private int currentIndex = 0;
    private boolean randomOrder = false;
    private TextToSpeech textToSpeech;
    private Locale selectedLanguage = Locale.ENGLISH; // Default language
    private static final String PREFS_NAME = "app_preferences";
    private static final String PREF_LANGUAGE = "selected_language";
    private static final long DOUBLE_BACK_PRESS_DURATION = 2000; // 2 seconds
    private long lastBackPressTime = 0;
    private Toast backPressToast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_game, container, false);

        createMenu = view.findViewById(R.id.imageButtonCreateMenu);
        addNewWordMenu = view.findViewById(R.id.imageButtonAddNewWordMenu);
        dictionaryMenu = view.findViewById(R.id.imageButtonDictionaryMenu);
        backPlayGame = view.findViewById(R.id.imageButtonBackPlayGameDic);
        spinnerDataBase = view.findViewById(R.id.spinnerDataBase);
        switchMix = view.findViewById(R.id.switchMischen);
        soundHelp = view.findViewById(R.id.imageButtonSoundHelp);
        buttonCheck = view.findViewById(R.id.buttonCheck);
        buttonHelp = view.findViewById(R.id.buttonHelp);
        wordTextView = view.findViewById(R.id.editTextWord);
        translateEditText = view.findViewById(R.id.editTextTranslation);

        backPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentHome());
            }
        });
        addNewWordMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new AddWord());
            }
        });

        dictionaryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new Dictionary());
            }
        });
        createMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentCreate());
            }
        });

        dbHelper = new DataBase(getActivity());
        db = dbHelper.getWritableDatabase();

        loadSelectedLanguage();
        setupTextToSpeech();

        loadTableNames();
        spinnerDataBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = (String) spinnerDataBase.getSelectedItem();
                if (selectedTable != null && !selectedTable.isEmpty()) {
                    loadWordsFromTable(selectedTable);
                } else {
                    Toast.makeText(getActivity(), "Please select a table", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        switchMix.setOnCheckedChangeListener((buttonView, isChecked) -> {
            randomOrder = isChecked;
            if (words != null && !words.isEmpty()) {
                if (randomOrder) {
                    Collections.shuffle(words);
                } else {
                    words.sort((w1, w2) -> w1.getWord().compareTo(w2.getWord())); // Sort by word
                }
                currentIndex = 0;
                showNextWord();
            }
        });
        soundHelp.setOnClickListener(v -> {
            speakCurrentWord();
        });

        buttonCheck.setOnClickListener(v -> checkTranslation());
        buttonHelp.setOnClickListener(v -> showHelp());

        return view;
    }



    private void loadTableNames() {
        List<String> tableNames = dbHelper.getTableNames(db);
        tableNames.add(0, ""); // Add empty option at the beginning
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataBase.setAdapter(adapter);
    }

    private void loadWordsFromTable(String tableName) {
        words = dbHelper.getWordsFromTable(db, tableName);
        currentIndex = 0;
        if (randomOrder) {
            Collections.shuffle(words);
        }
        showNextWord();
    }

    private void showNextWord() {
        if (words != null && !words.isEmpty()) {
            wordTextView.setText("");
            translateEditText.setText(words.get(currentIndex).getTranslation());
        }
    }

    public void checkTranslation() {
        if (words != null && !words.isEmpty()) {
            String userWord = wordTextView.getText().toString().trim();
            String correctWord = words.get(currentIndex).getWord().trim();

            if (userWord.equalsIgnoreCase(correctWord)) {
                showCustomToast();
                currentIndex++;
                if (currentIndex >= words.size()) {
                    currentIndex = 0;
                    if (randomOrder) {
                        Collections.shuffle(words);
                    }

                }
                wordTextView.setTextColor(Color.BLACK); // Reset text color to black
                showNextWord();
            } else {
                wordTextView.setTextColor(Color.RED); // Set text color to red
                Toast.makeText(getActivity(), "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No words loaded!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showHelp() {
        if (words != null && !words.isEmpty()) {
            wordTextView.setText(words.get(currentIndex).getWord());
        }
    }

    private void loadSelectedLanguage() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = preferences.getString(PREF_LANGUAGE, "English"); // Default to English
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
                selectedLanguage = new Locale("es", "ES"); // Spanish
                break;
            case "Italian":
                selectedLanguage = Locale.ITALIAN;
                break;
        }
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(getActivity(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(selectedLanguage);
            } else {
                Log.d("TestWord", "TextToSpeech initialization failed.");
            }
        });
    }

    private void speakCurrentWord() {
        if (words != null && !words.isEmpty()) {
            String word = words.get(currentIndex).getWord();
            Log.d("TestWord", "Speaking word: " + word);
            textToSpeech.setLanguage(selectedLanguage);
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.d("TestWord", "Words list is empty or null.");
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.custom_toast_container));

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
