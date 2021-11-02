package com.doggabyte;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/tt")
    public ResponseEntity<Object> getSomeParameters() {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "key-1", "value-1",
                "key-2", "value-2",
                "key-3", "value-3"));
    }
    @GetMapping("/user")
    public ResponseEntity<Object> getUser() {
        List<String> entities = new ArrayList<>();
        JSONObject entity = new JSONObject();
        entity.put("aa", "bb");
        entities.add("test");
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @GetMapping(path = "/test")
    public ResponseEntity<JsonNode> get() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"id\": \"132\", \"name\": \"Alice\"}");
        return ResponseEntity.ok(json);
    }

//    @GetMapping(path = "/login")
//    public ResponseEntity<ServiceStatus> restApiExample(HttpServletRequest request,
//                                                        HttpServletResponse response) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        loginService.login(username, password, request);
//        return new ResponseEntity<ServiceStatus>(ServiceStatus.LOGIN_SUCCESS,
//                HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping(path = "/hello")
//    public ResponseEntity<Object> sayHello()
//    {
//        //Get data from service layer into entityList.
//
//        List<JSONObject> entities = new ArrayList<JSONObject>();
//        for (Entity n : entityList) {
//            JSONObject entity = new JSONObject();
//            entity.put("aa", "bb");
//            entities.add(entity);
//        }
//        return new ResponseEntity<Object>(entities, HttpStatus.OK);
//    }
}