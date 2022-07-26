package com.example.uas.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uas.Preferences;
import com.example.uas.R;
import com.example.uas.models.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DeleteFragmentAdapter extends RecyclerView.Adapter<DeleteFragmentAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Data> mData;

    public DeleteFragmentAdapter(Context mContext, List<Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_delete, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Data dataCurrent = mData.get(position);
        holder.title.setText(dataCurrent.getJudul());
        holder.description.setText(dataCurrent.getDeskripsi());
        Glide.with(mContext).load(dataCurrent.getImageUrl()).apply(new RequestOptions().centerCrop().override(500, 500)).into(holder.image);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                // set title
                alertDialogBuilder.setTitle("Check");
                alertDialogBuilder
                        .setMessage("Apakah yakin ingin menghapus!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(dataCurrent.getImageUrl());
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                                Query applesQuery = databaseRef.child("Data").orderByChild("judul").equalTo(dataCurrent.getJudul());
                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(mContext, "failed database", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "Delete sukses", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "failed storage", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        ImageView image;
        Button delete;

        public ImageViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.tv_title_delete);
            description = itemView.findViewById(R.id.tv_description_delete);
            image = itemView.findViewById(R.id.iv_gambar_delete);
            delete = itemView.findViewById(R.id.delete_button);
            title.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
            description.setTextSize((float) Preferences.getFontSize(itemView.getContext()));
        }
    }
}
