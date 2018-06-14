package org.androidtown.voice.MemoRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Sansung on 2016-07-26.
 */
public class MemoModel {
    Realm realm;

    //MemoModel 객체 생성될 때 Realm도 같이 인스턴스화 한다.

    public MemoModel() {
        realm = Realm.getDefaultInstance();
    }

    //전체 개수 반환 메소드
    public int count() {
        return (int) realm.where(Memo.class).count();
    }

    public ArrayList<Memo> getAllMemos() {
        //Realm에 저장된 모든 Memo 데이터들을 AraayList<Memo> 객체로 반환
        RealmResults<Memo> realmResults = realm.where(Memo.class).findAll();

        ArrayList<Memo> memoList = new ArrayList<>();
        for (Memo memo : realmResults) {
            memoList.add(memo);
        }

        return memoList;
    }

    //날짜에 대한 Memo 데이터 받아오기
    public ArrayList<Memo> getDateOfMemos(String curCalendarDate) {
        RealmResults<Memo> realmResults = realm.where(Memo.class).equalTo("memoday", curCalendarDate).findAll();

        ArrayList<Memo> memoList = new ArrayList<>();
        for (Memo memo : realmResults) {
            memoList.add(memo);
        }

        return memoList;
    }

//    public ArrayList<Memo> getMemosInSameFolder(Folder folder) {
//        Folder folder1 = realm.where(Folder.class).equalTo("folderId", folder.getFolderId()).findFirst();
//        FolderModel model = new FolderModel();
//        model.
//        RealmResults<Memo> memos = folder1.
//
//        ArrayList<Memo> memoList = new ArrayList<>();
//        for (Memo memo : memos) {
//            memoList.add(memo);
//        }
//
//        return memoList;
//
//    }

    public ArrayList<Memo> getMemosInSameFolder(int folderId) {
        RealmResults<Memo> memos = realm.where(Memo.class).equalTo("idOfFolder", folderId).findAll();

        ArrayList<Memo> memoList = new ArrayList<>();
        for (Memo memo : memos) {
            memoList.add(memo);
        }

        return memoList;
    }

    //primary key인 id를 통해 Memo 데이터 받아오기
    public Memo getMemoById(int memoId) {
        //Realm 내의 Memo 데이터 중 해당 id의 데이터만을 가져오기
        return realm.where(Memo.class).equalTo("memoId", memoId).findFirst();
    }

    public void editMemo(Memo memo) {
        //Memo 데이터 수정 메소드
        //Realm 내의 Memo 데이터 중 하나를 Update하는 코드
        realm.beginTransaction();

        //realm 이 변경되는 코드
        realm.copyToRealmOrUpdate(memo);
        realm.commitTransaction();
    }

    //Memo 객체를 전달받아 copyToRealm 메소드 이용해서 Realm에 저장
    public void addMemo(Memo memo) {
        //Realm 에 Memo 데이터를 삽입
        realm.beginTransaction();
        realm.copyToRealm(memo);
        realm.commitTransaction();
    }

    //id에 해당하는 Memo 모델 삭제
    public void deleteMemo(int memoId) {
        //Realm 내의 Memo 데이터 중 해당 id의 데이터 제거
        realm.beginTransaction();

        if(realm.where(Memo.class).equalTo("memoId", memoId).findFirst().isValid())
            realm.where(Memo.class).equalTo("memoId", memoId).findFirst().removeFromRealm();

        realm.commitTransaction();
    }

    //id생성 메소드
    //Realm에서는 id를 AutoIncrement로 생성해주지 않아서 직접 정해줘야 함.
    //Realm에 저장된 Memo 데이터 중 가장 나중에 생성된 데이터의 id 값에 +1하는 방식으로 id생성
    public int getNewId() {
        int newId = 0;
        RealmResults<Memo> realmResults = realm.where(Memo.class).findAll();

        //Realm에 데이터가 하나도 없으면 realmResults에 데이터가 없어
        //realmResults 가 0보다 큰 경우에만 id생성, 작은 경우에는 0반환
        if (realmResults.size() > 0) {
            newId = realmResults.last().getMemoId() + 1;
        }
        return newId;
    }

    //MemoModel에서 인스턴스화했던 Realm 객체를 메모리에서 해제
    public void closeRealm() {
        realm.close();
    }

}
