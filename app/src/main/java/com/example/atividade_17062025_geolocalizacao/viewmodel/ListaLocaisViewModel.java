package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

import java.util.List;

public class ListaLocaisViewModel extends ViewModel {

    private final LocalRepository localRepository;
    private final MutableLiveData<List<Local>> locaisLiveData = new MutableLiveData<>();

    public ListaLocaisViewModel() {
        this.localRepository = LocalRepository.getInstance();
    }

    public LiveData<List<Local>> getLocais() {
        return locaisLiveData;
    }

    public void carregarLocais() {
        List<Local> locais = localRepository.getTodosOsLocais();
        locaisLiveData.setValue(locais);
    }
}