package com.sbego.library2.services;

import com.sbego.library2.models.Book;
import com.sbego.library2.models.Person;
import com.sbego.library2.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findAllPages(int page, int booksPerPage, boolean sortByYear) {
        if(sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> book = booksRepository.findById(id);

        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);

        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void makeReservation(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        book.setTimeOfReservation(new Date());
        book.setPerson(person);
    }

    @Transactional
    public void returnBook(int id) {
        Book book = booksRepository.findById(id).get();
        book.setTimeOfReservation(null);
        book.setPerson(null);
    }

    public List<Book> searchBook(String query) {
        return booksRepository.findByTitleStartingWith(query);
    }

}
