package com.example.androidproject2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class FloatingActivity extends AppCompatActivity implements OnMapReadyCallback {
    final static String TAG="SQLITEDBTEST";

    EditText mId;
    EditText mTitle;
    EditText mPlace;
    EditText mMemo;

    private DBHelper mDbHelper;
    GoogleMap mGoogleMap = null;
    private TimePicker mTimePicker;
    int x = 0, y = 0;
    double find_latitude = 0, find_longitutde = 0; //위도, 경도 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mId = (EditText)findViewById(R.id._id);
        mTitle = (EditText)findViewById(R.id.edit_title);
        mPlace = (EditText)findViewById(R.id.edit_place);
        mMemo = (EditText)findViewById(R.id.edit_memo);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);

        GregorianCalendar cal = new GregorianCalendar();
//        System.out.println(cal);

        mDbHelper = new DBHelper(this);

        //저장 버튼 로직
        Button button = (Button)findViewById(R.id.insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
            }
        });

        //취소 버튼 로직
        Button button6 = (Button)findViewById(R.id.cancel);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mId.setText("");
                mTitle.setText("");
                mPlace.setText("");
                mMemo.setText("");
            }
        });

        //삭제 버튼 로직
        Button button1 = (Button)findViewById(R.id.delete);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show(); //확인 받는 다이얼로그 출력하여 확인 시 삭제 처리
            }
        });

        //조회
        Button button3 = (Button)findViewById(R.id.veiwall);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   viewAllToTextView();

            }
        });

        //지도 검색
        Button button5 = (Button)findViewById(R.id.search);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPlace = (EditText)findViewById(R.id.edit_place); //지도 검색할 지명
                String str_find = mPlace.getText().toString();    //문자열로 변경

                try {
                    Geocoder geocoder = new Geocoder(FloatingActivity.this, Locale.KOREA);
                    List<Address> addresses = geocoder.getFromLocationName(str_find,1);
                    if (addresses.size() >0) {
                        Address bestResult = (Address) addresses.get(0);

                        find_latitude = bestResult.getLatitude();    // 문자열로 검색한 위도 정보
                        find_longitutde = bestResult.getLongitude(); // 문자열로 검색한 경도 정보

                        if (mGoogleMap != null) {
                            LatLng location = new LatLng(find_latitude, find_longitutde); //문자열의 위도, 경도정보로 위치정보를 구한다.
                            mGoogleMap.addMarker(                // 찾은 위치에 Maker 표시
                                    new MarkerOptions().
                                            position(location).
                                            title(str_find).
                                            alpha(0.8f).
                                            icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).
                                            snippet(str_find)
                            );
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15)); //위치 이동
                        }
                    }
                } catch (IOException e) {
                    Log.e(getClass().toString(),"Failed in using Geocoder.", e);
                    return;
                }
            }
        });
    }

    //일정관리 확인 Dialog 함수
    void show()
    {
        new AlertDialog.Builder(this)
        .setTitle("일정관리")
        .setMessage("삭제하시겠습니까?")
        .setIcon(android.R.drawable.ic_menu_save)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 확인시 처리 로직
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        deleteRecord();
            }})
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 취소시 처리 로직
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
            }}).show();
    }

    // DB에 저장된 일정정보 SELECT (LOG 확인용)
    private void viewAllToTextView() {
        TextView result = (TextView)findViewById(R.id.result);

        Cursor cursor = mDbHelper.getAllUsersBySQL();

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getInt(0)+" \t");
            buffer.append(cursor.getString(1)+" \t");
            buffer.append(cursor.getString(2)+"\n");
            buffer.append(cursor.getString(3)+"\n");
            buffer.append(cursor.getString(4)+"\n");
            buffer.append(cursor.getString(5)+"\n");
            buffer.append(cursor.getString(6)+" \t");
            buffer.append(cursor.getString(7)+"\n");
            System.out.println("id_ : " + cursor.getInt(0) + " | "
                             + "Title : " + cursor.getString(1) + " | "
                             + "start_hour : " + cursor.getString(2) + " | "
                             + "start_minute : " + cursor.getString(3) + " | "
                             + "end_hour : " + cursor.getString(4) + " | "
                             + "end_minute : " + cursor.getString(5) + " | "
                             + "Place : " + cursor.getString(6) + " | "
                             + "Memo : " + cursor.getString(7)
            );
        }
        result.setText(buffer);
    }

    //DB 내 일정관리테이블의 MAX ID 정보를 가져온다.(일정 삭제를 하기 위해 사용되며 일정저장 직후 호출되는 함수)
    private int searchMaxId() {
        int maxId = 0;
        TextView result = (TextView)findViewById(R.id.result);

        Cursor cursor = mDbHelper.getMaxIdBySQL();

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            maxId = cursor.getInt(0);
            buffer.append(cursor.getInt(0)+" \t");
            System.out.println("MAX ID : " + buffer);
        }

        return maxId;
    }

    // DB에 저장된 일정 삭제 함수(ID값을 기준으로 삭제한다.)
    private void deleteRecord() {
        EditText _id = (EditText)findViewById(R.id._id);

        long nOfRows = mDbHelper.deleteUserByMethod(_id.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,"Record Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Deleted", Toast.LENGTH_SHORT).show();
    }

    // DB에 저장된 일정 등록 함수
    private void insertRecord() {

        mId = (EditText)findViewById(R.id._id);
        EditText _id = (EditText)findViewById(R.id._id);
        EditText title = (EditText)findViewById(R.id.edit_title);
        EditText place = (EditText)findViewById(R.id.edit_place);
        EditText memo = (EditText)findViewById(R.id.edit_memo);
        TimePicker mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        TimePicker mTimePicker2 = (TimePicker) findViewById(R.id.timePicker2);

        String shour, ehour;
        String sminute, eminute;
        int maxId;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            shour = mTimePicker.getHour() + "";
            sminute = mTimePicker.getMinute() + "";

            ehour = mTimePicker2.getHour() + "";
            eminute = mTimePicker2.getMinute() + "";
        }
        else
        {
            shour = mTimePicker.getCurrentHour() + "";
            sminute = mTimePicker.getCurrentMinute() + "";

            ehour = mTimePicker2.getCurrentHour() + "";
            eminute = mTimePicker2.getCurrentMinute() + "";
        }

        long nOfRows = mDbHelper.insertUserByMethod(title.getText().toString(),shour,sminute,ehour,eminute,place.getText().toString(),memo.getText().toString());

        if (nOfRows >0) {
            Toast.makeText(this, nOfRows + " Record Inserted", Toast.LENGTH_SHORT).show();
            maxId = searchMaxId(); //일정 등록 직후 생성된 ID값을 가져오기 위해 일정관리 테이블 내 ID Max 값을 가져옴.
            mId.setText(Integer.toString(maxId)); //Activity ID 위젯에 표시
        }
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng hansung = new LatLng(37.5817891, 127.009854);
        googleMap.addMarker(
                new MarkerOptions().
                        position(hansung).
                        title("한성대학교"));

        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung,15));

        mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getTitle().equals("한성대입구역")) {
                Toast.makeText(getApplicationContext(),"한성대입구역을 선택하셨습니다", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}

