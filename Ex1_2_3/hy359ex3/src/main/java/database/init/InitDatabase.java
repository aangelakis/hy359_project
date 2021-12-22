/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.init;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import database.tables.EditBloodTestTable;
import static database.DB_Connection.getInitialConnection;
import database.tables.EditMessageTable;
import database.tables.EditRandevouzTable;
import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import database.tables.EditTreatmentTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import mainClasses.BloodTest;
import mainClasses.Doctor;
import mainClasses.Message;
import mainClasses.Randevouz;
import mainClasses.SimpleUser;
import mainClasses.Treatment;

/**
 *
 * @author micha
 */
public class InitDatabase {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
        // init.updateRecords();
        init.databaseToJSON();

        // init.deleteRecords();
    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE HY359");
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {
        EditSimpleUserTable eut = new EditSimpleUserTable();
        eut.createSimpleUserTable();

        EditDoctorTable edt = new EditDoctorTable();
        edt.createDoctorTable();

        EditBloodTestTable ett = new EditBloodTestTable();
        ett.createBloodTestTable();

        EditMessageTable emt = new EditMessageTable();
        emt.createMessageTable();

        EditRandevouzTable er = new EditRandevouzTable();
        er.createRandevouzTable();

        EditTreatmentTable etr = new EditTreatmentTable();
        etr.createTreatmentTable();
    }

    public void addToDatabaseExamples() throws ClassNotFoundException {
        //Users
        String userJSON = "{\"username\":\"mountanton\",\"email\":\"mike@mike.com\",\"password\":\"123456\","
                + "\"firstname\":\"Michalis\",\"lastname\":\"Mountanton\",\"birthdate\":\"1992-06-03\",\"gender\":\"Male\","
                + "\"amka\":\"03069200000\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"CSD Voutes\",\"lat\":\"35.3053121\","
                + "\"lon\":\"25.0722869\",\"telephone\":\"1234567890\",\"height\":\"173\",\"weight\":\"82.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A+\"}";
        
        String admin = "{\"username\":\"admin\",\"email\":\"admin@admin.gr\","
                + "\"password\":\"admin12*\","
                + "\"firstname\":\"Admin\",\"lastname\":\"Admin\",\"birthdate\":\"1970-01-01"
                + "\",\"gender\":\"Male\","
                + "\"amka\":\"01234567890\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Liontaria\",\"lat\":\"0.5\","
                + "\"lon\":\"0.1\",\"telephone\":\"281000000\",\"height\":\"200\",\"weight\":\"100\",\"blooddonor\":\"0\","
                + "\"bloodtype\":\"A+\"}";

        EditSimpleUserTable eut = new EditSimpleUserTable();
        eut.addSimpleUserFromJSON(userJSON);
        eut.addSimpleUserFromJSON(admin);
        //Doctors
        String jsonDoctor = "{\"username\":\"papadakis\",\"email\":\"papadakis@doctor.gr\",\"password\":\"doctor12*\","
                + "\"firstname\":\"Nikos\",\"lastname\":\"Papadakis\",\"birthdate\":\"1982-10-03\",\"gender\":\"Male\","
                + "\"amka\":\"03108200123\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Evans 83\",\"lat\":\"35.3361866\","
                + "\"lon\":\"25.1342504\",\"telephone\":\"2810123456\",\"height\":\"182\",\"weight\":\"80.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A+\","
                + "\"specialty\":\"GeneralDoctor\","
                + "\"doctor_info\":\"Exei megali empiria se axiologisi emvoliwn.\","
                + "\"certified\":\"1\""
                + "}"; 
         String jsonDoctor2 = "{\"username\":\"stefanos\",\"email\":\"stefanos@doctor.gr\",\"password\":\"abcd12$3\","
                + "\"firstname\":\"Stefanos\",\"lastname\":\"Kapelakis\",\"birthdate\":\"1958-01-10\",\"gender\":\"Male\","
                + "\"amka\":\"10015800234\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Kalokairinou 50\",\"lat\":\"35.3376963\","
                + "\"lon\":\"25.1276121\",\"telephone\":\"2810654321\",\"height\":\"170\",\"weight\":\"68.0\",\"blooddonor\":\"0\","
                + "\"bloodtype\":\"B+\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"O kaluteros giatros gia ti gripi.\","
                + "\"certified\":\"1\""
                + "}"; 
        String jsonDoctor3 = "{\"username\":\"papadopoulou\","
                + "\"email\":\"papadopoulou@doctor.gr\",\"password\":\"doct12##\","
                + "\"firstname\":\"Eleni\",\"lastname\":\"Papopoulou\",\"birthdate\":\"1980-05-05\",\"gender\":\"Female\","
                + "\"amka\":\"05058000123\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Machis Kritis 10\","
                + "\"lat\":\"35.3375925\","
                + "\"lon\":\"25.1219286\",\"telephone\":\"2810281028\",\"height\":\"170\",\"weight\":\"60.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"AB+\","
                + "\"specialty\":\"GeneralDoctor\","
                + "\"doctor_info\":\"Exei kanei metaptyxiakes spoudes stin ameriki.\","
                + "\"certified\":\"1\""
                + "}"; 
        String jsonDoctor4 = "{\"username\":\"aggelopoulos\",\"email\":\"aggelopoulos@doctor.gr\","
                + "\"password\":\"agge58$1\","
                + "\"firstname\":\"Giorgos\",\"lastname\":\"Aggelopoulos\",\"birthdate\":\"1978-01-12\","
                + "\"gender\":\"Male\","
                + "\"amka\":\"01127800111\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Leoforos Knossou 200\",\"lat\":\"35.3152534\","
                + "\"lon\":\"25.1474208\",\"telephone\":\"2811111111\",\"height\":\"175\",\"weight\":\"60.0\","
                + "\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A-\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"Kathigitis iatrikis sto panepistimio.\","
                + "\"certified\":\"1\""
                + "}"; 
     
         String jsonDoctor5 = "{\"username\":\"papatheodorou\",\"email\":\"papatheodorou@doctor.gr\","
                + "\"password\":\"papap$75\","
                + "\"firstname\":\"Konstantina\",\"lastname\":\"Papatheodorou\","
                 + "\"birthdate\":\"1968-01-03\","
                + "\"gender\":\"Female\","
                + "\"amka\":\"03016800111\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Leoforos 62 Martyron 100\",\"lat\":\"35.3361846\","
                + "\"lon\":\"35.3361846\",\"telephone\":\"2811121111\",\"height\":\"160\",\"weight\":\"65.0\","
                + "\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A-\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"Exei empiria se zaxaro kai xolisterini.\","
                + "\"certified\":\"1\""
                + "}"; 
        
        EditDoctorTable edt = new EditDoctorTable();
        edt.addDoctorFromJSON(jsonDoctor);
        edt.addDoctorFromJSON(jsonDoctor2);
        edt.addDoctorFromJSON(jsonDoctor3);
        edt.addDoctorFromJSON(jsonDoctor4);
        edt.addDoctorFromJSON(jsonDoctor5);
         //BloodTest
        EditBloodTestTable ebt = new EditBloodTestTable();
        String bloodTestjson = "{\"amka\":\"03069200000\",\"test_date\":\"2021-10-11\",\"medical_center\":\"pagni\","
                + "\"blood_sugar\":\"100.0\","
                + "\"cholesterol\":\"220.0\","
                + "\"iron\":\"60.0\","
                + "\"vitamin_d3\":\"30.0\","
                + "\"vitamin_b12\":\"50.0\"}";
        ebt.addBloodTestFromJSON(bloodTestjson);

        //Randevouz
        String randevouzJSON = "{\"doctor_id\":\"1\",\"date_time\":\"2021-10-29 15:00:00\","
                + "\"price\":\"50\","
                + "\"doctor_info\":\"Krata covid pass\", \"status\":\"free\"}";
        EditRandevouzTable er = new EditRandevouzTable();
        er.addRandevouzFromJSON(randevouzJSON);

        //Treatment
        EditTreatmentTable ert = new EditTreatmentTable();
        String trjson = "{\"doctor_id\":\"1\",\"user_id\":\"1\",\"start_date\":\"2021-10-26\","
                + "\"end_date\":\"2021-11-09\","
                + "\"treatment_text\":\"Xapia gia xolisterini 3 fores ti mera\","
                + "\"bloodtest_id\":\"1\"}";

        ert.addTreatmentFromJSON(trjson);

        //Message
        EditMessageTable ett = new EditMessageTable();
        String msgjson = "{\"doctor_id\":\"1\",\"user_id\":\"1\",\"date_time\":\"2021-10-11 15:11:00\","
                + "\"message\":\"Den eimai kala\","
                + "\"sender\":\"user\"}";

        ett.addMessageFromJSON(msgjson);

    }

    public void databaseToJSON() throws ClassNotFoundException, SQLException {
        //Users
        EditSimpleUserTable eut = new EditSimpleUserTable();
        SimpleUser su = eut.databaseToSimpleUser("mountanton", "123456");
        String json = eut.simpleUserToJSON(su);
        System.out.println("User\n" + json + "\n");

        //Doctors
        EditDoctorTable edt = new EditDoctorTable();
        ArrayList<Doctor> doctors = edt.databaseToDoctors();
        Gson gson = new Gson();
        JsonArray jsonDoc = gson.toJsonTree(doctors).getAsJsonArray();
        System.out.println("Doctors\n" + jsonDoc + "\n");

        //Blood Test
        EditBloodTestTable ebt = new EditBloodTestTable();
        BloodTest bt = ebt.databaseToBloodTest("03069200000", "2021-10-11");
        String jsonBT = ebt.bloodTestToJSON(bt);
        System.out.println("BloodTest\n" + jsonBT + "\n");

        //Randevouz
        EditRandevouzTable er = new EditRandevouzTable();
        Randevouz r = er.databaseToRandevouz(1);
        String jsonRand = er.randevouzToJSON(r);
        System.out.println("Randevouz\n" + jsonRand + "\n");

        //Message
        EditMessageTable emt = new EditMessageTable();
        Message msg = emt.databaseToMessage(1);
        String jsonmsg = emt.messageToJSON(msg);
        System.out.println("Message\n" + jsonmsg + "\n");

        //Treatment
        EditTreatmentTable ett = new EditTreatmentTable();
        Treatment tr = ett.databaseToTreatment(1);
        String jsontr = ett.treatmentToJSON(tr);
        System.out.println("Treatment\n" + jsontr + "\n");
    }

    public void updateRecords() throws ClassNotFoundException, SQLException {
        EditSimpleUserTable es = new EditSimpleUserTable();
        es.updateSimpleUser("mountanton", 85.0);

        EditDoctorTable edt = new EditDoctorTable();
        edt.updateDoctor("mountanton", 190);

        EditBloodTestTable ebt = new EditBloodTestTable();
        ebt.updateBloodTest(1, 180);

        EditRandevouzTable er = new EditRandevouzTable();
        er.updateRandevouz(1, 1, "Provlima Me vitamini", "selected");
    }

    public void deleteRecords() throws ClassNotFoundException, SQLException {
        EditRandevouzTable er = new EditRandevouzTable();
        er.deleteRandevouz(1);

        EditBloodTestTable eb = new EditBloodTestTable();
        eb.deleteBloodTest(1);
    }
}
