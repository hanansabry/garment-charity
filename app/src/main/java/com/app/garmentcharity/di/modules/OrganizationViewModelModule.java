package com.app.garmentcharity.di.modules;

import com.app.garmentcharity.di.ViewModelKey;
import com.app.garmentcharity.presentation.organization.OrganizationViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class OrganizationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationViewModel.class)
    public abstract ViewModel bindViewModel(OrganizationViewModel viewModel);
}
