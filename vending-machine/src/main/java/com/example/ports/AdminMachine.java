package com.example.ports;

/**
 * Interface to be accessed by the Admins to refill the ingredients running low
 */
public interface AdminMachine {
    void fillStorage(String ingredient, int quantity);
}