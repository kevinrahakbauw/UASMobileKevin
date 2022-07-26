package com.example.uas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uas.R;
import com.example.uas.models.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
    ImageView image;
    EditText title, description;
    Button update;
    ProgressDialog progressDialog;

    StorageTask mUploadTask;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    final int PICK_IMAGE_REQUEST = 1;
    public Uri FilePathUri;
    UploadTask uploadTask;

    private String titleSelected, descriptionSelected, imageSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        titleSelected = getIntent().getStringExtra("title");
        descriptionSelected = getIntent().getStringExtra("description");
        imageSelected = getIntent().getStringExtra("image");

        title = findViewById(R.id.et_title_update);
        title.setText(titleSelected);
        description = findViewById(R.id.et_description_update);
        description.setText(descriptionSelected);
        image = findViewById(R.id.image_update);
        Glide.with(this).load(imageSelected).apply(new RequestOptions().centerCrop().override(500, 500)).into(image);

        update = findViewById(R.id.button_update_update);
        progressDialog = new ProgressDialog(UpdateActivity.this);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Data");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallery();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(UpdateActivity.this, "Upload In Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data != null) {
                FilePathUri = data.getData();
                Glide.with(this).load(FilePathUri).into(image);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                    image.setImageBitmap(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFile() {
        if (FilePathUri != null) {
            StorageReference oldImage = FirebaseStorage.getInstance().getReferenceFromUrl(imageSelected);
            oldImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(UpdateActivity.this, "Remove old image success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateActivity.this, "Remove old image failed!!", Toast.LENGTH_SHORT).show();
                }
            });

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2 = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(FilePathUri));
            uploadTask = storageReference2.putFile(FilePathUri);

            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            final String sjudul = title.getText().toString().trim();
                            final String sdeskripsi = description.getText().toString();
                            progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            if (sjudul != titleSelected){
                                mDatabaseRef.child(titleSelected).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Update failed!!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            Data upload = new Data(sjudul, sdeskripsi, downloadUrl.toString());
                            mDatabaseRef.child(sjudul).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress =  (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setTitle("Image is Uploading... (" + progress + "%)");
                        }
                    });
        } else {
            final String sjudul = title.getText().toString().trim();
            final String sdeskripsi = description.getText().toString();
            if (sjudul != titleSelected){
                mDatabaseRef.child(titleSelected).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Update failed!!!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            Data upload = new Data(sjudul, sdeskripsi,imageSelected);
            mDatabaseRef.child(sjudul).setValue(upload);
        }
    }

    public void handlerOnClickBatal(View view) {
         onBackPressed();
    }
}