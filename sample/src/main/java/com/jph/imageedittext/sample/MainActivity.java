package com.jph.imageedittext.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jph.imageedittext.ImageEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageEditText edt = (ImageEditText) findViewById(R.id.main_edt);
        edt.setMaxLength(50);

        findViewById(R.id.main_btn_insert_pic_net).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491563257433&di=0dbf3410d593767b6c7ad1350ca62fde&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1510%2F08%2Fc4%2F13641017_13641017_1444274038495.jpg";
                edt.insertNetImage(new NetPic(url));
            }
        });

        findViewById(R.id.main_btn_insert_pic_local).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.main_btn_multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List list = new ArrayList();
                String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491563257433&di=0dbf3410d593767b6c7ad1350ca62fde&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1510%2F08%2Fc4%2F13641017_13641017_1444274038495.jpg";
                String imgPath = Environment.getExternalStorageDirectory().getPath() + "/test.jpg";
                for (int i = 0; i < 10; i++) {
                    list.add(new NetPic(url));
                    list.add(new LocalPic(imgPath));
//                    list.add("九分裤了圣诞节疯狂了圣诞节疯狂了圣诞节疯狂了圣诞节疯狂就圣诞快乐发简历上岛咖啡饥渴了圣诞节福利圣诞节疯狂了圣诞节分克里斯打飞机 ");
                    list.add("012345678901234567890123456789012345678901234567890000");
                    list.add(new Extra(i, "这是测试帖子" + i));
                }
                //You can show loading in here
                edt.setPatches(list, new ImageEditText.SetPatchesCallback() {
                    @Override
                    public void onFinished() {
                        //You can hide loading in here
                    }
                });
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
