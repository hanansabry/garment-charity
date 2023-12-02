package com.app.garmentcharity.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.garmentcharity.databinding.ActivityMainBinding;
import com.app.garmentcharity.presentation.authentication.LoginActivity;
import com.app.garmentcharity.presentation.authentication.RegisterActivity;
import com.app.garmentcharity.utils.Constants;

public class MainActivity extends BaseActivity {

    @Override
    public View getDataBindingView() {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onLoginAsClientClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.CLIENT);
        startActivity(intent);
    }

    public void onLoginAsVendorClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.ORGANIZATION);
        startActivity(intent);
    }

    public void onRegisterNowClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}