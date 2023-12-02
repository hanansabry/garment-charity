package com.app.garmentcharity.di.modules;

import com.app.garmentcharity.di.ViewModelKey;
import com.app.garmentcharity.presentation.items.ItemsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ItemsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ItemsViewModel.class)
    public abstract ViewModel bindViewModel(ItemsViewModel viewModel);
}
