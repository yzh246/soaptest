package com.ourlead.controller;

import com.ourlead.service.SOAPService;
import com.ourlead.utils.ParseXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Salvatore
 * @date 2023/3/31
 */
@RestController()
@RequestMapping("frontend-api/soaptest")
public class TestController {
  @Autowired
  private SOAPService soapService;

  @PostMapping("/connect")
  public Map test(@RequestBody Map<String, Object> requestBody) {
    Map map = new HashMap<>();
    String username = (String) requestBody.get("username");
    String password = (String) requestBody.get("password");
    String response = soapService.connect(username, password);
    map.put("code", 200);
    map.put("msg", "success");
    map.put("data", response);
    return map;
  }

  @PostMapping("/parse")
  public Map parse(@RequestBody String body) {
    Map map = new HashMap<>();
    System.out.println(body);
    ParseXML.testXML(body);
    map.put("code", 200);
    map.put("msg", "success");
    return map;
  }
}
