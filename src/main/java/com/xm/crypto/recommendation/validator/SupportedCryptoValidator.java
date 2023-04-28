package com.xm.crypto.recommendation.validator;

import com.xm.crypto.recommendation.service.RecommendationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator whether application supports crypto symbol.
 * Validator is dynamic based on content of CSV files.
 */
public class SupportedCryptoValidator implements ConstraintValidator<SupportedCrypto, String> {

    private final RecommendationService recommendationService;

    public SupportedCryptoValidator(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return recommendationService.isSupported(value);
    }
}
