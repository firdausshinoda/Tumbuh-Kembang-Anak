package com.tumbuhkembanganak.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.tumbuhkembanganak.databinding.ActivityPerkembanganStimulaiBinding;

public class PerkembanganStimulaiActivity extends AppCompatActivity {

    private ActivityPerkembanganStimulaiBinding binding;
    private Context context;
    private String img = "";
    private String gerak_halus = "kemampuan_gerak_halus.pdf", gerak_kasar = "kemampuan_gerak_kasar.pdf";
    private String bicara_bahasa = "kemampuan_bicara_dan_bahasa.pdf";
    private String sosialisasi_kemandirian = "kemampuan_bersosialisasi_dan_kemandirian.pdf", umur = "";

    private MaterialCardView card03;
    private MaterialCardView card36;
    private MaterialCardView card69;
    private MaterialCardView card912;
    private MaterialCardView card1215;
    private MaterialCardView card1518;
    private MaterialCardView card1824;
    private MaterialCardView card2436;
    private MaterialCardView card3648;
    private MaterialCardView card4860;
    private MaterialCardView card6072;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerkembanganStimulaiBinding.inflate(getLayoutInflater());
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

        initView();
    }

    private void initView() {
        card03 = binding.contentPanel.card03;
        card36 = binding.contentPanel.card36;
        card69 = binding.contentPanel.card69;
        card912 = binding.contentPanel.card912;
        card1215 = binding.contentPanel.card1215;
        card1518 = binding.contentPanel.card1518;
        card1824 = binding.contentPanel.card1824;
        card2436 = binding.contentPanel.card2436;
        card3648 = binding.contentPanel.card3648;
        card4860 = binding.contentPanel.card4860;
        card6072 = binding.contentPanel.card6072;

        card03.setOnClickListener(v -> {
            img = "0_3.png"; umur = "0 - 3";
            pilihButton();
        });
        card36.setOnClickListener(v -> {
            img = "3_6.png"; umur = "3 - 6";
            pilihButton();
        });
        card69.setOnClickListener(v -> {
            img = "6_9.png"; umur = "6 - 9";
            pilihButton();
        });
        card912.setOnClickListener(v -> {
            img = "9_12.png"; umur = "9 - 12";
            pilihButton();
        });
        card1215.setOnClickListener(v -> {
            img = "12_15.png"; umur = "12 - 15";
            pilihButton();
        });
        card1518.setOnClickListener(v -> {
            img = "15_18.png"; umur = "15 - 18";
            pilihButton();
        });
        card1824.setOnClickListener(v -> {
            img = "18_24.png"; umur = "18 - 24";
            pilihButton();
        });
        card2436.setOnClickListener(v -> {
            img = "24_36.png"; umur = "24 - 36";
            pilihButton();
        });
        card3648.setOnClickListener(v -> {
            img = "36_48.png"; umur = "36 - 48";
            pilihButton();
        });
        card4860.setOnClickListener(v -> {
            img = "48_60.png"; umur = "48 - 60";
            pilihButton();
        });
        card6072.setOnClickListener(v -> {
            img = "60_72.png"; umur = "60 - 72";
            pilihButton();
        });
    }

    private void pilihButton() {
        Intent intent = new Intent(context, PerkembangandanStimulasiPilihActivity.class);
        intent.putExtra("img",img);
        intent.putExtra("gerak_halus",gerak_halus);
        intent.putExtra("gerak_kasar",gerak_kasar);
        intent.putExtra("bicara_bahasa",bicara_bahasa);
        intent.putExtra("sosialisasi_kemandirian",sosialisasi_kemandirian);
        intent.putExtra("umur",umur);
        startActivity(intent);
    }
}