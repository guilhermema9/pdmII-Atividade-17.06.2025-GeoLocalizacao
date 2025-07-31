package com.example.atividade_17062025_geolocalizacao.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.atividade_17062025_geolocalizacao.adapter.LocalAdapter;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import com.example.atividade_17062025_geolocalizacao.viewmodel.ListaLocaisViewModel;

import java.util.List;

public class ListaLocaisActivity extends AppCompatActivity {

    private ListaLocaisViewModel listaLocaisViewModel;
    private RecyclerView recyclerViewLocais;
    private LocalAdapter localAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_locais);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaLocaisViewModel = new ViewModelProvider(this).get(ListaLocaisViewModel.class);

        recyclerViewLocais = findViewById(R.id.recyclerLocais);
        recyclerViewLocais.setLayoutManager(new LinearLayoutManager(this));

        listaLocaisViewModel.getLocais().observe(this, locais ->{
            localAdapter = new LocalAdapter(locais, this);
            localAdapter.setOnItemClickListener(local -> {
                Intent intent = new Intent(ListaLocaisActivity.this, DetalhesLocalActivity.class);
                intent.putExtra("local_lat", local.getLatitude());
                intent.putExtra("local_lon", local.getLongitude());
                startActivity(intent);
            });
            recyclerViewLocais.setAdapter(localAdapter);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaLocaisViewModel.carregarLocais();
    }
}