package com.bank.transfer.filters;

import com.bank.transfer.notifications.ENotification;
import com.bank.transfer.notifications.NotificationContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class NotificationContextFilter implements ResponseBodyAdvice<Object> {

    private NotificationContext notificationContext;

    public NotificationContextFilter(@Autowired NotificationContext notificationContext) {
        this.notificationContext = notificationContext;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter arg1, MediaType arg2,
            Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest request, ServerHttpResponse response) {
        if (!this.notificationContext.getNotifications().isEmpty()) {
            var notification = this.notificationContext.getNotifications().get(0);
            if (notification.getNotificationType() == ENotification.NOT_EXISTS) {
                response.setStatusCode(HttpStatus.NOT_FOUND);
            }
            if (notification.getNotificationType() == ENotification.DUPLICATED) {
                response.setStatusCode(HttpStatus.CONFLICT);
            }
            if (notification.getNotificationType() == ENotification.VALIDATION) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
            }
            response.getHeaders().clear();
            return this.notificationContext.getNotifications();
        }
        return body;
    }

    @Override
    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
        return true;
    }
}