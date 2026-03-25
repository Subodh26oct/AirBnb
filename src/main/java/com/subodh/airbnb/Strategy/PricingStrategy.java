package com.subodh.airbnb.Strategy;

import com.subodh.airbnb.Entities.InventoryEntity;

import java.math.BigDecimal;
public interface PricingStrategy {

    BigDecimal calculatePrice(InventoryEntity inventory);
}
