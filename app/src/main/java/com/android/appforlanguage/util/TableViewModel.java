package com.android.appforlanguage.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TableViewModel extends ViewModel {
    private final MutableLiveData<String> newTableName = new MutableLiveData<>();

    public void setNewTableName(String tableName) {
        newTableName.setValue(tableName);
    }

    public LiveData<String> getNewTableName() {
        return newTableName;
    }
}