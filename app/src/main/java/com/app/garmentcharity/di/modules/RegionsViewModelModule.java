package com.app.garmentcharity.di.modules;

import com.app.garmentcharity.di.ViewModelKey;
import com.app.garmentcharity.presentation.regions.RegionsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RegionsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegionsViewModel.class)
    public abstract ViewModel bindViewModel(RegionsViewModel viewModel);
}
