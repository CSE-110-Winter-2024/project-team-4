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

        view.recurrenceOptions.setVisibility(View.INVISIBLE);
        view.recurrenceField.setVisibility(View.GONE);
        MainActivity mainActivity = (MainActivity) getActivity();
        System.out.println("SPINNER STATUS: " + mainActivity.getSpinnerStatus());
        switch(mainActivity.getSpinnerStatus()) {
            case "Pending":
                view.recurrenceOptions.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                break;
            case "Recurring":
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setVisibility(View.VISIBLE);
                view.oneTime.setVisibility(View.GONE);
                view.starting.setVisibility(View.VISIBLE);
                break;
            default:
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                view.oneTime.setVisibility(View.VISIBLE);
        }

        Button dialogSaveButton = (Button) view.saveButton;
        Dialog recurDialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
                var name = view.recurringInput.getText().toString();
                String recurrenceText = "";
                if (!mainActivity.getSpinnerStatus().equals("Pending")) {
                    // Retrieving dialog's selection for recurrence
                    int checkedRadio = view.recurrenceOptions.getCheckedRadioButtonId();

                    if (checkedRadio != -1) {
                        View selectedRecurrence = view.recurrenceOptions.findViewById(checkedRadio);

                        int radioIndex = view.recurrenceOptions.indexOfChild(selectedRecurrence);
                        RadioButton recurrence = (RadioButton) view.recurrenceOptions.getChildAt(radioIndex);
                        recurrenceText = recurrence.getText().toString();
                        System.out.println("recurrenceText: " + recurrenceText);
                    }
                }
                // Check that the required input string and radio selection are valid
                if(!name.equals("") && (!recurrenceText.equals("") || mainActivity.getSpinnerStatus().equals("Pending")))
                {
                    Calendar cal = CalendarUpdate.getCal();
                    SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
                    String formattedDate = customFormat.format(cal.getTime());
                    var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
//                    MainActivity mainActivity = (MainActivity) getActivity();

                    var task = new Task(null, name, false, -1, formattedDate,
                            mainActivity.getSpinnerStatus(), 0, cal,
                            true, null, true, null);


                    if (!mainActivity.getSpinnerStatus().equals("Pending")){
                        Calendar nextTaskDate = (Calendar) task.startDate().clone();
                        switch (recurrenceText) {
                            case "one-time":
                                task.setRecurringInterval(-1);
                                task.setNextDate(nextTaskDate);
                                break;
                            case "daily":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(1));
                                nextTaskDate.add(Calendar.DATE, 1);
                                task.setNextDate(nextTaskDate);

                                break;
                            case "weekly":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(7));
                                nextTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
                                task.setNextDate(nextTaskDate);
                                break;
                            case "monthly":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(30));
                                nextTaskDate.add(Calendar.WEEK_OF_YEAR, 4); // TODO: FIX
                                task.setNextDate(nextTaskDate);
                                break;
                            case "yearly":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(365));
                                nextTaskDate.add(Calendar.YEAR, 1);
                                task.setNextDate(nextTaskDate);
                                break;
                            default:
                                System.out.println("Unknown recurrence.");
                        }

                    }

//                    ISSUE HERE, task id is null when calling append
                    System.out.println("AddRecTaskDialogFrag onClick task = " + task);
                    System.out.println("task.id: " + task.id() + ". task.name: " + task.name());

                    activityModel.append(task);
                    recurDialog.dismiss();

                    if (recurrenceText.equals("daily")) {
                        Calendar tomorrowDate = (Calendar) cal.clone();
                        tomorrowDate.add(Calendar.DATE, 1);
                        String formattedDate2 = customFormat.format(tomorrowDate.getTime());

                        var task2 = new Task(null, name, false, -1, formattedDate2,
                                mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1), tomorrowDate,
                                false, null, false, null);

                        Calendar nextTaskDate2 = (Calendar) task2.startDate().clone();
                        nextTaskDate2.add(Calendar.DATE, 1);
                        task2.setNextDate(nextTaskDate2);
                        activityModel.append(task2);
                    }

                    ((MainActivity)getActivity()).swapFragments();

                    MainViewModel model = ModelFetch.getModel();
                    if(mainActivity.getSpinnerStatus().equals("Today")){
                        model.getTodayTasks();
                    }
                    else if(mainActivity.getSpinnerStatus().equals("Tomorrow")){
                        model.getTomorrowTasks();
                    }
                    else if(mainActivity.getSpinnerStatus().equals("Recurring")){
                        model.getRecurringTasks();
                    }

                }

            }
        });

        return recurDialog;

    }


//    private void onPositiveButtonClick(DialogInterface dialog, int which) {
//        Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
//        var name = view.recurringInput.getText().toString();
//
//        // Retrieving dialog's selection for recurrence
//        View selectedRecurrence = view.recurrenceOptions.findViewById(view.recurrenceOptions.getCheckedRadioButtonId());
//        int radioIndex = view.recurrenceOptions.indexOfChild(selectedRecurrence);
//        RadioButton recurrence = (RadioButton) view.recurrenceOptions.getChildAt(radioIndex);
//        String recurrenceText = recurrence.getText().toString();
//        System.out.println("recurrenceText: " + recurrenceText);
//
//        if(!name.equals(""))
//        {
//            Calendar cal = CalendarUpdate.getCal();
//            SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
//            String formattedDate = customFormat.format(cal.getTime());
//            var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
//            MainActivity mainActivity = (MainActivity) getActivity();
//            var task = new Task(null, name, false, -1, formattedDate,
//                    mainActivity.getSpinnerStatus(), 0, cal,
//                    true, null, false, null);
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


//    private void onNegativeButtonClick(DialogInterface dialog, int which) {
//        dialog.cancel();
//    }
//



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        ModelFetch.setModel(activityModel);

    }

}
