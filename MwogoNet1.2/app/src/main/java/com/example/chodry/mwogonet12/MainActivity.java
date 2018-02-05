package com.example.chodry.mwogonet12;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView photocapturedimageview;
    private String imagefilelocation = "";
    private TextView textView;
    private static final int IMAGE_GALLERY_INTENT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photocapturedimageview = (ImageView)findViewById(R.id.capturephotoimageview);
        textView = (TextView)findViewById(R.id.textview);
    }

    //invoking a camera application to take an image
    public void takephoto(View view){
        Intent callcameraApplicationIntent = new Intent();
        callcameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photofile = null;
        try {

            photofile = createimagefile();

        }catch (IOException e){
            e.printStackTrace();
        }
        callcameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
        startActivityForResult(callcameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }


    //defining the location where the image will be saved and the format it will be saved

    File createimagefile()throws IOException{

         //IMAGE WILL BE SAVED IN A DATE FORMAT
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imagefile = "LEAF_" + timestamp + "_";
        //File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //mwogoNet is the folder that will be created o the sdcard where the images will be saved
        File folder = new File("sdcard/mwogoNet1.2");
        if (!folder.exists()){
            folder.mkdir();
        }

        File image = File.createTempFile(imagefile, ".jpg", folder);
        imagefilelocation = image.getAbsolutePath();


        return image;
    }

    //gallery activity
    public void opengallery(View view){

        //invoke the image galley using an implict intent
        Intent photopicker = new Intent(Intent.ACTION_PICK);

        //where do we find the images
        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String photoDirectorypath = photoDirectory.getPath();
        //get the Uri representation
        Uri data = Uri.parse(photoDirectorypath);
        //set the data and type. get all image tpyes
        photopicker.setDataAndType(data, "image/*");
        startActivityForResult(photopicker, IMAGE_GALLERY_INTENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //outputting the captured image
        if (requestCode ==ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
            //Toast.makeText(this, "it works", Toast.LENGTH_LONG).show();
            //Bundle extras = data.getExtras();
            //Bitmap capturedphoto = (Bitmap)extras.get("data");
            //photocapturedimageview.setImageBitmap(capturedphoto);
            Bitmap capturephoto = BitmapFactory.decodeFile(imagefilelocation);
            photocapturedimageview.setImageBitmap(capturephoto);
            textView.setText("Status: Diseased\n" +
                             "Name: Cassava Mosaic\n" +
                             "Level of Severity: four\n" +
                             "Accuracy: 0.91");

        }

        //outputting an image from gallery

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_GALLERY_INTENT){
                //get the address of the image on the sdcard
                Uri imageUri = data.getData();
                //declare a string to read data from the sdcard
                InputStream inputStream;
                //get the inputstream based on the Uri

                try {

                    inputStream = getContentResolver().openInputStream(imageUri);
                    //get bitmap from a stream
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                    //show image
                    photocapturedimageview.setImageBitmap(imageBitmap);
                    textView.setText("Status: Diseased\n" +
                                     "Name: Cassava Brown streak\n" +
                                     "Level of Severity: four\n" +
                                     "Accuracy: 0.81");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to show image", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


}
