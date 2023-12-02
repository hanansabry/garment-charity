package com.app.garmentcharity.presentation.regions;

import com.app.garmentcharity.data.DatabaseRepository;
import com.app.garmentcharity.data.Organization;
import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.presentation.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegionsViewModel extends BaseViewModel {

    private final MutableLiveData<List<Region>> regionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Organization>> organizationLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addRegionsStateLiveData = new MutableLiveData<>();

    @Inject
    public RegionsViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void retrieveAllRegions() {
        databaseRepository.retrieveAllRegions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Region>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Region> regions) {
                        regionsLiveData.setValue(regions);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void retrieveRegions(String organizationId) {
        databaseRepository.retrieveRegions(organizationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Region>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Region> regions) {
                        regionsLiveData.setValue(regions);
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

    public void retrieveOrganizationsOfRegion(String regionId) {
        databaseRepository.retrieveOrganizationsOfRegion(regionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Organization>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Organization> organizations) {
                        organizationLiveData.setValue(organizations);
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

    public void addRegionsToOrganization(String organizationId, List<Region> selectedRegion) {
        databaseRepository.addRegionsToOrganization(organizationId, selectedRegion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addRegionsStateLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<List<Region>> getRegionsLiveData() {
        return regionsLiveData;
    }

    public MutableLiveData<List<Organization>> getOrganizationOfRegionLiveData() {
        return organizationLiveData;
    }

    public MutableLiveData<Boolean> getAddRegionsStateLiveData() {
        return addRegionsStateLiveData;
    }
}
