package cs.ua.edu.flavortown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    TextView restaurantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        restaurantName = (TextView) findViewById(R.id.restaurantTextView);

        Bundle extras = getIntent().getExtras();
        restaurantName.setText(extras.getString("restaurantName"));
    }
}
