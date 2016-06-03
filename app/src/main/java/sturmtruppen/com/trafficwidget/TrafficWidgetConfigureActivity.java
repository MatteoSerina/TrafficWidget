package sturmtruppen.com.trafficwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

/**
 * The configuration screen for the {@link TrafficWidget TrafficWidget} AppWidget.
 */
public class TrafficWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "sturmtruppen.com.trafficwidget.TrafficWidget";
    private static final String PREF_PREFIX_KEY = "TW_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    //EditText mAppWidgetText;
    //--
    EditText mFrom;
    EditText mTo;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = TrafficWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            //String widgetText = mAppWidgetText.getText().toString();
            //saveTitlePref(context, mAppWidgetId, widgetText);
            //--
            String from = mFrom.getText().toString();
            String to = mTo.getText().toString();
            saveDestinationPref(context, mAppWidgetId, from, to);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            TrafficWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);


            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public TrafficWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static void saveDestinationPref(Context context, int appWidgetId, String from, String to) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + "from", from);
        prefs.putString(PREF_PREFIX_KEY + "to", to);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadFromPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String fromValue = prefs.getString(PREF_PREFIX_KEY + "from", null);
        if (fromValue != null) {
            return fromValue;
        } else {
            return "dummyFrom";
        }
    }

    static String loadToPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String toValue = prefs.getString(PREF_PREFIX_KEY + "to", null);
        if (toValue != null) {
            return toValue;
        } else {
            return "dummyTo";
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.traffic_widget_configure);
        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        //--
        mFrom = (EditText) findViewById(R.id.txtFrom);
        mTo = (EditText) findViewById(R.id.txtTo);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //mAppWidgetText.setText(loadTitlePref(TrafficWidgetConfigureActivity.this, mAppWidgetId));
        //--
        mFrom.setText(loadFromPref(TrafficWidgetConfigureActivity.this, mAppWidgetId));
        mTo.setText(loadToPref(TrafficWidgetConfigureActivity.this, mAppWidgetId));
    }
}

