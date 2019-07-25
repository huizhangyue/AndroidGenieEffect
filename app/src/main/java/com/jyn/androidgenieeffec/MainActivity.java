package com.jyn.androidgenieeffec;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genieEffectLayout = findViewById(R.id.genie_effect_layout);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.image);
        layout = findViewById(R.id.layout);
        text = findViewById(R.id.text);

        genieEffectLayout
                .setMaximizeView(layout)
                .setMinimizeView(text);
    }

    public void minimize(View view) {
        genieEffectLayout.start();
    }
}
