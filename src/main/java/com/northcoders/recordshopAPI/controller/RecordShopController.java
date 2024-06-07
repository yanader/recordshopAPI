package com.northcoders.recordshopAPI.controller;

import com.northcoders.recordshopAPI.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/recordstore")
public class RecordShopController {

    @Autowired
    RecordShopService recordShopService;
}
