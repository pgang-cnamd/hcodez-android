package com.hcodez.android.viewmodel;

import android.app.Application;

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
        mCodeId = codeId;
        mContentId = contentId;

        mObservableCodeEntity = repository.loadCode(mCodeId);
        mObservableContentEntity = repository.loadContent(mContentId);
    }

    public LiveData<CodeEntity> getObservableCode() {
        return mObservableCodeEntity;
    }

    public LiveData<ContentEntity> getObservableContent() {
        return mObservableContentEntity;
    }

    public void setCode(CodeEntity code) {
        this.code.set(code);
    }

    /**
     * A creator is used to inject the code ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the code ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mCodeId;

        private final int mContentId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int codeId, int contentId) {
            mApplication = application;
            mCodeId = codeId;
            mContentId = contentId;
            mRepository = ((HcodezApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CodeViewModel(mApplication, mRepository, mCodeId, mContentId);
        }
    }
}
