package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding;

public class MainActivity extends AppCompatActivity {

    private Date date;
    private Calendar cal;

    private FragmentDialogCreateCardBinding view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        //view.placeholderText.setText(R.string.hello_world);
        //setContentView(view.getRoot());

        setContentView(R.layout.activity_main);
        cal = Calendar.getInstance();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());

        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(dateFormat.toString());

    }

//    @Nullable
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.view = .inflate(inflater, container, false);
//
//        view.addTaskButton.setOnClickListener(v -> {
//            var dialogFragment = AddTaskDialogFragment.newInstance();
//            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
//        });
//
//        return view.getRoot();
//    }

    public void incrementCurrentDate(View view){
        cal.add(cal.DATE, 1);
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(dateFormat.toString());
    }


}
