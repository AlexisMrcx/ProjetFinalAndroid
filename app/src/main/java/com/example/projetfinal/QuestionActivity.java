package com.example.projetfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projetfinal.models.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class QuestionActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btnValider, btnSupprimer;
    TextInputLayout champQ, champR;
    ImageView viewImg;
    String idQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        btnValider=findViewById(R.id.btnValider);
        btnSupprimer=findViewById(R.id.btnSupprimer);
        champQ=findViewById(R.id.champQuestion);
        champR=findViewById(R.id.champReponse);
        viewImg=findViewById(R.id.image_take);

        idQuestion=getIntent().getStringExtra("id");

        if(idQuestion != null){
            loadQuestion(idQuestion);
            champQ.setEnabled(false);
        } else{
            champR.setEnabled(false);
            btnSupprimer.setVisibility(View.INVISIBLE);
        }

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(champQ.isEnabled()){
                    if(champQ.getEditText().getText().length()>0){
                        QuestionModel nouvQuestion = new QuestionModel();

                        nouvQuestion.setIntitule(champQ.getEditText().getText().toString());
                        ajoutQuestion(nouvQuestion);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Le champ intitule doit obligatoirement être rempli !",Toast.LENGTH_LONG).show();
                    }
                } else if (idQuestion != null){

                    if(champR.getEditText().getText().length()>0){
                        sentReponse(idQuestion);
                    } else {
                        Toast.makeText(getApplicationContext(),"Veuillez entrer une réponse !",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        
        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQuestion(idQuestion);
            }
        });
    }

    private void sentReponse(String idQuestion) {
        db.collection("questions")
                .document(idQuestion)
                .update("reponse",champR.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Réponse envoyée avec succès !",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    private void deleteQuestion(String idQuestion) {
        db.collection("questions")
                .document(idQuestion)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Question supprimée avec succès !",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    private void loadQuestion(String idQuestion) {
        db.collection("questions")
                .document(idQuestion)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                              champQ.getEditText().setText(task.getResult().getString("intitule"));
                        }
                    }
                });
    }

    private void ajoutQuestion(QuestionModel question) {
        db.collection("questions")
                .add(question)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(),"Question enregistré avec succès",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue ", Toast.LENGTH_LONG).show();
                    }
                });
    }
}