package com.app.garmentcharity.presentation.categories;

import com.app.garmentcharity.data.Category;
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

public class CategoriesViewModel extends BaseViewModel {

    private final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addCategoryStateLiveData = new MutableLiveData<>();

    @Inject
    public CategoriesViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void retrieveCategories(String organizationId) {
        databaseRepository.retrieveCategories(organizationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Category>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Category> regions) {
                        categoriesLiveData.setValue(regions);
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

    public void retrieveCategoryItems(String organizationId, String categoryId) {
        databaseRepository.retrieveCategoryItems(organizationId, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        itemsLiveData.setValue(items);
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

    public void addCategory(String organizationId, Category category) {
        databaseRepository.addCategory(organizationId, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addCategoryStateLiveData.postValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Category>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public MutableLiveData<Boolean> getAddCategoryStateLiveData() {
        return addCategoryStateLiveData;
    }
}
