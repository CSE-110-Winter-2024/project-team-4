package edu.ucsd.cse110.successorator.ui.taskList;

import android.app.AlertDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.ListTaskItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;


public class TaskListAdapter extends ArrayAdapter<Task> {
    Consumer<Integer> onDeleteClick;
    MainViewModel activityModel;


    public TaskListAdapter(Context context, List<Task> tasks, Consumer<Integer> onDeleteClick, MainViewModel activityModel) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(tasks));
        this.onDeleteClick = onDeleteClick;
        this.activityModel = activityModel;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the flashcard for this position.
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        ListTaskItemBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListTaskItemBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListTaskItemBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the flashcard's data.
//        System.out.println("setting text on binding");
//        if (task.recurringInterval() >= 0) {
//            binding.taskNameText.setText(task.name());
//        } else {
//            binding.taskNameText.setText(task.name());
//        }
        binding.taskNameText.setText(task.name());
        binding.conText.setText(task.context());
//        System.out.println("TaskListAdapter getView");
//        System.out.println("task.context: " + task.context());

        switch(task.context()){
            case "H":
                binding.conText.setBackgroundResource(R.drawable.homecircle);
                break;
            case "W":
                binding.conText.setBackgroundResource(R.drawable.workcircle);
                break;
            case "S":
                binding.conText.setBackgroundResource(R.drawable.schoolcircle);
                break;
            case "E":
                binding.conText.setBackgroundResource(R.drawable.errandcircle);
                break;
        }


        if(task.complete()){
            binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.conText.setBackgroundResource(R.drawable.circle);
        }

        if(!task.complete()){
            binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            switch(task.context()){
                case "H":
                    binding.conText.setBackgroundResource(R.drawable.homecircle);
                    break;
                case "W":
                    binding.conText.setBackgroundResource(R.drawable.workcircle);
                    break;
                case "S":
                    binding.conText.setBackgroundResource(R.drawable.schoolcircle);
                    break;
                case "E":
                    binding.conText.setBackgroundResource(R.drawable.errandcircle);
                    break;
            }
        }


        MainActivity mainActivity = (MainActivity) getContext();
        String spinnerStatus = mainActivity.getSpinnerStatus();
        binding.taskNameText.setOnClickListener(v -> {
            if (!spinnerStatus.equals("Recurring") && !spinnerStatus.equals("Pending")) {
                if (!task.complete()) {
                    task.setComplete(true);
                    activityModel.setComplete(task.id(), true);
                    activityModel.remove(task.id());
                    System.out.println("TaskListAdapter task.id(): " + task.id());
                    activityModel.prepend(task);
                    System.out.println("TaskListAdapter after prepend task.id(): " + task.id());
                    activityModel.taskRepository().setTaskCompletedDate(task.id(), task);



                    System.out.println("MARKED AS COMPLETE");
                    // CITATION: https://www.codingdemos.com/android-strikethrough-text/
                    binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


//                MainActivity mainActivity = (MainActivity) getContext();
//                String spinnerStatus = mainActivity.getSpinnerStatus();
//                if(spinnerStatus.equals("Today")){
//                    activityModel.getTodayTasks();
//                }
//                else if (spinnerStatus.equals("Tomorrow")){
//                    activityModel.getTomorrowTasks();
//                }
//                else if (spinnerStatus.equals("Recurring")){
//                    activityModel.getRecurringTasks();
//                }

                } else {
                    task.setComplete(false);
                    activityModel.setComplete(task.id(), false);
                    activityModel.remove(task.id());
                    activityModel.prepend(task);

                    activityModel.taskRepository().setTaskCompletedDate(task.id(), null);
                    System.out.println("MARKED AS INCOMPLETE");
                    binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

//                MainActivity mainActivity = (MainActivity) getContext();
//                String spinnerStatus = mainActivity.getSpinnerStatus();
//                if(spinnerStatus.equals("Today")){
//                    activityModel.getTodayTasks();
//                }
//                else if (spinnerStatus.equals("Tomorrow")){
//                    activityModel.getTomorrowTasks();
//                }
//                else if (spinnerStatus.equals("Recurring")){
//                    activityModel.getRecurringTasks();
//                }


                    binding.conText.setBackgroundResource(R.drawable.circle);

                
                    switch(task.context()){
                        case "H":
                            binding.conText.setBackgroundResource(R.drawable.homecircle);
                            break;
                        case "W":
                            binding.conText.setBackgroundResource(R.drawable.workcircle);
                            break;
                        case "S":
                            binding.conText.setBackgroundResource(R.drawable.schoolcircle);
                            break;
                        case "E":
                            binding.conText.setBackgroundResource(R.drawable.errandcircle);
                            break;
                    }
                }
            }
        });

        // Pending long click menu listener
        binding.taskNameText.setOnLongClickListener(new View.OnLongClickListener() {
            MainActivity mainActivity = (MainActivity) getContext();
            String spinnerStatus = mainActivity.getSpinnerStatus();
            @Override
            public boolean onLongClick(View v) {
                System.out.println("SPINNERSTATuS " + spinnerStatus);
                if(spinnerStatus.equals("Pending")){
                    showOptionsDialog(task);
                }
                return true;
            }

            private void showOptionsDialog(Task task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setItems(new CharSequence[]{"Move to Today", "Move to Tomorrow", "Delete", "Finish"}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            activityModel.setType(task.id(), "Today");
                            task.setType("Today");
                            activityModel.remove(task.id());
                            activityModel.prepend(task);
                        }
                        else if(which == 1){
                            activityModel.setType(task.id(), "Tomorrow");
                            task.setType("Tomorrow");
                            activityModel.remove(task.id());
                            activityModel.prepend(task);
                        }
                        else if(which == 2){
                            activityModel.remove(task.id());
                        }
                        else if(which == 3){
                            activityModel.setType(task.id(), "Today");
                            task.setComplete(true);
                            task.setType("Today");
                            activityModel.setComplete(task.id(), true);
                            activityModel.remove(task.id());
                            activityModel.prepend(task);
                            System.out.println("MARKED AS COMPLETE");
                            binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                    }
                });
                builder.create().show();
            }
        });


//        MainActivity mainActivity = (MainActivity) getContext();
//        String spinnerStatus = mainActivity.getSpinnerStatus();
//        if(spinnerStatus.equals("Today")){
//            activityModel.getTodayTasks();
//        }
//        else if (spinnerStatus.equals("Tomorrow")){
//            activityModel.getTomorrowTasks();
//        }
//        else if (spinnerStatus.equals("Recurring")){
//            activityModel.getRecurringTasks();
//        }
        // Delete recurring task with long click on recurring view
        binding.taskNameText.setOnLongClickListener(new View.OnLongClickListener() {
            MainActivity mainActivity = (MainActivity) getContext();
            String spinnerStatus = mainActivity.getSpinnerStatus();
            @Override
            public boolean onLongClick(View v) {
                if(spinnerStatus.equals("Recurring")){
                    showOptionsDialog(task);
                }
                return true;
            }

            private void showOptionsDialog(Task task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setItems(new CharSequence[]{"Delete"}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            activityModel.remove(task.id());
                        }

                    }
                });
                builder.create().show();
            }
        });

        return binding.getRoot();
    }




    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.id();
        assert id != null;

        return id;
    }
}

