package com.example.uas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uas.Preferences;
import com.example.uas.R;
import com.example.uas.models.Data;

import java.util.List;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Data> mData;

    public ListFragmentAdapter(Context Context, List<Data> Datas) {
        mContext = Context;
        mData = Datas;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_list, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final Data dataCurrent = mData.get(position);
        holder.judul.setText(dataCurrent.getJudul());
        holder.deskripsi.setText(dataCurrent.getDeskripsi());
        Glide.with(mContext).load(dataCurrent.getImageUrl()).apply(new RequestOptions().centerCrop().override(500, 500)).into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView judul, deskripsi;
        ImageView gambar;

        public ImageViewHolder(View itemView){
            super(itemView);

            judul = itemView.findViewById(R.id.tv_judul_list);
            deskripsi = itemView.findViewById(R.id.tv_description_list);
            gambar = itemView.findViewById(R.id.iv_gambar_list);
            judul.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
            deskripsi.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
        }
    }
}
