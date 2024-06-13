package com.dawidrozewski.sandbox.homepage.controller.dto;

import com.dawidrozewski.sandbox.common.model.Product;

import java.util.List;

public record HomepageDto (List<Product> products) {}

