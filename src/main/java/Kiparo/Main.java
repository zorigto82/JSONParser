package Kiparo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;

import javax.swing.event.ListDataEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.constant.Constable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

//        List<Employee> list = parseXML("data.xml");

        String json = listToJson(list);

        writeString(json);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String xml) {
        List<Employee> value = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xml));

            Node root = doc.getDocumentElement();
            NodeList staff = root.getChildNodes();
            for (int i = 0; i < staff.getLength(); i++) {
                Node node = staff.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList nodeList = node.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node node1 =  nodeList.item(j);
                        //String attr = node1.getNodeName();
                        value= node1.getTextContent();
                        //System.out.println(attr + ":" + value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

//    public static void writeString(String json) {
//        try (FileWriter file = new FileWriter("data2.json")) {
//            file.write(json);
//            file.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
