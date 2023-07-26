package algonquin.cst2335.zhou0224;

import androidx.appcompat.app.AppCompatActivity;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.zhou0224.databinding.ActivityMainBinding;

/**
 * This page is provided to input password,
 * then verify and show if the password meets requirements.
 * @author Ziyue Zhou
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the top of the screen*/
    private TextView tv = null;
    /** This holds the editable text at the centre of the screen*/
    private EditText et = null;
    /** This holds the button at the bottom of the screen*/
    private Button btn = null;
    RequestQueue queue = null;
    protected String cityName;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);


        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());


        binding.button.setOnClickListener(click -> {
            cityName = binding.editText.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName, "UTF-8") + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null, (response) -> {
                try {
                    JSONObject coord = response.getJSONObject("coord");
                    JSONArray weatherArray = response.getJSONArray ( "weather" );
                    int vis = response.getInt("visibility");
                    String name = response.getString( "name" );
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");
                    JSONObject mainObject = response.getJSONObject( "main" );
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");


                    runOnUiThread(() -> {

                        binding.temp.setText("The current temperature is " + current);
                        binding.temp.setVisibility(View.VISIBLE);
                        binding.maxTemp.setText("The max temperature is "+max);
                        binding.maxTemp.setVisibility(View.VISIBLE);
                        binding.minTemp.setText("The min temperature is "+min);
                        binding.minTemp.setVisibility(View.VISIBLE);
                        binding.humidity.setText("The Humidity is "+humidity);
                        binding.humidity.setVisibility(View.VISIBLE);
                        binding.description.setText(description);
                        binding.description.setVisibility(View.VISIBLE);
                    });
                    String pictureURL = "https://openweathermap.org/img/w/" + iconName + ".png";
                    String pathName = getFilesDir() + "/" + iconName + ".png";

                    File file = new File(pathName);

                    if (file.exists()) {
                        image = BitmapFactory.decodeFile(pathName);
                        runOnUiThread(() -> {
                            binding.icon.setImageBitmap(image);
                            binding.icon.setVisibility(View.VISIBLE);
                        });
                    } else {
                        ImageRequest imgReq = new ImageRequest("http://openweathermap.org/img/w/" + iconName + ".png", new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                try {
                                    image = bitmap;
                                    image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                    binding.icon.setVisibility(View.VISIBLE);
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }
                            }
                        }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                (error) -> {
                                    Toast.makeText(MainActivity.this, "Image error", Toast.LENGTH_SHORT).show();
                                });

                        queue.add(imgReq);}



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, (error) -> { });

            queue.add(request);


        });
    }




}
