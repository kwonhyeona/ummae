package org.androidtown.voice.MemoRealm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.androidtown.voice.R;

import java.util.ArrayList;

/**
 * Created by Sansung on 2016-07-21.
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    ArrayList<Memo> memoList;
    LayoutInflater layoutInflater;
    boolean check;

    ArrayList<Boolean> isCheckedConfirm;


    public MemoAdapter(Context ctx) {
        //Context를 받아 LayoutInflater를 초기화하는 어뎁터 생성자 구현
        layoutInflater = LayoutInflater.from(ctx);
    }

    // 생성자
    public MemoAdapter(Context ctx, boolean check) {
        layoutInflater = LayoutInflater.from(ctx);
        this.check = check;
    }

    // 생성자
    public MemoAdapter(Context ctx, boolean check, ArrayList<Memo> mList) {
        layoutInflater = LayoutInflater.from(ctx);
        this.check = check;
        this.memoList = mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView memoName;
        TextView memoContents;
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);

            //TextView 초기화
            memoName = (TextView) itemView.findViewById(R.id.nameTextView);
            memoContents = (TextView) itemView.findViewById(R.id.contentsTextView);
            checkbox = (CheckBox) itemView.findViewById(R.id.check);
        }
    }


    //새로운 뷰 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("gilsoo_Memo", "onCreateViewHolder()");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);

        this.isCheckedConfirm = new ArrayList<>();

        for (int i = 0; i < memoList.size(); i++) {
            isCheckedConfirm.add(i, false);
            Log.i("MyTag", "isCheckedConfirm[i]값 : " + i + isCheckedConfirm.get(i));
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MemoAdapter.ViewHolder holder, int position) {
        Log.d("gilsoo_Memo", "onBindViewHolder()");

        //해당 position의 Memo 데이터 받아오기
        Memo memo = memoList.get(position);

        if (memo.isValid()) {
            holder.memoName.setText(memo.getMemoName());
            holder.memoContents.setText(memo.getMemoContents());
        }

        if (check) {    // deleteMemoActivity인 경우
            holder.checkbox.setVisibility(holder.checkbox.VISIBLE);

            // CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
            // 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
            holder.checkbox.setClickable(false);
            holder.checkbox.setFocusable(false);

            holder.checkbox.setChecked(isCheckedConfirm.get(position));

            Log.i("MyTag", "isCheckedConfirm + " + position + " " + isCheckedConfirm.get(position));
        } else
            holder.checkbox.setVisibility(holder.checkbox.GONE);
    }


    public void setMemo(ArrayList<Memo> list) {
        //어뎁터에 모델 설정
        memoList = list;
    }

    public void setChecked(int position) {
        isCheckedConfirm.set(position, !isCheckedConfirm.get(position));
    }

    public ArrayList<Memo> getChecked() {
        int tempSize = isCheckedConfirm.size();
        ArrayList<Memo> mCheckedArray = new ArrayList<>();

        for (int b = 0; b < tempSize; b++) {
            if (isCheckedConfirm.get(b)) {
                mCheckedArray.add(memoList.get(b));
                //Log.i("MyTag", "체크 배열 : " + mCheckedArray.get(b));
            }
        }
        for (int i = 0; i < mCheckedArray.size(); i++)
            Log.i("MyTag", "mCheckedArray" + mCheckedArray.get(i));
        return mCheckedArray;
    }

    @Override
    public int getItemCount() {
        return (memoList != null) ? memoList.size() : 0;
    }

    public Object getItem(int position) {
        return (memoList != null && (0 <= position && position < memoList.size()) ?
                memoList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return (memoList != null && (0 <= position && position < memoList.size()) ?
                memoList.get(position).getMemoId() : 0);
    }

    //ViewHolder를 이용해서 리사이클러뷰에 아이템을 뿌려주는 메소드
    //아이템이 화면 상에 다시 나타날 때마다 실행됨
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //아이템에 사용되는 뷰들(TextView 2개)를 담아두기 위해 사용되는 ViewHolder
//        MemoViewHolder viewHolder = new MemoViewHolder();
//
//        //convertView는 리싸이클러뷰에 보여지는 하나의 아이템!
//        //convertView == null 이면 한번도 화면 상에 보여진 적이 없다는 것
//        //convertView 가 한번이라도 화면 상에 나타났다면 값이 null이 아니니까 else 문으로!
//        if (convertView == null) {
//            //layoutInflater를 이용해 item_memo.xml 레이아웃을 뷰로 바꾸어 convertView를 초기화
//            convertView = layoutInflater.inflate(R.layout.item_memo, parent, false);
//
//
//
//            //viewHolder를 convertView에 태그로 달기
//            convertView.setTag(viewHolder);
//        } else {
//            //convertView에 태그로 달아두었던 ViewHolder 다시 받아오기
//            //setTag() 메소드는 Object객체 반환하기 때문에 MemoList로 캐스팅
//
//            viewHolder = (MemoViewHolder) convertView.getTag();
//
//        }
//
//        //해당 position의 Memo 데이터 받아오기
//        Memo memo = memoList.get(position);
//
//        viewHolder.memoName.setText(memo.getMemoName());
//        viewHolder.memoContents.setText(memo.getMemoContents());
//
//
//
//        return convertView;
//    }

}
