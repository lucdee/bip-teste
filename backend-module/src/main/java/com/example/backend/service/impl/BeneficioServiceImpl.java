package com.example.backend.service.impl;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.exception.BeneficioNotFoundException;
import com.example.backend.mapper.BeneficioMapper;
import com.example.backend.model.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.BeneficioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioServiceImpl implements BeneficioService {

    private final BeneficioRepository repository;
    private final BeneficioMapper mapper;

    public BeneficioServiceImpl(BeneficioRepository repository, BeneficioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BeneficioResponse> list() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    public BeneficioResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        Beneficio beneficio = mapper.toEntity(request);
        return mapper.toResponse(repository.save(beneficio));
    }

    @Override
    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio current = getById(id);
        mapper.updateEntityFromRequest(request, current);
        return mapper.toResponse(repository.save(current));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BeneficioNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Beneficio getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BeneficioNotFoundException(id));
    }
}
