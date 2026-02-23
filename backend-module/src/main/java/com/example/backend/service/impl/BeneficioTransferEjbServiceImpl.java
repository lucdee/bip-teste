package com.example.backend.service.impl;

import com.example.backend.exception.BeneficioNotFoundException;
import com.example.backend.exception.InactiveBeneficioException;
import com.example.backend.exception.InvalidTransferException;
import com.example.backend.exception.InsufficientBalanceException;
import com.example.backend.model.Beneficio;
import com.example.backend.service.BeneficioTransferService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BeneficioTransferEjbServiceImpl implements BeneficioTransferService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateTransferRequest(fromId, toId, amount);

        // SOLUÇÃO DO BUG
        // O OPTIMISTIC_FORCE_INCREMENT força o incremento da versão da entidade.
        // Assim, se duas transações tentarem alterar o mesmo registro ao mesmo tempo,
        // o JPA detecta o conflito e lança erro, evitando inconsistência de dados.

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Beneficio to = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        if (from == null) {
            throw new BeneficioNotFoundException(fromId);
        }
        if (to == null) {
            throw new BeneficioNotFoundException(toId);
        }

        if (!Boolean.TRUE.equals(from.getAtivo()) || !Boolean.TRUE.equals(to.getAtivo())) {
            throw new InactiveBeneficioException();
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.flush();
    }

    private void validateTransferRequest(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new InvalidTransferException("Origem e destino são obrigatórios");
        }

        if (fromId.equals(toId)) {
            throw new InvalidTransferException("Origem e destino devem ser diferentes");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Valor de transferência deve ser maior que zero");
        }
    }
}
