package com.app.garmentcharity.di.modules;

import com.app.garmentcharity.di.ViewModelKey;
import com.app.garmentcharity.presentation.requesets.RequestsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RequestsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RequestsViewModel.class)
    public abstract ViewModel bindViewModel(RequestsViewModel viewModel);
}
