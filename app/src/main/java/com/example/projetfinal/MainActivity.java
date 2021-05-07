package com.example.projetfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.projetfinal.models.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Map<String, String>> dataQuestions;
    List<QuestionModel> listQuestions;

    ListView questionsLstView;
    Button btnAjoutQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionsLstView=findViewById(R.id.lvQuestions);
        btnAjoutQ=findViewById(R.id.btnAjoutQuestion);

        loadQuestions();

        btnAjoutQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });

        questionsLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionModel q = listQuestions.get(position);

                openQuestion(q);
            }
        });
    }

    private void openQuestion(QuestionModel q) {
        db.collection("questions")
                .whereEqualTo("intitule",q.getIntitule())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().isEmpty()){
                                for(QueryDocumentSnapshot doc : task.getResult()){
                                    String id = doc.getId();
                                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);

                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
    }


    private void loadQuestions() {
        db.collection("questions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                listQuestions=new ArrayList<>();
                dataQuestions = new ArrayList<>();

                for(QueryDocumentSnapshot doc : value){
                    Map<String, String> item = new HashMap<>();
                    String intitule = doc.getString("intitule");
                    String reponse = doc.getString("reponse");
                    String indicateur;

                    if(reponse!=null){
                        indicateur = "OK";
                    } else {
                        indicateur = "KO";
                    }

                    item.put("First", intitule);
                    item.put("Second", reponse);
                    item.put("Third",indicateur);
                    dataQuestions.add(item);
                    listQuestions.add(new QuestionModel(intitule));
                }

                SimpleAdapter adapt = new SimpleAdapter(MainActivity.this, dataQuestions,
                        R.layout.item_list_view,
                        new String[] {"First", "Second", "Third" },
                        new int[] {R.id.line_a, R.id.line_b, R.id.indicateur });

                questionsLstView.setAdapter(adapt);
            }
        });

    }
}