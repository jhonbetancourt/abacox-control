package com.infomedia.abacox.control.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class InitService {
    private final ModuleService moduleService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        moduleService.initModules();
    }
}
