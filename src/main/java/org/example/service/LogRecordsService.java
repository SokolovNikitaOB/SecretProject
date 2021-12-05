package org.example.service;

import org.example.domain.Banners;
import org.example.domain.LogRecords;
import org.example.repository.BannersRepo;
import org.example.repository.LogRecordsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogRecordsService {

    @Autowired
    LogRecordsRepo logRecordsRepo;

    @Autowired
    BannersRepo bannersRepo;

    public List<LogRecords> getAllLogRecords(){
        Iterable<LogRecords> logRecordsIterable = logRecordsRepo.findAll();
        ArrayList<LogRecords> logRecords = new ArrayList<>();
        for(LogRecords lr: logRecordsIterable){
            logRecords.add(lr);
        }
        if(logRecords.isEmpty()){
            return null;
        }
        return logRecords;
    }

    public List<Banners> getNotDisplayedBanners(){
        List<LogRecords> lastDayLogs = getAllLogRecords()
                .stream()
                .filter(l -> l.getNoContentReason() == null && getDay(new Date().getTime() - l.getRequestTime().getTime()) <= 1)
                .collect(Collectors.toList());
        List<Banners> lastDayBanners = new ArrayList<>();
        for(LogRecords l : lastDayLogs){
            lastDayBanners.add(bannersRepo.findById(l.getBannerId()).get());
        }
        return lastDayBanners;
    }

    public Double getDay(Long milliseconds){
        return milliseconds / 1000. / 60. / 60. / 24.;
    }

}
