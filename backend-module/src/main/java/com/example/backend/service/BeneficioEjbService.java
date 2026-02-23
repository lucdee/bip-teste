package com.example.backend.service;

import com.example.backend.model.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateTransferRequest(fromId, toId, amount);

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Beneficio to = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        if (from == null || to == null) {
            throw new EntityNotFoundException("Benefício de origem ou destino não encontrado");
        }

        if (!Boolean.TRUE.equals(from.getAtivo()) || !Boolean.TRUE.equals(to.getAtivo())) {
            throw new IllegalStateException("Só é permitido transferir entre benefícios ativos");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para transferência");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.flush();
    }

    private void validateTransferRequest(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("Origem e destino são obrigatórios");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Origem e destino devem ser diferentes");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de transferência deve ser maior que zero");
        }
    }
}
