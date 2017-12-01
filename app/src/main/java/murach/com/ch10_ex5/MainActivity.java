package murach.com.ch10_ex5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{

    private TextView messageTextView;
    private Button startBtn;
    private Button stopBtn;
    private final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
    private final String FILENAME = "news_feed.xml";
    private Context context = null;
    public Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = findViewById(R.id.messageTextView);
        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);

        startBtn.setOnClickListener(clicked);
        stopBtn.setOnClickListener(clicked);
    }
    private OnClickListener clicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.startBtn:
                    startTimer();
                    downloadFile();
                    break;
                case R.id.stopBtn:
                    stopTimer();
            }
        }
    };
    public void onPause(){
        super.onPause();
        timer.cancel();
    }
    private void startTimer() {
        final long startMillis = System.currentTimeMillis();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;
                updateView(elapsedMillis);

            }
        };
        timer.schedule(task, 1, 1000);
    }
    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int timesDownloaded = (int) (elapsedMillis/10000);

            @Override
            public void run() {
                messageTextView.setText("File downloaded " + timesDownloaded + " time(s)");
            }
        });
    }
    public void downloadFile() {
        try{
            // get the URL
            URL url = new URL(URL_STRING);

            // get the input stream
            InputStream in = url.openStream();

            // get the output stream
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            Log.e("News reader", e.toString());
        }
    }
    private void stopTimer() {
        if(timer != null){
            timer.cancel();
        }
    }
}
