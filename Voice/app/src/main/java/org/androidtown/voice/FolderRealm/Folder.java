package org.androidtown.voice.FolderRealm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Folder extends RealmObject {
    //RealmObject 구현
    @PrimaryKey
    private int folderId;

    private String foldername;
    private int elementNum;

    @Ignore
    private boolean isSelected;

    public Folder() {
        // 기본 생성자
    }

    public Folder(int folderId, String foldername, int elementNum) {
        this.folderId = folderId;
        this.foldername = foldername;
        this.elementNum = elementNum;
    }

    public Folder(int folderId, String foldername, int elementNum, boolean isSelected) {
        this.folderId = folderId;
        this.foldername = foldername;
        this.elementNum = elementNum;
        this.isSelected = isSelected;
    }

    //getter, setter함수
    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public int getElementNum() {
        return elementNum;
    }

    public void setElementNum(int elementNum) {
        this.elementNum = elementNum;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
