package com.app.garmentcharity.data;

import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.datasource.FirebaseDataSource;

import java.util.List;

import javax.inject.Inject;

import androidx.core.util.Pair;
import io.reactivex.Observable;
import io.reactivex.Single;

public class DatabaseRepository {

    private final FirebaseDataSource firebaseDataSource;

    @Inject
    public DatabaseRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }

    public Single<Client> loginAsClient(String email, String password) {
        return firebaseDataSource.loginAsClient(email, password);
    }

    public Single<Organization> loginAsOrganization(String email, String password) {
        return firebaseDataSource.loginAsOrganization(email, password);
    }

    public Single<Client> registerClient(Client client) {
        return firebaseDataSource.registerClient(client);
    }

    public Single<Organization> registerOrganization(Organization organization) {
        return firebaseDataSource.registerOrganization(organization);
    }

    public Observable<List<Region>> retrieveRegions(String organizationId) {
        return firebaseDataSource.retrieveRegions(organizationId);
    }

    public Single<List<Region>> retrieveAllRegions() {
        return firebaseDataSource.retrieveAllRegions();
    }

    public Single<Boolean> addRegionsToOrganization(String organizationId, List<Region> selectedRegions) {
        return firebaseDataSource.addRegionsToOrganization(organizationId, selectedRegions);
    }

    public Observable<List<Category>> retrieveCategories(String organizationId) {
        return firebaseDataSource.retrieveCategories(organizationId);
    }

    public Single<Boolean> addCategory(String organizationId, Category category) {
        return firebaseDataSource.addCategory(organizationId, category);
    }

    public Observable<List<Item>> retrieveItems(String organizationId) {
        return firebaseDataSource.retrieveItems(organizationId);
    }

    public Single<Boolean> addItem(String organizationId, String categoryId, Item item) {
        return firebaseDataSource.addItem(organizationId, categoryId, item);
    }

    public Observable<List<Organization>> retrieveOrganizationsOfRegion(String regionId) {
        return firebaseDataSource.retrieveOrganizationsOfRegion(regionId);
    }

    public Observable<List<Item>> retrieveCategoryItems(String organizationId, String categoryId) {
        return firebaseDataSource.retrieveCategoryItems(organizationId, categoryId);
    }

    public Single<Boolean> addNewRequest(Request request) {
        return firebaseDataSource.addNewRequest(request);
    }

    public Observable<Pair<List<Request>, Double>> retrieveClientRequests(String clientId) {
        return firebaseDataSource.retrieveClientRequests(clientId);
    }

    public Observable<List<Request>> retrieveOrganizationRequests(String organizationId) {
        return firebaseDataSource.retrieveOrganizationRequests(organizationId);

    }

    public Single<Pair<Request, Client>> retrieveRequestDetails(String requestId) {
        return firebaseDataSource.retrieveRequestDetails(requestId);
    }

    public Single<Boolean> setRequestStatus(Request request, String status) {
        return firebaseDataSource.setRequestStatus(request, status);
    }
}
