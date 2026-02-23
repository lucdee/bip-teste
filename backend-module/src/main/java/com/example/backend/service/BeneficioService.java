package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.mapper.BeneficioMapper;
import com.example.backend.model.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository repository;
    private final BeneficioMapper mapper;
    private final BeneficioEjbService beneficioEjbService;

    public BeneficioService(BeneficioRepository repository, BeneficioMapper mapper, BeneficioEjbService beneficioEjbService) {
        this.repository = repository;
        this.mapper = mapper;
        this.beneficioEjbService = beneficioEjbService;
    }

    public List<BeneficioResponse> list() {
        return mapper.toResponseList(repository.findAll());
    }

    public BeneficioResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        Beneficio beneficio = mapper.toEntity(request);
        return mapper.toResponse(repository.save(beneficio));
    }

    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio current = getById(id);
        mapper.updateEntityFromRequest(request, current);
        return mapper.toResponse(repository.save(current));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Benefício não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        beneficioEjbService.transfer(fromId, toId, amount);
    }

    private Beneficio getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));
    }
}
