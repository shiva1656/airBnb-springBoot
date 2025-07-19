package com.projects.airBnbApp.strategy;

import com.projects.airBnbApp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
