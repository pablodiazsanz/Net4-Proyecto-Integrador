package com.pass.net4_proyecto_integrador.fragmentsMainActivity.helpAlert;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpAlertViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpAlertViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}