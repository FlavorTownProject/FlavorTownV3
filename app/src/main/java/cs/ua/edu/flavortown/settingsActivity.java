package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class settingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CheckBox a = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox b = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox c = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox d = (CheckBox) findViewById(R.id.checkBox4);
        Button chgPassword = (Button) findViewById(R.id.changePassword);

        a.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    Toast.makeText(buttonView.getContext(), "Vegetarian Checked", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(buttonView.getContext(), "Vegetarian Unchecked", Toast.LENGTH_SHORT).show();

                }
            }

        });

        b.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    Toast.makeText(buttonView.getContext(), "Gluten Free Checked", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(buttonView.getContext(), "Gluten Free Unchecked", Toast.LENGTH_SHORT).show();

                }
            }

        });

        c.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    Toast.makeText(buttonView.getContext(), "Extra Gluten Checked", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(buttonView.getContext(), "Extra Gluten Unchecked", Toast.LENGTH_SHORT).show();

                }
            }

        });

        d.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    Toast.makeText(buttonView.getContext(), "Spicy Checked", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(buttonView.getContext(), "Spicy Unchecked", Toast.LENGTH_SHORT).show();

                }
            }

        });

        chgPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Change Password Selected", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
