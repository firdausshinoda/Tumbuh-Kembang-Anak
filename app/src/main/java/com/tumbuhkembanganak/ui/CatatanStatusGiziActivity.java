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
import com.tumbuhkembanganak.config.adapter.MyAdapterCatatanSttGizi;
import com.tumbuhkembanganak.config.item.Item_Catatan_Stt_Gizi;
import com.tumbuhkembanganak.databinding.ActivityCatatanStatusGiziBinding;

import com.tumbuhkembanganak.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CatatanStatusGiziActivity extends AppCompatActivity {
    private ActivityCatatanStatusGiziBinding binding;
    private Context context;
    private DatabaseHandler db = new DatabaseHandler(this);
    private ArrayList<Item_Catatan_Stt_Gizi> item_catatan_stt_gizi = new ArrayList<>();
    private MyAdapterCatatanSttGizi myAdapterListCatatanSttGizi;
    private String id_anak;

    private RecyclerView recyclerView;
    private TextView tv_kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatatanStatusGiziBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setTitle("Catata Status Gizi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        context = this;

        Bundle b = getIntent().getExtras();
        id_anak = b.getString("id_anak");

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

    public void setData(){
        item_catatan_stt_gizi.clear();
        ArrayList<HashMap<String, String>> userList = db.getAllCatatanSttGizi(id_anak);
        if (userList.size() != 0) {
            for (int i=0;i<userList.size();i++){
                HashMap<String, String> hashmap= userList.get(i);
                String id = hashmap.get(Config.TAG_ID);
                String _id_anak = hashmap.get(Config.TAG_ID_ANAK);
                String key_1= hashmap.get(Config.TAG_KEY_1);
                String val_1= hashmap.get(Config.TAG_VALUE_1);
                String key_2= hashmap.get(Config.TAG_KEY_2);
                String val_2= hashmap.get(Config.TAG_VALUE_2);
                String ket = hashmap.get(Config.TAG_KETERANGAN);
                String tgl= hashmap.get(Config.TAG_TGL);
                String val= hashmap.get(Config.TAG_VALUE);
                item_catatan_stt_gizi.add(new Item_Catatan_Stt_Gizi(id,_id_anak,key_1,val_1,key_2,val_2,ket,tgl,val));
            }
            myAdapterListCatatanSttGizi = new MyAdapterCatatanSttGizi(item_catatan_stt_gizi,context);
            recyclerView.setAdapter(myAdapterListCatatanSttGizi);
            tv_kosong.setVisibility(View.GONE);
        } else {
            tv_kosong.setVisibility(View.VISIBLE);
        }
    }
}