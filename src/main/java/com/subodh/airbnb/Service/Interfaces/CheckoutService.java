package com.subodh.airbnb.Service.Interfaces;


import com.subodh.airbnb.Entities.BookingEntity;

public interface CheckoutService {

    String getCheckoutSession(BookingEntity booking, String successUrl, String failureUrl);

}
