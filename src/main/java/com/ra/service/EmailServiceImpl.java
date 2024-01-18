package com.ra.service;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.OrderDetailRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @Override
    public String sendThanks(String email, Orders orders) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("jav2306@gmail.com");
            simpleMailMessage.setTo(orders.getUser().getEmail());
            simpleMailMessage.setText("Thank you for your purchase");
            simpleMailMessage.setSubject("COGO fresh fruit store");
            javaMailSender.send(simpleMailMessage);
            return "OK sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String sendMail(User userLogin, Orders orders) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("jav2306@gmail.com");
            helper.setTo(userLogin.getEmail());
            helper.setSubject("Thank you for your purchase");

            // Tạo nội dung email
            String emailContent = createEmailContent(orders);

            helper.setText(emailContent, true);

            javaMailSender.send(message);

            return "Gửi mail thành công";
        } catch (Exception e) {
            e.printStackTrace();
            return "Gửi mail thất bại: " + e.getMessage();
        }
    }

    private String createEmailContent(Orders orders) {
        StringBuilder emailContentBuilder = new StringBuilder();
        emailContentBuilder.append("<html><body>");
        emailContentBuilder.append("<h2 style=\"color: #4caf50;\">Thank you for your Order!</h2>");
        emailContentBuilder.append("<p>Below are your order details:</p>");
        emailContentBuilder.append("<table style=\"border-collapse: collapse; width: 100%;\" border='1' cellpadding='10'>");
        emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">UserName</th><td>").append(orders.getUser().getUserName()).append("</td></tr>");
        emailContentBuilder.append("<tr><th style=\"background-color: #f2f2f2; text-align: left;\">OrderDate</th><td>").append(orders.getOrder_date()).append("</td></tr>");
        emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">PhoneNumber</th><td>").append(orders.getPhone()).append("</td></tr>");
        emailContentBuilder.append("<tr><th style=\"background-color: #f2f2f2; text-align: left;\">Address</th><td>").append(orders.getAddress()).append("</td></tr>");

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String formattedAmount = decimalFormat.format(orders.getTotal());
        emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">Total</th><td>").append(formattedAmount).append(" VND</td></tr>");

        List<OrderDetail> orderDetailsList = orderDetailRepository.findAllByOrders_Id(orders.getId());
        if (orderDetailsList != null) {
            for (OrderDetail orderDetail : orderDetailsList) {
                emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">ProductName</th><td>").append(orderDetail.getProduct().getName()).append("</td></tr>");
                emailContentBuilder.append("<tr><th style=\"background-color: #f2f2f2; text-align: left;\">Quantity</th><td>").append(orderDetail.getQuantity()).append("</td></tr>");// Thêm các thông tin khác tùy thuộc vào cấu trúc của OrderDetail
            }
        }

        emailContentBuilder.append("</table>");
        emailContentBuilder.append("</body></html>");
        return emailContentBuilder.toString();
    }
}
