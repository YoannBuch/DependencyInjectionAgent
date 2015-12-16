package com.shopping.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.shopping.repositories.CustomerRepository;
import com.shopping.repositories.ItemRepository;

@Service
public class ShoppingServiceImpl implements ShoppingService {

	@Inject
	public ShoppingServiceImpl(ItemRepository itemRepository, CustomerRepository customerRepository) {
		
	}
}
