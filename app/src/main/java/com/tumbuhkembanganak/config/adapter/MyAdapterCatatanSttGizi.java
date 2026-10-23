package com.tumbuhkembanganak.config.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tumbuhkembanganak.R;
import com.tumbuhkembanganak.config.DatabaseHandler;
import com.tumbuhkembanganak.config.item.Item_Catatan_Stt_Gizi;

import java.util.ArrayList;

public class MyAdapterCatatanSttGizi extends RecyclerView.Adapter<MyAdapterCatatanSttGizi.ViewHolder>
{
    private ArrayList<Item_Catatan_Stt_Gizi> item_catatan_stt_gizis;
    private Context context;
    private DatabaseHandler db;

    public MyAdapterCatatanSttGizi(ArrayList<Item_Catatan_Stt_Gizi> item_catatan_stt_gizis, Context context) {
        this.item_catatan_stt_gizis = item_catatan_stt_gizis;
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @Override
    public MyAdapterCatatanSttGizi.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_catatan_stt_gizi, viewGroup, false);
        return new MyAdapterCatatanSttGizi.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapterCatatanSttGizi.ViewHolder viewHolder, int i) {
        viewHolder.tv_key_1.setText(item_catatan_stt_gizis.get(i).getKey_1());
        viewHolder.tv_value_1.setText(item_catatan_stt_gizis.get(i).getVal_1());
        viewHolder.tv_key_2.setText(item_catatan_stt_gizis.get(i).getKey_2());
        viewHolder.tv_value_2.setText(item_catatan_stt_gizis.get(i).getVal_2());
        viewHolder.tv_keterangan.setText(item_catatan_stt_gizis.get(i).getKeterangan());
        viewHolder.tv_value.setText(item_catatan_stt_gizis.get(i).getVal());
        viewHolder.tv_tgl.setText(item_catatan_stt_gizis.get(i).getTgl());
    }

    @Override
    public int getItemCount() {
        return item_catatan_stt_gizis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tv_key_1;
        TextView tv_value_1;
        TextView tv_key_2;
        TextView tv_value_2;
        TextView tv_keterangan;
        TextView tv_value;
        TextView tv_tgl;
        MaterialCardView cardView;
        public ViewHolder(View view) {
            super(view);

            tv_key_1 = view.findViewById(R.id.tv_key_1);
            tv_value_1 = view.findViewById(R.id.tv_val_1);
            tv_key_2 = view.findViewById(R.id.tv_key_2);
            tv_value_2 = view.findViewById(R.id.tv_val_2);
            tv_keterangan = view.findViewById(R.id.tv_keterangan);
            tv_value = view.findViewById(R.id.tv_value);
            tv_tgl = view.findViewById(R.id.tv_tgl);
            cardView = view.findViewById(R.id.cardview);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            final int i = getAbsoluteAdapterPosition();
            final Context context = v.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Hapus").setMessage("Apakah yakin anda ingin menghapus?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        if (db.deleteCatatanSttGiziId(item_catatan_stt_gizis.get(i).getId())){
                            Toast.makeText(context, context.getString(R.string.sukses), Toast.LENGTH_SHORT).show();
                            item_catatan_stt_gizis.remove(i);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, context.getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel()).show();
            return true;
        }
    }
}
