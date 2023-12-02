package com.app.garmentcharity.di;


import com.app.garmentcharity.di.modules.AuthenticationViewModelModule;
import com.app.garmentcharity.di.modules.BaseViewModelModule;
import com.app.garmentcharity.di.modules.CategoriesViewModelModule;
import com.app.garmentcharity.di.modules.ItemsViewModelModule;
import com.app.garmentcharity.di.modules.RegionsViewModelModule;
import com.app.garmentcharity.di.modules.RequestsViewModelModule;
import com.app.garmentcharity.presentation.BaseActivity;
import com.app.garmentcharity.presentation.MainActivity;
import com.app.garmentcharity.presentation.SplashActivity;
import com.app.garmentcharity.presentation.authentication.LoginActivity;
import com.app.garmentcharity.presentation.authentication.RegisterActivity;
import com.app.garmentcharity.presentation.categories.AddCategoryActivity;
import com.app.garmentcharity.presentation.client.AddRequestActivity;
import com.app.garmentcharity.presentation.client.ClientHomeActivity;
import com.app.garmentcharity.presentation.items.AddItemActivity;
import com.app.garmentcharity.presentation.organization.OrganizationHomeActivity;
import com.app.garmentcharity.presentation.requesets.RequestDetailsActivity;
import com.app.garmentcharity.presentation.organization.OrganizationRequestsActivity;
import com.app.garmentcharity.presentation.regions.RegionListActivity;
import com.app.garmentcharity.presentation.requesets.AddRequestDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = {BaseViewModelModule.class})
    abstract BaseActivity contributeBaseActivity();

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = RequestsViewModelModule.class)
    abstract ClientHomeActivity contributeClientHomeActivity();

    @ContributesAndroidInjector(modules = {ItemsViewModelModule.class, CategoriesViewModelModule.class, RegionsViewModelModule.class})
    abstract OrganizationHomeActivity contributeOrganizationHomeActivity();

    @ContributesAndroidInjector(modules = RegionsViewModelModule.class)
    abstract RegionListActivity contributeRegionListActivity();

    @ContributesAndroidInjector(modules = CategoriesViewModelModule.class)
    abstract AddCategoryActivity contributeAddCategoryActivity();

    @ContributesAndroidInjector(modules = {ItemsViewModelModule.class, CategoriesViewModelModule.class})
    abstract AddItemActivity contributeAddItemActivity();

    @ContributesAndroidInjector(modules = RegionsViewModelModule.class)
    abstract AddRequestActivity contributeAddRequestActivity();

    @ContributesAndroidInjector(modules = {RequestsViewModelModule.class, CategoriesViewModelModule.class})
    abstract AddRequestDetailsActivity contributeAddRequestDetailsActivity();

    @ContributesAndroidInjector(modules = RequestsViewModelModule.class)
    abstract OrganizationRequestsActivity contributeOrganizationRequestsActivity();

    @ContributesAndroidInjector(modules = RequestsViewModelModule.class)
    abstract RequestDetailsActivity contributeOrganizationRequestDetailsActivity();

}
