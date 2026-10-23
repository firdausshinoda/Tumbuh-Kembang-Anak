package com.tumbuhkembanganak.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tumbuhkembanganak.databinding.ActivityPerkembanganDanStimulasiPilihBinding;

import com.tumbuhkembanganak.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PerkembangandanStimulasiPilihActivity extends AppCompatActivity {

    private ActivityPerkembanganDanStimulasiPilihBinding binding;
    private Context context;
    private String img = "", gerak_halus = "", gerak_kasar = "", bicara_bahasa = "", sosialisasi_kemandirian = "", umur = "";

    private MaterialCardView cardGerakKasar;
    private MaterialCardView cardGerakHalus;
    private MaterialCardView cardBicaraBahasa;
    private MaterialCardView cardSosialisasiKemandirian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerkembanganDanStimulasiPilihBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Perkembangan dan Stimulasi");
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        Bundle b = getIntent().getExtras();
        img = b.getString("img");
        gerak_halus = b.getString("gerak_halus");
        gerak_kasar = b.getString("gerak_kasar");
        bicara_bahasa = b.getString("bicara_bahasa");
        sosialisasi_kemandirian = b.getString("sosialisasi_kemandirian");
        umur = b.getString("umur");

        initView();
    }

    private void initView() {
        cardBicaraBahasa = binding.contentPanel.cardBicaraBahasa;
        cardGerakHalus = binding.contentPanel.cardGerakHalus;
        cardGerakKasar = binding.contentPanel.cardGerakKasar;
        cardSosialisasiKemandirian = binding.contentPanel.cardSosialisasiKemandirian;

        cardSosialisasiKemandirian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = img.replace(".png","_")+sosialisasi_kemandirian;
                setFile(file,"Kemampuan Bersosialisasi dan Kemandirian Umur "+umur);
            }
        });
        cardBicaraBahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = img.replace(".png","_")+bicara_bahasa;
                setFile(file,"Kemampuan Bicara dan Bahasa Umur "+umur);
            }
        });
        cardGerakKasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = img.replace(".png","_")+gerak_kasar;
                setFile(file,"Kemampuan Gerak Kasar Umur "+umur);
            }
        });
        cardGerakHalus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = img.replace(".png","_")+gerak_halus;
                setFile(file,"Kemampuan Gerak Halus Umur "+umur);
            }
        });
    }

    private void setFile(String file_pdf, String t_judul){
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        String nama_file = file_pdf;
        File file = new File(getFilesDir(), nama_file);
        try {
            in = assetManager.open(nama_file);
            out = openFileOutput(file.getName(), Context.MODE_PRIVATE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            Intent intent = new Intent(context, ReadPDFActivity.class);
            intent.putExtra("judul",t_judul);
            intent.putExtra("file_pdf",file_pdf);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Eror saat membuka file.", Toast.LENGTH_SHORT).show();
            Log.e("tag", e.getMessage());
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}