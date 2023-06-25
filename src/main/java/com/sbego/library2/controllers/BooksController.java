package com.sbego.library2.controllers;

import com.sbego.library2.models.Book;
import com.sbego.library2.models.Person;
import com.sbego.library2.services.BooksService;
import com.sbego.library2.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") boolean sortByYear) {

        if(page != null && booksPerPage != null)
            model.addAttribute("books", booksService.findAllPages(page, booksPerPage, sortByYear));
        else
            model.addAttribute("books", booksService.findAll(sortByYear));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        Book book = booksService.findOne(id);

        model.addAttribute("book", book);

        if(book.getPerson() != null)
            model.addAttribute("person", book.getPerson());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @PatchMapping("/{id}/reservation")
    public String reservation(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.makeReservation(id, person);

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable("id") int id) {
        booksService.returnBook(id);

        return "redirect:/books/" + id;
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());

        return "books/new";
    }

    @PostMapping
    public String createBook(@ModelAttribute("book") Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("book") Book book,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);

        return "redirect:/books/";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }
}
