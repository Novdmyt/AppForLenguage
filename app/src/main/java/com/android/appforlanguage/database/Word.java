package com.android.appforlanguage.database;

public class Word {
    int id;
    private String word;
    private String translation;

    public Word(int id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    public int  getId() {
        return id;
    }
    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }
}
