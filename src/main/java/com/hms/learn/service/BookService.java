package com.hms.learn.service;

import com.hms.learn.domain.BookEntity;
import com.hms.learn.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RollbackException.class)
    public BookEntity addBook(BookEntity bookEntity) {
        return bookRepository.saveAndFlush(bookEntity);
    }

    public Page<BookEntity> getBookByPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<BookEntity> getBookEntitiesByAuthorStartingWith(String author) {
        return bookRepository.getBookEntitiesByAuthorStartingWith(author);
    }

    public List<BookEntity> getBookEntitiesByPriceGreaterThan(Float price) {
        return bookRepository.getBookEntitiesByPriceGreaterThan(price);
    }

    public BookEntity getMaxIdBook() {
        return bookRepository.getMaxIdBook();
    }

    public List<BookEntity> getBookListByIdAndAuthor(Long id, String author) {
        return bookRepository.getBookListByIdAndAuthor(id, author);
    }

    public List<BookEntity> getBookListByIdAndName(Long id, String name) {
        return bookRepository.getBookListByIdAndName(id, name);
    }
}
