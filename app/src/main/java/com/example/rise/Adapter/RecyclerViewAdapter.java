package com.example.rise.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rise.Create_Alarm;
import com.example.rise.MainActivity;
import com.example.rise.MyDataBase.DataBaseManager;
import com.example.rise.R;

import java.util.ArrayList;
import java.util.List;

// Реализуем адаптер и наследуемся от RecyclerView.Adapter, который параметризован нашим AlarmViewHolder
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AlarmViewHolder>{

    private Context context;
    private List<ListItem> list;

    // Констурктор класса RecyclerViewAdapter
    // Передаём в него context
    public RecyclerViewAdapter(Context context){
        this.context = context;
        // Создаём наш ArrayList, в котором будут хранится значения элементов класса ListItem
        list = new ArrayList<>();
    }

    // Метод, создающий новые viewHolder (новые элементы списка)
    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.element_of_recyclerview, parent, false);
        // Передаём AlarmViewHolder view, context и массив list
        return new AlarmViewHolder(view, context, list);
    }

    // Метод, который использует новые viewHolder и обновляет в них информацию
    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        holder.setAlarmInform(list.get(position).getTime(), list.get(position).getTitle(), list.get(position).getDescription());
    }

    // Метод, возвращающий кол-во элементов в списке
    @Override
    public int getItemCount() {
        return list.size();
    }



    // Вложенный класс, который оборачивает в себя элемент списка
    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView rv_time;
        private TextView rv_title;
        private TextView rv_desc;
        private Context context;
        private List<ListItem> list;

        // Конструктор
        // Находит в element_of_recyclerview (layout-файле) интересующие нас компоненты
        public AlarmViewHolder(View itemView, Context context, List<ListItem> list) {
            super(itemView);

            this.context = context;
            this.list = list;
            rv_time = itemView.findViewById(R.id.rv_time);
            rv_title = itemView.findViewById(R.id.rv_title);
            rv_desc = itemView.findViewById(R.id.rv_desc);
            itemView.setOnClickListener(this);
        }

        // Метод, совершающий замену значений элементов
        public void setAlarmInform(String time, String title, String description){
            rv_time.setText(time);
            rv_title.setText(title);
            rv_desc.setText(description);
        }

        @Override
        public void onClick(View v) {
            // Создаем intent, чтобы передать Create_Alarm данные нажатого будильника
            Intent intent = new Intent(context, Create_Alarm.class);
            // Отправляем данные активности Create_Alarm
            intent.putExtra("LIST_RV_INTENT", list.get(getAdapterPosition()));
            intent.putExtra("EDIT_STATE", false);
            context.startActivity(intent);
        }
    }

    // Метод, обновляющий адаптер (запускаем в onResume)
    public void updateAdapter(List<ListItem> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void deleteItem(int position, DataBaseManager dataBaseManager){
        // Из БД удаляем элемент по позиции элемента, получая id
        dataBaseManager.deleteFromDb(list.get(position).getId());
        // Удалем из списка элемнт по его позиции
        list.remove(position);
        // Сообщаем адаптеру, что у нас удалились элементы и список изменился
        notifyItemChanged(0, list.size());
        notifyItemRemoved(position);
        Toast.makeText(context, "Будильник удалён!", Toast.LENGTH_SHORT).show();
    }

    // Это для фрагмента
    public int ListSize(){
        return list.size();
    }
}
