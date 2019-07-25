package com.jyn.androidgenieeffec;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jyn.genieeffec.GenieEffectLayout;

/**
 * @author jiao
 */
@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private GenieEffectLayout genieEffectLayout;
    Button button;
    ImageView imageView;
    ImageView imageView2;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genieEffectLayout = findViewById(R.id.genie_effect_layout);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.image);
        imageView2 = findViewById(R.id.image2);
        layout = findViewById(R.id.layout);

        genieEffectLayout
                .setMaximizeView(layout)
                .setMinimizeView(button)
                .init();
    }

    public void minimize(View view) {
        genieEffectLayout.start();
    }
}
