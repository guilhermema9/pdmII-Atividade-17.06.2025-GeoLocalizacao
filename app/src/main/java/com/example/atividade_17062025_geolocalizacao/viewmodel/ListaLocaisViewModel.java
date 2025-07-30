package com.example.atividade_17062025_geolocalizacao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import java.util.List;

public class ListaLocaisViewModel extends ViewModel {

    private final LocalRepository localRepository;
    // LiveData que conterá a lista de locais. A View vai observar este objeto.
    private final MutableLiveData<List<Local>> locaisLiveData = new MutableLiveData<>();

    public ListaLocaisViewModel() {
        this.localRepository = LocalRepository.getInstance();
    }

    /**
     * Expõe a lista de locais como LiveData imutável para a View.
     * @return um LiveData contendo a lista de locais.
     */
    public LiveData<List<Local>> getLocais() {
        return locaisLiveData;
    }

    /**
     * Carrega a lista de locais do repositório e atualiza o LiveData.
     * A View chamará este método (por exemplo, no onResume).
     */
    public void carregarLocais() {
        List<Local> locais = localRepository.getTodosOsLocais();
        locaisLiveData.setValue(locais);
    }
}