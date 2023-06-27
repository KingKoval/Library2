package com.sbego.library2.services;

import com.sbego.library2.models.Book;
import com.sbego.library2.models.Person;
import com.sbego.library2.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService{

    private final long MILLISECONDS_OF_10DAYS = 864000;

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        return person.orElse(null);
    }

    public List<Book> getBooksByPerson(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if(person.isPresent()){
            person.get().getBooks().forEach(book -> {
                if(book.getTimeOfReservation() != null) {
                    long difference = new Date().getTime() - book.getTimeOfReservation().getTime();

                    if(difference > MILLISECONDS_OF_10DAYS){
                        book.setExpired(true);
                    }
                }
            });
        }

        return person.get().getBooks();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);

        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
