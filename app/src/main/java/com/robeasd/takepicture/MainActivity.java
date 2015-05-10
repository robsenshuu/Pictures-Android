package com.robeasd.takepicture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private Button buttonPicture;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;
    private String fileName;

    private File file;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPicture = (Button) findViewById(R.id.buttonImage);

        buttonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elige alguna opcion");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which] == "Tomar foto") {
                            openCamera();
                        } else if (options[which] == "Elegir de galeria") {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    SELECT_PICTURE);

                        } else if (options[which] == "Cancelar") {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void openCamera() {
        /**
         *  here, we are making a folder named myPictureApp to store pics taken by the camera using this application
         */
        this.file = new File(Environment.getExternalStorageDirectory(),
                MEDIA_DIRECTORY);
        //file.mkdirs();

        path = Environment.getExternalStorageDirectory()
                + File.separator + MEDIA_DIRECTORY + File.separator
                + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        cameraIntent.putExtra("file", path);
        startActivityForResult(cameraIntent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory()
                            + File.separator + MEDIA_DIRECTORY + File.separator
                            + TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
            break;

            case SELECT_PICTURE:
                if (resultCode == RESULT_OK){
                    Uri path = data.getData();
                    ImageView image = (ImageView) findViewById(R.id.setPicture);
                    image.setImageURI(path);

                }
        }
    }

    public void decodeBitmap(String dir) {
        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(dir, bitmapOptions);

        ImageView mImageSet = (ImageView) findViewById(R.id.setPicture);
        mImageSet.setImageBitmap(bitmap);

    }
}