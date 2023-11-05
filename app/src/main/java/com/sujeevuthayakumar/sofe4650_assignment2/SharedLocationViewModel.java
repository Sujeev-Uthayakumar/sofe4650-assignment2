package com.sujeevuthayakumar.sofe4650_assignment2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedLocationViewModel extends ViewModel {
    private final MutableLiveData<Location> data = new MutableLiveData<>();

    public void setData(Location value) {
        data.setValue(value);
    }

    public LiveData<Location> getData() {
        return data;
    }
}
