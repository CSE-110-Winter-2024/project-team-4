package edu.ucsd.cse110.successorator.ui.noTasksList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.successorator.databinding.FragmentNoTasksBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.ui.taskList.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.taskList.dialog.AddTaskDialogFragment;

public class NoTasksFragment extends Fragment{
    private FragmentNoTasksBinding view;

    public NoTasksFragment() {

    }

    public static NoTasksFragment newInstance() {
        NoTasksFragment fragment = new NoTasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
            ) {
        this.view = FragmentNoTasksBinding.inflate(inflater, container, false);

        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = AddTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "AddTaskDialogFragment");
        });

        return view.getRoot();
    }


}

