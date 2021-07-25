package com.example;

import com.example.ports.AdminMachine;
import com.example.ports.Machine;
import com.example.model.Beverage;
import com.example.model.MachineImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.example.util.StartupException;
import com.example.util.StartupUtil;

import java.io.File;
import java.util.List;

public class FunctionalTest {
    @Test
    void test1() throws StartupException, InterruptedException {
        File file = new File("src/test/resources/input.json");
        MachineImpl machineImpl = StartupUtil.startFromFile(file);
        // To demonstrate public usage of machine...
        List<Beverage> beverages = ((Machine) machineImpl).getBeverages();
        ((Machine) machineImpl).startServing(beverages);
        Thread.sleep(100);
        // To demonstrate admin usage of machine...
        ((AdminMachine) machineImpl).fillStorage("hot_water", 400);
    }

    @Test
    void test2() {
        File file = new File("src/test/resources/input1.json");
        Assertions.assertThrows(StartupException.class, () -> StartupUtil.startFromFile(file));
    }
}