package com.app.garmentcharity.di;

import android.app.Application;

import com.app.garmentcharity.AppBase;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {ActivityBuildersModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        FragmentBuildersModule.class,
        ViewModelFactoryModule.class})
public interface AppComponent extends AndroidInjector<AppBase> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
