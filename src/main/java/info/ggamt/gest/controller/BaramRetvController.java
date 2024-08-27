package info.ggamt.gest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.ggamt.gest.dto.baram.BaramHistoryDTO;
import info.ggamt.gest.service.baram.BaramService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/api/baram")
public class BaramRetvController {

    @Autowired
    private BaramService BaramService;

    @GetMapping
    public BaramHistoryDTO getAllUsers() {
        return BaramService.getAllBaramHistory();
    }

    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
