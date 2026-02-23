package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
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

    public BeneficioService(BeneficioRepository repository) {
        this.repository = repository;
    }

    public List<BeneficioResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public BeneficioResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        Beneficio beneficio = fromRequest(request);
        return toResponse(repository.save(beneficio));
    }

    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio current = getById(id);
        current.setNome(request.nome());
        current.setDescricao(request.descricao());
        current.setValor(request.valor());
        current.setAtivo(request.ativo());
        return toResponse(repository.save(current));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Benefício não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Origem e destino devem ser diferentes");
        }

        Beneficio from = getById(fromId);
        Beneficio to = getById(toId);

        if (!Boolean.TRUE.equals(from.getAtivo()) || !Boolean.TRUE.equals(to.getAtivo())) {
            throw new IllegalStateException("Só é permitido transferir entre benefícios ativos");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para transferência");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        repository.save(from);
        repository.save(to);
    }

    private Beneficio getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));
    }

    private Beneficio fromRequest(BeneficioRequest request) {
        Beneficio beneficio = new Beneficio();
        beneficio.setNome(request.nome());
        beneficio.setDescricao(request.descricao());
        beneficio.setValor(request.valor());
        beneficio.setAtivo(request.ativo());
        return beneficio;
    }

    private BeneficioResponse toResponse(Beneficio b) {
        return new BeneficioResponse(
                b.getId(),
                b.getNome(),
                b.getDescricao(),
                b.getValor(),
                b.getAtivo(),
                b.getVersion()
        );
    }
}
