package com.app.garmentcharity.presentation.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.databinding.ActivityOrganizationHomeBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.MainActivity;
import com.app.garmentcharity.presentation.categories.AddCategoryActivity;
import com.app.garmentcharity.presentation.categories.CategoriesViewModel;
import com.app.garmentcharity.presentation.items.AddItemActivity;
import com.app.garmentcharity.presentation.items.ItemsViewModel;
import com.app.garmentcharity.presentation.regions.RegionListActivity;
import com.app.garmentcharity.presentation.regions.RegionsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizationHomeActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityOrganizationHomeBinding binding;
    private OrganizationMainItemsAdapter regionsAdapter;
    private OrganizationMainItemsAdapter categoriesAdapter;

    @Override
    public View getDataBindingView() {
        binding = ActivityOrganizationHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        binding.setName(sessionManager.getUserName());

        CategoriesViewModel categoriesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(CategoriesViewModel.class);
        RegionsViewModel regionsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(RegionsViewModel.class);
        ItemsViewModel itemsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(ItemsViewModel.class);

        regionsViewModel.retrieveRegions(sessionManager.getFirebaseId());
        categoriesViewModel.retrieveCategories(sessionManager.getFirebaseId());
        itemsViewModel.retrieveItems(sessionManager.getFirebaseId());

        regionsViewModel.getRegionsLiveData().observe(this, regions -> {
            regionsAdapter = new OrganizationMainItemsAdapter(regions, null, null, Region.class.getName());
            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            binding.regionsRecyclerview.setLayoutManager(layout);
            binding.regionsRecyclerview.setAdapter(regionsAdapter);
            binding.regionsProgressbar.setVisibility(View.GONE);
        });

        categoriesViewModel.getCategoriesLiveData().observe(this, categories -> {
            categoriesAdapter = new OrganizationMainItemsAdapter(null, categories, null, Category.class.getName());
            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            binding.categoriesRecyclerview.setLayoutManager(layout);
            binding.categoriesRecyclerview.setAdapter(categoriesAdapter);
            binding.categoriesProgressbar.setVisibility(View.GONE);
        });

        itemsViewModel.getItemsLiveData().observe(this, items -> {
            OrganizationMainItemsAdapter adapter = new OrganizationMainItemsAdapter(null, null, items, Item.class.getName());
            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            binding.itemsRecyclerview.setLayoutManager(layout);
            binding.itemsRecyclerview.setAdapter(adapter);
            binding.itemsProgressbar.setVisibility(View.GONE);
        });

        itemsViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
        categoriesViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
        regionsViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    public void onLogoutClicked(View view) {
        FirebaseAuth.getInstance().signOut();
        sessionManager.logoutUser();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onAddRegionClicked(View view) {
        startActivity(new Intent(this, RegionListActivity.class));
    }

    public void onAddCategoryClicked(View view) {
        startActivity(new Intent(this, AddCategoryActivity.class));
    }

    public void onAddItemClicked(View view) {
        startActivity(new Intent(this, AddItemActivity.class));
    }

    public void onShowRequestsClicked(View view) {
        startActivity(new Intent(this, OrganizationRequestsActivity.class));
    }
}