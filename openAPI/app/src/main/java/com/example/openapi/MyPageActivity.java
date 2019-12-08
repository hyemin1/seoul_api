package com.example.openapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyPageActivity extends AppCompatActivity {

    ListView myReviewList;    //list of reviews
    LinearLayout myInfo;

    TextView myNickname;

    EditText changeNickName;
    EditText changePW;
    EditText changePWCheck;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private FirebaseAuth mAuth;

    private ReviewAdaptor reviewAdaptor;
    private ArrayList<ListItem> reviewItems;
    private ArrayList<ReviewItem> reviewPosts;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mAuth = FirebaseAuth.getInstance();

        myReviewList = (ListView)findViewById(R.id.myReviewList);
        myInfo = (LinearLayout)findViewById(R.id.myInfo);

        myNickname = (TextView)findViewById(R.id.curNickname);

        changeNickName = (EditText)findViewById(R.id.newNicknameText);
        changePW = (EditText)findViewById(R.id.newPWText);
        changePWCheck = (EditText)findViewById(R.id.newPWCheckText);

        //show current nickname of user
        String uid = mAuth.getCurrentUser().getUid();
        myRef.child("users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nickname = dataSnapshot.getValue(UserData.class).getNickname();
                        myNickname.append(nickname);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        findViewById(R.id.saveChangeB).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                String newNickname = changeNickName.getText().toString();
                String newPW = changePW.getText().toString();
                String newPWCheck = changePWCheck.getText().toString();

                if(!newPW.equals(newPWCheck) || newPW.length()<6){
                    Toast.makeText(MyPageActivity.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /***upadate && delete ***/
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        findViewById(R.id.InfoB).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                myInfo.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.reviewB).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                myInfo.setVisibility(View.GONE);
                myReviewList.setVisibility(View.VISIBLE);

                //load reviews of this wedding obj from database
                myRef.child(mAuth.getCurrentUser().getUid()).child("posts").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                reviewItems = new ArrayList<ListItem>();    //for list view
                                reviewPosts = new ArrayList<ReviewItem>();  //for review detail

                                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                                    ReviewItem reviewItem = postSnapShot.getValue(ReviewItem.class);
                                    reviewPosts.add(reviewItem);
                                    //Log.d("목록", reviewItem.getContent());
                                    reviewItems.add(new ListItem(reviewItem.content, reviewItem.getDate()));
                                }

                                if(reviewItems.isEmpty())
                                    Toast.makeText(MyPageActivity.this, "아직 후기가 없습니다.", Toast.LENGTH_SHORT).show();
                                else{
                                    printReviewList();
                                    myReviewList.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
            }
        });

        myReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReviewItem review = reviewPosts.get(i);

                Intent intent = new Intent(getApplicationContext(), DetailReviewActivity.class);
                intent.putExtra("post", review);
                intent.putExtra("from", "mypage");
                intent.putExtra("weddingName", review.getWeddingName());
                startActivity(intent);
                finish();
            }
        });

    }

    private void printReviewList(){
        reviewAdaptor = new ReviewAdaptor(reviewPosts, getApplicationContext());
        myReviewList.setAdapter(reviewAdaptor);
    }
}
