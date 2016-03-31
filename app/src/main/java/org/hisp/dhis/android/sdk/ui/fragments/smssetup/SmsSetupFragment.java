package org.hisp.dhis.android.sdk.ui.fragments.smssetup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;

/**
 * Created by premer on 30.03.16.
 */
public class SmsSetupFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener{

    public static final String TAG = SmsSetupFragment.class.getSimpleName();
    private Switch notificationSwitch;
    private Switch gatewaySwitch;
    private LinearLayout notification_layout_enabled;
    private Spinner contact_attributesSpinner;
    private Spinner phoneNum_attributesSpinner;

    public SmsSetupFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sms_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        notificationSwitch = (Switch) view.findViewById(R.id.sms_notifiaction_switch);
        gatewaySwitch = (Switch) view.findViewById(R.id.sms_gateway_switch);
        notification_layout_enabled = (LinearLayout) view.findViewById(R.id.sms_notification_setup_enabled_layout);
        contact_attributesSpinner = (Spinner) view.findViewById(R.id.contact_attr_spinner);
        phoneNum_attributesSpinner = (Spinner) view.findViewById(R.id.phonenum_attr_spinner);

        // Enable the layout belonging to the switch if the switch is enabled
        notificationSwitch.setOnCheckedChangeListener(this);
        gatewaySwitch.setOnCheckedChangeListener(this);

        // Get the values set in the spinners to use in orgUnit thing
        contact_attributesSpinner.setOnItemSelectedListener(this);
        phoneNum_attributesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.sms_notifiaction_switch){
            if(isChecked){
                notification_layout_enabled.setVisibility(View.VISIBLE);
            }else{
                notification_layout_enabled.setVisibility(View.GONE);
            }
        }else if(buttonView.getId() == R.id.sms_gateway_switch){
            //Do stuff to show gatewaysetup
            if(isChecked){
                Log.d(TAG, "Checked gateway switch");
            }else{
                Log.d(TAG, "Unchecked gateway switch");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.contact_attr_spinner){
            Log.d(TAG, "Item is selected with id == " + id);
            Log.d(TAG, "Item is selected with position == " + position);
            Log.d(TAG, "Spinner view id == " + parent.getId());
            Log.d(TAG, "contact attr spinner id == " + R.id.contact_attr_spinner);
            Log.d(TAG, "contact attr spinner item == " + contact_attributesSpinner.getSelectedItem().toString());
        }else if(parent.getId() == R.id.phonenum_attr_spinner){
            Log.d(TAG, "Item is selected with id == " + id);
            Log.d(TAG, "Item is selected with position == " + position);
            Log.d(TAG, "Spinner view id == " + parent.getId());
            Log.d(TAG, "contact attr spinner id == " + R.id.contact_attr_spinner);
            Log.d(TAG, "contact attr spinner item == " + contact_attributesSpinner.getSelectedItem().toString());
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

}
