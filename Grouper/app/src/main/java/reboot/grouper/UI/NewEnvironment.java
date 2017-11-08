package reboot.grouper.UI;
import android.content.Intent;
//import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import reboot.grouper.FrontEnd.Volley;
import reboot.grouper.Model.Environment;
import reboot.grouper.R;

public class NewEnvironment extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.aesthetics.simple.grouper.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_environment);
        setTitle("New Environment");
        Button btn_Submit = (Button)findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Submit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Submit() throws JSONException {
        String          Name = ((TextView)findViewById(R.id.txt_Name)).getText().toString().trim();
        String          Password = ((TextView)findViewById(R.id.txt_Password)).getText().toString().trim();
        int             size = Integer.parseInt(((TextView)findViewById(R.id.txt_Max)).getText().toString());
        String          DueDate = ((TextView)findViewById(R.id.txt_DueDate)).getText().toString().trim();
        String          QuizData = ((TextView)findViewById(R.id.txt_Quiz)).getText().toString().trim();
        List<quizPart>  Quiz = new ArrayList<quizPart>();

        boolean err = false;
        String errMSG="";
        Environment env= new Environment(Name,false,Password,size);
        addEnvironment(env);
        if(Name.equals("")||DueDate.equals("")){
            err=true;
            errMSG="ERROR: A name and a Due Date is required.";
        }

        int i,j;
        j=-1;
        //Parse Quiz
        String[] Lines = QuizData.split("\\r?\\n");
        if(Lines.length>0){
            for(i=0;i<Lines.length;i++){
                if(Lines[i].equals("")){
                    continue;
                }
                if(Lines[i].substring(0,1).equals("+")){
                    Quiz.add(new quizPart(Lines[i].substring(0,Lines[i].length())));
                    j++;
                }
                if(Lines[i].substring(0,1).equals("-")&&j>=0){
                    Quiz.get(j).addA(Lines[i].substring(0,Lines[i].length()));
                }
            }
        }
        else{
            err=true;
            errMSG="ERROR: The quiz could not be parsed.";
        }

        if(err){
            Toast.makeText(getApplicationContext(), errMSG, Toast.LENGTH_LONG).show();
            return;
        }
        //Intent intent = new Intent(NewEnvironment.this, MainLists.class);
        //startActivity(intent);
        Toast.makeText(getApplicationContext(), "Request Parsed", Toast.LENGTH_LONG).show();
    }

    private void addEnvironment(Environment env) throws JSONException {
        /*
        Intent intentCourse = getIntent();
        String uni  = intentCourse.getStringExtra("uni");
        String course  = intentCourse.getStringExtra("course");
        Gson gson = new Gson();
        String obj = gson.toJson(env);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, Volley.I(this).getAddress()+"universities/"+uni+"/courses/"+course+"/environments",new JSONObject(obj),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", error.getMessage());
                    }
                }
        ){
            //here I want to post data to sever
        };
        vClient.I(this).addtoReqQueue(jsonobj);
    */
    }




}

class quizPart{
    public String Q;
    public List<String> A;
    public quizPart(String q){
        Q=q;
        A = new ArrayList<String>();
    }
    public void addA(String Ans){
        A.add(Ans);
    }
    public String parse(){
        String out = "{\"question\":\"" + Q + "\",\"answers\":[";
        for(int i = 0; i < A.size(); i++){
            out+= "\"" + A.get(i)+ "\"" + (i==A.size()-1?"":",");
        }
        out+="]}";
        return out;
    }
}