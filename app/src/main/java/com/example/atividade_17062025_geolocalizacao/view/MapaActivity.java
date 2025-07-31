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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import com.example.atividade_17062025_geolocalizacao.viewmodel.MapaViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapaViewModel mapaViewModel;
    private FloatingActionButton fabListaLocais;
    private LatLng latLngLocal;
    private ActivityResultLauncher<String> locationPermissionLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private GoogleMap mGoogleMap;

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
        mapaViewModel = new ViewModelProvider(this).get(MapaViewModel.class);
        fabListaLocais = findViewById(R.id.fabListaLocais);
        fabListaLocais.setOnClickListener(v -> {
            Intent intent = new Intent(MapaActivity.this, ListaLocaisActivity.class);
            startActivity(intent);
        });

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted && mGoogleMap != null) {
                        verificaPermissaoLocaizacao();
                    } else {
                        Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted && mGoogleMap != null) {
                        habilitarLocalizacao();
                    } else {
                        Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        verificaPermissaoLocaizacao();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-
                10.184300433266566, -48.33377376019333),12));

        mGoogleMap.setOnMapClickListener(latLng -> {
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
                            mapaViewModel.adicionarLocal(nome, descricao, latLng.latitude, latLng.longitude);

                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(nome)
                                    .snippet(descricao));

                            this.latLngLocal = latLng;
                            dialog.dismiss();
                            verificaPermissaoCamera();
                        } else {
                            Toast.makeText(this, "Digite um nome para o local.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void abrirFragmentCamera() {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", latLngLocal.latitude);
        bundle.putDouble("lng", latLngLocal.longitude);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CameraFragment fragment = new CameraFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void verificaPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirFragmentCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void verificaPermissaoLocaizacao(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void habilitarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
}