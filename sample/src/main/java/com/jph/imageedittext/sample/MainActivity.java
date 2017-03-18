package com.jph.imageedittext.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jph.imageedittext.ImageEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageEditText edt = (ImageEditText) findViewById(R.id.main_edt);
//        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        findViewById(R.id.main_btn_insert_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPath = Environment.getExternalStorageDirectory().getPath() + "/test.jpg";
                edt.insertLocalImage(new LocalPic(imgPath));

            }
        });


        findViewById(R.id.main_btn_insert_post).setOnClickListener(new View.OnClickListener() {

            int id = 1;

            @Override
            public void onClick(View v) {
                edt.insertExtra(new Extra(id, "这是测试帖子" + id));
                id++;
            }
        });

        findViewById(R.id.main_btn_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("------", edt.getText().toString());
//                Toast.makeText(v.getContext(), "字数：" + edt.getText().length(), Toast.LENGTH_LONG).show();
//                Toast.makeText(v.getContext(), "附件个数：" + edt.getExtraSpanCount(), Toast.LENGTH_LONG).show();
//                List<IExtra> es = edt.getExtras();
//                for (IExtra extra :
//                        es) {
//                    Log.i("------", "" + extra.getXTitle());
//                }
                List list = edt.getPatches();
                Toast.makeText(v.getContext(), "分成小段数：" + list.size(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
