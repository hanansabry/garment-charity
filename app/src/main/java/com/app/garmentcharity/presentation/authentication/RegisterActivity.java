package com.app.garmentcharity.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.R;
import com.app.garmentcharity.data.Client;
import com.app.garmentcharity.data.Organization;
import com.app.garmentcharity.databinding.ActivityRegisterBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.MainActivity;
import com.app.garmentcharity.presentation.client.ClientHomeActivity;
import com.app.garmentcharity.presentation.organization.OrganizationHomeActivity;
import com.app.garmentcharity.utils.Constants;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;

public class RegisterActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private AuthenticationViewModel authenticationViewModel;
    private ActivityRegisterBinding binding;
    private String accountType = Constants.CLIENT;

    @Override
    public View getDataBindingView() {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        authenticationViewModel.getValidationState().observe(this, status -> {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
        });

        authenticationViewModel.getClientLiveData().observe(this, client -> {
            Toast.makeText(this, client.getId(), Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
            sessionManager.createLoginSession(client.getId(), client.getName(), client.getEmail(), Constants.CLIENT);
            //go to client path
            startActivity(new Intent(this, ClientHomeActivity.class));
        });

        authenticationViewModel.getOrganizationLiveData().observe(this, organization -> {
            Toast.makeText(this, organization.getId(), Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
            sessionManager.createLoginSession(organization.getId(), organization.getName(), organization.getEmail(), Constants.ORGANIZATION);
            //go to organization path
            startActivity(new Intent(this, OrganizationHomeActivity.class));
        });

        authenticationViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
        });
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onRegisterClicked(View view) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.registerButton.setVisibility(View.GONE);

        String organizationName = binding.organizationEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String rePassword = binding.confirmPasswordEditText.getText().toString();
        String phone = binding.phoneEditText.getText().toString();
        String address = binding.addressEditText.getText().toString();
        accountType = binding.accountTypeRadioGroup.getCheckedRadioButtonId() == R.id.client_radio ? Constants.CLIENT : Constants.ORGANIZATION;

        boolean isValid = authenticationViewModel.validateRegister(
                organizationName, email, password, rePassword, phone, address, accountType
        );
        if (isValid) {
            if (accountType.equals(Constants.CLIENT)) {
                Client client = new Client();
                client.setName(organizationName);
                client.setEmail(email);
                client.setPassword(password);
                client.setPhone(phone);
                client.setAddress(address);
                authenticationViewModel.registerClient(client);
            } else {
                Organization organization = new Organization();
                organization.setName(organizationName);
                organization.setEmail(email);
                organization.setPassword(password);
                organization.setPhone(phone);
                organization.setAddress(address);
                authenticationViewModel.registerOrganization(organization);
            }
        }
    }

}