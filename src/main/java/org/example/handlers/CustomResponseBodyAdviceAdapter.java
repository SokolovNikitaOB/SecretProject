package org.example.handlers;

import org.example.domain.Banners;
import org.example.domain.LogRecords;
import org.example.repository.LogRecordsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Banners> {

    @Autowired
    private LogRecordsRepo logRecordsRepo;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getName() == "get" ? true : false;
    }

    @Override
    public Banners beforeBodyWrite(Banners banners,
                                   MethodParameter methodParameter,
                                   MediaType mediaType,
                                   Class<? extends HttpMessageConverter<?>> aClass,
                                   ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse) {
        if(banners != null) {
            LogRecords log = new LogRecords();
            log.setBannerId(banners.getId());
            log.setBannerPrice(banners.getPrice());
            log.setRequestIpAddress(serverHttpRequest.getRemoteAddress().toString().substring(1));
            log.setUserAgent(serverHttpRequest.getHeaders().get("User-Agent").toString());
            log.setRequestTime(new Date());
            List<String> categoryRequestIds = getListCategoryRequestIds(serverHttpRequest.getURI().getQuery());
            log.setCategories(banners.getCategories().stream().filter(c -> categoryRequestIds.contains(c.getRequestId())).collect(Collectors.toList()));
            logRecordsRepo.save(log);
        }
        return banners;
    }

    public List<String> getListCategoryRequestIds(String query){
        List<String> categoryRequestIds = new ArrayList<>();
        String[] parameters = query.split("\\&");
        for(int i = 0; i < parameters.length; i++){
            if(parameters[i].substring(0,4).equals("cat=")) categoryRequestIds.add(parameters[i].substring(4));
        }
        return categoryRequestIds;

    }

}