package com.tumbuhkembanganak.ui;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tumbuhkembanganak.config.Config;
import com.tumbuhkembanganak.config.DatabaseHandler;
import com.tumbuhkembanganak.config.SessionManager;
import com.tumbuhkembanganak.config.adapter.MyAdapterAnak;
import com.tumbuhkembanganak.config.item.Item_Anak;
import com.tumbuhkembanganak.databinding.ActivityProfilBinding;

import com.tumbuhkembanganak.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {

    private ActivityProfilBinding binding;
    private Context context;
    private DatabaseHandler db = new DatabaseHandler(this);
    private MyAdapterAnak myAdapterAnak;

    private ArrayList<Item_Anak> item_anak = new ArrayList<>();
    private ArrayList<String> listSex = new ArrayList<>();
    private SessionManager session;
    private RecyclerView recyclerView;
    private TextView tv_kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        session = new SessionManager(this);
        context = this;

        HashMap<String, String> user = session.getDetailOrtu();
        binding.contentPanel.tvNama.setText(user.get(Config.TAG_NAMA));

        initView();
        setData();
    }

    private void initView() {
        tv_kosong = binding.contentPanel.tvKosong;
        recyclerView = binding.contentPanel.recyclerView;

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setData(){
        listSex.add("Laki - Laki");
        listSex.add("Perempuan");
        item_anak.clear();
        ArrayList<HashMap<String, String>> userList = db.getAllAnak();
        if (userList.size() != 0) {
            for (int i=0;i<userList.size();i++){
                HashMap<String, String> hashmap= userList.get(i);
                String id = hashmap.get(Config.TAG_ID);
                String nama_anak= hashmap.get(Config.TAG_NAMA_ANAK);
                String sex= hashmap.get(Config.TAG_SEX);
                String tgl_lahir = hashmap.get(Config.TAG_TGL_LAHIR);
                item_anak.add(new Item_Anak(id,nama_anak,sex,tgl_lahir));
            }
            myAdapterAnak = new MyAdapterAnak(item_anak,context);
            recyclerView.setAdapter(myAdapterAnak);
            tv_kosong.setVisibility(View.GONE);
        } else {
            tv_kosong.setVisibility(View.VISIBLE);
        }
    }
}