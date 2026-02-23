package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;

import java.util.List;

public interface BeneficioService {

    List<BeneficioResponse> list();

    BeneficioResponse findById(Long id);

    BeneficioResponse create(BeneficioRequest request);

    BeneficioResponse update(Long id, BeneficioRequest request);

    void delete(Long id);
}
