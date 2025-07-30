package com.example.atividade_17062025_geolocalizacao.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.adapter.FotoAdapter;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import com.example.atividade_17062025_geolocalizacao.viewmodel.DetalhesLocalViewModel;

public class DetalhesLocalActivity extends AppCompatActivity {

    private TextView textNomeLocal;
    private TextView textDescricaoLocal;
    private RecyclerView recyclerFotos;
    private DetalhesLocalViewModel detalhesLocalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes_local);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        detalhesLocalViewModel = new ViewModelProvider(this).get(DetalhesLocalViewModel.class);

        // Inicializa as Views
        textNomeLocal = findViewById(R.id.textNomeLocal);
        textDescricaoLocal = findViewById(R.id.textDescricaoLocal);
        recyclerFotos = findViewById(R.id.recyclerFotos);

        // Pega as coordenadas passadas pela Intent
        double lat = getIntent().getDoubleExtra("local_lat", 0);
        double lon = getIntent().getDoubleExtra("local_lon", 0);
        detalhesLocalViewModel.carregarLocal(lat, lon);

        detalhesLocalViewModel.getLocal().observe(this, local -> {
            if (local != null) {
                // Se o local for encontrado, preenche as informações na tela
                textNomeLocal.setText(local.getNome());
                textDescricaoLocal.setText(local.getDescricao());

                // Configura o RecyclerView para exibir as fotos
                recyclerFotos.setLayoutManager(new LinearLayoutManager(this));
                FotoAdapter fotoAdapter = new FotoAdapter(local.getPhotoPaths(), this);
                recyclerFotos.setAdapter(fotoAdapter);
            } else {
                // Se o local não for encontrado, exibe uma mensagem de erro e fecha a atividade
                Toast.makeText(this, "Erro: Local não encontrado.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

/*
        // Busca o local no repositório
        Local local = LocalRepository.getInstance().getLocalByLatLng(lat, lon);

        if (local != null) {
            // Se o local for encontrado, preenche as informações na tela
            textNomeLocal.setText(local.getNome());
            textDescricaoLocal.setText(local.getDescricao());

            // Configura o RecyclerView para exibir as fotos
            recyclerFotos.setLayoutManager(new LinearLayoutManager(this));
            FotoAdapter fotoAdapter = new FotoAdapter(local.getPhotoPaths(), this);
            recyclerFotos.setAdapter(fotoAdapter);
        } else {
            // Se o local não for encontrado, exibe uma mensagem de erro e fecha a atividade
            Toast.makeText(this, "Erro: Local não encontrado.", Toast.LENGTH_LONG).show();
            finish();
        }*/
    }
}