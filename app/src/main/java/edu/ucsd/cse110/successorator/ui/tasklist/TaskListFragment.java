//package edu.ucsd.cse110.successorator.ui.tasklist;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import edu.ucsd.cse110.successorator.MainViewModel;
//
//public class TaskListFragment {
//
//    private MainViewModel activityModel;
//    private FragmentTaskListBinding view;
//
//    public TaskListFragment() {
//        // Required empty public constructor
//    }
//
//    public static TaskListFragment newInstance() {
//        TaskListFragment fragment = new TaskListFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.view = FragmentCardListBinding.inflate(inflater, container, false);
//
//        // Set the adapter on the ListView
//        view.cardList.setAdapter(adapter);
//
//        view.createCardButton.setOnClickListener(v -> {
//            var dialogFragment = CreateCardDialogFragment.newInstance();
//            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
//        });
//
//        return view.getRoot();
//    }
//}
