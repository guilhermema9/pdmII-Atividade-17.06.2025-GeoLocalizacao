package com.example.atividade_17062025_geolocalizacao.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.LifecycleCameraController;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final LocalRepository localRepository = LocalRepository.getInstance();
    private static final int REQUEST_CODE_PERMISSOES = 1001;
    private LifecycleCameraController cameraController;
    private FloatingActionButton fabListaLocais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabListaLocais = findViewById(R.id.fabListaLocais);

        fabListaLocais.setOnClickListener(v -> {
            Intent intent = new Intent(MapaActivity.this, ListaLocaisActivity.class);
            startActivity(intent);
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        verificaPermissoes();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-
                10.184300433266566, -48.33377376019333),12));

        googleMap.setOnMapClickListener(latLng -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_save_local, null);

            EditText editNome = dialogView.findViewById(R.id.editNome);
            EditText editDescricao = dialogView.findViewById(R.id.editDescricao);
            TextView textLatLng = dialogView.findViewById(R.id.textLatLng);

            String coordenadas = "Latitude: " + latLng.latitude + "\nLongitude: " + latLng.longitude;
            textLatLng.setText(coordenadas);

            new AlertDialog.Builder(this)
                    .setTitle("Adicionar Local")
                    .setView(dialogView)
                    .setPositiveButton("Salvar", (dialog, which) -> {
                        String nome = editNome.getText().toString().trim();
                        String descricao = editDescricao.getText().toString().trim();

                        if (!nome.isEmpty()) {
                            Local local = new Local(nome, descricao, latLng.latitude, latLng.longitude);
                            LocalRepository.getInstance().adicionarLocal(local);

                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(nome)
                                    .snippet(descricao));

                            dialog.dismiss();
                            abrirFragmentCamera(nome, descricao, latLng);
                        } else {
                            Toast.makeText(this, "Digite um nome para o local.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void abrirFragmentCamera(String nome, String descricao, LatLng latLng) {
        Bundle bundle = new Bundle();
        bundle.putString("nome", nome);
        bundle.putString("descricao", descricao);
        bundle.putDouble("lat", latLng.latitude);
        bundle.putDouble("lng", latLng.longitude);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CameraFragment fragment = new CameraFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void verificaPermissoes() {
        requestPermissions(new String[] { Manifest.permission.CAMERA },1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSOES);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSOES && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão da câmera concedida", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_SHORT).show();
        }
    }
}