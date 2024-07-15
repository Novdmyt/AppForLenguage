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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;

import java.util.Locale;

public class Setting extends Fragment {

    private ImageButton buttonBack;
    private Spinner spinnerLanguages;
    private ImageButton buttonCreate;
    private ImageButton buttonPlayGame;
    private ImageButton buttonAddData;
    private ImageButton buttonDictionary;
    private static TextToSpeech textToSpeech;
    private SQLiteDatabase db;
    private static Locale selectedLanguage = null;

    private static final String PREFS_NAME = "app_preferences";
    private static final String PREF_LANGUAGE = "selected_language";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        buttonBack = view.findViewById(R.id.imageButtonBackSett);
        spinnerLanguages = view.findViewById(R.id.spinnerLanguages);
        buttonCreate = view.findViewById(R.id.imageButtonNeuGroupeDic);
        buttonPlayGame = view.findViewById(R.id.imageButtonSpeilenDic);
        buttonAddData = view.findViewById(R.id.imageButtonNewWordDic);
        buttonDictionary = view.findViewById(R.id.imageButtonKorrigiernSett);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentHome());
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentCreate());
            }
        });
        buttonPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PlayGame());
            }
        });
        buttonAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new AddWord());
            }
        });

        buttonDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new Dictionary());
            }
        });
        loadSelectedLanguage();
        setupLanguageSpinner();

        textToSpeech = new TextToSpeech(getActivity(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(selectedLanguage);
            } else {
                Log.d("SettingApp", "TextToSpeech initialization failed.");
            }
        });



        return view;
    }
    private void setupLanguageSpinner() {
        // Додайте більше мов, якщо потрібно
        String[] languages = {"English", "German", "French", "Spanish", "Italian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);

        // Встановлюємо обраний раніше мову в спіннері
        String selectedLanguageString = getLanguageString(selectedLanguage);
        int position = adapter.getPosition(selectedLanguageString);
        spinnerLanguages.setSelection(position);

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageString = (String) spinnerLanguages.getSelectedItem();
                switch (selectedLanguageString) {
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
                        selectedLanguage = new Locale("es", "ES"); // Іспанська
                        break;
                    case "Italian":
                        selectedLanguage = Locale.ITALIAN;
                        break;
                }
                textToSpeech.setLanguage(selectedLanguage);
                saveSelectedLanguage(selectedLanguageString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void saveSelectedLanguage(String language) {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_LANGUAGE, language);
        editor.apply();
    }

    private void loadSelectedLanguage() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = preferences.getString(PREF_LANGUAGE, "English"); // За замовчуванням English
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
                selectedLanguage = new Locale("es", "ES"); // Іспанська
                break;
            case "Italian":
                selectedLanguage = Locale.ITALIAN;
                break;
        }
    }

    private String getLanguageString(Locale locale) {
        if (locale.equals(Locale.ENGLISH)) {
            return "English";
        } else if (locale.equals(Locale.GERMAN)) {
            return "German";
        } else if (locale.equals(Locale.FRENCH)) {
            return "French";
        } else if (locale.equals(new Locale("es", "ES"))) {
            return "Spanish";
        } else if (locale.equals(Locale.ITALIAN)) {
            return "Italian";
        }
        return "English";
    }

    public static void speakWord(String word) {
        if (textToSpeech != null && word != null && !word.isEmpty()) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.d("SettingApp", "TextToSpeech is not initialized or word is empty.");
        }
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }

    @Override
    public void onDestroy() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }



    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
