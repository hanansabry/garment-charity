package com.app.garmentcharity.presentation.requesets;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.data.models.RequestItem;
import com.app.garmentcharity.databinding.ActivityAddRequestDetailsBinding;
import com.app.garmentcharity.di.ViewModelProviderFactory;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.categories.CategoriesViewModel;
import com.app.garmentcharity.presentation.organization.OrganizationMainItemsAdapter;
import com.app.garmentcharity.utils.Constants;
import com.app.garmentcharity.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddRequestDetailsActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private ActivityAddRequestDetailsBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private Request request;
    private HashMap<String, RequestItemsAdapter> categoryItemsAdapterMap = new HashMap<>();
    private String lastCategorySelected;
    private RequestItemsAdapter itemsAdapter;
    private RequestsViewModel requestsViewModel;

    @Override
    public View getDataBindingView() {
        binding = ActivityAddRequestDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request = getIntent().getParcelableExtra(Constants.REQUEST);
        binding.setRequest(request);

        requestsViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(RequestsViewModel.class);
        categoriesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(CategoriesViewModel.class);
        categoriesViewModel.retrieveCategories(request.getOrganizationId());
        categoriesViewModel.getCategoriesLiveData().observe(this, this::populateCategoriesRecyclerView);
        categoriesViewModel.getItemsLiveData().observe(this, this::populateItemsRecyclerView);

        requestsViewModel.getAddRequestStateLiveData().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Request is added successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error while adding request, Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCategoriesRecyclerView(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            binding.categoriesProgressbar.setVisibility(View.GONE);
            OrganizationMainItemsAdapter categoriesAdapter = new OrganizationMainItemsAdapter(null, categories, null, Category.class.getName());
            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            binding.categoriesRecyclerview.setLayoutManager(layout);
            binding.categoriesRecyclerview.setAdapter(categoriesAdapter);
            binding.categoriesRecyclerview.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, binding.categoriesRecyclerview,
                            (view, position) -> {
                                lastCategorySelected = categories.get(position).getId();
                                categoriesAdapter.setSelectedItem(position);
                                categoriesViewModel.retrieveCategoryItems(request.getOrganizationId(), categories.get(position).getId());
                            }));
            //retrieve items of first category
            categoriesAdapter.setSelectedItem(0);
            lastCategorySelected = categories.get(0).getId();
            categoriesViewModel.retrieveCategoryItems(request.getOrganizationId(), categories.get(0).getId());
        }
    }

    private void populateItemsRecyclerView(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            binding.itemsHeader.setVisibility(View.VISIBLE);
            binding.itemsRecyclerview.setVisibility(View.VISIBLE);

            if (categoryItemsAdapterMap.containsKey(lastCategorySelected)) {
                itemsAdapter = categoryItemsAdapterMap.get(lastCategorySelected);
            } else {
                itemsAdapter = new RequestItemsAdapter(items);
                categoryItemsAdapterMap.put(lastCategorySelected, itemsAdapter);
            }
            binding.itemsRecyclerview.setAdapter(itemsAdapter);
        } else {
            binding.itemsHeader.setVisibility(View.GONE);
            binding.itemsRecyclerview.setVisibility(View.GONE);
        }
    }

    public void onSendRequestClicked(View view) {
        List<RequestItem> requestItemList = new ArrayList<>();
        for (String categoryId : categoryItemsAdapterMap.keySet()) {
            RequestItemsAdapter requestItemsAdapter = categoryItemsAdapterMap.get(categoryId);
            List<Item> categoryItems = requestItemsAdapter.getItemList();
            for (int position : requestItemsAdapter.getQuantitiesEditTextValues().keySet()) {
                String quantity = requestItemsAdapter.getQuantitiesEditTextValues().get(position);
                if (quantity != null && !quantity.isEmpty()) {
                    RequestItem requestItem = new RequestItem();
                    requestItem.setItem(categoryItems.get(position));
                    requestItem.setQuantity(Double.parseDouble(quantity));
                    requestItemList.add(requestItem);
                }
            }
        }

        if (requestItemList.isEmpty()) {
            Toast.makeText(this, "You must select at least one item", Toast.LENGTH_SHORT).show();
        } else {
            request.setRequestItemList(requestItemList);
            requestsViewModel.addNewRequest(request);
        }
    }
}