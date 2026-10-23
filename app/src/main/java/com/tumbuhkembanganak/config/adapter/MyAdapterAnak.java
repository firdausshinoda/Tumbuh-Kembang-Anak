package com.tumbuhkembanganak.config.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tumbuhkembanganak.R;
import com.tumbuhkembanganak.config.DatabaseHandler;
import com.tumbuhkembanganak.config.item.Item_Anak;
import com.tumbuhkembanganak.ui.CatatanStatusGiziActivity;

import java.util.ArrayList;

public class MyAdapterAnak extends RecyclerView.Adapter<MyAdapterAnak.ViewHolder>
{
    private ArrayList<Item_Anak> item_anakArrayList;
    private Context context;
    private DatabaseHandler db;

    public MyAdapterAnak(ArrayList<Item_Anak> item_anakArrayList, Context context) {
        this.item_anakArrayList = item_anakArrayList;
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @Override
    public MyAdapterAnak.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_anak, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapterAnak.ViewHolder viewHolder, int i) {
        viewHolder.t_nama.setText(item_anakArrayList.get(i).getNama_anak());
        viewHolder.t_sex.setText(item_anakArrayList.get(i).getSex());
        viewHolder.t_tgl.setText(item_anakArrayList.get(i).getTgl_lahir());
    }

    @Override
    public int getItemCount() {
        return item_anakArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_nama;
        TextView t_sex;
        TextView t_tgl;
        private MaterialCardView cardview;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardview = view.findViewById(R.id.cardview);
            cardview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int i = getAbsoluteAdapterPosition();
            if (v == cardview){
                Intent intent = new Intent(context, CatatanStatusGiziActivity.class);
                intent.putExtra("id_anak",item_anakArrayList.get(i).getId());
                context.startActivity(intent);
            }
        }
    }
}
