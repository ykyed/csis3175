package com.example.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

public class FilterViewModel extends ViewModel {

    private final MutableLiveData<Set<String>> selectedBrands = new MutableLiveData<>(new HashSet<>());
    private final MutableLiveData<Set<String>> selectedColors = new MutableLiveData<>(new HashSet<>());
    private final MutableLiveData<Set<String>> selectedStyles = new MutableLiveData<>(new HashSet<>());

    public FilterViewModel() {
        if (selectedBrands.getValue() == null) {
            selectedBrands.setValue(new HashSet<>());
        }
        if (selectedColors.getValue() == null) {
            selectedColors.setValue(new HashSet<>());
        }
        if (selectedStyles.getValue() == null) {
            selectedStyles.setValue(new HashSet<>());
        }
    }

    public LiveData<Set<String>> getSelectedBrands() {
        return selectedBrands;
    }

    public LiveData<Set<String>> getSelectedColors() {
        return selectedColors;
    }

    public LiveData<Set<String>> getSelectedStyles() {
        return selectedStyles;
    }

    public void setSelectedBrands(Set<String> brands) {
        selectedBrands.setValue(new HashSet<>(brands));
    }

    public void setSelectedColors(Set<String> colors) {
        selectedColors.setValue(new HashSet<>(colors));
    }

    public void setSelectedStyles(Set<String> styles) {
        selectedStyles.setValue(new HashSet<>(styles));
    }
}
