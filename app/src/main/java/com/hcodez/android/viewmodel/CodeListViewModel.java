package com.hcodez.android.viewmodel;

import android.app.Application;

import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.ui.fragment.CodeListFragment;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class CodeListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<CodeEntity>> mObservableCodes;

    public CodeListViewModel(@NonNull Application application) {
        super(application);

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
        return this.mObservableCodes;
    }

    /**
     * Search for codes
     * @param query query to search by
     * @return live data list of codes
     */
    public LiveData<List<CodeEntity>> searchCodes(String query) {
        return mRepository.searchCodes(query);
    }
}
