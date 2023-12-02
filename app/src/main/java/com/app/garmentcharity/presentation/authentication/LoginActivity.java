package com.app.garmentcharity.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.R;
import com.app.garmentcharity.databinding.ActivityLoginBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.client.ClientHomeActivity;
import com.app.garmentcharity.presentation.organization.OrganizationHomeActivity;
import com.app.garmentcharity.utils.Constants;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityLoginBinding binding;
    private AuthenticationViewModel authenticationViewModel;
    private String loginType;

    @Override
    public View getDataBindingView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginType = getIntent().getStringExtra(Constants.LOGIN_TYPE);
        if (loginType.equals(Constants.CLIENT)) {
            binding.loginLbl.setText(R.string.login_as_client);
        } else if (loginType.equals(Constants.ORGANIZATION)) {
            binding.loginLbl.setText(R.string.login_as_organization);
        }

        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        authenticationViewModel.getClientLiveData().observe(this, client -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            if (client != null) {
                Toast.makeText(this, "Login as client successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ClientHomeActivity.class));
                sessionManager.createLoginSession(client.getId(), client.getName(), client.getEmail(), Constants.CLIENT);
            }
        });
        authenticationViewModel.getOrganizationLiveData().observe(this, organization -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            if (organization != null) {
                Toast.makeText(this, "Login as organization successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, OrganizationHomeActivity.class));
                sessionManager.createLoginSession(organization.getId(), organization.getName(), organization.getEmail(), Constants.ORGANIZATION);
            }
        });

        authenticationViewModel.getErrorState().observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    public void onLoginClicked(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setVisibility(View.GONE);
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.email_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (loginType.equals(Constants.CLIENT)) {
            authenticationViewModel.loginAsClient(email, password);
        } else if (loginType.equals(Constants.ORGANIZATION)) {
            authenticationViewModel.loginAsOrganization(email, password);
        }
    }
    
}