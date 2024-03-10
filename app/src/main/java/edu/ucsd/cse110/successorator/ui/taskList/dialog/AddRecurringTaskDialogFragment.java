package edu.ucsd.cse110.successorator.ui.taskList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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
    private edu.ucsd.cse110.successorator.databinding.FragmentDialogRecurringMenuBinding view;
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
        this.view = edu.ucsd.cse110.successorator.databinding.FragmentDialogRecurringMenuBinding.inflate(getLayoutInflater());
        String[] choices = {"one time", "daily", "weekly", "monthly", "yearly"};

        Button dialogSaveButton = (Button) view.saveButton;
        Dialog recurDialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
                var name = view.recurringInput.getText().toString();

                // Retrieving dialog's selection for recurrence
                int checkedRadio = view.recurrenceOptions.getCheckedRadioButtonId();
                String recurrenceText = "";
                if (checkedRadio != -1) {
                    View selectedRecurrence = view.recurrenceOptions.findViewById(checkedRadio);

                    int radioIndex = view.recurrenceOptions.indexOfChild(selectedRecurrence);
                    RadioButton recurrence = (RadioButton) view.recurrenceOptions.getChildAt(radioIndex);
                    recurrenceText = recurrence.getText().toString();
                    System.out.println("recurrenceText: " + recurrenceText);
                }

                // Check that the required input string and radio selection are valid
                if(!name.equals("") && !recurrenceText.equals(""))
                {
                    Calendar cal = CalendarUpdate.getCal();
                    SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
                    String formattedDate = customFormat.format(cal.getTime());
                    var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
                    MainActivity mainActivity = (MainActivity) getActivity();
                    var task = new Task(null, name, false, -1, formattedDate, mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1),cal.getTimeInMillis());
                    activityModel.append(task);
                    recurDialog.dismiss();

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
        });

        return recurDialog;

    }


    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
        var name = view.recurringInput.getText().toString();

        // Retrieving dialog's selection for recurrence
        View selectedRecurrence = view.recurrenceOptions.findViewById(view.recurrenceOptions.getCheckedRadioButtonId());
        int radioIndex = view.recurrenceOptions.indexOfChild(selectedRecurrence);
        RadioButton recurrence = (RadioButton) view.recurrenceOptions.getChildAt(radioIndex);
        String recurrenceText = recurrence.getText().toString();
        System.out.println("recurrenceText: " + recurrenceText);

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
