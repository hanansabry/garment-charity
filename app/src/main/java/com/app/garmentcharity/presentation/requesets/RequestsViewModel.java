package com.app.garmentcharity.presentation.requesets;

import com.app.garmentcharity.data.Client;
import com.app.garmentcharity.data.DatabaseRepository;
import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.presentation.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RequestsViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> addRequestStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Request>> requestsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> clientPointsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Request> requestDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Client> clientDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> requestStatusLiveData = new MutableLiveData<>();

    @Inject
    public RequestsViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void addNewRequest(Request request) {
        databaseRepository.addNewRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addRequestStateLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void retrieveClientRequests(String clientId) {
        databaseRepository.retrieveClientRequests(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<List<Request>, Double>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Pair<List<Request>, Double> requestsPointsPair) {
                        requestsLiveData.setValue(requestsPointsPair.first);
                        clientPointsLiveData.setValue(requestsPointsPair.second);
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

    public void retrieveOrganizationRequests(String organizationId) {
        databaseRepository.retrieveOrganizationRequests(organizationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Request>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Request> requests) {
                        requestsLiveData.setValue(requests);
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

    public void retrieveRequestDetails(String requestId) {
        databaseRepository.retrieveRequestDetails(requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Pair<Request, Client>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Pair<Request, Client> requestClientPair) {
                        requestDetailsLiveData.setValue(requestClientPair.first);
                        clientDetailsLiveData.setValue(requestClientPair.second);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void setRequestStatus(Request request, String status) {
        databaseRepository.setRequestStatus(request, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        requestStatusLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<Boolean> getAddRequestStateLiveData() {
        return addRequestStateLiveData;
    }

    public MutableLiveData<List<Request>> getRequestsLiveData() {
        return requestsLiveData;
    }

    public MutableLiveData<Request> getRequestDetailsLiveData() {
        return requestDetailsLiveData;
    }

    public MutableLiveData<Client> getClientDetailsLiveData() {
        return clientDetailsLiveData;
    }

    public MutableLiveData<Boolean> getRequestStatusLiveData() {
        return requestStatusLiveData;
    }

    public MutableLiveData<Double> getClientPointsLiveData() {
        return clientPointsLiveData;
    }
}
