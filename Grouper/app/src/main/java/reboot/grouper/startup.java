package reboot.grouper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import reboot.grouper.UI.Lists;
import reboot.grouper.UI.LoginActivity;

public class startup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
