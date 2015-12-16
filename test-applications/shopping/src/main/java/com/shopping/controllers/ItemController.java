package com.shopping.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.services.ShoppingService;

@RestController
public class ItemController {

	@Autowired
	public ItemController(ShoppingService service) {
	}
}
