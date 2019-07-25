package com.jyn.androidgenieeffec;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.jyn.genieeffec.GenieEffectLayout;

/**
 * @author jiao
 */
@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private GenieEffectLayout genieEffectLayout;
    Button button;
    ImageView imageView;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genieEffectLayout = findViewById(R.id.genie_effect_layout);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.image);
        layout = findViewById(R.id.layout);

        genieEffectLayout
                .setMaximizeView(layout)
                .setMinimizeView(button);
    }

    public void minimize(View view) {
        genieEffectLayout.start();
    }
}
