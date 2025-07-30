package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

public class DetalhesLocalViewModel extends ViewModel {

    private final LocalRepository localRepository;
    // LiveData que conterá o local específico.
    private final MutableLiveData<Local> localLiveData = new MutableLiveData<>();

    public DetalhesLocalViewModel() {
        this.localRepository = LocalRepository.getInstance();
    }

    /**
     * Expõe o local como LiveData imutável para a View.
     * @return um LiveData contendo o local.
     */
    public LiveData<Local> getLocal() {
        return localLiveData;
    }

    /**
     * Carrega um local específico do repositório com base nas coordenadas.
     * @param latitude A latitude do local.
     * @param longitude A longitude do local.
     */
    public void carregarLocal(double latitude, double longitude) {
        Local local = localRepository.getLocalByLatLng(latitude, longitude);
        localLiveData.setValue(local);
    }
}