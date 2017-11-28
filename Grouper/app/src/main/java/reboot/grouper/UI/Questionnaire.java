package reboot.grouper.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import reboot.grouper.FrontEnd.Dispatcher;
import reboot.grouper.FrontEnd.UserSession;
import reboot.grouper.Model.Environment;
import reboot.grouper.Model.User;
import reboot.grouper.R;

public class Questionnaire extends AppCompatActivity {
    private Dispatcher      controller;
    private List<Question>  Questions;
    private String     E;
    private List<Integer>   response;
    private int             currentQuestion;

    private TextView    txt_Question;
    private RadioGroup  radioGroup;
    private Button      btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = Dispatcher.getList();
        E = getIntent().getExtras().getString("envName");
        setContentView(R.layout.activity_questionnaire);
        radioGroup      = findViewById(R.id.radio_Group);
        txt_Question    = findViewById(R.id.txt_Question);
        btn_submit      = findViewById(R.id.btn_Submit);
        populateQuestions();
        createQuestionnaire();
    }

    private void populateQuestions(){
        Questions = new ArrayList<>();
        response = new ArrayList<>();
        for (Map.Entry<String,List<String>> entry : controller.getQuestionnaire().entrySet()) {
            Questions.add(new Question(entry.getKey(),entry.getValue()));
        }

    }

    private void createQuestionnaire(){
        radioGroup.removeAllViews();
        txt_Question.setText(Questions.get(currentQuestion).Q);
        if(currentQuestion<Questions.size()-1)  btn_submit.setText("Next");
        else                                    btn_submit.setText("Submit");
        for(String A : Questions.get(currentQuestion).A){
            RadioButton button = new RadioButton(this);
            button.setText(A);
            radioGroup.addView(button);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checked = radioGroup.getCheckedRadioButtonId();
                if(checked<0){ return; }
                response.add(checked);
                currentQuestion++;
                if(currentQuestion<Questions.size()){
                    createQuestionnaire();
                }
                else{
                    User usr = new User(UserSession.I().getUser().getUsername());
                    usr.setQuestionAnswers(response);
                    controller.setQuestionnaireResponse(usr,E);
                    controller.updateView();
                    finish();
                }
            }
        });
    }
}
