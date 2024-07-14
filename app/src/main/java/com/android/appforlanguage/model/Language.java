package com.android.appforlanguage.model;

import android.content.Context;
import android.os.LocaleList;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.android.appforlanguage.R;


public class Language
{
    private final Context context;

    public Language(Context context) {

        this.context = context;
    }

    public void showWordLanguageMenu(View v, Spinner spinner, EditText editText) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.language_word, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.language_english) {
                spinner.setSelection(0); // Assuming 0 is the position for English in the spinner
                setLanguageInputType(editText, "en");
            } else if (itemId == R.id.language_german) {
                spinner.setSelection(1); // Assuming 1 is the position for German in the spinner
                setLanguageInputType(editText, "de");
            }
            return true;
        });
        popupMenu.show();
    }

    public void showTranslationLanguageMenu(View v, Spinner spinner, EditText editText) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.language_translation, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.language_ukrainian) {
                spinner.setSelection(0); // Assuming 0 is the position for Ukrainian in the spinner
                setLanguageInputType(editText, "uk");
            } else if (itemId == R.id.language_russian) {
                spinner.setSelection(1); // Assuming 1 is the position for Russian in the spinner
                setLanguageInputType(editText, "ru");
            }
            return true;
        });
        popupMenu.show();
    }

    private void setLanguageInputType(EditText editText, String languageCode) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            LocaleList localeList = LocaleList.forLanguageTags(languageCode);
            editText.setImeHintLocales(localeList);
            imm.restartInput(editText);
        }
    }
}
