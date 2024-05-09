package com.java.quizzappprj.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Send data from Fragment To Activity
public class ChoiceViewModel extends ViewModel {

    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();

    public void setData(String item) {
        selectedItem.setValue(item);
    }

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }

}
