package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Beverage {
    String name;
    Map<String, Integer> recipe;
}