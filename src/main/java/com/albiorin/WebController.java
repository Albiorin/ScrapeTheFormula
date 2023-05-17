package com.albiorin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @EventListener(ApplicationReadyEvent.class)
    public void startup(){
        Main.scraper();
    }

    @GetMapping("/reload")
    public static void main(String[] args) {
        Main.scraper();
    }
}
