package edu.ucsd.cse110.successorator.ui.taskList;



import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.ui.taskList.dialog.AddTaskDialogFragment;

public class TaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel::remove, activityModel);



        // Initialize the Adapter (with an empty list for now)
        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.taskList.setAdapter(adapter);

        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = AddTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "AddTaskDialogFragment");
        });

//        view.taskNameText.setOnClickListener(v -> {
//            if (task.complete()) {
//                task.setComplete(false);
//                System.out.println("MARKED AS COMPLETE");
//                // CITATION: https://www.codingdemos.com/android-strikethrough-text/
//                binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//
//            } else {
//                task.setComplete(true);
//                System.out.println("MARKED AS INCOMPLETE");
//                binding.taskNameText.setPaintFlags(binding.taskNameText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//            }
//        });

        return view.getRoot();
    }

}

