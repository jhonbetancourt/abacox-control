package com.infomedia.abacox.control.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitService {
    private final ModuleService moduleService;



    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        moduleService.initModules();
    }
}
