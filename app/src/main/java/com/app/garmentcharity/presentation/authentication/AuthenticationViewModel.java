package com.app.garmentcharity.presentation.authentication;

import com.app.garmentcharity.R;
import com.app.garmentcharity.data.Client;
import com.app.garmentcharity.data.DatabaseRepository;
import com.app.garmentcharity.data.Organization;
import com.app.garmentcharity.presentation.BaseViewModel;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthenticationViewModel extends BaseViewModel {

    private final MutableLiveData<Client> clientLiveData = new MutableLiveData<>();
    private final MutableLiveData<Organization> OrganizationLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> validationState = new MutableLiveData<>();

    @Inject
    public AuthenticationViewModel(DatabaseRepository databaseRepository) {
        super(databaseRepository);
    }

    public void loginAsClient(String email, String password) {
        databaseRepository.loginAsClient(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Client>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Client client) {
                        // Emit the Employee and Farm objects to the corresponding LiveData
                        clientLiveData.postValue(client);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void loginAsOrganization(String email, String password) {
        databaseRepository.loginAsOrganization(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Organization>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Organization organization) {
                        // Emit the Employee and Farm objects to the corresponding LiveData
                        OrganizationLiveData.postValue(organization);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void registerClient(Client client) {
        databaseRepository.registerClient(client)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Client>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Client registeredClient) {
                        clientLiveData.postValue(registeredClient);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void registerOrganization(Organization organization) {
        databaseRepository.registerOrganization(organization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Organization>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Organization registeredOrganization) {
                        OrganizationLiveData.postValue(registeredOrganization);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<Client> getClientLiveData() {
        return clientLiveData;
    }

    public MutableLiveData<Organization> getOrganizationLiveData() {
        return OrganizationLiveData;
    }

    public MutableLiveData<Integer> getValidationState() {
        return validationState;
    }

    public boolean validateRegister(String name,
                                    String email,
                                    String password,
                                    String rePassword,
                                    String phone,
                                    String address,
                                    String accountType) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || rePassword.isEmpty() || phone.isEmpty() || address.isEmpty() || accountType.isEmpty()) {
            validationState.setValue(R.string.all_fields_required);
            return false;
        } else if (!password.equals(rePassword)) {
            validationState.setValue(R.string.password_not_match_error);
            return false;
        } else {
            return true;
        }
    }

}
