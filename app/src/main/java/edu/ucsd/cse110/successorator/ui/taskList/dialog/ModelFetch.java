package edu.ucsd.cse110.successorator.ui.taskList.dialog;

import edu.ucsd.cse110.successorator.MainViewModel;

public class ModelFetch {

    private static MainViewModel model;

    public static void setModel(MainViewModel model) {
        ModelFetch.model = model;
    }

    public static MainViewModel getModel() {
        return model;
    }
}
