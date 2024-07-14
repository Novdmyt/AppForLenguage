package com.android.appforlanguage.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;
import com.android.appforlanguage.database.DataBase;

public class FragmentCreate extends Fragment {

    private ImageButton buttonBack;
    private ImageButton buttonPlayGame;
    private ImageButton buttonNewWord;
    private ImageButton buttonDictionary;
    private Button createButtonCard;
    private EditText tableNameEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        buttonBack = view.findViewById(R.id.imageButtonBack);
        buttonPlayGame = view.findViewById(R.id.imageButtonPlayGame);
        buttonNewWord = view.findViewById(R.id.imageButtonNewWord);
        buttonDictionary = view.findViewById(R.id.imageButtonDictionary);
        createButtonCard = view.findViewById(R.id.buttonCreateCard);
        tableNameEditText = view.findViewById(R.id.editTextNameCard);

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
                openFragment(new AddWord());
            }
        });
        buttonNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new PlayGame());
            }
        });
        buttonDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Dictionary());
            }
        });
        createButtonCard.setOnClickListener(v -> {
            String tableName = tableNameEditText.getText().toString().trim();
            if (isValidTableName(tableName)) {
                createTable(tableName);
            } else {
                Toast.makeText(getActivity(), "Invalid table name. Ensure it starts with a letter and contains only alphanumeric characters and spaces.", Toast.LENGTH_SHORT).show();
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

    private boolean isValidTableName(String tableName) {
        return tableName.matches("[a-zA-Zа-яА-ЯäöüÄÖÜß][a-zA-Zа-яА-ЯäöüÄÖÜß0-9 ]*");
    }

    private void createTable(String tableName) {
        DataBase dataBase = new DataBase(getActivity());
        SQLiteDatabase db = dataBase.getWritableDatabase();
        if (db != null) {
            if (!dataBase.doesTableExist(db, tableName)) {
                dataBase.createTable(db, tableName);
                showCustomToast();  // Викликаємо користувацький Toast
                tableNameEditText.getText().clear(); // Очистити EditText
            } else {
                Toast.makeText(getActivity(), "Table '" + tableName + "' already exists.", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } else {
            Toast.makeText(getActivity(), "Failed to create table.", Toast.LENGTH_SHORT).show();
        }
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
