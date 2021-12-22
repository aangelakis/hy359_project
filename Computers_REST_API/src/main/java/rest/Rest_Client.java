package rest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Michalis
 */
public class Rest_Client {

    private HttpClient client;
    private HttpGet bloodTest;
    private HttpGet bloodTestWithMeasure;
    private HttpPut bloodTestUpdate;
    private HttpDelete BloodTestDelete;
    private HttpPost addbloodtestService;
    private static final String URL
            = "http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests";

    private String serviceName;

    /**
     * Used to open connection with client and LODsyndesis
     */
    public Rest_Client() {
        client = HttpClientBuilder.create().build();
    }

    public void updateBloodTest(int bloodtest_id, String measure, double value) throws IOException {
        try {
            serviceName = "bloodTest";
            bloodTestUpdate = new HttpPut(URL + "/" + serviceName + "/" + bloodtest_id + "/" + measure + "/" + value);
            bloodTestUpdate.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(bloodTestUpdate);
            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code " + responseCode);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(Rest_Client.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void getBloodTest(String amka) throws IOException {
        try {
            serviceName = "bloodTests/";
            bloodTest = new HttpGet(URL + "/" + serviceName + amka);
            bloodTest.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(bloodTest);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(Rest_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getBloodTestWithMeasure(String amka, String measure) throws IOException {
        try {
            serviceName = "bloodTestMeasure/" + amka + "/" + measure;
            bloodTestWithMeasure = new HttpGet(URL + "/" + serviceName);
            bloodTestWithMeasure.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(bloodTestWithMeasure);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(Rest_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBloodTest(String bloodtest) throws IOException {
        try {
            serviceName = "newBloodTest/";
            addbloodtestService = new HttpPost(URL + "/" + serviceName);
            addbloodtestService.addHeader(ACCEPT, "application/json");
            addbloodtestService.addHeader(CONTENT_TYPE, "application/json");
            StringEntity toSend = new StringEntity(bloodtest);
            addbloodtestService.setEntity(toSend);
            HttpResponse response = client.execute(addbloodtestService);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(Rest_Client.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void deleteBloodTest(int bloodtest_id) throws IOException {
        try {
            serviceName = "bloodTestDeletion";
            BloodTestDelete = new HttpDelete(URL + "/" + serviceName + "/" + bloodtest_id);
            BloodTestDelete.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(BloodTestDelete);
            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code " + responseCode);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            Logger.getLogger(Rest_Client.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public static void main(String[] args) throws IOException {
        Rest_Client rs = new Rest_Client();

        System.out.println("BloodTests with AMKA : 17080123456");
        rs.getBloodTest("17080123456");

        System.out.println("Blood_sugar results of AMKA : 03069200000");
        rs.getBloodTestWithMeasure("03069200000", "blood_sugar");

        System.out.println("Cholesterol results of AMKA : 17080198765");
        rs.getBloodTestWithMeasure("17080198765", "cholesterol");


        String json1 = "{\n"
                + "    \"amka\":\"17080123411\","
                + "    \"test_date\": \"2021-12-11\","
                + "    \"medical_center\": \"pagni\","
                + "    \"blood_sugar\": 80"
                + "}";

        String json2 = "{\n"
                + "    \"amka\":\"17080123411\","
                + "    \"test_date\": \"2021-12-12\","
                + "    \"medical_center\": \"pagni\","
                + "    \"iron\": \"100\","
                + "    \"vitamin_b12\": 150"
                + "}";

        System.out.println("Add BloodTest");
        rs.addBloodTest(json1);

        System.out.println("Add another BloodTest");
        rs.addBloodTest(json2);

        System.out.println("Delete BloodTest with ID : 5");
        rs.deleteBloodTest(5);

        System.out.println("Update BloodTest with ID : 4 --> blood_sugar = 250");
        rs.updateBloodTest(4, "blood_sugar", 250);

        //  System.out.println("Update BloodTest with ID : 5 --> iron = 300");
        //  rs.updateBloodTest(5, "iron", 300);

        System.out.println("Update BloodTest with ID : 1 --> cholesterol = 336");
        rs.updateBloodTest(1, "cholesterol", 336);

        System.out.println("Print BloodTests of AMKA : 03069200000");
        rs.getBloodTest("03069200000");

    }

}
