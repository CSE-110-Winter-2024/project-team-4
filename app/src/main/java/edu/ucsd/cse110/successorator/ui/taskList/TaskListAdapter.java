package edu.ucsd.cse110.successorator.ui.taskList;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import android.content.Context;
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
        binding.taskNameText.setText(task.name());

//
//        binding.cardDeleteButton.setOnClickListener(v -> {
//            var id = flashcard.id();
//            assert id != null;
//            onDeleteClick.accept(id);
//        });

        if(task.complete()){
            binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if(!task.complete()){
            binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        binding.taskNameText.setOnClickListener(v -> {
            if (!task.complete()) {
                task.setComplete(true);
                activityModel.setComplete(task.id(), true);
                activityModel.remove(task.id());
                activityModel.prepend(task);
                System.out.println("MARKED AS COMPLETE");
                // CITATION: https://www.codingdemos.com/android-strikethrough-text/
                binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            } else {
                task.setComplete(false);
                activityModel.setComplete(task.id(), false);
                activityModel.remove(task.id());
                activityModel.prepend(task);
                System.out.println("MARKED AS INCOMPLETE");
                binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
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

