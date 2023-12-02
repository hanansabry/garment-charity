package com.app.garmentcharity.presentation.organization;

import com.app.garmentcharity.data.DatabaseRepository;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.presentation.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;

public class OrganizationViewModel extends BaseViewModel {

    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();

    @Inject
    public OrganizationViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void retrieveItems(String organizationId) {
        Item r1 = new Item();
        r1.setName("Item 1");

        Item r2 = new Item();
        r2.setName("Item 2");
        List<Item> items = new ArrayList<>();
        items.add(r1);
        items.add(r2);

        itemsLiveData.postValue(items);
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }
}
