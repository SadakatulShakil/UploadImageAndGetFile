package com.example.uploadimageandgetfile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
private ImageView profileImage;
private Button pickBtn;
private TextView fileNameTv;
private Uri imgUri;
public static final String TAG ="Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromGallery();
            }
        });
    }

    private void chooseFromGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                1);
    }

   /* private File createFile(){

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + "."+getMimeType(imgUri));


        return mediaFile;
    }*/
   public String getMimeType(Uri uri) {
       String extension;

       //Check uri format to avoid null
       if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
           //If scheme is a content
           final MimeTypeMap mime = MimeTypeMap.getSingleton();
           extension = mime.getExtensionFromMimeType(getContentResolver().getType(uri));
       } else {
           //If scheme is a File
           //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
           extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

       }

       return extension;
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                imgUri = data.getData();
                String path = imgUri.getPath()+"."+getMimeType(imgUri);
                Log.d(TAG, "onActivityResult: " + "imagUri: "+imgUri.toString());
                Log.d(TAG, "onActivityResult: " + "imagePath: : "+path);

                String filePath = Environment.getExternalStorageDirectory().toString() + path;

                File f = new File(filePath);
                String nickPath = f.getName();
                Log.d(TAG, "onActivityResult: "+"OriginalFile: "+f.toString());
                Log.d(TAG, "onActivityResult: "+"NickNameOfFile: "+nickPath);

                Picasso.get().load(imgUri).into(profileImage);
                /** If the image comes from gallery **/
            }
        }
    }


    private void initView() {

        profileImage = findViewById(R.id.profileImage);
        pickBtn = findViewById(R.id.pick);
        fileNameTv = findViewById(R.id.fileName);
    }
}