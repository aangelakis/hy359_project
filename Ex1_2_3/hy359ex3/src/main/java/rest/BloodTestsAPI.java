/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.tables.EditBloodTestTable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import mainClasses.BloodTest;

/**
 * REST Web Service
 *
 * @author csd4355,csd4334
 */
@Path("tests")
public class BloodTestsAPI {

    @POST
    @Path("/newBloodTest")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addBloodTest(String test) throws ClassNotFoundException, SQLException {

        System.out.println("rest.BloodTestsAPI.addBloodTest()");
        LocalDate date_now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Gson gson = new Gson();
        BloodTest bt = gson.fromJson(test, BloodTest.class);
        LocalDate bt_date = LocalDate.parse(bt.getTest_date(), formatter);
        EditBloodTestTable ebtt = new EditBloodTestTable();

        if (bt_date.compareTo(date_now) > 0) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Wrong Date\"}").build();
        }
        if (bt.getBlood_sugar() <= 0 && bt.getCholesterol() <= 0 && bt.getIron() <= 0 && bt.getVitamin_b12() <= 0 && bt.getVitamin_d3() <= 0) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"You didn't give any measurements\"}").build();
        }
        if (ebtt.databaseToBloodTest(bt.getAmka(), bt.getTest_date()) == null) {
            ebtt.addBloodTestFromJSON(test);
            return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"BloodTest Added\"}").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"AMKA already exists\"}").build();
        }
    }

    @GET
    @Path("/bloodTests/{AMKA}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBloodTestsWithAMKA(@PathParam("AMKA") String amka,
            @QueryParam("toDate") String toDate, @QueryParam("fromDate") String fromDate) throws SQLException, ClassNotFoundException {

        EditBloodTestTable ebtt = new EditBloodTestTable();
        BloodTest bt;
        String json;
        ArrayList<BloodTest> bloodtests = null;

        if (toDate != null && fromDate != null) {
            if (fromDate.compareTo(toDate) > 0) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"toDate can't be greater than fromDate\"}").build();
            }
            if ((bloodtests = ebtt.databaseToBloodTestFromDateToDate(amka, fromDate, toDate)) != null) {
                Gson gson = new Gson();
                JsonArray jsonDoc = gson.toJsonTree(bloodtests).getAsJsonArray();
                return Response.status(Response.Status.OK).type("application/json").entity(jsonDoc.toString()).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"There is no bloodtests with this AMKA in this range\"}").build();
            }
        } else if (toDate != null) {
            if ((bloodtests = ebtt.databaseToBloodTestToDate(amka, toDate)) != null) {
                Gson gson = new Gson();
                JsonArray jsonDoc = gson.toJsonTree(bloodtests).getAsJsonArray();
                return Response.status(Response.Status.OK).type("application/json").entity(jsonDoc.toString()).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"There is no bloodtests with this AMKA in this range\"}").build();
            }
        } else if (fromDate != null) {
            if ((bloodtests = ebtt.databaseToBloodTestFromDate(amka, fromDate)) != null) {
                Gson gson = new Gson();
                JsonArray jsonDoc = gson.toJsonTree(bloodtests).getAsJsonArray();
                return Response.status(Response.Status.OK).type("application/json").entity(jsonDoc.toString()).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"There is no bloodtests with this AMKA in this range\"}").build();
            }
        }

        if ((bloodtests = ebtt.databaseToBloodTestAMKA(amka)) != null) {
            Gson gson = new Gson();
            JsonArray jsonDoc = gson.toJsonTree(bloodtests).getAsJsonArray();
            return Response.status(Response.Status.OK).type("application/json").entity(jsonDoc.toString()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"There is no bloodtests with this AMKA in this range\"}").build();
        }

    }

    @GET
    @Path("/bloodTestMeasure/{AMKA}/{Measure}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBloodTestsMeasurement(@PathParam("AMKA") String amka, @PathParam("Measure") String measure) throws SQLException, ClassNotFoundException {

        BloodTest bt;
        EditBloodTestTable ebtt = new EditBloodTestTable();
        String json;
        ArrayList<BloodTest> bloodtests = null;

        if (ebtt.databaseToBloodTestAMKA(amka) == null) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"AMKA does not exist\"}").build();
        }

        bloodtests = ebtt.databaseToBloodTestAMKA(amka);
        Gson gson = new Gson();
        JsonArray jsonDoc = gson.toJsonTree(bloodtests).getAsJsonArray();
        JsonArray jsonNew = new JsonArray();

        for (int i = 0; i < jsonDoc.size(); i++) {
            JsonObject lol = jsonDoc.get(i).getAsJsonObject();
            JsonObject xd = new JsonObject();

            xd.add("test_date", lol.get("test_date"));
            xd.add("medical_center", lol.get("medical_center"));
            xd.add(measure, lol.get(measure));
            xd.add(measure + "_level", lol.get(measure + "_level"));

            jsonNew.add(xd);
            /*
            if (i == jsonDoc.size() - 1) {
                break;
            }

            xd.remove("test_date");
            xd.remove("medical_center");
            xd.remove(measure);
            xd.remove(measure + "_level");
             */
        }

        System.out.println(jsonNew.toString());
        return Response.status(Response.Status.OK).type("application/json").entity(jsonNew.toString()).build();
    }

    @PUT
    @Path("/bloodTest/{bloodTestID}/{measure}/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBloodTest(@PathParam("bloodTestID") int btID, @PathParam("measure") String measure, @PathParam("value") Double value) throws SQLException, ClassNotFoundException {

        EditBloodTestTable ebtt = new EditBloodTestTable();
        BloodTest bt;

        if (ebtt.databaseToBloodTestID(btID) == null) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"bloodtest_id does not exist\"}").build();
        }

        if (!measure.equals("cholesterol") && !measure.equals("iron") && !measure.equals("blood_sugar") && !measure.equals("vitamin_d3") && !measure.equals("vitamin_b12")) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Wrong measurement input\"}").build();
        }
        if (value <= 0) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Wrong value input\"}").build();
        }

        bt = ebtt.databaseToBloodTestID(btID);
        ebtt.updateBloodTest(btID, measure, value);
        return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Measurement Updated\"}").build();
    }

    @DELETE
    @Path("/bloodTestDeletion/{bloodTestID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBloodTest(@PathParam("bloodTestID") int btID) throws SQLException, ClassNotFoundException {
        EditBloodTestTable ebtt = new EditBloodTestTable();

        if (ebtt.databaseToBloodTestID(btID) == null) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"bloodtest_id does not exist\"}").build();
        }

        ebtt.deleteBloodTest(btID);
        return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Deleted Successfully\"}").build();
    }
}
