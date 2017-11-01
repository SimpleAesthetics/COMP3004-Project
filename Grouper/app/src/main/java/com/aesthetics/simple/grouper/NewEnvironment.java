package com.aesthetics.simple.grouper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class NewEnvironment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_environment);
        setTitle("New Environment");
        Button btn_Submit = (Button)findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });
    }

    private void Submit(){
        String          Name = ((TextView)findViewById(R.id.txt_Name)).getText().toString().trim();
        String          Password = ((TextView)findViewById(R.id.txt_Password)).getText().toString().trim();
        String          DueDate = ((TextView)findViewById(R.id.txt_DueDate)).getText().toString().trim();
        String          QuizData = ((TextView)findViewById(R.id.txt_Quiz)).getText().toString().trim();
        List<quizPart>  Quiz = new ArrayList<quizPart>();

        boolean err = false;
        String errMSG="";

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

        Toast.makeText(getApplicationContext(), "Request Parsed", Toast.LENGTH_LONG).show();
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