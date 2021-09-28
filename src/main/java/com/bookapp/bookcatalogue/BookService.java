package com.bookapp.bookcatalogue;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    public static final String TARGET_SERVICE = "isbn-service";
    final BookRepository bookRepository;
    private WebClient client;
    //    private final WebClient client = WebClient.create("http://localhost:8080");
    final DiscoveryClient discoveryClient;

    public BookService(BookRepository bookRepository, DiscoveryClient discoveryClient) {
        this.bookRepository = bookRepository;
        this.discoveryClient = discoveryClient;
        createClient();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        var isbn = getBookISBN(book);
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    private String getBookISBN(Book book) {
        return client.get()
                .uri("/isbn")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void delete(Long id) {
        var person = bookRepository.findById(id);
        if (person.isEmpty()) throw new RuntimeException("NOT FOUND " + id);
        bookRepository.delete(person.get());
    }

    private void createClient() {
        var serviceInstanceList = discoveryClient.getInstances(TARGET_SERVICE);
        String clientURI = serviceInstanceList.get(0).getUri().toString();
        client = WebClient.create(clientURI);
    }
}
