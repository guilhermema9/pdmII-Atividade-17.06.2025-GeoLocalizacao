package com.example.atividade_17062025_geolocalizacao.repository;

import com.example.atividade_17062025_geolocalizacao.model.Local;

import java.util.ArrayList;
import java.util.List;

public class LocalRepository {

    private static LocalRepository instance;
    private final List<Local> locaisList;

    private LocalRepository() {
        locaisList = new ArrayList<>();
    }

    public static synchronized LocalRepository getInstance() {
        if (instance == null) {
            instance = new LocalRepository();
        }
        return instance;
    }

    public void adicionarLocal(Local local) {
        locaisList.add(local);
    }

    public List<Local> getTodosOsLocais() {
        return new ArrayList<>(locaisList);
    }

    public Local getLocalByLatLng(double latitude, double longitude) {
        for (Local local : locaisList) {
            if (local.getLatitude() == latitude && local.getLongitude() == longitude) {
                return local;
            }
        }
        return null;
    }

    public void limpar() {
        locaisList.clear();
    }
}
