package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        String selectedItem = parent.getItemAtPosition(pos).toString();

        switch (selectedItem) {
            case "Today":
                // Do something for Today
                showToast("Selected: Today");
                System.out.println("Today");
                break;
            case "Tomorrow":
                // Do something for Tomorrow
                showToast("Selected: Tomorrow");
                System.out.println("Tomorrow");
                break;
            case "Recurring":
                // Do something for Recurring
                showToast("Selected: Recurring");
                System.out.println("Recurring");
                break;
            case "Pending":
                // Do something for Pending
                showToast("Selected: Pending");
                System.out.println("Pending");
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}