package com.example.backend.mapper;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.model.Beneficio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeneficioMapper {

    Beneficio toEntity(BeneficioRequest request);

    BeneficioResponse toResponse(Beneficio entity);

    List<BeneficioResponse> toResponseList(List<Beneficio> entities);

    void updateEntityFromRequest(BeneficioRequest request, @MappingTarget Beneficio entity);
}
