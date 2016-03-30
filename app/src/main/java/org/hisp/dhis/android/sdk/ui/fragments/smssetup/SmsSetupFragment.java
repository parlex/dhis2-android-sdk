package org.hisp.dhis.android.sdk.ui.fragments.smssetup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.hisp.dhis.android.sdk.R;

/**
 * Created by premer on 30.03.16.
 */
public class SmsSetupFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = SmsSetupFragment.class.getSimpleName();
    private Switch notificationSwitch;
    private Switch gatewaySwitch;
    private LinearLayout notification_layout_enabled;
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

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notification_layout_enabled.setVisibility(View.VISIBLE);
                }else{
                    notification_layout_enabled.setVisibility(View.GONE);
                }
            }
        });

        gatewaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Show extra layouts for gateway setup
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    public void onCheckedChangeListener(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            notification_layout_enabled.setVisibility(View.VISIBLE);
        }else{
            notification_layout_enabled.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
