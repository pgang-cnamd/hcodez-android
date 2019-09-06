package com.hcodez.android.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;

public class CodeViewModel extends AndroidViewModel {

    private static final String TAG = "CodeViewModel";

    /**
     * Code entity stuff
     */
    private final LiveData<CodeEntity> mObservableCodeEntity;
    private final int mCodeId;

    /**
     * Content entity stuff
     */
    private final LiveData<ContentEntity> mObservableContentEntity;
    private final int mContentId;

    // what does this do?
    public ObservableField<CodeEntity> code = new ObservableField<>();


    public CodeViewModel(@NonNull Application application,
                         DataRepository repository,
                         final int codeId,
                         final int contentId) {
        super(application);
        Log.d(TAG, "CodeViewModel() called with: application = [" + application + "], repository = [" + repository + "], codeId = [" + codeId + "], contentId = [" + contentId + "]");
        mCodeId = codeId;
        mContentId = contentId;

        mObservableCodeEntity = repository.loadCode(mCodeId);
        mObservableContentEntity = repository.loadContent(mContentId);
    }

    public LiveData<CodeEntity> getObservableCode() {
        Log.d(TAG, "getObservableCode() called");
        return mObservableCodeEntity;
    }

    public LiveData<ContentEntity> getObservableContent() {
        Log.d(TAG, "getObservableContent() called");
        return mObservableContentEntity;
    }

    public void setCode(CodeEntity code) {
        Log.d(TAG, "setCode() called with: code = [" + code + "]");
        this.code.set(code);
    }

    /**
     * A creator is used to inject the code ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the code ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private static final String TAG = "CodeViewModelFactory";

        @NonNull
        private final Application mApplication;

        private final int mCodeId;

        private final int mContentId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int codeId, int contentId) {
            Log.d(TAG, "Factory() called with: application = [" + application + "], codeId = [" + codeId + "], contentId = [" + contentId + "]");
            mApplication = application;
            mCodeId = codeId;
            mContentId = contentId;
            mRepository = ((HcodezApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            Log.d(TAG, "create() called with: modelClass = [" + modelClass + "]");
            //noinspection unchecked
            return (T) new CodeViewModel(mApplication, mRepository, mCodeId, mContentId);
        }
    }
}
