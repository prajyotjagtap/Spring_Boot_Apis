package com.example.DealVPS.controller;
import org.springframework.ui.Model;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DealVPS.data.ItemRepository;
import com.example.DealVPS.model.Item;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/items")
public class ItemController 
{
    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    
    @PostMapping
    public ResponseEntity<String> addItem( @RequestBody Item item) {
    	
    	if ((item.getDescription() == null || item.getDescription().trim().isEmpty()) && (item.getName() == null || item.getName().trim().isEmpty())) {
            return new ResponseEntity<>("Item Name and Description is required", HttpStatus.BAD_REQUEST);
        }
        
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return new ResponseEntity<>("Item name is required", HttpStatus.BAD_REQUEST);
        }

        
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            return new ResponseEntity<>("Item description is required", HttpStatus.BAD_REQUEST);
        }
        
              
        itemRepository.addItem(item);

        return new ResponseEntity<>("Item added successfully", HttpStatus.CREATED);
    }
    
    
    
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable long itemId) {
        Item item = itemRepository.getItemById(itemId);

        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
    }
    
    
    @GetMapping("/addItem")
    public String showAddItemForm(Model model) {
        model.addAttribute("item", new com.example.DealVPS.model.Item());
        return "addItem";
    }

    
    
    @PostMapping("/saveItem")
    public String addItem(@ModelAttribute Item item, Model model, RedirectAttributes redirectAttributes) 
    {
        if (item.getName() == null || item.getName().trim().isEmpty() ||
            item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Item name and description are required");
            return "addItem";
        }

        // Add the item to the repository
        itemRepository.addItem(item);

        // Add a success message attribute
        redirectAttributes.addFlashAttribute("successMessage", "Item added successfully");
        
        // Log success message
        System.out.println("Item added successfully");

        // Redirect to the index page
        return "redirect:/items"; 
    }



    @GetMapping
    public String showIndexPage() {
        return "index";
    }
    

    
    @GetMapping("/viewItem")
    public String showViewItemOptions() {
        return "viewItemOptions";
    }

    @GetMapping("/viewAllItems")
    public String viewAllItems(Model model) {
        List<Item> allItems = itemRepository.getAllItems();
        model.addAttribute("items", allItems);
        return "viewAllItems";
    }

    @GetMapping("/viewItemById")
    public String showViewItemByIdForm() {
        return "viewItemById";
    }


    
    @GetMapping("/viewItemByIdResult")
    public String viewItemById(@RequestParam String itemId, Model model) {
        // Validate if itemId is a number
        try {
            long id = Long.parseLong(itemId);
            Item item = itemRepository.getItemById(id);

            if (item != null) {
                model.addAttribute("item", item);
                return "viewItemByIdResult";
            } else {
                model.addAttribute("errorMessage", "Item not found");
                return "viewItemByIdResult";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Please enter a valid item ID (numeric value)");
            return "viewItemByIdResult";
        }
    }	
    
    @GetMapping("/index")
    public String redirectToIndex() {
        return "redirect:/";
    }
}
