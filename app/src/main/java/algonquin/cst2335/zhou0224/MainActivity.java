package algonquin.cst2335.zhou0224;

import androidx.appcompat.app.AppCompatActivity;
//import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            boolean isPasswordValid = checkPasswordComplexity(password);
            TextView textView = findViewById(R.id.textView);
            if (isPasswordValid) {
                textView.setText("Your password meets the requirements");
            } else {
                textView.setText("You shall not pass!");
            }
        });
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
