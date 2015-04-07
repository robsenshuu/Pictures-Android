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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Environment;
import android.widget.ImageView;
import com.facebook.Session;

import java.io.File;
import java.io.IOException;


public class MainActivity extends ActionBarActivity{

    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private Button buttonPicture;
    private Button btnIntent;
    private int PHOTO_CODE = 100;
    private String fileName;

    private File file;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPicture = (Button) findViewById(R.id.buttonImage);
        btnIntent = (Button) findViewById(R.id.buttonI);
        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hola.class);
                startActivity(intent);
            }
        });


        buttonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elige alguna opcion");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which] == "Tomar foto"){
                            openCamera();
                        }else if(options[which] == "Elegir de galeria"){

                        }else if(options[which] == "Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void openCamera(){
        /*
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        */

        /**
         *  here, we are making a folder named myPictureApp to store pics taken by the camera using this application
         */
        this.file = new File(Environment.getExternalStorageDirectory(),
                MEDIA_DIRECTORY);
        file.mkdirs();

         path = Environment.getExternalStorageDirectory()
                + File.separator + MEDIA_DIRECTORY + File.separator
                + TEMPORAL_PICTURE_NAME;

        Log.i("Directorio ", "Imagen " + path);

        File newFile = new File(path);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        cameraIntent.putExtra("file", path);
        startActivityForResult(cameraIntent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == PHOTO_CODE){
                setImageUI();
            }
        }else{
            Log.i("NO FUE VALIDO", "mCode == FALSE");
        }
    }

    public void setImageUI(){
        String dir =  Environment.getExternalStorageDirectory()
                    + File.separator + MEDIA_DIRECTORY + File.separator
                    + TEMPORAL_PICTURE_NAME;

        Log.e("Roberto", "Image path: " + dir);
        decodeBitmap(dir);
    }

    public void decodeBitmap(String dir){
        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(dir, bitmapOptions);

        ImageView mImageSet = (ImageView) findViewById(R.id.setPicture);
        mImageSet.setImageBitmap(bitmap);

    }

    // /storage/sdcard0/myPictureApp/media/temporal
    // /storage/sdcard0/myPictureApp/media/temporal

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
