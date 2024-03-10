package edu.ucsd.cse110.successorator.ui.taskList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class AddRecurringTaskDialogFragment extends DialogFragment {
    private edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding view;
    private MainViewModel activityModel;

    public AddRecurringTaskDialogFragment()
    {

    }

    public static AddRecurringTaskDialogFragment newInstance() {
        var fragment = new AddRecurringTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding.inflate(getLayoutInflater());
        String[] choices = {"one time", "daily", "weekly", "monthly", "yearly"};
//        final EditText input = (EditText) view.findViewById(R.id.input);

        return new AlertDialog.Builder(getActivity())
//                .setView(view.getRoot())
                .setView(view.)
                .setPositiveButton("Save", this::onPositiveButtonClick)
//                .setPositiveButtonIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_checkmark))

                .setSingleChoiceItems(choices, 0, (dialog, which) -> {
                })
                .create();

    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
        var name = view.taskInput.getText().toString();
        if(!name.equals(""))
        {
            Calendar cal = CalendarUpdate.getCal();
            SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
            String formattedDate = customFormat.format(cal.getTime());
            var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
            MainActivity mainActivity = (MainActivity) getActivity();
            var task = new Task(null, name, false, -1, formattedDate, mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1),cal.getTimeInMillis());
            activityModel.append(task);
            dialog.dismiss();

            ((MainActivity)getActivity()).swapFragments();

            MainViewModel model = ModelFetch.getModel();
            if(mainActivity.getSpinnerStatus().equals("Today")){
                model.getTodayTasks();
            }
            else if(mainActivity.getSpinnerStatus().equals("Tomorrow")){
                model.getTomorrowTasks();
            }

        }

    }


    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        ModelFetch.setModel(activityModel);

    }

}
