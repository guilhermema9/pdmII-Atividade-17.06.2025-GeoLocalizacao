package com.example.atividade_17062025_geolocalizacao.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.adapter.LocalAdapter;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;

import java.util.List;

public class ListaLocaisActivity extends AppCompatActivity {

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


        // Inicializa o RecyclerView
        recyclerViewLocais = findViewById(R.id.recyclerLocais);
        recyclerViewLocais.setLayoutManager(new LinearLayoutManager(this));

        // Busca a lista de locais do repositório
        List<Local> locais = LocalRepository.getInstance().getTodosOsLocais();

        // Cria e configura o adapter
        localAdapter = new LocalAdapter(locais, this);
        recyclerViewLocais.setAdapter(localAdapter);

        /*localAdapter.setOnItemClickListener(local -> {
            // Cria uma Intent para abrir a DetalhesLocalActivity
            Intent intent = new Intent(ListaLocaisActivity.this, DetalhesLocalActivity.class);
            // Passa a latitude e longitude como "extras" para a próxima atividade
            intent.putExtra("local_lat", local.getLatitude());
            intent.putExtra("local_lon", local.getLongitude());
            startActivity(intent);
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // --- TAREFAS DE ATUALIZAÇÃO ---
        // Sempre que a tela se torna visível, busca a lista mais recente de locais.
        List<Local> locaisAtualizados = LocalRepository.getInstance().getTodosOsLocais();

        // Cria um novo adapter com os dados frescos.
        localAdapter = new LocalAdapter(locaisAtualizados, this);

        // Define o listener de clique para o novo adapter.
        localAdapter.setOnItemClickListener(local -> {
            // Cria uma Intent para abrir a DetalhesLocalActivity
            Intent intent = new Intent(ListaLocaisActivity.this, DetalhesLocalActivity.class);
            // Passa a latitude e longitude como "extras" para a próxima atividade
            intent.putExtra("local_lat", local.getLatitude());
            intent.putExtra("local_lon", local.getLongitude());
            startActivity(intent);
        });

        // Conecta o novo adapter ao RecyclerView para exibir a lista atualizada.
        recyclerViewLocais.setAdapter(localAdapter);
    }
}