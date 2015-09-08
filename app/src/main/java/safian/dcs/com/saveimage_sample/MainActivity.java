package safian.dcs.com.saveimage_sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareNilaiIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareNilaiIntent(){
        ShareActionProvider mShareActionProvider = null;

        try {
            /*
            *
            * Create screenshot from current activity
            *
            * */
            View theView = findViewById(R.id.content);
            theView.setDrawingCacheEnabled(true);
            theView.buildDrawingCache(true);
            Bitmap b = Bitmap.createBitmap(theView.getDrawingCache());
            theView.setDrawingCacheEnabled(false);

            /*
            *
            * Save Bitmap "b" as result of creating screenshot
            * It'll be saved in /mnt/sdcard/SaveImage directory
            *
            * */
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
            File directory = new File(Environment.getExternalStorageDirectory(), "SaveImage");

            if(!directory.exists()){
                directory.mkdir();
            }

            File file = new File(directory.getPath(), fileName);

            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            /*
            *
            * After Image has been saved the next step is share it using ShareActionProvider
            *
            * */
            Uri uri = Uri.parse(file.getAbsolutePath());
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            sharingIntent.setType("image/png");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent,
                    "Share image using"));
            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(createShareNilaiIntent());
            }

            return sharingIntent;
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }
}
