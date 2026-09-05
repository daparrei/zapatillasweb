package com.example.zapatillasweb.api;

import java.time.OffsetDateTime;
import java.util.Date;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;


@Service
public class WorldclockApi {
    
    public  Date getCurrentTime() {
        try {
            String url = "http://worldclockapi.com/api/json/utc/now";
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                String fechaString = response
                        .split("\"currentDateTime\":\"")[1]
                        .split("\"")[0];

                return Date.from(
                        OffsetDateTime.parse(fechaString).toInstant()
                );
            } else {
                return new Date();
            }
        
        } catch (Exception e) {
           
            System.out.println(
                    "Error al obtener fecha desde WorldClockAPI. Se utilizará fecha local."
            );
            
            return new Date();
        }
    }

}
