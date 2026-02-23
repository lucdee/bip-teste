package com.example.backend.mapper;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.model.Beneficio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-23T16:25:07-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class BeneficioMapperImpl implements BeneficioMapper {

    @Override
    public Beneficio toEntity(BeneficioRequest request) {
        if ( request == null ) {
            return null;
        }

        Beneficio beneficio = new Beneficio();

        beneficio.setNome( request.nome() );
        beneficio.setDescricao( request.descricao() );
        beneficio.setValor( request.valor() );
        beneficio.setAtivo( request.ativo() );

        return beneficio;
    }

    @Override
    public BeneficioResponse toResponse(Beneficio entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String descricao = null;
        BigDecimal valor = null;
        Boolean ativo = null;
        Long version = null;

        id = entity.getId();
        nome = entity.getNome();
        descricao = entity.getDescricao();
        valor = entity.getValor();
        ativo = entity.getAtivo();
        version = entity.getVersion();

        BeneficioResponse beneficioResponse = new BeneficioResponse( id, nome, descricao, valor, ativo, version );

        return beneficioResponse;
    }

    @Override
    public List<BeneficioResponse> toResponseList(List<Beneficio> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BeneficioResponse> list = new ArrayList<BeneficioResponse>( entities.size() );
        for ( Beneficio beneficio : entities ) {
            list.add( toResponse( beneficio ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(BeneficioRequest request, Beneficio entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setDescricao( request.descricao() );
        entity.setValor( request.valor() );
        entity.setAtivo( request.ativo() );
    }
}
