package com.app.garmentcharity.presentation;

import android.os.Bundle;
import android.view.View;

import com.app.garmentcharity.utils.SessionManager;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    public SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getDataBindingView());
    }

    public abstract View getDataBindingView();

    public void onBackClicked(View view) {
        onBackPressed();
    }

    public View getRootView(){
        return null;
    }
}