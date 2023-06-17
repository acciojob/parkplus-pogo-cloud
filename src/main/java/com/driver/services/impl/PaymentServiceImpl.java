package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        if(mode.length()<3 || mode.length()>4 )throw new Exception("Payment mode not detected");
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int bill = reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();
        if(amountSent<bill)throw new Exception("Insufficient Amount");
        PaymentMode paymentMode = null;
        for(int i = 0;i<mode.length();i++){
            char ch = mode.charAt(i);
            if(i==0){
                if(ch=='c' || ch=='C')paymentMode=PaymentMode.CARD;
                else if(ch=='u' || ch=='U')paymentMode=PaymentMode.UPI;
                else throw new Exception("Payment mode not detected");
            }
            else if(i==1){
                if(ch=='a' || ch=='A')paymentMode=PaymentMode.CARD;
                else if(ch=='p' || ch=='P')paymentMode=PaymentMode.UPI;
                else throw new Exception("Payment mode not detected");
            }
            else if(i==2){
                if(ch=='r' || ch=='R')paymentMode=PaymentMode.CARD;
                if(ch=='s' || ch=='s')paymentMode=PaymentMode.CASH;
                else if(ch=='i' || ch=='I')paymentMode=PaymentMode.UPI;
                else throw new Exception("Payment mode not detected");
            }
            else if(i==3){
                if(ch=='d' || ch=='D')paymentMode=PaymentMode.CARD;
                if(ch=='h' || ch=='H')paymentMode=PaymentMode.CASH;
                else throw new Exception("Payment mode not detected");
            }
        }
        Payment payment = new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(Boolean.TRUE);
        payment.setReservation(reservation);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
        return payment;

    }
}
