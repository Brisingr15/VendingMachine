package com.example.model;

import com.example.ports.AdminMachine;
import com.example.ports.Machine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class MachineImpl implements Machine, AdminMachine {
    int outlets;
    Map<String, Integer> storage;
    List<Beverage> beverages;

    public MachineImpl(int outlets, Map<String, Integer> storage, List<Beverage> beverages) {
        this.outlets = outlets;
        this.storage = storage;
        this.beverages = beverages;
    }

    /**
     * Refills the given ingredient in the machine storage
     * @param ingredient
     * @param quantity
     */
    @Override
    public void fillStorage(String ingredient, int quantity) {
        int stored_amount = storage.getOrDefault(ingredient,0);
        int total_amount = stored_amount+quantity;
        storage.put(ingredient, total_amount);
        System.out.println(ingredient + " refilled from " + stored_amount +" to " + total_amount);
    }

    /**
     * Gives list of beverages the machine can serve
     * @return Beverages
     */
    @Override
    public List<Beverage> getBeverages() {
        return beverages;
    }

    /**
     * Serves given list of beverages in parallel based on the number of outlets available
     * @param beverages
     */
    @Override
    public void startServing(List<Beverage> beverages) {
        ForkJoinPool customThreadPool = new ForkJoinPool(outlets);
        customThreadPool.submit(
                () -> beverages.parallelStream().forEach(this::serveBeverage));
        customThreadPool.shutdown();
    }

    /**
     * Serves given beverage
     * @param beverage
     */
    private void serveBeverage(Beverage beverage) {
        if (isPossible(beverage)) {
            Map<String, Integer> recipe = beverage.getRecipe();
            for (String ingredient: recipe.keySet()) {
                int stored_amount = storage.get(ingredient);
                storage.replace(ingredient, stored_amount-recipe.get(ingredient));
            }
            System.out.println(beverage.getName() + " is prepared");
        }
    }

    /**
     * Checks if given beverage can be prepared based on the stored quantity of ingredients
     * @param beverage
     * @return boolean
     */
    private boolean isPossible(Beverage beverage) {
        Map<String, Integer> recipe = beverage.getRecipe();
        Set<String> ingredients = recipe.keySet();
        for (String ingredient: ingredients) {
            if (storage.containsKey(ingredient)) {
                int stored_amount = storage.get(ingredient);
                if (stored_amount < recipe.get(ingredient)) {
                    System.out.println(beverage.getName() + " cannot be prepared because item " + ingredient +" is not sufficient");
                    return false;
                }
            }
            else {
                System.out.println(beverage.getName() + " cannot be prepared because "+ ingredient + " is not available");
                return false;
            }
        }
        return true;
    }
}