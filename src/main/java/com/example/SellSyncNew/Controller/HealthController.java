package com.example.SellSyncNew.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {

        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "SellSync Backend");

        return response;
    }
}
