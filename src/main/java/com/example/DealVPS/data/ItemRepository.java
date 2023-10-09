package com.example.DealVPS.data;

import java.util.List;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

import com.example.DealVPS.model.Item;

@Component
public class ItemRepository 
{
    private static List<Item> items = new ArrayList<>();
    
    private static long nextId = 1;

    public List<Item> getAllItems() 
    {
        return new ArrayList<>(items);
    }

    public Item getItemById(long id) 
    {
        for (Item item : items) 
        {
            if (item.getId() == id) 
            {
                return item;
            }
        }
        return null;
    }

    public void addItem(Item item) 
    {
        item.setId(nextId++);
        items.add(item);
    }
}