package com.example.backend.controller;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferRequest;
import com.example.backend.service.BeneficioService;
import com.example.backend.service.BeneficioTransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioService service;
    private final BeneficioTransferService serviceTransfer;

    public BeneficioController(BeneficioService service, BeneficioTransferService serviceTransfer) {
        this.service = service;
        this.serviceTransfer = serviceTransfer;
    }

    @GetMapping
    public List<BeneficioResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BeneficioResponse find(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficioResponse create(@Valid @RequestBody BeneficioRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BeneficioResponse update(@PathVariable Long id, @Valid @RequestBody BeneficioRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@Valid @RequestBody TransferRequest request) {
        serviceTransfer.transfer(request.fromId(), request.toId(), request.amount());
    }
}
