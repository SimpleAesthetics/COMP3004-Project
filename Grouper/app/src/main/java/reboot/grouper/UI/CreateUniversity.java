package reboot.grouper.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import reboot.grouper.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CreateUniversity extends AppCompatActivity {
    private Button btn;
    public static final String EXTRA_MESSAGE = "com.aesthetics.simple.grouper.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_university);
        btn = (Button)findViewById(R.id.btn_Submit);
        final EditText editText = (EditText) findViewById(R.id.txt_Name);
        final Intent intent = new Intent(CreateUniversity.this, Lists.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }
}
