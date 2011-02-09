/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */ 
package net.jonrichards.batterycalibrator.ui;

import net.jonrichards.batterycalibrator.system.DS2784Battery;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A class for prepping the battery to enter learn mode.
 * @author Jon Richards
 * @author Roger Podacter
 */
public class LearnPrepActivity extends Activity {

	//Instance Variables
	
	//Handler for updating the UI
	private final Handler my_handler = new Handler();

	private TextView my_age;
	private EditText my_age_input;
	private Button my_age_save_button;
	private Button my_age_cancel_button;
	
	private TextView my_full_40;
	private Button my_full_40_save_button;
	private Button my_full_40_cancel_button;
	private EditText my_full_40_input;
	
	private TextView my_set_reg;
	private TextView my_lbl_set_reg;
	private TextView my_lbl_set_reg_value;
	private Button my_set_reg_save_button;
	private Button my_set_reg_cancel_button;
	private EditText my_set_reg_input;
	private EditText my_set_reg_value_input;
	private View my_divider_last;
	
	private PowerManager my_power_manager;
	private WakeLock my_wake_lock;
	
	private DS2784Battery my_battery_info;
	
	/**
	 * The polling frequency in milliseconds.
	 */
	private int my_sample_poll = 60000;
	
	//Public Methods
	
	/**
	 * Called when the activity is first created, initializations happen here.
	 * @param savedInstanceState 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learnpreplayout);
        
        my_battery_info = new DS2784Battery();
        
        my_power_manager = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);
        my_wake_lock = my_power_manager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "LearnModeActivity");


        //text views and button for the UI for this tab
        my_age = (TextView)findViewById(R.id.txtAge);
        my_age_input = (EditText)findViewById(R.id.etAge);
        my_age_save_button = (Button)findViewById(R.id.btnAgeSave);
        my_age_cancel_button = (Button)findViewById(R.id.btnAgeCancel);
        
        my_full_40 = (TextView)findViewById(R.id.txtFull40);
        my_full_40_save_button = (Button)findViewById(R.id.btnFull40Save);
        my_full_40_cancel_button = (Button)findViewById(R.id.btnFull40Cancel);
        my_full_40_input = (EditText)findViewById(R.id.etFull40);
        
        my_divider_last = (View)findViewById(R.id.dividerlast);
        my_set_reg = (TextView)findViewById(R.id.txtsetreg);
        my_lbl_set_reg = (TextView)findViewById(R.id.lblsetreg);
        my_lbl_set_reg_value = (TextView)findViewById(R.id.lblsetregvalue);
        my_set_reg_save_button = (Button)findViewById(R.id.btnSetregSave);
        my_set_reg_cancel_button = (Button)findViewById(R.id.btnSetregCancel);
        my_set_reg_input = (EditText)findViewById(R.id.etsetreg);
        my_set_reg_value_input = (EditText)findViewById(R.id.etsetregvalue);
        
        setUIText();

        //Sets the new age value when pressed
        my_age_save_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DS2784Battery battery = new DS2784Battery();
				try {
					int new_age = Integer.parseInt((my_age_input.getText().toString()));
					battery.setAge(new_age);
					my_age_input.setText("");
				} catch(Exception e) {
					my_age_input.setText("");
				}
        		setUIText();
			}
		});
        
        //Clears the age input field when pressed
        my_age_cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				my_age_input.setText("");
        		setUIText();
			}
		});
        
        
        //Sets the new full 40 value when pressed
        my_full_40_save_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					int new_full_40 = Integer.parseInt((my_full_40_input.getText().toString()));
					my_battery_info.setFull40(new_full_40);
					my_full_40_input.setText("");
				} catch(Exception e) {
					my_full_40_input.setText("");
				}
        		setUIText();
			}
		});
        
        //Clears the full 40 input field when pressed
        my_full_40_cancel_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				my_full_40_input.setText("");
        		setUIText();
			}
		});
        
        //Set the register with the given value when pressed
        my_set_reg_save_button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        			my_battery_info.setReg(my_set_reg_input.getText().toString(), my_set_reg_value_input.getText().toString());
        			my_set_reg_input.setText("");
            		my_set_reg_value_input.setText("");
        		} catch(Exception e) {
        			my_set_reg_input.setText("");
            		my_set_reg_value_input.setText("");
        		}
        		setUIText();
        	}
        });
        
        //Clears the advanced setreg input fields when pressed
        my_set_reg_cancel_button.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		my_set_reg_input.setText("");
        		my_set_reg_value_input.setText("");
        		setUIText();
        	}
        });
        
        
        //Initial poll when activity is first created
        my_handler.postDelayed(mUpdateUITimerTask, my_sample_poll);
        if (savedInstanceState == null) {
            setUIText();
        }
    }
	
	/**
	 * Creates an options menu.
	 * @param menu The options menu to place the menu items in.
	 * @return Returns boolean true.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	/**
	 * Called when an item in the options menu is selected.
	 * @param item The MenuItem selected.
	 * @return Returns a boolean true.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.about:
	        	Intent myIntent = new Intent();
	            myIntent.setClass(this, AboutActivity.class);
	            startActivity(myIntent);
	            break;
	        case R.id.settings:
	        	startActivity(new Intent(this, SettingsActivity.class));
	            break;
	        case R.id.log:
	        	startActivity(new Intent(this, LogActivity.class));
	            break;
	        case R.id.registers:
	        	startActivity(new Intent(this, RegisterActivity.class));
	            break;
	        case R.id.exit:
	        	finish();
	        	break;
	        default:
	        	break;
	    }
	    return true;
	}

	/**
	 * Called when this activity is resumed.
	 */
	@Override
    public void onResume() {
        super.onResume();
		if(LearnModeActivity.LEARN_MODE && SettingsActivity.getEnableScreenOn(getBaseContext())) {
			if(!my_wake_lock.isHeld()) {
				my_wake_lock.acquire();
			}
		}
		
		//my_handler.postDelayed(mUpdateUITimerTask, 2000);
		my_handler.post(mUpdateUITimerTask);
            
		/**Should update the UI onResume; for now we won't.  We see a delay
		 *when calling the detUIText function onResume.  We need a way to
		 *only poll the dynamic variables regularly, and only poll the static
		 *variables when they change, or less often.
		 */
        //setUIText();
    }

	/**
	 * Called when this activity is paused.
	 */
	@Override
    public void onPause() {
        super.onPause();
		if(my_wake_lock.isHeld()) {
			my_wake_lock.release();
		}
		
		my_handler.removeCallbacks(mUpdateUITimerTask);
    }

	//Private Methods
	
	/**
	 * Sets the UI text.
	 */
	private void setUIText() {
		DS2784Battery battery_info = new DS2784Battery();		

		//Populate age
		String age_text = battery_info.getDumpRegister(20);
		try {
			int age_converted = (Integer.parseInt(age_text,16))*100/128;
			age_text = Integer.toString(age_converted);
		} catch(Exception e) {
			
		}
		my_age.setText(age_text + "%");

		//Populate full40
		String full_40_text = battery_info.getFull40();
		my_full_40.setText(full_40_text + " mAh");
		
		if(!SettingsActivity.getEnableAdvancedOptions(getBaseContext())) {
			my_divider_last.setVisibility(View.GONE);
			my_set_reg.setVisibility(View.GONE);
			my_lbl_set_reg.setVisibility(View.GONE);
			my_lbl_set_reg_value.setVisibility(View.GONE);
			my_set_reg_input.setVisibility(View.GONE);
			my_set_reg_value_input.setVisibility(View.GONE);
			my_set_reg_save_button.setVisibility(View.GONE);
			my_set_reg_cancel_button.setVisibility(View.GONE);
		} else {
			my_divider_last.setVisibility(View.VISIBLE);
			my_set_reg.setVisibility(View.VISIBLE);
			my_lbl_set_reg.setVisibility(View.VISIBLE);
			my_lbl_set_reg_value.setVisibility(View.VISIBLE);
			my_set_reg_input.setVisibility(View.VISIBLE);
			my_set_reg_value_input.setVisibility(View.VISIBLE);
			my_set_reg_save_button.setVisibility(View.VISIBLE);
			my_set_reg_cancel_button.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Our runnable to continuously update the UI.
	 */
	private final Runnable mUpdateUITimerTask = new Runnable() {
	    public void run() {
	    	setUIText();
	        my_handler.postDelayed(mUpdateUITimerTask, my_sample_poll);
	    }
	};
}
//End of class LearnPrepActivity