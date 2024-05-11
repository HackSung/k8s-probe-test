package com.example.k8sprobetest;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ProbeTestController {

    String hostName;
    HttpStatus livenessHttpStatus = OK;

    @PostConstruct
    public void init() throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();
    }

    @GetMapping()
    public String hello() {
        return "Hello, I'm " + hostName;
    }

    @GetMapping("/probe/liveness")
    public ResponseEntity<Void> probeLiveness() {
        return new ResponseEntity<>(livenessHttpStatus);
    }

    @GetMapping("/probe/liveness/{status}")
    public ResponseEntity<String> setLivenessHttpStatus(@PathVariable Integer status) {
        livenessHttpStatus = HttpStatus.valueOf(status);
        return ResponseEntity.ok(hostName + "'s liveness status set to " + livenessHttpStatus);
    }

    @GetMapping("/probe/readiness")
    public ResponseEntity<Void> probeReadiness() {
        HttpStatus readinessHttpStatus = INTERNAL_SERVER_ERROR;
        String ReadinessFile = "readiness.ok";
        if (Files.exists(Paths.get(ReadinessFile))) {
            readinessHttpStatus = OK;
        }
        return new ResponseEntity<>(readinessHttpStatus);
    }

    @GetMapping("/probe/startup")
    public ResponseEntity<Void> probeStartup() {
        HttpStatus startupHttpStatus = INTERNAL_SERVER_ERROR;
        String StartupFile = "startup.ok";
        if (Files.exists(Paths.get(StartupFile))) {
            startupHttpStatus = OK;
        }
        return new ResponseEntity<>(startupHttpStatus);
    }
}