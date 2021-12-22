package rest;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import database.tables.EditBloodTestTable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.BloodTest;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Michalis
 */
@Path("bloodTestRequests")
public class BloodTestAPI {

    /**
     * Retrieves representation of an instance of restApi.GenericResource
     *
     * @param amka
     * @param brand
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/bloodTests/{amka}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBloodTest(@PathParam("amka") String amka) {
        Response.Status status = Response.Status.OK;
        EditBloodTestTable ebt = new EditBloodTestTable();
        try {
            ArrayList<BloodTest> bloodtests = ebt.databaseToBloodTests();
            JSONArray bloodtestsArray = new JSONArray(bloodtests);
            JSONObject obj;
            for (int i = 0; i < bloodtestsArray.length(); i++) {
                obj = bloodtestsArray.getJSONObject(i);
                if (!(obj.getString("amka").equals(amka))) {
                    bloodtestsArray.remove(i);
                    i = -1;
                }

            }
            if (bloodtestsArray.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).type("application/json").entity("{\"error\":\"BloodTests with this AMKA do not exist\"}").build();
            } else {
                return Response.status(status).entity(bloodtestsArray.toString()).build();
            }

        } catch (SQLException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @GET
    @Path("/bloodTestMeasure/{amka}/{measure}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBloodTestWithMeasure(@PathParam("amka") String amka, @PathParam("measure") String measure) {
        Response.Status status = Response.Status.OK;

           EditBloodTestTable ebt = new EditBloodTestTable();

        try {
            ArrayList<BloodTest> bloodtests = ebt.databaseToBloodTests();
            JSONArray bloodTestsArray = new JSONArray(bloodtests);
            JSONObject obj;
            for (int i = 0; i < bloodTestsArray.length(); i++) {
                obj = bloodTestsArray.getJSONObject(i);
                if (!(obj.getString("amka").equals(amka))) {
                    bloodTestsArray.remove(i);
                    i = -1;
                }
            }

            if (bloodTestsArray.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).type("application/json").entity("{\"error\":\"BloodTests with this AMKA do not exist\"}").build();
            }

            if (!"iron".equals(measure) && !"cholesterol".equals(measure) && !"blood_sugar".equals(measure) && !"vitamin_b12".equals(measure) && !"vitamin_d3".equals(measure)) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Measure could not be found\"}").build();
            }

            for (int i = 0; i < bloodTestsArray.length(); i++) {
                bloodTestsArray.getJSONObject(i).remove("bloodtest_id");
                bloodTestsArray.getJSONObject(i).remove("amka");
                if ("blood_sugar".equals(measure)) {
                    bloodTestsArray.getJSONObject(i).remove("cholesterol");
                    bloodTestsArray.getJSONObject(i).remove("cholesterol_level");
                    bloodTestsArray.getJSONObject(i).remove("iron");
                    bloodTestsArray.getJSONObject(i).remove("iron_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12_level");
                } else if ("cholesterol".equals(measure)) {
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar_level");
                    bloodTestsArray.getJSONObject(i).remove("iron");
                    bloodTestsArray.getJSONObject(i).remove("iron_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12_level");
                } else if ("iron".equals(measure)) {
                    bloodTestsArray.getJSONObject(i).remove("cholesterol");
                    bloodTestsArray.getJSONObject(i).remove("cholesterol_level");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12_level");
                } else if ("vitamin_d3".equals(measure)) {
                    bloodTestsArray.getJSONObject(i).remove("cholesterol");
                    bloodTestsArray.getJSONObject(i).remove("cholesterol_level");
                    bloodTestsArray.getJSONObject(i).remove("iron");
                    bloodTestsArray.getJSONObject(i).remove("iron_level");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_b12_level");
                } else if ("vitamin_b12".equals(measure)) {
                    bloodTestsArray.getJSONObject(i).remove("cholesterol");
                    bloodTestsArray.getJSONObject(i).remove("cholesterol_level");
                    bloodTestsArray.getJSONObject(i).remove("iron");
                    bloodTestsArray.getJSONObject(i).remove("iron_level");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar");
                    bloodTestsArray.getJSONObject(i).remove("blood_sugar_level");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3");
                    bloodTestsArray.getJSONObject(i).remove("vitamin_d3_level");
                }
            }
            return Response.status(status).type("application/json").entity(bloodTestsArray.toString()).build();
        } catch (SQLException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @POST
    @Path("/newBloodTest")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addBloodTest(String bloodtest) {
        int flag = 0;
        EditBloodTestTable ebt = new EditBloodTestTable();
        ebt.jsonToBloodTest(bloodtest);

        JSONObject obj = new JSONObject(bloodtest);

        LocalDate date = LocalDate.parse(obj.getString("test_date"));

        if (date.isAfter(java.time.LocalDate.now())) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Date cannot be in the future\"}").build();
        }


        try {
            if (ebt.databaseToBloodTest(obj.getString("amka"), obj.getString("test_date")) != null) {
                return Response.status(Response.Status.CONFLICT).type("application/json").entity("{\"error\":\"BloodTest Exists\"}").build();
            }

            if (obj.has("blood_sugar")) {
                if (obj.getInt("blood_sugar") <= 0) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"blood_sugar must be over 0\"}").build();
                }
                flag = 1;
            }
            if (obj.has("cholesterol")) {
                if (obj.getInt("cholesterol") <= 0) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"cholesterol must be over 0\"}").build();
                }
                flag = 1;
            }
            if (obj.has("iron")) {
                if (obj.getInt("iron") <= 0) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"iron must be over 0\"}").build();
                }
                flag = 1;
            }
            if (obj.has("vitamin_d3")) {
                if (obj.getInt("vitamin_d3") <= 0) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"vitamin_d3 must be over 0\"}").build();
                }
                flag = 1;
            }
            if (obj.has("vitamin_b12")) {
                if (obj.getInt("vitamin_b12") <= 0) {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"vitamin_b12 must be over 0\"}").build();
                }
                flag = 1;
            }

            if (flag == 1) {
                ebt.addBloodTestFromJSON(bloodtest);
                return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"BloodTest Added\"}").build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"You need to enter a value to atleast one element \"}").build();
            }

        } catch (SQLException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @PUT
    @Path("/bloodTest/{bloodtest_id}/{measure}/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBloodTest(@PathParam("bloodtest_id") int bloodtest_id, @PathParam("measure") String measure, @PathParam("value") double value) {

        EditBloodTestTable ebt = new EditBloodTestTable();
        try {
            if (ebt.databaseToBloodTest_id(bloodtest_id) == null) {
                return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"BloodTest does not exist\"}").build();
            } else if (value <= 0) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Value must be over 0\"}").build();
            } else {
                BloodTest bt = ebt.databaseToBloodTest_id(bloodtest_id);
                if ("blood_sugar".equals(measure)) {
                    ebt.updateBloodTest_bloodSugar(bloodtest_id, value);
                } else if ("cholesterol".equals(measure)) {
                    ebt.updateBloodTest_chol(bloodtest_id, value);
                } else if ("iron".equals(measure)) {
                    ebt.updateBloodTest_iron(bloodtest_id, value);
                } else if ("vitamin_d3".equals(measure)) {
                    ebt.updateBloodTest_vitamin_d3(bloodtest_id, value);
                } else if ("vitamin_b12".equals(measure)) {
                    ebt.updateBloodTest_vitamin_b12(bloodtest_id, value);
                } else {
                    return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"Measure is not right\"}").build();
                }
                return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Value Updated\"}").build();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @DELETE
    @Path("/bloodTestDeletion/{bloodtest_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBloodTest(@PathParam("bloodtest_id") int bloodtest_id) {
        Response.Status status = Response.Status.OK;
        EditBloodTestTable ebt = new EditBloodTestTable();
        try {
                if (ebt.databaseToBloodTest_id(bloodtest_id) == null) {
                    return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"BloodTest does not exist\"}").build();
                } else {
                    ebt.deleteBloodTest(bloodtest_id);
                    return Response.status(status).type("application/json").entity("{\"success\":\"BloodTest deleted\"}").build();
                }
        } catch (SQLException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BloodTestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
