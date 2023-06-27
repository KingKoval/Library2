package com.sbego.library2.controllers;

import com.sbego.library2.models.Person;
import com.sbego.library2.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);

        model.addAttribute("person", person);
        model.addAttribute("books", peopleService.getBooksByPerson(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping
    public String createPerson(@ModelAttribute("person") Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable("id") int id,
                               @ModelAttribute("person") Person person,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }
}
