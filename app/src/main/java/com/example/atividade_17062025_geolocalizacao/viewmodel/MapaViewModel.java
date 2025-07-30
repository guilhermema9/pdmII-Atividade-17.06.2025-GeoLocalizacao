package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

public class MapaViewModel extends ViewModel {

    private final LocalRepository localRepository;

    public MapaViewModel() {
        // Obtém a instância do repositório.
        this.localRepository = LocalRepository.getInstance();
    }

    /**
     * Lógica de negócio para adicionar um novo local.
     * A View (MapaActivity) irá chamar este método em vez de acessar o repositório diretamente.
     * @param nome O nome do local.
     * @param descricao A descrição do local.
     * @param latitude A latitude do local.
     * @param longitude A longitude do local.
     */
    public void adicionarLocal(String nome, String descricao, double latitude, double longitude) {
        Local novoLocal = new Local(nome, descricao, latitude, longitude);
        localRepository.adicionarLocal(novoLocal);
    }
}