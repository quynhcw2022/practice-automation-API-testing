package com.api.auto.testcase;

import static org.testng.AssertJUnit.assertTrue;

import static org.testng.Assert.assertTrue;


import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.PropertiesFileUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBodyData;
import static org.testng.Assert.assertEquals;


public class TC_API_Login {
	private String account;
	private String password;
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;
	private JsonPath bodyJson;
	private String token;//body của response đã được convert sang JSON
   
	@BeforeClass
   public void init() {
		// Init data
        String baseUrl = "http://13.228.39.1:5000";
        String loginPath = "/api/users/login";
         account = PropertiesFileUtils.getProperty("account");
        System.out.println("account =" + account);
         password = PropertiesFileUtils.getProperty("password");
        System.out.println("password =" + password);
        
        
        
  
   
        RestAssured.baseURI = baseUrl;
        // make body
        Map<String, Object> body = new HashMap<String, Object>();
        
        body.put("account", account);
        body.put("password", password);

        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body);

        response = request.post(loginPath);
        responseBody = response.body();
        jsonBody = responseBody.jsonPath();
        bodyJson = jsonBody;

        System.out.println("" + responseBody.asPrettyString());
        
        
        token = jsonBody.getString("token");
        PropertiesFileUtils.setProperty("token", token);
    }

    @Test(priority = 0)
    public void TC01_Validate200Ok() {
        // Kiểm chứng HTTP status có = 200 hay không
        assertEquals(200, response.getStatusCode(), "Status Check Failed!");
    }

    @Test(priority = 1)
    public void TC02_ValidateMessage() {
        // Kiểm chứng response body có chứa trường message hay không
        assertTrue(responseBody.asString().contains("message"), "message field check Failed!");
    }

    @Test
    public void TC03_MessageContent() {
       // kiểm chứng trường message có = "Đăng nhập thành công
    String message = bodyJson.getString("message");
	assertEquals("Đăng nhập thành công", message, "Message content check Failed!");
	}

	@Test(priority = 2)
	public void TC03_ValidateToken() {
		
		// kiểm chứng response body có chứa trường token hay không
		String token = jsonBody.getString("token");
		Assert.assertNotNull(token, "token field is null");
	}
	
	@Test
	    // lưu lại token
	public void saveToken() {
        System.out.println("token = " + token);
        PropertiesFileUtils.setProperty("token", token);
}

	@Test(priority = 3)
	public void TC05_ValidateUserType() {
		// kiểm chứng response body có chứa thông tin user và trường type hay không
		String type = jsonBody.getString("user.type");
	    Assert.assertNotNull(type, "type field is null");;
		
	    // kiểm chứng trường type có phải là “UNGVIEN”
	    assertEquals("UNGVIEN", type, "type field does not have expected value");
}

	@Test(priority = 4)

	public void TC06_ValidateAccount() {
		
		// Kiểm chứng response chứa thông tin user và trường account hay không
		String account = jsonBody.getString("user.account");
	    Assert.assertNotNull(account, "account field is null");
		
		
		// Kiểm chứng trường account có khớp với account đăng nhập
	    assertEquals(account, this.account, "account field does not match login account");

		// Kiểm chứng response chứa thông tin user và trường password hay không
	    String responsePassword = jsonBody.getString("user.password");
	    Assert.assertNotNull(responsePassword, "password field is null");
		
		// Kiểm chứng trường password có khớp với password đăng nhập
	    assertEquals(responsePassword, this.password, "password field does not match login password");
	}
}