package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

public class DetalhesLocalViewModel extends ViewModel {

    private final LocalRepository localRepository;
    private final MutableLiveData<Local> localLiveData = new MutableLiveData<>();

    public DetalhesLocalViewModel() {
        this.localRepository = LocalRepository.getInstance();
    }

    public LiveData<Local> getLocal() {
        return localLiveData;
    }

    public void carregarLocal(double latitude, double longitude) {
        Local local = localRepository.getLocalByLatLng(latitude, longitude);
        localLiveData.setValue(local);
    }
}