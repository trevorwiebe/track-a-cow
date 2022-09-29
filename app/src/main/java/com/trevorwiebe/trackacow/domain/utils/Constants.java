package com.trevorwiebe.trackacow.domain.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    public static final DatabaseReference BASE_REFERENCE = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    public static final String BASE_REFERENCE_STRING = "/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/";
    public static final String RATIONS = "/rations";
    public static final String CALLS = "/calls";
    public static final String PENS = "pens";
    public static final String LOTS = "cattleLot";

    public static final int SUCCESS = 1;
    public static final int NO_NETWORK_CONNECTION = 2;
    public static final int ERROR_FETCHING_DATA_FROM_CLOUD = 3;
    public static final int ERROR_PUSHING_DATA_TO_CLOUD = 4;
    public static final int ERROR_ACTIVITY_DESTROYED_BEFORE_LOADED = 5;

    public static final int MEDICATE = 1;
    public static final int FEED = 2;
    public static final int MOVE = 3;
    public static final int REPORTS = 4;
    public static final int MORE = 5;

    /* holding entity 'what happened' keys */
    public static final int INSERT_UPDATE = 1;
    public static final int DELETE = 3;

    public static final int LOT = 1;
    public static final int ARCHIVE = 2;

    /* time drug report types */
    public static final int DAY_DRUG_REPORT = 1;
    public static final int WEEK_DRUG_REPORT = 2;
    public static final int MONTH_DRUG_REPORT = 3;

    // Preference keys
    public static final String NEW_DATA_TO_UPLOAD_NAME = "new_data_to_upload_name";
    public static final String NEW_DATA_TO_UPLOAD_KEY = "new_data_to_upload_key";

    // Drug report types
    public static final int YESTERDAY = 1;
    public static final int MONTH = 2;
    public static final int ALL = 3;
    public static final int CUSTOM = 4;

    // Manage Ration Type
    public static final int ADD_RATION = 1;
    public static final int EDIT_RATION = 2;
}
