package com.sujeevuthayakumar.sofe4650_assignment2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This class extends the ViewModel class and is used to share Location data across different
 * components of the application. It utilizes LiveData to observe changes in the Location data
 * reactively.
 */
public class SharedLocationViewModel extends ViewModel {
    // MutableLiveData to hold the Location data. It allows us to set and post Location updates.
    private final MutableLiveData<Location> data = new MutableLiveData<>();


    // Updates the value of the Location data. Any active observers will be notified of this change.
    public void setData(Location value) {
        data.setValue(value);
    }

    /**
     * Returns the LiveData object. This allows observers to subscribe to changes in the Location data
     * without being able to modify it.
     */
    public LiveData<Location> getData() {
        return data;
    }
}
