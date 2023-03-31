package com.ourlead.utils;


import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Salvatore
 * @date 2023/3/31
 */
@Service
public class ParseXML {

  public static void testXML(String xmlString) {
    try {
      // 创建DOM解析器工厂
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      // 创建DOM解析器
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
      // 解析XML文档，获取Document对象
      Document doc = dBuilder.parse(input);

      // 获取根元素
      Element root = doc.getDocumentElement();
      root.normalize();

      // 获取LoginVerifyServiceResult元素
      Node loginVerifyServiceResult = root.getElementsByTagName("LoginVerifyServiceResult").item(0);

      // 获取ReturnStatus元素的值
      Element returnStatusElement = (Element) ((Element) loginVerifyServiceResult).getElementsByTagName("ReturnStatus").item(0);
      String returnStatus = returnStatusElement.getTextContent();
      System.out.println("returnStatus: " + returnStatus);
      // 获取ReturnData元素的值
      Element returnDataElement = (Element) ((Element) loginVerifyServiceResult).getElementsByTagName("ReturnData").item(0);
      NodeList memberOfList = returnDataElement.getElementsByTagName("MemberOfList");
      Node memberOfListNode = memberOfList.item(0);
      NodeList memberOfStrings = ((Element)memberOfListNode).getElementsByTagName("string");
      for (int i = 0; i < memberOfStrings.getLength(); i++) {
        Node memberOfString = memberOfStrings.item(i);
        String value = memberOfString.getTextContent();
        System.out.println("MemberOfList: " + value);
      }
      // 获取ReturnMessage元素的值
      Element returnMessageElement = (Element) ((Element) loginVerifyServiceResult).getElementsByTagName("ReturnMessage").item(0);
      Element sourceElement = (Element) returnMessageElement.getElementsByTagName("Source").item(0);
      String value = sourceElement.getTextContent();
      System.out.println("Source: " + value);
      Element taskCategoryElement = (Element) returnMessageElement.getElementsByTagName("TaskCategory").item(0);
      String taskCategory = taskCategoryElement.getTextContent();
      System.out.println("taskCategory: " + taskCategory);
      Element levelElement = (Element) returnMessageElement.getElementsByTagName("Level").item(0);
      String level = levelElement.getTextContent();
      System.out.println("Level: " + level);
      Element eventIDElement = (Element) returnMessageElement.getElementsByTagName("EventID").item(0);
      String eventID = eventIDElement.getTextContent();
      System.out.println("EventID: " + eventID);
      Element userElement = (Element) returnMessageElement.getElementsByTagName("User").item(0);
      String user = userElement.getTextContent();
      System.out.println("User: " + user);
      Element computerElement = (Element) returnMessageElement.getElementsByTagName("Computer").item(0);
      String computer = computerElement.getTextContent();
      System.out.println("Computer: " + computer);
      Element loggedElement = (Element) returnMessageElement.getElementsByTagName("Logged").item(0);
      String logged = loggedElement.getTextContent();
      System.out.println("Logged: " + logged);
      Element messageElement = (Element) returnMessageElement.getElementsByTagName("Message").item(0);
      String message = messageElement.getTextContent();
      System.out.println("Message: " + message);
      Element detailElement = (Element) returnMessageElement.getElementsByTagName("Detail").item(0);
      String detail = detailElement.getTextContent();
      System.out.println("Detail: " + detail);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
