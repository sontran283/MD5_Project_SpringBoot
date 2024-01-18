package com.ra.service;

import com.ra.model.entity.Orders;
import com.ra.model.entity.User;

public interface EmailService {
    String sendMail(User user, Orders orders);

    String sendThanks(String email, Orders orders);
}
