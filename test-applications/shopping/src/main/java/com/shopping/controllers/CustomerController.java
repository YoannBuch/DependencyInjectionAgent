package com.shopping.controllers;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RestController;

import com.shopping.services.ShoppingService;

@RestController
public class CustomerController {

	@Inject
	public CustomerController(ShoppingService shoppingService) {
	}
	
	
}
