package org.fireking.uugame.uis.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fireking.uudownload.DownloadRequest;
import org.fireking.uudownload.UUDownloadManager;
import org.fireking.uugame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllGameFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllGameAdapter mAllGameAdapter;
    private List<DownloadRequest> requestList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //错误的请求地址
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w222.apk").setStoreFileName("任务1"));

        //高清壁纸
        requestList.add(new DownloadRequest("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546850344680&di=f50b2b9f76b79f1fd1f74531230337c6&imgtype=0&src=http%3A%2F%2Fs16.sinaimg.cn%2Forignal%2F3ea244b9g79e6249ed12f%26690").setStoreFileName("高清壁纸"));

        //正常请求地址
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w1.apk").setStoreFileName("任务2"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w2.apk").setStoreFileName("任务3"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w3.apk").setStoreFileName("任务4"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w4.apk").setStoreFileName("任务5"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w5.apk").setStoreFileName("任务6"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w6.apk").setStoreFileName("任务7"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w7.apk").setStoreFileName("任务8"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w8.apk").setStoreFileName("任务9"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w9.apk").setStoreFileName("任务10"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w10.apk").setStoreFileName("任务11"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w11.apk").setStoreFileName("任务12"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w12.apk").setStoreFileName("任务13"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w13.apk").setStoreFileName("任务14"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w14.apk").setStoreFileName("任务15"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w15.apk").setStoreFileName("任务16"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w16.apk").setStoreFileName("任务17"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w17.apk").setStoreFileName("任务18"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w18.apk").setStoreFileName("任务19"));
        requestList.add(new DownloadRequest("http://101.34.101.222:8080/download/w19.apk").setStoreFileName("任务20"));

        //做一个数据同步
        UUDownloadManager.getInstance().dataSynchronization(requestList);

        mAllGameAdapter = new AllGameAdapter(getActivity(), requestList);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAllGameAdapter);
    }
}
