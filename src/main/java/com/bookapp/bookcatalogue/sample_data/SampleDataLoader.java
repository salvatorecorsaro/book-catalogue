package com.bookapp.bookcatalogue.sample_data;

import com.bookapp.bookcatalogue.Book;
import com.bookapp.bookcatalogue.BookRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final Faker faker;

    public SampleDataLoader(BookRepository bookRepository, Faker faker) {
        this.bookRepository = bookRepository;
        this.faker = faker;
    }

    @Override
    public void run(String... args) {

        List<Book> sampleBooks = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new Book(
                        faker.book().title(),
                        faker.book().author(),
                        faker.book().genre(),
                        faker.code().isbn10()
                )).collect(Collectors.toList());

        bookRepository.saveAll(sampleBooks);

    }

}
