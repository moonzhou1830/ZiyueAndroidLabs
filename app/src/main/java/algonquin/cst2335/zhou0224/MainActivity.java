package algonquin.cst2335.zhou0224;

import androidx.appcompat.app.AppCompatActivity;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    RequestQueue queueImage = null;
    protected String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        queueImage = Volley.newRequestQueue(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater());
        setContentView(binding.getRoot());

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.cityTextField);
        btn = findViewById(R.id.forecastButton);

        binding.forecastButton.setOnClickListener(clk -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = "";
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName,"UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            //This goes in the button click handler:
            JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET,stringURL,null,
                    (response) -> {
                try{
                    JSONArray weatherArray = response.getJSONArray ( "weather" );
                    JSONObject position0 =  weatherArray.getJSONObject(0);
                    JSONObject mainObject = response.getJSONObject( "main" );

                    int vis = response.getInt("visibility");
                    String name = response.getString( "name" );

                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");

                    String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";

                    runOnUiThread( (  )  -> {
                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.humidity.setText("The humitidy is " + humidity + "%");
                                binding.humidity.setVisibility(View.VISIBLE);

                                binding.description.setText("The description is " + description);
                                binding.description.setVisibility(View.VISIBLE);
                            });
                    ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                                String filePath = getFilesDir().getAbsolutePath() + "/" + iconName + ".png";
                                Bitmap savedBitmap = BitmapFactory.decodeFile(filePath);
                                binding.icon.setImageBitmap( savedBitmap );
                                binding.icon.setVisibility(View.VISIBLE);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                    });
                    queueImage.add(imgReq);
                } catch (JSONException e){
                    throw new RuntimeException(e);
                }
                    },
                    (error) -> {  } );
            queue.add(request);
        });



        /*btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            boolean isPasswordValid = checkPasswordComplexity(password);
            TextView textView = findViewById(R.id.textView);
            if (isPasswordValid) {
                textView.setText("Your password meets the requirements");
            } else {
                textView.setText("You shall not pass!");
            }
        });*/

    }

    /**
     * This function should check if this string has an Upper Case letter,
     * a lower case letter, a number, and a special symbol (#$%^&*!@?).
     *
     * @param pw The String object that we are checking
     * @return Returns true if the password is complex enough,
     * and false if it is not complex enough.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }
        if (!foundUpperCase) {
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();// Say that they are missing an upper case letter;
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Your password does not have an lower case letter", Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Your password does not have an number", Toast.LENGTH_SHORT).show(); // Say that they are missing a number;
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show(); // Say that they are missing a special symbol;
            return false;
        }
        else
            return true; //only get here if they're all true
        }

    /**
     * This function should check if this string has a special symbol.
     * @param c each character of pw
     * @return Returns true if there is a special symbol, otherwise returns false.
     */
    boolean isSpecialCharacter(char c)
        {
            switch (c)
            {
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '!':
                case '@':
                case '?':
                    return true;
                default:
                    return false;
            }
        }

    }
