package algonquin.cst2335.zhou0224.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.zhou0224.data.MainViewModel;
import algonquin.cst2335.zhou0224.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        //variableBinding.textview.setText(model.editString);
        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });

        variableBinding.myimagebutton.setOnClickListener(click ->
        {
            int width=this.variableBinding.myimagebutton.getWidth();
            int height=this.variableBinding.myimagebutton.getHeight();
            CharSequence text = "The width = "+ width + " and height = " + height ;

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this /* MyActivity */, text, duration);
            toast.show();
        });

        model.editString.observe(this, s ->{
            variableBinding.textview.setText("Your edit text has "+s);
        });

        variableBinding.checkbox.setOnCheckedChangeListener((btn, isChecked) ->{
            model.isSelected.postValue(variableBinding.checkbox.isChecked());
                }
        );
        variableBinding.radioButton.setOnCheckedChangeListener((btn, isChecked) ->{
                    model.isSelected.postValue(variableBinding.radioButton.isChecked());
                }
        );

        variableBinding.switch1.setOnCheckedChangeListener((btn, isChecked) ->{
                    model.isSelected.postValue(variableBinding.switch1.isChecked());
                }
        );


        model.isSelected.observe(this, selected ->{
            variableBinding.checkbox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
            CharSequence text = "The value is now " + selected;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, text, duration);
            toast.show();
        });



    }}