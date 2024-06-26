//package edu.ucsd.cse110.successorator.ui.taskList.dialog;
//
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Spinner;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.DialogFragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.room.Room;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.concurrent.TimeUnit;
//
//import edu.ucsd.cse110.successorator.MainActivity;
//import edu.ucsd.cse110.successorator.MainViewModel;
//import edu.ucsd.cse110.successorator.R;
//import edu.ucsd.cse110.successorator.data.db.TaskDatabase;
//import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
//import edu.ucsd.cse110.successorator.lib.domain.Task;
//import edu.ucsd.cse110.successorator.ui.taskList.TaskListFragment;
//
//public class AddTaskDialogFragment extends DialogFragment {
//
//    private edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding view;
//    private MainViewModel activityModel;
//
//    public AddTaskDialogFragment()
//    {
//
//    }
//
//    public static AddTaskDialogFragment newInstance() {
//        var fragment = new AddTaskDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        this.view = edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding.inflate(getLayoutInflater());
//
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("New Task")
//                .setMessage("Please provide the new task name.")
//                .setView(view.getRoot())
//                .setPositiveButton("Checkmark", this::onPositiveButtonClick)
////                .setPositiveButtonIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_checkmark))
//                .setNegativeButton("Cancel", this::onNegativeButtonClick)
//                .create();
//
//    }
//
//    private void onPositiveButtonClick(DialogInterface dialog, int which) {
//        Log.d("AddTaskDialogFragment", "This is a debug message");
//        var name = view.taskInput.getText().toString();
//        if(!name.equals(""))
//        {
//            Calendar cal = CalendarUpdate.getCal();
//            SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
//            String formattedDate = customFormat.format(cal.getTime());
//            var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
//            MainActivity mainActivity = (MainActivity) getActivity();
//            var task = new Task(null, name, false, -1, formattedDate, mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1),cal.getTimeInMillis());
//            activityModel.append(task);
//            dialog.dismiss();
//
//            ((MainActivity)getActivity()).swapFragments();
//
//            MainViewModel model = ModelFetch.getModel();
//            if(mainActivity.getSpinnerStatus().equals("Today")){
//                model.getTodayTasks();
//            }
//            else if(mainActivity.getSpinnerStatus().equals("Tomorrow")){
//                model.getTomorrowTasks();
//            }
//
//        }
//
//    }
//
//
//    private void onNegativeButtonClick(DialogInterface dialog, int which) {
//        dialog.cancel();
//    }
//
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);
//
//        ModelFetch.setModel(activityModel);
//
//    }
//
//
//
//
//}
