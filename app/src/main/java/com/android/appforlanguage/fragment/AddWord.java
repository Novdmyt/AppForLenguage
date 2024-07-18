package com.android.appforlanguage.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.appforlanguage.R;
import com.android.appforlanguage.database.DataBase;
import com.android.appforlanguage.util.HideKeyboard;
import com.android.appforlanguage.util.Language;
import com.android.appforlanguage.util.TableViewModel;

import java.util.List;

public class AddWord extends Fragment {

    private ImageButton playGameAdd;
    private ImageButton dictionaryMenu;
    private ImageButton createCard;
    private ImageButton backToHome;
    private Spinner openDataBase;
    private Button addWords;
    private EditText viewWord;
    private EditText viewTranslate;
    private DataBase dbHelper;
    private TableViewModel tableViewModel;
    private ArrayAdapter<String> adapter;
    private Spinner wordLanguageSpinner;
    private Spinner translateLanguageSpinner;
    private Language language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_word, container, false);

        playGameAdd = view.findViewById(R.id.imageButtonPlayGameMenuAdd);
        dictionaryMenu = view.findViewById(R.id.imageButtonDictionaryMenuAdd);
        createCard = view.findViewById(R.id.imageButtonCreateCardMenuAdd);
        backToHome = view.findViewById(R.id.imageButtonBackToHome);
        openDataBase = view.findViewById(R.id.spinnerOpenDataBase);
        addWords = view.findViewById(R.id.buttonAddWords);
        viewWord = view.findViewById(R.id.textViewWord);
        viewTranslate = view.findViewById(R.id.textViewTranslate);
        wordLanguageSpinner = view.findViewById(R.id.wordLanguageSpinner);
        translateLanguageSpinner = view.findViewById(R.id.translateLanguageSpinner);

        language = new Language(getContext());
        dbHelper = new DataBase(getContext());
        populateTableSpinner();
        addWords.setOnClickListener(v -> addWordToDatabase());

        tableViewModel = new ViewModelProvider(requireActivity()).get(TableViewModel.class);
        setDrawableRightClickListener(viewWord, wordLanguageSpinner, true);
        setDrawableRightClickListener(viewTranslate, translateLanguageSpinner, false);
        tableViewModel.getNewTableName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String tableName) {
                adapter.add(tableName);
                adapter.notifyDataSetChanged();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideKeyboard(getActivity());
                openFragment(new FragmentHome());
            }
        });
        playGameAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new PlayGame());
            }
        });
        dictionaryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Dictionary());
            }
        });

        createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new FragmentCreate());
            }
        });

        return view;
    }

    private void populateTableSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> tables = dbHelper.getTableNames(db);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openDataBase.setAdapter(adapter);
    }

    private void addWordToDatabase() {
        String selectedTable = openDataBase.getSelectedItem().toString();
        String word = viewWord.getText().toString().trim();
        String translation = viewTranslate.getText().toString().trim();

        if (!word.isEmpty() && !translation.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertWord(db, selectedTable, word, translation);
            viewWord.setText("");
            viewTranslate.setText("");
            showCustomToast();
        } else {
            Toast.makeText(getContext(), "Please enter both word and translation", Toast.LENGTH_SHORT).show();
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
        viewWord.getLocationOnScreen(location);
        int buttonX = location[0];
        int buttonY = location[1];
        // Встановити розташування Toast над кнопкою
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, buttonY - viewWord.getHeight());

        toast.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> toast.cancel(), 800); // 1000 мс = 1 секунда
    }

    private void setDrawableRightClickListener(EditText editText, Spinner spinner, boolean isWordEditText) {
        Drawable drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.group_langue_menu);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            editText.setCompoundDrawables(null, null, drawable, null);
            editText.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (editText.getRight() - drawable.getBounds().width())) {
                    editText.requestFocus();  // Зробити EditText активним
                    editText.post(() -> {
                        if (isWordEditText) {
                            language.showWordLanguageMenu(v, spinner, editText);
                        } else {
                            language.showTranslationLanguageMenu(v, spinner, editText);
                        }
                    });
                    return true;
                }
                return false;
            });
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
