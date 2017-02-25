package com.example.unifood.models;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Unit tests for Product
 */

public class ProductTest {

    private final String NAME = "Coxinha";
    private final String NEW_NAME = "Pastel";
    private final float COST = 3.9f;
    private final float NEW_COST = 2.5f;
    private final boolean AVAILABILITY = true;
    private final boolean NEW_AVAILABILITY = false;
    private final String DESCRIPTION = "Pastel recheado de queijo";
    private final String NEW_ID = "2447-9-4b11";
    private Product product;

    @Before
    public void setUp(){
        product = new Product(NAME, COST, AVAILABILITY);
    }

    @Test
    public void testConstructor(){
        assertEquals(NAME, product.getName());
        assertEquals(COST, product.getCost());
        assertTrue(product.isAvailability());
        assertTrue(product.getDescription().isEmpty());
    }

    @Test
    public void testEditProduct(){
        product.setName(NEW_NAME);
        product.setCost(NEW_COST);
        product.setAvailability(NEW_AVAILABILITY);
        product.setDescription(DESCRIPTION);
        product.setId(NEW_ID);

        assertEquals(NEW_NAME, product.getName());
        assertEquals(NEW_COST, product.getCost());
        assertEquals(NEW_AVAILABILITY, product.isAvailability());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(NEW_ID, product.getId());
    }
}