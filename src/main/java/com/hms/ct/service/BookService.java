package com.hms.ct.service;

import com.hms.ct.domain.BookEntity;
import com.hms.ct.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;
import java.util.List;

/**
 * 书籍业务
 *
 * @author chentay
 * @date 2021-03-04 12:48:21
 */
@Service
@CacheConfig(cacheNames = "book_cache")
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @CacheEvict(key = "getTargetClass()", allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RollbackException.class)
    public BookEntity addBook(BookEntity bookEntity) {
        return bookRepository.saveAndFlush(bookEntity);
    }

    @CachePut(key = "#p0.id")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RollbackException.class)
    public BookEntity updateBookById(BookEntity bookEntity) {
        return bookRepository.saveAndFlush(bookEntity);
    }

    @Cacheable(key = "#p0")
    public BookEntity getBookEntityById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Cacheable
    public BookEntity getBookEntityByAuthorAndName(String author, String name) {
        return bookRepository.getBookEntityByAuthorAndName(author, name);
    }

    @Cacheable(key = "getTargetClass()")
    public Page<BookEntity> getBookByPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Cacheable
    public List<BookEntity> getBookEntitiesByAuthorStartingWith(String author) {
        return bookRepository.getBookEntitiesByAuthorStartingWith(author);
    }

    @Cacheable
    public List<BookEntity> getBookEntitiesByPriceGreaterThan(Float price) {
        return bookRepository.getBookEntitiesByPriceGreaterThan(price);
    }

    @Cacheable
    public BookEntity getMaxIdBook() {
        return bookRepository.getMaxIdBook();
    }

    @Cacheable
    public List<BookEntity> getBookListByIdAndAuthor(Long id, String author) {
        return bookRepository.getBookListByIdAndAuthor(id, author);
    }

    @Cacheable
    public List<BookEntity> getBookListByIdAndName(Long id, String name) {
        return bookRepository.getBookListByIdAndName(id, name);
    }
}
