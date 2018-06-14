package org.androidtown.voice;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//Application 객체는 어플 상에 단 하나만 존재
//어플이 맨 처음 시작될 때 ApplicationController 부터 시작
public class ApplicationController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Realm 설정 초기화
        RealmConfiguration config = new RealmConfiguration
                .Builder(getApplicationContext())
                .name("voice.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config); //config를 디폴트로 설정

    }
}
