package org.androidtown.voice.FolderRealm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.voice.R;

import java.util.ArrayList;

/**
 * Created by IT on 2016-07-28.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    ArrayList<Folder> folderlist;
    LayoutInflater layoutInflater;
    ArrayList<Boolean> isCheckedFolder;


    boolean check;

    public FolderAdapter(Context ctx) {
        //Context를 받아 LayoutInflater를 초기화하는 어뎁터 생성자 구현
        layoutInflater = LayoutInflater.from(ctx);
    }

    // 생성자
    public FolderAdapter(Context ctx, boolean check) {
        layoutInflater = LayoutInflater.from(ctx);
        this.check = check;
    }

    //새로운 뷰 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);

        this.isCheckedFolder = new ArrayList<>();

        for (int i = 0; i < folderlist.size(); i++) {
            isCheckedFolder.add(i, false);
            Log.i("MyTag", "isCheckedConfirm[i]값 : " + i + isCheckedFolder.get(i));
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FolderAdapter.ViewHolder holder, final int position) {
        //해당 position의 Memo 데이터 받아오기
        Folder folder = folderlist.get(position);
        final int num = position;
        holder.folderName.setText(folder.getFoldername());
        holder.elementNum.setText(folder.getElementNum() + "files");

        if(check) {
            holder.checkbox.setVisibility(holder.checkbox.VISIBLE);


            holder.checkbox.setClickable(false);
            holder.checkbox.setFocusable(false);

            holder.checkbox.setChecked(isCheckedFolder.get(position));
        }
        else
            holder.checkbox.setVisibility(holder.checkbox.GONE);


        holder.folderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyTag", "권현아 문제있다." + folderlist.get(num).getIsSelected());
                if(folderlist.get(num).getIsSelected()) {
                    holder.folderName.setBackgroundColor(Color.parseColor("#ffffff"));
                    folderlist.get(num).setIsSelected(false);
                } else {
                    holder.folderName.setBackgroundColor(Color.parseColor("#d5d5d5"));
                    folderlist.get(num).setIsSelected(true);
                }

            }
        });

       // if(folderlist.get(position).getIsSelected())

    }


    public void setFolder(ArrayList<Folder> list) {
        //어뎁터에 모델 설정
        folderlist = list;
    }

    public void setChecked(int position) {
        isCheckedFolder.set(position, !isCheckedFolder.get(position));
    }

    public ArrayList<Folder> getChecked() {
        int tempSize = isCheckedFolder.size();
        ArrayList<Folder> mCheckedArray = new ArrayList<>();

        for (int b = 0; b < tempSize; b++) {
            if (isCheckedFolder.get(b)) {
                mCheckedArray.add(folderlist.get(b));
                //Log.i("MyTag", "체크 배열 : " + mCheckedArray.get(b));
            }
        }
        for (int i = 0; i < mCheckedArray.size(); i++)
            Log.i("MyTag", "mCheckedArray" + mCheckedArray.get(i));
        return mCheckedArray;
    }


    @Override
    public int getItemCount() {
        return (folderlist != null) ? folderlist.size() : 0;
    }

    public Object getItem(int position) {
        return (folderlist != null && (0 <= position && position < folderlist.size()) ?
                folderlist.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return (folderlist != null && (0 <= position && position < folderlist.size()) ?
                folderlist.get(position).getFolderId() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        TextView elementNum;
        LinearLayout folderLayout;
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);

            //TextView 초기화
            folderName = (TextView) itemView.findViewById(R.id.folder_name);
            elementNum = (TextView) itemView.findViewById(R.id.element_num);
            checkbox = (CheckBox)itemView.findViewById(R.id.check);
            folderLayout = (LinearLayout) itemView.findViewById(R.id.folderLayout);
        }
    }
}
