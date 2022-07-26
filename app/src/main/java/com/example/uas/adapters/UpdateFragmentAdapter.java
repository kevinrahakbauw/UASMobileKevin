package com.example.uas.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uas.Preferences;
import com.example.uas.R;
import com.example.uas.activities.UpdateActivity;
import com.example.uas.models.Data;

import java.util.List;

public class UpdateFragmentAdapter extends RecyclerView.Adapter<UpdateFragmentAdapter.ImageViewHolder>  {
    private Context mContext;
    private List<Data> mData;

    public UpdateFragmentAdapter(Context Context, List<Data> Datas) {
        mContext = Context;
        mData = Datas;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_update, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpdateFragmentAdapter.ImageViewHolder holder, int position) {
        final Data dataCurrent = mData.get(position);
        holder.judul.setText(dataCurrent.getJudul());
        holder.deskripsi.setText(dataCurrent.getDeskripsi());
        Glide.with(mContext).load(dataCurrent.getImageUrl()).apply(new RequestOptions().centerCrop().override(500, 500)).into(holder.gambar);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateActivity.class);
                intent.putExtra("title", dataCurrent.getJudul());
                    intent.putExtra("description", dataCurrent.getDeskripsi());
                intent.putExtra("image", dataCurrent.getImageUrl());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView judul, deskripsi;
        ImageView gambar;
        Button update;

        public ImageViewHolder(View itemView){
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judul_update);
            deskripsi = itemView.findViewById(R.id.tv_description_update);
            gambar = itemView.findViewById(R.id.iv_gambar_update);
            update = itemView.findViewById(R.id.update_button);
            judul.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
            deskripsi.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
        }
    }
}
