package com.example.ejb;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateTransferRequest(fromId, toId, amount);

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Beneficio to = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        if (from == null || to == null) {
            throw new EJBException("Benefício de origem ou destino não encontrado");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new EJBException("Saldo insuficiente para transferência");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.flush();
    }

    private void validateTransferRequest(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new EJBException("Origem e destino são obrigatórios");
        }

        if (fromId.equals(toId)) {
            throw new EJBException("Origem e destino devem ser diferentes");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new EJBException("Valor de transferência deve ser maior que zero");
        }
    }
}
