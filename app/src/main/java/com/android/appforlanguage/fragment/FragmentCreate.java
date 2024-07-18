package com.android.appforlanguage.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.appforlanguage.R;
import com.android.appforlanguage.database.DataBase;
import com.android.appforlanguage.util.HideKeyboard;

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

        buttonBack = view.findViewById(R.id.imageButtonBackSett);
        buttonPlayGame = view.findViewById(R.id.imageButtonPlayGame);
        buttonNewWord = view.findViewById(R.id.imageButtonNewWord);
        buttonDictionary = view.findViewById(R.id.imageButtonDictionary);
        createButtonCard = view.findViewById(R.id.buttonCreateCard);
        tableNameEditText = view.findViewById(R.id.editTextNameCard);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideKeyboard(getActivity());
                openFragment(new FragmentHome());
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
                HideKeyboard.hideKeyboard(getActivity()); // Сховати клавіатуру
            } else {
                showCustomToastError();
            }
            db.close();
        } else {
            showCustomToastError2();
        }
    }

    private void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.custom_toast_container));
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        // Отримати координати кнопки
        int[] location = new int[2];
        buttonBack.getLocationOnScreen(location);
        int buttonX = location[0];
        int buttonY = location[1];
        // Встановити розташування Toast над кнопкою
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, buttonY - buttonBack.getHeight());
        toast.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> toast.cancel(), 900); // 1000 мс = 1 секунда
    }

    private void showCustomToastError() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_error, (ViewGroup) getView().findViewById(R.id.custom_toast_container));

        // Налаштування повідомлення
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText("einen solchen Namen gibt es");
        // Використовуємо Toast для відображення спеціального повідомлення
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        // Отримати координати кнопки
        int[] location = new int[2];
        buttonBack.getLocationOnScreen(location);
        int buttonX = location[0];
        int buttonY = location[1];
        // Встановити розташування Toast над кнопкою
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, buttonY - buttonBack.getHeight());
        toast.show();
        // Використання Handler для приховування Toast через короткий проміжок часу
        new Handler(Looper.getMainLooper()).postDelayed(toast::cancel, 1000); // 1000 мс = 1 секунда
    }
    private void showCustomToastError2() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_error, (ViewGroup) getView().findViewById(R.id.custom_toast_container));

        // Налаштування повідомлення
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText("Erstellen der Tabelle fehlgeschlagen.");
        // Використовуємо Toast для відображення спеціального повідомлення
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        // Отримати координати кнопки
        int[] location = new int[2];
        buttonBack.getLocationOnScreen(location);
        int buttonX = location[0];
        int buttonY = location[1];
        // Встановити розташування Toast над кнопкою
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, buttonY - buttonBack.getHeight());
        toast.show();
        // Використання Handler для приховування Toast через короткий проміжок часу
        new Handler(Looper.getMainLooper()).postDelayed(toast::cancel, 1000); // 1000 мс = 1 секунда
    }
   /* private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/
}
