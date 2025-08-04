package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

public class MapaViewModel extends ViewModel {

    private final LocalRepository localRepository;

    public MapaViewModel() {
        this.localRepository = LocalRepository.getInstance();
    }

    public void adicionarLocal(String nome, String descricao, double latitude, double longitude) {
        Local novoLocal = new Local(nome, descricao, latitude, longitude);
        localRepository.adicionarLocal(novoLocal);
    }
}