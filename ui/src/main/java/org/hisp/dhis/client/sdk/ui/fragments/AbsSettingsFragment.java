package org.hisp.dhis.client.sdk.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.hisp.dhis.client.sdk.ui.R;
import org.hisp.dhis.client.sdk.ui.activities.INavigationHandler;
import org.hisp.dhis.client.sdk.ui.utils.UiUtils;

/**
 * This is the view, using MVP.
 * Supposed to only show the information.
 * Calls methods in ISettingsPresenter to execute the selected actions.
 *
 * Created by Vladislav Georgiev Alfredov on 1/15/16.
 */
public abstract class AbsSettingsFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TAG = AbsSettingsFragment.class.getSimpleName();
    public INavigationHandler mNavigationHandler;

    private Button logoutButton;
    private Button synchronizeButton;
    private TextView syncTextView;

    private Spinner updateFrequencySpinner;
    private ProgressBar mProgessBar;
    private String progressMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        updateFrequencySpinner = (Spinner) view.findViewById(R.id.settings_update_frequency_spinner);

        updateFrequencySpinner.setSelection(getUpdateFrequency(getActivity()));

        updateFrequencySpinner.setOnItemSelectedListener(this);

        synchronizeButton = (Button) view.findViewById(R.id.settings_sync_button);
        logoutButton = (Button) view.findViewById(R.id.settings_logout_button);
        mProgessBar = (ProgressBar) view.findViewById(R.id.settings_progessbar);
        syncTextView = (TextView) view.findViewById(R.id.settings_sync_textview);
        mProgessBar.setVisibility(View.GONE);
        logoutButton.setOnClickListener(this);
        synchronizeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settings_logout_button) {

            UiUtils.showConfirmDialog(getActivity().getSupportFragmentManager(), getString(R.string.logout_title), getString(R.string.logout_message),
                    getString(R.string.logout_option), getString(R.string.cancel_option),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout(getContext());
                            getActivity().finish();
                        }
                    });
        } else if (view.getId() == R.id.settings_sync_button) {
            if (isAdded()) {
                final Context context = getActivity().getBaseContext();
                Toast.makeText(context, getString(R.string.syncing), Toast.LENGTH_SHORT).show();
                //Extend and synchronize...
                synchronize(context);

                synchronizeButton.setEnabled(false);
                mProgessBar.setVisibility(View.VISIBLE);
                synchronizeButton.setText("Synchronizing...");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
        if(view != null) {
           setUpdateFrequency(view.getContext(), position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // stub implementation
    }

    public void setSynchronizingUi(boolean enable) {
        /*if(!enable){
            synchronizeButton.setEnabled(false);
            mProgessBar.setVisibility(View.VISIBLE);
            synchronizeButton.setText("Synchronizing...");
            syncTextView.setText(getProgressMessage());
        } else {
            synchronizeButton.setEnabled(true);
            mProgessBar.setVisibility(View.GONE);
            syncTextView.setText(/*DhisController.getLastSynchronizationSummary()*//*"");
            synchronizeButton.setText(R.string.synchronize_with_server);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        if(activity instanceof INavigationHandler) {
            mNavigationHandler = (INavigationHandler) activity;
        } else {
            throw new IllegalArgumentException("Activity must " +
                    "implement INavigationHandler interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // we need to nullify reference
        // to parent activity in order not to leak it
        mNavigationHandler = null;
    }

    protected abstract void  logout(Context context);

    protected abstract void synchronize(Context context);

    protected abstract void setUpdateFrequency(Context context, int frequency);

    protected abstract int getUpdateFrequency(Context context);
}
