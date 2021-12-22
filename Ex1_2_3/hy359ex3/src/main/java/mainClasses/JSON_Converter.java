/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainClasses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author micha
 */
public class JSON_Converter {
    
    public String getJSONFromAjax(BufferedReader reader) throws IOException{
	StringBuilder buffer = new StringBuilder();
	String line;
	while ((line = reader.readLine()) != null) {
		buffer.append(line);
	}
	String data = buffer.toString();
	return data;
    }

    public User jsonToUser(BufferedReader json) {
        Gson gson = new Gson();
        User msg = gson.fromJson(json, User.class);
        return msg;
    }

    public Doctor jsonToDoctor(BufferedReader json) {
        Gson gson = new Gson();
        Doctor msg = gson.fromJson(json, Doctor.class);
        return msg;
    }

    public String JavaObjectToJSONRemoveElements(User p, String removeProp) {
        // Creating a Gson Object
        Gson gson = new Gson();
        String json = gson.toJson(p, User.class);
        JsonObject object = (JsonObject) gson.toJsonTree(p);
        object.remove(removeProp);
        return object.toString();
    }

    public String UserToJSON(User per) {
        Gson gson = new Gson();

        String json = gson.toJson(per, User.class);
        return json;
    }

    public String DoctorToJSON(User per) {
        Gson gson = new Gson();

        String json = gson.toJson(per, Doctor.class);
        return json;
    }


    
    
}
