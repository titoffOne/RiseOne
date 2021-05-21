package com.example.rise;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.rise.Adapter.RecyclerViewAdapter;
import com.example.rise.AlarmSettings.MyAlarm;
import com.example.rise.MyDataBase.ConstantsDB;
import com.example.rise.MyDataBase.DataBaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton fab_add;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DataBaseManager dataBaseManager;
    private TextView pb_tv_desc;
    private TextView pb_tv_time;
    private MyAlarm myAlarm;
    private CircularProgressBar circularProgressBar;


    @Override
    protected void onResume() {
        super.onResume();
        // Открываем БД
        dataBaseManager.openDb();
        // Обновляем адаптер
        recyclerViewAdapter.updateAdapter(dataBaseManager.getInformAlarmFromDb());

        setFragmentInform();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Находим элементы по Id
        initById();
        // RecyclerView
        initRecyclerView();
        setFragmentInform();
        // Передаём context БД
        dataBaseManager = new DataBaseManager(this);

        // Слушатель на кнопку
        fab_add.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем БД
        dataBaseManager.closeDb();
    }

    // Инициализация элементов активности по id
    private void initById() {
        // MainActivity
        fab_add = findViewById(R.id.fab_add);
        recyclerView = findViewById(R.id.recyclerView);
        // ProgressBarFragment
        pb_tv_desc = findViewById(R.id.pb_tv_desc);
        pb_tv_time = findViewById(R.id.pb_tv_time);
        circularProgressBar = findViewById(R.id.circularProgressBar);

    }

    // Инциализация RecyclerView
    private void initRecyclerView() {
        // Используем LinearLayoutManager, чтобы элементы отображались в виде списка (последовательно)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Передаём layoutManager в recyclerView
        recyclerView.setLayoutManager(layoutManager);

        // Создаём адаптер для recyclerView
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        // Подключаем к recyclerView возможность свайпа
        getItemTouchHelper().attachToRecyclerView(recyclerView);
        // Передаём recyclerView адаптер
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // Создаём слушатель на кнопку
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                Intent intent = new Intent(MainActivity.this, Create_Alarm.class);
                startActivityForResult(intent, 1);

                // При каждом нажатии на кнопку адаптер обновляется, получая данные из БД
                recyclerViewAdapter.updateAdapter(dataBaseManager.getInformAlarmFromDb());

                setFragmentInform();
                break;
        }
    }


    // Проверяем создан будильник или нет
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Toast.makeText(this, "Теперь вы не упустите самое важное!",
                        Toast.LENGTH_SHORT).show();
                myAlarm = new MyAlarm(this, dataBaseManager.getHour(), dataBaseManager.getMinute());
                myAlarm.createAlarm();
            }
        } else {
            Toast.makeText(this, "Упс! Будильник не был создан!", Toast.LENGTH_LONG).show();
        }
    }

    private void sss(){
        MyAlarm myAlarm = new MyAlarm(this, dataBaseManager.getHour(), dataBaseManager.getMinute());
    }


    // Класс, с помощью которого можно будет свайпнуть элемент recyclerView и удалить его из списка
    private ItemTouchHelper getItemTouchHelper(){
        // Возвращаем ItemTouchHelper. В SimpleCallback задаём перемещение элементов: 0, свайп: влево и вправо
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                recyclerViewAdapter.deleteItem(viewHolder.getAdapterPosition(), dataBaseManager);
                setFragmentInform();
            }
        });
    }

    public void setFragmentInform(){
        pb_tv_time.setText(ConstantsDB.PB_TIME);

        if (pb_tv_time.getText() != "--:--"){
            pb_tv_time.setText(myAlarm.informForFragmentString());
            pb_tv_desc.setText("Сработает через");
            circularProgressBar.setProgress(myAlarm.informForFragmentInt());
        }
        if (recyclerViewAdapter.ListSize() == 0){
            pb_tv_time.setText("--:--");
            pb_tv_desc.setText("Нет будильников!");
            circularProgressBar.setProgress(0);
        }



    }


}