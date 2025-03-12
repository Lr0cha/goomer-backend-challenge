package com.api.goomer.web.validations.offer;

import com.api.goomer.web.dtos.product.ProductCreateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OfferFieldsValidator implements ConstraintValidator<OfferFieldsRequiredIfOnOffer, ProductCreateDto> {

    @Override
    public boolean isValid(ProductCreateDto value, ConstraintValidatorContext context) {
        if (value.isOnOffer()) {
            return value.promotionalDescription() != null
                    && value.promotionalPrice() != null
                    && value.promotionalDays() != null
                    && value.promotionalTime() != null;
        }
        return true;
    }
}

