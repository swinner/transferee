package com.hitomi.transferimage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.hitomi.yifangbao.tilibrary.TransferWindow;
import com.hitomi.yifangbao.tilibrary.loader.glide.GlideImageLoader;
import com.hitomi.yifangbao.tilibrary.style.anim.TransitionAnimator;
import com.hitomi.yifangbao.tilibrary.style.indicat.ProgressPieIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button btnClearFresco, btnClearGlide, btnJump;

    private ImageView imageView1, imageView2, imageView3;
    private List<String> imageStrList;
    private List<ImageView> imageViewList;

    {
        imageStrList = new ArrayList<>();
        imageStrList.add("http://img5.niutuku.com/phone/1212/3752/3752-niutuku.com-22310.jpg");
        imageStrList.add("http://c.hiphotos.baidu.com/zhidao/pic/item/b7003af33a87e950e7d5403816385343faf2b4a0.jpg");
        imageStrList.add("http://e.hiphotos.baidu.com/zhidao/pic/item/7aec54e736d12f2ed5568f4c4dc2d5628535684e.jpg");
        imageStrList.add("http://img2.imgtn.bdimg.com/it/u=1506529296,2739202796&fm=23&gp=0.jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(getApplicationContext());
        ImagePipelineFactory.initialize(getApplicationContext());

        btnClearFresco = (Button) findViewById(R.id.btn_clear_fresco);
        btnClearGlide = (Button) findViewById(R.id.btn_clear_glide);
        btnJump = (Button) findViewById(R.id.btn_jump);

        btnClearFresco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fresco.getImagePipeline().clearCaches();
            }
        });

        btnClearGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getApplicationContext()).clearDiskCache();
                        Glide.get(getApplicationContext()).clearMemory();
                    }
                });
            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GlideTestActivity.class));
            }
        });

        imageView1 = (ImageView) findViewById(R.id.image_view1);
        imageView2 = (ImageView) findViewById(R.id.image_view2);
        imageView3 = (ImageView) findViewById(R.id.image_view3);

        TouchViewMotion touchViewMotion = new TouchViewMotion();
        imageView1.setOnTouchListener(touchViewMotion);
        imageView2.setOnTouchListener(touchViewMotion);
        imageView3.setOnTouchListener(touchViewMotion);


        ShowViewHDListener showViewHDListener = new ShowViewHDListener();
        imageView1.setOnClickListener(showViewHDListener);
        imageView2.setOnClickListener(showViewHDListener);
        imageView3.setOnClickListener(showViewHDListener);

        imageViewList = new ArrayList<>();
        imageViewList.add(imageView1);
        imageViewList.add(imageView2);
        imageViewList.add(imageView3);
    }

    private class ShowViewHDListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TransferWindow transferLayout = new TransferWindow.Builder(MainActivity.this)
                    .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                    .setTransferAnima(new TransitionAnimator())
                    .setProgressIndicator(new ProgressPieIndicator())
                    .setBackgroundColor(Color.BLACK)
                    .setImageStrList(imageStrList)
                    .setOriginImageList(imageViewList)
                    .setOriginIndex(imageViewList.indexOf(v))
                    .create();
            transferLayout.show();
        }
    }

    private class TouchViewMotion implements View.OnTouchListener {
        private float preX, preY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    preX = event.getRawX();
                    preY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float diffX = event.getRawX() - preX;
                    float diffY = event.getRawY() - preY;

                    v.setX(v.getX() + diffX);
                    v.setY(v.getY() + diffY);

                    preX = event.getRawX();
                    preY = event.getRawY();
                    break;
            }
            return false;
        }
    }
}
