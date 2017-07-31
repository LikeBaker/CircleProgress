package like.circleprogress;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    int progress = 0;
    private CircleProgressbar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(mProgressBar);
        mProgressBar = (CircleProgressbar) findViewById(R.id.circle);


        final Handler handler = new Handler();
        final Runnable runnable=new Runnable(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgressbar(progress++);
                    }
                });

                if (progress <= 270) {
                    handler.postDelayed(this, 1);
                }

            }
        };

        handler.postDelayed(runnable, 50);
    }
}
