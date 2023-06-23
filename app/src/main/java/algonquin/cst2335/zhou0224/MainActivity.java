package algonquin.cst2335.zhou0224;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.text.BreakIterator;
import algonquin.cst2335.zhou0224.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e(TAG, "In onCreate() - Loading Widgets");

        //where can you save files:
        File myDir =getFilesDir();
        String path = myDir.getAbsolutePath();

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        binding.emailText.setText(emailAddress);

        binding.loginButton.setOnClickListener(clk -> {
            Log.e(TAG, "You clicked the button");

            String whatIsTyped = binding.emailText.getText().toString();

            //get an editor
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", whatIsTyped);
            editor.putFloat("Hi", 4.5f);
            editor.putInt("Age", 35);
            editor.apply();

            //saves to disk
            editor.commit();

            // Retrieve the saved text from SharedPreferences and update the EditText
            // binding.emailText.setText(prefs.getString("LoginName", " "));

            //where to go:             leaving here          going to SecondActivity
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress",binding.emailText.getText().toString() );

//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent .setType("*/*");
//            intent .putExtra(Intent.EXTRA_EMAIL, new String[]{"A@A.com", "B@B.com"}  );
//            intent .putExtra(Intent.EXTRA_SUBJECT, "Subject");

            //go to another page, carries all the data to the next page
            startActivity(nextPage);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "The onStart()- the application is now visible on the screen");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d( TAG, "The onResume()- the application is now responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e( TAG, "onPause()- The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( TAG, "he application is no longer visible" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e( TAG, "Any memory used by the application is freed" );
    }

}