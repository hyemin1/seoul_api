package com.example.openapi;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import android.graphics.Color;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;


public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {
    WeddingObj weddingObj;
    TextView nameText;
    ListView listView;

    private MyAdaptor adaptor;
    private ArrayList<ListItem>items;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //receive one WeddingObj data from MainActivity
        Intent intent = getIntent();
        weddingObj = (WeddingObj)intent.getSerializableExtra("object");

        //show name of weddingObj on the screen
        nameText = (TextView)findViewById(R.id.name);
        nameText.append(weddingObj.getName());

        listView = (ListView)findViewById(R.id.listView);

        initMap();  //initiate map
        initData(); //initiate data to show on listView
        printList();//show the whole data one listView

        //create Button that goes back to MainActivity
        Button backButton = (Button)findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /********  Map Configure  ********/
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        LatLng location = new LatLng(weddingObj.getCoord_y(), weddingObj.getCoord_x());
        CameraPosition cameraPosition = new CameraPosition(location, 15);

        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);

        //set camera position at which the place is located
        naverMap.setCameraPosition(cameraPosition);

        //create Marker and set its position at the same location as camera
        Marker marker = new Marker();
        marker.setPosition(new LatLng(weddingObj.getCoord_y(), weddingObj.getCoord_x()));
        //set marker color
        marker.setIcon(MarkerIcons.BLACK);
        marker.setIconTintColor(Color.rgb(231, 138, 138));
        //set marker size
        marker.setWidth(Marker.SIZE_AUTO);
        marker.setHeight(Marker.SIZE_AUTO);
        //set marker on map
        marker.setMap(naverMap);

    }

    /********  initialize Map  **********/
    private void initMap(){
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    private void initData(){
        items = new ArrayList<ListItem>();

        items.add(new ListItem("*주소",weddingObj.getFull_addr_new()));
        items.add(new ListItem("*전화번호",weddingObj.getPhone()));
        items.add(new ListItem("*예식장 이름",weddingObj.getPlace_name()));
        items.add(new ListItem("*교통편",weddingObj.getTransport()));
        items.add(new ListItem("*대관 비용",weddingObj.getCost()));
        items.add(new ListItem("*주차 요금",weddingObj.getParking_cost()));
        items.add(new ListItem("*사용가능 요일/시설 정보",weddingObj.getAvail_time()));
        items.add(new ListItem("*추가 제공 사항",weddingObj.getAvail_obj()));
        items.add(new ListItem("*상세 정보",weddingObj.getDetails()));

    }

    /********* show data on listView ************/
    private void printList(){
        adaptor = new MyAdaptor(items, getApplicationContext());
        listView.setAdapter(adaptor);
    }
}
