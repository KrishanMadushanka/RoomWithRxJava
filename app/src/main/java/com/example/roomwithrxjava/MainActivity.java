package com.example.roomwithrxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomwithrxjava.database.AppDatabase;
import com.example.roomwithrxjava.database.entity.Item;

import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.text);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insetItem();
            }
        });
    }

    private void insetItem() {
        Random rand = new Random();
        int i = rand.nextInt(1000);
        Item item = new Item();
        item.setName("Item  " + i);
        item.setColor("Color  " + i);
        disposable.add(AppDatabase.getAppDatabase(this).itemDao().insertAddon(item).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        Toast.makeText(MainActivity.this, "Added item with id: " + aLong, Toast.LENGTH_SHORT).show();
                        getItems();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                }));
    }

    private void getItems() {
        disposable.add(AppDatabase.getAppDatabase(this).itemDao().getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Item>>() {


                    @Override
                    public void onSuccess(@NonNull List<Item> items) {
                        String str = "";
                        for (Item itm : items) {
                            str = str + itm.getName() + " " + itm.getColor() + "\n";

                        }
                        tv.setText(str);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
//handle errors
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}