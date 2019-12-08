package com.example.openapi;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** contains info of review text **/
public class ReviewItem implements Serializable {
    String weddingName;
    String content;
    String nickname;
    String date;

    /**constructors**/
    public ReviewItem(){}

    public ReviewItem(String weddingName, String content, String nickname, String date){
        this.weddingName = weddingName; //name of the place
        this.content = content;         //content of review
        this.nickname = nickname;       //writer
        this.date = date;               //written date
    }

    //update
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("weddingName", weddingName);
        result.put("content", content);
        result.put("nickname", nickname);
        result.put("date", date);

        return result;
    }


    /**setter**/
    public void setWeddingName(String weddingName){ this.weddingName = weddingName; }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDate(String date) {
        this.date = date;
    }


    /**getter**/
    public String getWeddingName() { return weddingName;}

    public String getContent() { return content; }

    public String getNickname() { return nickname; }

    public String getDate() { return date; }

}
