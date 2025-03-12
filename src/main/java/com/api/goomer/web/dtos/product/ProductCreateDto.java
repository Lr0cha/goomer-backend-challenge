package com.api.goomer.web.dtos.product;

import com.api.goomer.web.validations.offer.OfferFieldsRequiredIfOnOffer;
import com.api.goomer.web.validations.promotionalTime.ValidTimeInterval;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@OfferFieldsRequiredIfOnOffer
public record ProductCreateDto(
        @NotBlank @URL
        String urlImage,
        @NotBlank @Size(min=3,max=100)
        String productName,
        @NotNull @Positive
        BigDecimal price,
        @NotNull @Min(1)
        Long categoryId,
        @NotNull
        Boolean isOnOffer,
        @Size(min=10, max=100)
        String promotionalDescription,
        @Positive
        BigDecimal promotionalPrice,
        @Pattern(regexp = "^(Segunda|Terça|Quarta|Quinta|Sexta|Sábado|Domingo)(,\\s*(Segunda|Terça|Quarta|Quinta|Sexta|Sábado|Domingo))*$",
                message = "Os dias promocionais devem ser uma lista válida de dias da semana (ex: Segunda, Terça, Quarta).")
        String promotionalDays,
        @ValidTimeInterval
        LocalTime[] promotionalTime,
        @NotNull
        UUID restaurantId
) {

}
