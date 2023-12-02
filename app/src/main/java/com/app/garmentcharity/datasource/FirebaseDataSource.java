package com.app.garmentcharity.datasource;

import android.net.Uri;

import com.app.garmentcharity.data.Category;
import com.app.garmentcharity.data.Client;
import com.app.garmentcharity.data.Item;
import com.app.garmentcharity.data.Organization;
import com.app.garmentcharity.data.Region;
import com.app.garmentcharity.data.models.Request;
import com.app.garmentcharity.data.models.RequestItem;
import com.app.garmentcharity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import io.reactivex.Observable;
import io.reactivex.Single;

public class FirebaseDataSource {

    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private final StorageReference storageReference;

    @Inject
    public FirebaseDataSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, StorageReference storageReference) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.storageReference = storageReference;
    }

    public Single<Client> loginAsClient(String email, String password) {
        return Single.create(emitter -> {
            DatabaseReference clientsRef = firebaseDatabase.getReference(Constants.CLIENTS_NODE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String clientId = task.getResult().getUser().getUid();
                            clientsRef.child(clientId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Client client = snapshot.getValue(Client.class);
                                            if (client != null) {
                                                client.setId(clientId);
                                                emitter.onSuccess(client);
                                            } else {
                                                emitter.onError(new Throwable("Invalid email or password"));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            emitter.onError(error.toException());
                                        }
                                    });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<Organization> loginAsOrganization(String email, String password) {
        return Single.create(emitter -> {
            DatabaseReference organizationsRef = firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String organizationId = task.getResult().getUser().getUid();
                            organizationsRef.child(organizationId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Organization organization = snapshot.getValue(Organization.class);
                                            if (organization != null) {
                                                organization.setId(organizationId);
                                                emitter.onSuccess(organization);
                                            } else {
                                                emitter.onError(new Throwable("Invalid email or password"));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            emitter.onError(error.toException());
                                        }
                                    });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<Client> registerClient(Client client) {
        return Single.create(emitter -> {
            DatabaseReference clientsRef = firebaseDatabase.getReference(Constants.CLIENTS_NODE);
            //create client with email and password
            firebaseAuth.createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String clientId = task.getResult().getUser().getUid();
                            client.setId(clientId);
                            clientsRef.child(clientId)
                                    .setValue(client)
                                    .addOnCompleteListener(saveTask -> {
                                        if (saveTask.isSuccessful()) {
                                            emitter.onSuccess(client);
                                        } else {
                                            emitter.onError(saveTask.getException());
                                        }
                                    });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<Organization> registerOrganization(Organization organization) {
        return Single.create(emitter -> {
            DatabaseReference organizationsRef = firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE);
            //create client with email and password
            firebaseAuth.createUserWithEmailAndPassword(organization.getEmail(), organization.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String organizationId = task.getResult().getUser().getUid();
                            organization.setId(organizationId);
                            organizationsRef.child(organizationId)
                                    .setValue(organization)
                                    .addOnCompleteListener(saveTask -> {
                                        if (saveTask.isSuccessful()) {
                                            emitter.onSuccess(organization);
                                        } else {
                                            emitter.onError(saveTask.getException());
                                        }
                                    });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Observable<List<Region>> retrieveRegions(String organizationId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.REGIONS_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Region> regions = new ArrayList<>();
                            for (DataSnapshot regionSnapshot : snapshot.getChildren()) {
                                Region region = regionSnapshot.getValue(Region.class);
                                if (region != null) {
                                    region.setId(regionSnapshot.getKey());
                                }
                                regions.add(region);
                            }
                            emitter.onNext(regions);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<List<Region>> retrieveAllRegions() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.REGIONS_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Region> regions = new ArrayList<>();
                            for (DataSnapshot regionSnapshot : snapshot.getChildren()) {
                                Region region = regionSnapshot.getValue(Region.class);
                                if (region != null) {
                                    region.setId(regionSnapshot.getKey());
                                }
                                regions.add(region);
                            }
                            emitter.onSuccess(regions);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Boolean> addRegionsToOrganization(String organizationId, List<Region> selectedRegions) {
        return Single.create(emitter -> {
            HashMap<String, Object> updatedValues = new HashMap<>();
            DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.REGIONS_NODE);
            for (Region region : selectedRegions) {
                updatedValues.put(databaseReference.push().getKey(), region);
            }
            databaseReference.updateChildren(updatedValues)
                    .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful()));
        });
    }

    public Observable<List<Category>> retrieveCategories(String organizationId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.CATEGORIES_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Category> categories = new ArrayList<>();
                            for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                                Category category = categorySnapshot.getValue(Category.class);
                                if (category != null) {
                                    category.setId(categorySnapshot.getKey());
                                }
                                categories.add(category);
                            }
                            emitter.onNext(categories);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Boolean> addCategory(String organizationId, Category category) {
        return Single.create(emitter -> {
            //upload image firstly
            Uri imageUri = Uri.parse(category.getImage());
            StorageReference imageReference = storageReference
                    .child(Constants.ORGANIZATIONS_NODE + "/" +
                            organizationId + "/" +
                            Constants.CATEGORIES_NODE + "/" +
                            System.currentTimeMillis() +
                            Constants.IMAGE_FILE_TYPE);

            UploadTask uploadFileTask = imageReference.putFile(imageUri);
            uploadFileTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                } else {
                    // Continue with the fileTask to get the download URL
                    imageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        String downloadUrl = task1.getResult().toString();
                        category.setImage(downloadUrl);
                        firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                                .child(organizationId)
                                .child(Constants.CATEGORIES_NODE)
                                .push()
                                .setValue(category)
                                .addOnCompleteListener(saveTask -> {
                                    emitter.onSuccess(saveTask.isSuccessful());
                                });
                    });
                }
                return imageReference.getDownloadUrl();
            });
        });
    }

    public Observable<List<Item>> retrieveItems(String organizationId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.CATEGORIES_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Item> items = new ArrayList<>();
                            for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                                if (categorySnapshot.hasChild(Constants.ITEMS_NODE)) {
                                    for (DataSnapshot itemSnapshot : categorySnapshot.child(Constants.ITEMS_NODE).getChildren()) {
                                        Item item = itemSnapshot.getValue(Item.class);
                                        if (item != null) {
                                            item.setId(itemSnapshot.getKey());
                                            items.add(item);
                                        }
                                    }
                                }
                            }
                            emitter.onNext(items);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Boolean> addItem(String organizationId, String categoryId, Item item) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.CATEGORIES_NODE)
                    .child(categoryId)
                    .child(Constants.ITEMS_NODE)
                    .push()
                    .setValue(item)
                    .addOnCompleteListener(saveTask -> {
                        emitter.onSuccess(saveTask.isSuccessful());
                    });
        });
    }

    public Observable<List<Organization>> retrieveOrganizationsOfRegion(String regionId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Organization> organizations = new ArrayList<>();
                            for (DataSnapshot organizationSnapshot : snapshot.getChildren()) {
                                if (organizationSnapshot.hasChild(Constants.REGIONS_NODE)) {
                                    for (DataSnapshot regionSnapshot : organizationSnapshot.child(Constants.REGIONS_NODE).getChildren()) {
                                        Region region = regionSnapshot.getValue(Region.class);
                                        if (region != null && region.getId().equals(regionId)) {
                                            Organization organization = organizationSnapshot.getValue(Organization.class);
                                            organization.setId(organizationSnapshot.getKey());
                                            organizations.add(organization);
                                            break;
                                        }
                                    }
                                }
                            }
                            emitter.onNext(organizations);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Observable<List<Item>> retrieveCategoryItems(String organizationId, String categoryId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.ORGANIZATIONS_NODE)
                    .child(organizationId)
                    .child(Constants.CATEGORIES_NODE)
                    .child(categoryId)
                    .child(Constants.ITEMS_NODE)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Item> items = new ArrayList<>();
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                Item item = itemSnapshot.getValue(Item.class);
                                if (item != null) {
                                    item.setId(itemSnapshot.getKey());
                                }
                                items.add(item);
                            }
                            emitter.onNext(items);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Boolean> addNewRequest(Request request) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.REQUESTS_NODE)
                    .push()
                    .setValue(request)
                    .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful()));
        });
    }

    public Observable<Pair<List<Request>, Double>> retrieveClientRequests(String clientId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.REQUESTS_NODE)
                    .orderByChild("clientId")
                    .equalTo(clientId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Request> requests = new ArrayList<>();
                            for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                                Request request = requestSnapshot.getValue(Request.class);
                                if (request != null) {
                                    request.setId(requestSnapshot.getKey());
                                }
                                requests.add(request);
                            }
                            //get client points
                            firebaseDatabase.getReference(Constants.CLIENTS_NODE)
                                    .child(clientId)
                                    .child("points")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Double totalPoints = 0.0;
                                            if (snapshot.getValue() != null)  {
                                                totalPoints = snapshot.getValue(Double.class);
                                            }
                                            Pair<List<Request>, Double> requestsPointsPair = new Pair<>(requests, totalPoints);
                                            emitter.onNext(requestsPointsPair);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            emitter.onError(error.toException());
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Observable<List<Request>> retrieveOrganizationRequests(String organizationId) {
        return Observable.create(emitter -> {
            firebaseDatabase.getReference(Constants.REQUESTS_NODE)
                    .orderByChild("organizationId")
                    .equalTo(organizationId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Request> requests = new ArrayList<>();
                            for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                                Request request = requestSnapshot.getValue(Request.class);
                                if (request != null) {
                                    request.setId(requestSnapshot.getKey());
                                }
                                requests.add(request);
                            }
                            emitter.onNext(requests);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Pair<Request, Client>> retrieveRequestDetails(String requestId) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.REQUESTS_NODE)
                    .child(requestId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Request request = snapshot.getValue(Request.class);
                            if (request != null) {
                                request.setId(snapshot.getKey());
                                //get client details
                                firebaseDatabase.getReference(Constants.CLIENTS_NODE)
                                        .child(request.getClientId())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Client client = snapshot.getValue(Client.class);
                                                if (client != null) {
                                                    client.setId(snapshot.getKey());
                                                }
                                                Pair<Request, Client> requestClientPair = new Pair<>(request, client);
                                                emitter.onSuccess(requestClientPair);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                emitter.onError(error.toException());
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    public Single<Boolean> setRequestStatus(Request request, String status) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.REQUESTS_NODE)
                    .child(request.getId())
                    .child("status")
                    .setValue(status)
                    .addOnCompleteListener(task -> {
                        emitter.onSuccess(task.isSuccessful());
                        //add this request points to the client's data if the status is delivered
                        if (status.equals(Request.RequestStatus.Delivered.name())) {
                            //firstly get current client's point then add new points to it
                            firebaseDatabase.getReference(Constants.CLIENTS_NODE)
                                    .child(request.getClientId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Client client = snapshot.getValue(Client.class);
                                            Double currentPoints = 0.0;
                                            if (snapshot.hasChild("points") && snapshot.child("points").getValue() != null) {
                                                currentPoints = snapshot.child("points").getValue(Double.class);
                                            }
                                            Double newPoints = calculateRequestPoints(request.getRequestItemList()) + currentPoints;
                                            snapshot.child("points").getRef().setValue(newPoints);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            emitter.onError(error.toException());
                                        }
                                    });
                        }
                    });
        });
    }

    private Double calculateRequestPoints(List<RequestItem> requestItemList) {
        double points = 0.0;
        for (RequestItem requestItem : requestItemList) {
            points += requestItem.getItem().getPoints() * requestItem.getQuantity();
        }
        return points;
    }
}
