package reboot.grouper.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import reboot.grouper.R;

public class CreateCourse extends AppCompatActivity {
    private Button btn;
    public static final String EXTRA_MESSAGE = "com.aesthetics.simple.grouper.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        btn = (Button)findViewById(R.id.btn_Submit);
        final EditText editTextName = (EditText) findViewById(R.id.txt_Name);
        final EditText editTextCode = (EditText) findViewById(R.id.txt_Code);
        final EditText editTextInst = (EditText) findViewById(R.id.txt_Inst);
        final Intent intent = new Intent(CreateCourse.this, Lists.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = editTextName.getText().toString();
                final String Code = editTextCode.getText().toString();
                final String Inst = editTextInst.getText().toString();
                String[] course = {Name,Code,Inst};
                intent.putExtra(EXTRA_MESSAGE,course);
                startActivity(intent);
                finish();
            }
        });
    }
}
