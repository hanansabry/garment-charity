package com.app.garmentcharity.presentation.items;

import com.app.garmentcharity.data.DatabaseRepository;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.presentation.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ItemsViewModel extends BaseViewModel {

    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addItemStateLiveData = new MutableLiveData<>();

    @Inject
    public ItemsViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void retrieveItems(String organizationId) {
        databaseRepository.retrieveItems(organizationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Item> regions) {
                        itemsLiveData.setValue(regions);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addItem(String organizationId, String categoryId, Item item) {
        databaseRepository.addItem(organizationId, categoryId, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addItemStateLiveData.postValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public MutableLiveData<Boolean> getAddItemStateLiveData() {
        return addItemStateLiveData;
    }
}
