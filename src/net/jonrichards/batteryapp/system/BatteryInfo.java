package net.jonrichards.batteryapp.system;

import android.util.Log;

import com.teslacoilsw.quicksshd.ShellCommand;
import com.teslacoilsw.quicksshd.ShellCommand.CommandResult;

public class BatteryInfo {
	
	//Static Variables
	
	/**
	 * The tag to identify the source of a log message. 
	 */
	private static final String TAG = "BatteryInfo.java";
	
	/**
	 * The path to the statusreg file.
	 */
	private static String STATUS_REGISTER_PATH = "/sys/devices/platform/ds2784-battery/statusreg";
	
	/**
	 * The path to the getvoltage file.
	 */
	private static String VOLTAGE_PATH = "/sys/devices/platform/ds2784-battery/getvoltage";
	
	/**
	 * The path to the uevent file.
	 */
	private static String UEVENT_PATH = "/sys/devices/platform/ds2784-battery/power_supply/battery/uevent";
	
	/**
	 * The path to the dumpreg file.
	 */
	private static String DUMP_REGISTER_PATH = "/sys/devices/platform/ds2784-battery/dumpreg";
	
	/**
	 * The path to the getcurrent file.
	 */
	private static String GET_CURRENT_PATH = "/sys/devices/platform/ds2784-battery/getcurrent";
	
	/**
	 * The path to the getavgcurrent file.
	 */
	private static String GET_AVG_CURRENT_PATH = "/sys/devices/platform/ds2784-battery/getavgcurrent";
	
	/**
	 * The path to the getfull40 file.
	 */
	private static String GET_FULL40_PATH = "/sys/devices/platform/ds2784-battery/getFull40";
	
	/**
	 * The path to the getmAh file.
	 */
	private static String GET_MAH_PATH = "/sys/devices/platform/ds2784-battery/getmAh";
	
	
	
	//Instance Variables
	
	//Constructors

	public BatteryInfo() {
		
	}
	
	//Public Methods
	
	/**
	 * Returns the value of the corresponding bit in the status register, or -1 if the given bit placement is invalid.
	 * @param the_register_position A 0 based position of the bit in the status register to return the value for.
	 */
	public int getStatusRegister(int the_register_position) {
		int value = -1;
		
		//If the register position is invalid
		if(the_register_position < 0 || the_register_position > 7) {
			return value;
		}
		
		String result = this.catFile(STATUS_REGISTER_PATH);
		
		switch(the_register_position) {
			case 1: value = ((Integer.parseInt("02", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			case 2: value = ((Integer.parseInt("04", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			case 4: value = ((Integer.parseInt("10", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			case 5: value = ((Integer.parseInt("20", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			case 6: value = ((Integer.parseInt("40", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			case 7: value = ((Integer.parseInt("80", 16) & Integer.parseInt(result.substring(2), 16)) != 0)?1:0; break;
			default: break;
		}
		
		return value;
	}
	
	/**
	 * Returns the current voltage.
	 * @return Returns the current voltage.
	 */
	public String getVoltage() {
		String voltage = this.catFile(VOLTAGE_PATH);
		
		return voltage;
	}
	
	/**
	 * Returns the value of the dump register from the given position.
	 * @param the_dump_register_position The position in the dump register to return.
	 * @return The value of the dump register from the given position.
	 */
	public String getDumpRegister(int the_dump_register_position) {
		String value = this.catFile(DUMP_REGISTER_PATH);
		
		String[] dump_reg = new String[82];
		
		for(int index=0; index<value.length(); index++) {
			String temp = value.substring(0, value.indexOf(" "));
			if(!temp.contains(":")) {
				dump_reg[index] = temp;
			}
			
			value = value.substring(value.indexOf(" "));
		}
		
		return value;
	}
	
	//Private Methods	
	
	/**
	 * Returns the contents of a file.
	 * @return Returns the contents of a file.
	 */
	private String catFile(String the_file) {
		String file_contents = "";
		
		ShellCommand cmd = new ShellCommand();
		CommandResult r = cmd.sh.runWaitFor("cat " + the_file);
		
		if (!r.success()) {
			Log.v(TAG, "Error " + r.stderr);
		} else {
			file_contents = r.stdout;
		}
		
		return file_contents;
	}
}
//End of class BatteryInfo