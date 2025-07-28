package com.example.atividade_17062025_geolocalizacao.view;

import static androidx.core.content.ContextCompat.getMainExecutor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.model.Local;
import com.example.atividade_17062025_geolocalizacao.repository.LocalRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Button btnTirarFoto;
    private String nome, descricao;
    private LatLng latLng;
    private ImageButton btnVoltar;
    private LifecycleCameraController cameraController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        previewView = view.findViewById(R.id.preview_view);
        btnTirarFoto = view.findViewById(R.id.button_tirar_foto);

        if (getArguments() != null) {
            nome = getArguments().getString("nome");
            descricao = getArguments().getString("descricao");
            double lat = getArguments().getDouble("lat");
            double lng = getArguments().getDouble("lng");
            latLng = new LatLng(lat, lng);
        }

        startCamera();

        btnTirarFoto.setOnClickListener(v -> tirarFoto());
        btnVoltar.setOnClickListener(v -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void startCamera() {
        //PreviewView previewView = findViewById(R.id.preview_view);
        cameraController = new LifecycleCameraController(getContext());
        cameraController.bindToLifecycle(this);
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
        previewView.setController(cameraController);
    }

    private void tirarFoto() {
        String nomeFoto = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis());
        File arquivo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/", nomeFoto+".jpg");
        //arquivo.mkdirs(); se precisar criar o diretório use este metodo
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(arquivo).build();
        ImageCapture.OnImageSavedCallback onImageSavedCallback = new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                //Local local = new Local(nome, descricao, latLng.latitude, latLng.longitude, file.getAbsolutePath());
                //LocalRepository.getInstance().adicionarLocal(local);
                Toast.makeText(getContext(), "Foto Salva em: " + outputFileResults.getSavedUri(), Toast.LENGTH_LONG).show();
                //requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(getContext(), "Erro ao tentar salvar", Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        };
        cameraController.takePicture(outputFileOptions, getMainExecutor(getContext()),onImageSavedCallback);
    }
}