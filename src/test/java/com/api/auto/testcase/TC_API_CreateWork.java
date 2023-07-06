package com.api.auto.testcase;

import static org.testng.AssertJUnit.assertTrue;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.PropertiesFileUtils;


import static org.testng.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBodyData;
import static org.testng.Assert.assertEquals;

public class TC_API_CreateWork {

	private String token;
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;

	
	//Tạo data
	private String nameWork = "Kế toán";
	private String experience = "1 năm";
	private String education = "Đại học";
	
	@BeforeClass
	public void init() {
		// Init data
        String baseUrl = PropertiesFileUtils.getProperty("baseUrl");
        String createWorkPath = PropertiesFileUtils.getProperty("createWorkPath");
        token = PropertiesFileUtils.getToken();
        RestAssured.baseURI = baseUrl;

        // make body
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("nameWork", nameWork);
        body.put("experience", experience);
        body.put("education", education);

        RequestSpecification request = RestAssured.given()
        		.contentType(ContentType.JSON)
        		.header("token", token)
        		.body(body);
        
        

        response = request.post(createWorkPath);
        responseBody = response.body();
        jsonBody = responseBody.jsonPath();
        
        System.out.println(" " + responseBody.asPrettyString());
        
}
	

	@Test(priority = 0)
	public void TC01_Validate201Created() {
		// kiểm chứng status code
		assertEquals(200, response.getStatusCode(), "Status Check Failed!");
    
	}
	
	@Test(priority = 2)
	public void TC03_ValidateNameOfWorkMatched() {
		// kiểm chứng tên công việc nhận được có giống lúc tạo
		String nameOfWork = jsonBody.getString("nameWork");
		assertEquals(nameWork, nameOfWork, "Name of work does not match");
	}
	
	@Test(priority = 3)
	public void TC04_ValidateExperienceMatched() {
		// kiểm chứng kinh nghiệm nhận được có giống lúc tạo
		String experienceReceived = jsonBody.getString("experience");
	    assertEquals(experience, experienceReceived, "Experience does not match");
	}
	
	@Test(priority = 4)
	public void TC05_ValidateEducationMatched() {
		// kiểm chứng học vấn nhận được có giống lúc tạo
		String educationReceived = jsonBody.getString("education");
	    assertEquals(education, educationReceived, "Education does not match");
	}
}
