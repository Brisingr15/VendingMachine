package com.example.ports;

import com.example.model.Beverage;

import java.util.List;

/**
 * Interface to be accessed by the Customers to get list of Beverages available and to serve Beverages
 */
public interface Machine {
    void startServing(List<Beverage> beverages);
    List<Beverage> getBeverages();
}