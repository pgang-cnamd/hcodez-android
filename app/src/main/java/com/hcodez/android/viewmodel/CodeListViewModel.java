package com.hcodez.android.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.entity.CodeEntity;

import java.util.List;

public class CodeListViewModel extends AndroidViewModel {

    private static final String TAG = "CodeListViewModel";

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CodeEntity>> mObservableCodes;

    public CodeListViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "CodeListViewModel() called with: application = [" + application + "]");

        this.mObservableCodes = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        this.mObservableCodes.setValue(null);

        this.mRepository = ((HcodezApp) application).getRepository();
        LiveData<List<CodeEntity>> codes = mRepository.getCodes();

        // observe the changes of the products from the database and forward them
        this.mObservableCodes.addSource(codes, mObservableCodes::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<CodeEntity>> getCodes() {
        Log.d(TAG, "getCodes() called");
        return this.mObservableCodes;
    }

    /**
     * Search for codes
     * @param query query to search by
     * @return live data list of codes
     */
    public LiveData<List<CodeEntity>> searchCodes(String query) {
        Log.d(TAG, "searchCodes() called with: query = [" + query + "]");
        return mRepository.searchCodes(query);
    }
}
