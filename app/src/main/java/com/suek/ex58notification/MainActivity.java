package com.suek.ex58notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
//모든 액티비티들은 context 를 가지고있음(context 안에 여러가지 기능들이있음,
// ex;LayoutInflater, Resources,bluetooth관리자, autoM, vibrater...-보통 get.이렇게 부름)

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void clickBtn(View view) {
        // 1) Notification Manager(알림 관리자) 을 띄워주는 관리자 객체 '소환'
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 2) 알림(Notification)객체를 만들어주는 건축가객체 생성
        NotificationCompat.Builder builder= null;

        //오레오버젼 api26버전 때부터 '알림채널'이라는 것이 생겼음
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //'알림채널' 객체 생성
            NotificationChannel channel= new NotificationChannel("ch01", "Channel #1", NotificationManager.IMPORTANCE_HIGH);  //id:식별자, name:노출되는 이름
            notificationManager.createNotificationChannel(channel);   //notificationManager 가 channel 을 만듦

            builder= new NotificationCompat.Builder(this,"ch01");

        }else {
            builder= new NotificationCompat.Builder(this, null);  //오레오 전에는 채널이라는 버전이 없어서 null 을 줌?
        }

        //만들어진 건축가에게 Notification 의 모양을 설정
        builder.setSmallIcon(R.drawable.ic_stat_name);    //상태표시줄에 보이는 아이콘지정

        //확장상태바[상태표시줄을 드래그하여 아래로 내리면 보이는 알림창]
        //그곳에 보이는 설정들
        builder.setContentTitle("문자왓숑~~~");
        builder.setContentText("알림메세지 입니다. 주의하세요!");
        builder.setSubText("서브 텍스트 쓰는곳");

        //res 폴더 창고관리자 객체 소환
        Resources res= getResources();
        Bitmap bm= BitmapFactory.decodeResource(res, R.drawable.giraffe);  //옆에 작은 그림 보이기
        builder.setLargeIcon(bm);

        //확장상태바에 큰이미지 스타일 적용
        NotificationCompat.BigPictureStyle bigPictureStyle= new NotificationCompat.BigPictureStyle(builder);
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(res, R.drawable.moana01));

        builder.setProgress(100, 50, true);  //3번째 파라미터: 상태표시줄의 게이지가 무한루프 여부

        //여러스타일 객체들..
        //Notification.BigTextStyle
        //Notification.InboxStyle
        //Notification.MediaStyle

        //진동 [AndroidManifest.xml 에서 퍼미션 추가 - 동적퍼미션이 필요없음]
        builder.setVibrate(new long[]{0, 2000, 1000, 3000});  //0초대기, 2초진동, 1초대기, 3초진동

        //사운드
        //Uri uri= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION); //URI 리소스 식별자
        //builder.setSound(uri);
        Uri uri= Uri.parse("android.resource://"+ getPackageName() + "/" +R.raw.pumkin);  //android.resource://-> res 폴더지칭  //getPackageName()-> 패키지명. com.suek...
        builder.setSound(uri);

        //확장상태바의 알림을 클릭했을때 새로운 액티비티로 이동
        Intent intent= new Intent(this, SecondActivity.class);
        //보류중인 인텐트생성 (막 start 를 하지않고 보류하고 싶을때)
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //알림 확인을 했을대 자동으로 알림이 사라지는 것(setContentIntent()했을때만 가능)
        builder.setAutoCancel(true);




        // 1.1) 알림객체 생성
        Notification notification= builder.build();

        //알림 관리자에게 알림을 띄우도록 공지!
        notificationManager.notify(1, notification);
    }
}
