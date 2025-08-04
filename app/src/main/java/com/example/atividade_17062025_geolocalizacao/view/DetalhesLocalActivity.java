package com.example.atividade_17062025_geolocalizacao.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.adapter.FotoAdapter;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.viewmodel.DetalhesLocalViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetalhesLocalActivity extends AppCompatActivity {

    private TextView textNomeLocal;
    private TextView textDescricaoLocal;
    private RecyclerView recyclerFotos;
    private DetalhesLocalViewModel detalhesLocalViewModel;
    private FloatingActionButton fabAdicionarFoto;
    private Local localAtual;

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

        textNomeLocal = findViewById(R.id.textNomeLocal);
        textDescricaoLocal = findViewById(R.id.textDescricaoLocal);
        recyclerFotos = findViewById(R.id.recyclerFotos);
        fabAdicionarFoto = findViewById(R.id.fabAdicionarFoto);

        constroiListaFotosRecyclerView();

        fabAdicionarFoto.setOnClickListener(v -> {
            if (localAtual != null) {
                abrirCamera();
            } else {
                Toast.makeText(this, "Aguarde o local ser carregado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirCamera() {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", localAtual.getLatitude());
        bundle.putDouble("lng", localAtual.getLongitude());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CameraFragment fragment = new CameraFragment();
        fragment.setArguments(bundle);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void constroiListaFotosRecyclerView(){
        detalhesLocalViewModel.getLocal().observe(this, local -> {
            if (local != null) {
                this.localAtual = local;
                textNomeLocal.setText(local.getNome());
                textDescricaoLocal.setText(local.getDescricao());

                recyclerFotos.setLayoutManager(new LinearLayoutManager(this));
                FotoAdapter fotoAdapter = new FotoAdapter(local.getPhotoPaths(), this);
                recyclerFotos.setAdapter(fotoAdapter);
            } else {
                Toast.makeText(this, "Erro: Local não encontrado.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        double lat = getIntent().getDoubleExtra("local_lat", 0);
        double lon = getIntent().getDoubleExtra("local_lon", 0);
        detalhesLocalViewModel.carregarLocal(lat, lon);
    }
}