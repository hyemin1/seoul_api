package com.example.openapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


/**** shows detail of review ****/
public class DetailReviewActivity extends AppCompatActivity {
    ReviewItem reviewItem;  //review Item to be shown
    WeddingObj weddingObj;  //from DetailActivity
    String from;            //DetailActivity: "detail", MyPageActivity: "mypage"
    String weddingName;     //name of this wedding place

    TextView placeName;     //where weddingName shows

    //info of review
    TextView rDate;     //written date
    TextView rNickname; //writer
    TextView rContent;  //content of review


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        reviewItem = (ReviewItem) intent.getSerializableExtra("post");
        from = (String) intent.getSerializableExtra("from");
        //from MyPageActivity
        if(from.equals("mypage")){
            weddingName = (String)intent.getSerializableExtra("weddingName");
        }
        //from DetailActivity
        else{
            weddingObj = (WeddingObj) intent.getSerializableExtra("object");
            weddingName = weddingObj.getName();
        }

        //show name of this place
        placeName = (TextView)findViewById(R.id.placeName);
        placeName.append(weddingName);

        //show info of review
        rDate = (TextView)findViewById(R.id.reviewDate);
        rNickname = (TextView)findViewById(R.id.nickname);
        rContent = (TextView)findViewById(R.id.reviewContent);

        rDate.append(reviewItem.date);
        rNickname.append(reviewItem.nickname);
        rContent.append(reviewItem.content);

        rContent.setVisibility(View.VISIBLE);

        //go back button
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //go back to my page
                if(from.equals("mypage")) {
                    Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                //go back to detail page
                else {
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("object", weddingObj);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
