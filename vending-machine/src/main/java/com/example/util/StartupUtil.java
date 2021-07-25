package com.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.model.Beverage;
import com.example.model.MachineImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utility class to startup machine using configuration details from a json file
 */
public class StartupUtil {
    public static MachineImpl startFromFile(File file) throws StartupException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree;
        try {
            tree = mapper.readTree(file);
        } catch (IOException e) {
            throw new StartupException("Exception occurred while loading Json file | " + e);
        }
        try {
            //Get Outlets
            int outlets = tree.get("machine").get("outlets").get("count_n").asInt();
            //Get Storage (total_items_quantity)
            Map<String, Integer> storage
                    = mapper.convertValue(tree.get("machine").get("total_items_quantity"), new TypeReference<Map<String,Integer>>(){});
            //Get Beverages
            Iterator<Map.Entry<String, JsonNode>> iter = tree.get("machine").get("beverages").fields();
            List<Beverage> beverages = new ArrayList<>();
            while (iter.hasNext()){
                Map.Entry<String, JsonNode> entry = iter.next();
                Beverage beverage = new Beverage();
                beverage.setName(entry.getKey());
                Map<String, Integer> ingredients = mapper.convertValue(entry.getValue(), new TypeReference<Map<String,Integer>>(){});
                beverage.setRecipe(ingredients);
                beverages.add(beverage);
            }
            return new MachineImpl(outlets, storage, beverages);
        } catch (NullPointerException e) {
            throw new StartupException("Incorrect Json file format | " + e);
        }
    }
}