package org.example.handlers;

import org.example.domain.LogRecords;
import org.example.repository.LogRecordsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    LogRecordsRepo logRecordsRepo;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(response.getStatus() != 200) {
            LogRecords log = new LogRecords();
            log.setNoContentReason(String.valueOf(response.getStatus()));
            logRecordsRepo.save(log);
        }
    }

}
