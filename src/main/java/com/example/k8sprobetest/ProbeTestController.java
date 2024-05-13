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

    private String hostName;
    private HttpStatus livenessHttpStatus = OK;

    @PostConstruct
    public void init() throws UnknownHostException {
        hostName = InetAddress.getLocalHost().getHostName();
    }

    @GetMapping()
    public String hello() {
        return "Hello, I'm " + hostName + "\n";
    }

    @GetMapping("/probe/liveness")
    public ResponseEntity<Void> probeLiveness() {
        return new ResponseEntity<>(livenessHttpStatus);
    }

    @GetMapping("/probe/liveness/{status}")
    public ResponseEntity<String> setLivenessHttpStatus(@PathVariable Integer status) {
        livenessHttpStatus = HttpStatus.valueOf(status);
        return ResponseEntity.ok(hostName + "'s liveness http status set to " + livenessHttpStatus + "\n");
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

    @GetMapping("/cpu-load")
    public ResponseEntity<String> generateCpuLoad() {
        for (int i = 0; i < 100000000; i++) {
            Math.tan(Math.random());
            Math.cos(Math.random());
            Math.sin(Math.random());
            Math.sqrt(Math.random());
        }
        return ResponseEntity.ok(hostName + ": CPU load generated");
    }
}
