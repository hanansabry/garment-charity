package com.app.garmentcharity.di.modules;

import com.app.garmentcharity.di.ViewModelKey;
import com.app.garmentcharity.presentation.categories.CategoriesViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CategoriesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel.class)
    public abstract ViewModel bindViewModel(CategoriesViewModel viewModel);
}
