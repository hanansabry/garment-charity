package com.app.garmentcharity.presentation.organization;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.databinding.ActivityOrganizationRequestsBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.requesets.RequestDetailsActivity;
import com.app.garmentcharity.presentation.requesets.RequestsAdapter;
import com.app.garmentcharity.presentation.requesets.RequestsViewModel;
import com.app.garmentcharity.utils.Constants;

import java.util.List;

import javax.inject.Inject;

public class OrganizationRequestsActivity extends BaseActivity implements RequestsAdapter.RequestsCallback {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityOrganizationRequestsBinding binding;

    @Override
    public View getDataBindingView() {
        binding = ActivityOrganizationRequestsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestsViewModel requestsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(RequestsViewModel.class);
        requestsViewModel.retrieveOrganizationRequests(sessionManager.getFirebaseId());
        requestsViewModel.getRequestsLiveData().observe(this, this::populateRequestsRecyclerView);

        requestsViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void populateRequestsRecyclerView(List<Request> requests) {
        if (requests != null && !requests.isEmpty()) {
            RequestsAdapter requestsAdapter = new RequestsAdapter(requests, this);
            binding.requestsRecyclerview.setAdapter(requestsAdapter);
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRequestClick(Request request) {
        Intent intent = new Intent(this, RequestDetailsActivity.class);
        intent.putExtra(Constants.REQUEST, request.getId());
        intent.putExtra(Constants.CLIENT, request.getClientId());
        startActivity(intent);
    }
}