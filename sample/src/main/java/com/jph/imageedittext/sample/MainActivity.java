package com.jph.imageedittext.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jph.imageedittext.ImageCompressUtils;
import com.jph.imageedittext.ImageEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageEditText edt = (ImageEditText) findViewById(R.id.main_edt);
        findViewById(R.id.main_btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPath = Environment.getExternalStorageDirectory().getPath() + "/test.jpg";
//                edt.insertLocalImage(imgPath);

                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                Bitmap bitmapQuality = ImageCompressUtils.compressToBitmap(bitmap, 50);
//                Bitmap bitmapSize = ImageCompressUtils.compressToBitmap(bitmap, 500, 500);
                saveToFile(bitmapQuality, "111111.jpg");
                Log.i("-----------------", "Process Success");
            }
        });
    }

    private void saveToFile(Bitmap bitmap, String fileName) {
        String savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
