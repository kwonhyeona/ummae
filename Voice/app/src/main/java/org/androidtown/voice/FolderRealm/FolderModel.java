package org.androidtown.voice.FolderRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Sansung on 2016-07-26.
 */

public class FolderModel {
    Realm realm;

    //FolderModel 객체 생성될 때 Realm도 같이 인스턴스화 한다.

    public FolderModel() {
        realm = Realm.getDefaultInstance();
    }

    //전체 개수 반환 메소드
    public int count() {
        return (int) realm.where(Folder.class).count();
    }

    public ArrayList<Folder> getAllFolders() {
        //Realm에 저장된 모든 Folder 데이터들을 AraayList<Folder> 객체로 반환
        RealmResults<Folder> realmResults = realm.where(Folder.class).findAll();

        ArrayList<Folder> folderList = new ArrayList<>();
        for (Folder folder : realmResults) {
            folderList.add(folder);
        }
        return folderList;
    }


    //primary key인 id를 통해 Memo 데이터 받아오기
    public Folder getFolderById(int folderId) {
        //Realm 내의 Folder 데이터 중 해당 id의 데이터만을 가져오기
        return realm.where(Folder.class).equalTo("folderId", folderId).findFirst();
    }

    public void editFolder(Folder folder) {
        //Memo 데이터 수정 메소드
        //Realm 내의 Folder 데이터 중 하나를 Update하는 코드
        realm.beginTransaction();

        //realm 이 변경되는 코드
        realm.copyToRealmOrUpdate(folder);
        realm.commitTransaction();
    }

    //Folder 객체를 전달받아 copyToRealm 메소드 이용해서 Realm에 저장
    public void addFolder(Folder folder) {
        //Realm 에 Folder 데이터를 삽입
        realm.beginTransaction();
        realm.copyToRealm(folder);
        realm.commitTransaction();
    }

    //id에 해당하는 Folder 모델 삭제
    public void deleteFolder(int folderId) {
        //Realm 내의 Folder 데이터 중 해당 id의 데이터 제거
        realm.beginTransaction();
        realm.where(Folder.class).equalTo("folderId", folderId).findFirst().removeFromRealm();
        realm.commitTransaction();
    }

    //

    //id생성 메소드
    //Realm에서는 id를 AutoIncrement로 생성해주지 않아서 직접 정해줘야 함.
    //Realm에 저장된 Folder 데이터 중 가장 나중에 생성된 데이터의 id 값에 +1하는 방식으로 id생성
    public int getNewId() {
        int newId = 0;
        RealmResults<Folder> realmResults = realm.where(Folder.class).findAll();

        //Realm에 데이터가 하나도 없으면 realmResults에 데이터가 없어
        //realmResults 가 0보다 큰 경우에만 id생성, 작은 경우에는 0반환
        if (realmResults.size() > 0) {
            newId = realmResults.last().getFolderId() + 1;
        }
        return newId;
    }

    //FolderModel에서 인스턴스화했던 Realm 객체를 메모리에서 해제
    public void closeRealm() {
        realm.close();
    }

}
