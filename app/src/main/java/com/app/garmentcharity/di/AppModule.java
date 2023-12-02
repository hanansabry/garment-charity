package com.app.garmentcharity.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.garmentcharity.utils.Constants;
import com.app.garmentcharity.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    static FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    static StorageReference provideFirebaseStorage() {
        return FirebaseStorage.getInstance().getReference();
    }

    @Singleton
    @Provides
    static SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    static SessionManager provideSessionManager(Application application, SharedPreferences sharedPreferences) {
        return new SessionManager(application, sharedPreferences);
    }

}
