package org.androidtown.voice.MemoRealm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sansung on 2016-07-26.
 */
public class Memo extends RealmObject {
    //RealmObject 구현
    @PrimaryKey
    private int memoId;

    //외래키
    private int idOfFolder;

    private String memoName;
    private String memoContents;
    private String memoday;
    private String memoTime;
    @Ignore
    private boolean isSelected;

    //기본생성자
    public Memo() {
    }

    public Memo(int memoId, String memoName, String memoContents) {
        this.memoId = memoId;
        this.memoName = memoName;
        this.memoContents = memoContents;
    }

    public Memo(int memoId, String memoName, String memoContents, boolean isSelected) {
        this.memoId = memoId;
        this.memoName = memoName;
        this.memoContents = memoContents;
        this.isSelected = isSelected;
    }

    public Memo(int memoId, String memoName, String memoContents, String memoday) {
        this.memoId = memoId;
        this.memoName = memoName;
        this.memoContents = memoContents;
        this.memoday = memoday;
    }

    public Memo(int memoId, String memoName, String memoContents, int idOfFolder, String memoday, String memoTime) {
        this.memoId = memoId;
        this.memoName = memoName;
        this.memoContents = memoContents;
        this.idOfFolder = idOfFolder;
        this.memoday = memoday;
        this.memoTime = memoTime;
    }

    public Memo(int memoId, String memoName, String memoContents, String memoday, boolean isSelected) {
        this.memoId = memoId;
        this.memoName = memoName;
        this.memoContents = memoContents;
        this.memoday = memoday;
        this.isSelected = isSelected;
    }

    public Memo(int memoId, int idOfFolder, String memoName, String memoday, String memoContents, boolean isSelected) {
        this.memoId = memoId;
        this.idOfFolder = idOfFolder;
        this.memoName = memoName;
        this.memoday = memoday;
        this.memoContents = memoContents;
        this.isSelected = isSelected;
    }

    //getter, setter 함수


    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getIdOfFolder() {
        return idOfFolder;
    }

    public void setIdOfFolder(int idOfFolder) {
        this.idOfFolder = idOfFolder;
    }

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }

    public String getMemoContents() {
        return memoContents;
    }

    public void setMemoContents(String memoContents) {
        this.memoContents = memoContents;
    }

    public String getMemoday() {
        return memoday;
    }

    public void setMemoday(String memoday) {
        this.memoday = memoday;
    }

    public String getMemoTime() {
        return memoTime;
    }

    public void setMemoTime(String memoTime) {
        this.memoTime = memoTime;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
