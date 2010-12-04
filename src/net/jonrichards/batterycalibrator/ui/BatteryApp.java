package net.jonrichards.batterycalibrator.ui;

import net.jonrichards.batterycalibrator.ui.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * The main 
 * @author Jon Richards
 * @author Roger Podacter
 */
public class BatteryApp extends TabActivity {
	
	//Instance Variables
	
	private Resources my_resources;
    private TabHost my_tab_host;
    private TabSpec my_tab_spec;
    private Intent my_intent;
	
	//Public Methods
	
	/**
	 * Called when the activity is first created, initializations happen here.
	 * @param savedInstanceState 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    my_resources = getResources();
	    my_tab_host = getTabHost();
	    
	    //Create the General tab
	    my_intent = new Intent().setClass(this, GeneralActivity.class);
	    my_tab_spec = my_tab_host.newTabSpec("general").setIndicator("General", my_resources.getDrawable(R.drawable.ic_tab_general)).setContent(my_intent);
    	my_tab_host.addTab(my_tab_spec);
    	
    	//Create the Learn Prep tab
    	my_intent = new Intent().setClass(this, LearnPrepActivity.class);
	    my_tab_spec = my_tab_host.newTabSpec("learnprep").setIndicator("LearnPrep", my_resources.getDrawable(R.drawable.ic_tab_learnprep)).setContent(my_intent);
	    my_tab_host.addTab(my_tab_spec);
	    
	    //Create the Learn Mode tab
	    my_intent = new Intent().setClass(this, LearnModeActivity.class);
	    my_tab_spec = my_tab_host.newTabSpec("learnmode").setIndicator("LearnMode", my_resources.getDrawable(R.drawable.ic_tab_learnmode)).setContent(my_intent);
	    my_tab_host.addTab(my_tab_spec);
	    
	    //Create the Registers tab
	    my_intent = new Intent().setClass(this, RegistersActivity.class);
	    my_tab_spec = my_tab_host.newTabSpec("registers").setIndicator("Registers", my_resources.getDrawable(R.drawable.ic_tab_registers)).setContent(my_intent);
	    my_tab_host.addTab(my_tab_spec);
	    
	    //Set the default tab when the app is opened to the General tab
	    my_tab_host.setCurrentTab(0);
	}
	
	/**
	 * Called when this activity is paused.
	 */
	@Override
    public void onPause() {
        super.onPause();
    }
	
	/**
	 * Called when this activity resumes.
	 */
	@Override
    public void onResume() {
        super.onResume();
    }
}
//End of class BatteryApp