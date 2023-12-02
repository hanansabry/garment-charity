package com.app.garmentcharity.presentation.regions;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.databinding.ActivityRegionListBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class RegionListActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityRegionListBinding binding;
    private RegionsAdapter regionsAdapter;
    private RegionsViewModel regionsViewModel;

    @Override
    public View getDataBindingView() {
        binding = ActivityRegionListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        regionsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(RegionsViewModel.class);
        regionsViewModel.retrieveAllRegions();
        regionsViewModel.getRegionsLiveData().observe(this, regions -> {
            if (regions != null && !regions.isEmpty()) {
                regionsAdapter = new RegionsAdapter(regions);
                binding.regionsRecyclerview.setAdapter(regionsAdapter);
            }
        });

        regionsViewModel.getAddRegionsStateLiveData().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Regions are added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error while adding regions, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        regionsViewModel.getErrorState().observe(this, error -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }


    // Method to retrieve selected items
    private ArrayList<Region> getSelectedItems() {
        ArrayList<Region> selectedItems = new ArrayList<>();
        SparseBooleanArray selectedPositions = regionsAdapter.getSelectedItems();

        for (int i = 0; i < selectedPositions.size(); i++) {
            int position = selectedPositions.keyAt(i);
            if (selectedPositions.valueAt(i)) {
                selectedItems.add(regionsAdapter.getItem(position));
            }
        }
        return selectedItems;
    }

    public void onSelectRegionsClicked(View view) {
        if (regionsAdapter != null) {
            ArrayList<Region> selectedRegions = getSelectedItems();
            regionsViewModel.addRegionsToOrganization(sessionManager.getFirebaseId(), selectedRegions);
//            Intent data = new Intent();
//            data.putParcelableArrayListExtra(Constants.SELECTED_REGIONS, selectedRegions);
//            setResult(RESULT_OK, data);
//            finish();
        }
    }
}