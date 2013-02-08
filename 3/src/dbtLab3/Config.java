package dbtLab3;

import java.util.HashMap;

public class Config {
	private HashMap<String,String> config;
	public void reload() {
		
	}
	
	public String getSetting(String key){
		return config.get(key);
	}
	
	public String putSetting(String key,String value){
		return config.put(key, value);
	}
}
