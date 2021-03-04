package com.hms.ct.api.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hms.ct.domain.BookEntity;
import com.hms.ct.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 书籍api
 *
 * @author chentay
 * @date 2020-03-04 14:13:08
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody BookEntity request) {
        BookEntity result = bookService.getBookEntityByAuthorAndName(request.getAuthor(), request.getName());
        JSONObject json = new JSONObject();
        if (result != null) {
            json.put("code", 105);
            json.put("message", "The book is existed");
        } else {
            result = bookService.addBook(request);
            log.info("【BookController】save result: {}", JSON.toJSONString(result));
            if (result != null) {
                json.put("code", 200);
                json.put("message", "OK");
            } else {
                json.put("code", 201);
                json.put("message", "Failure");
            }
        }
        return ResponseEntity.ok(json);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookEntityPage = bookService.getBookByPage(pageable);
        log.info("【BookController】findAll result: {}", JSON.toJSONString(bookEntityPage));
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "ok");
        json.put("data", bookEntityPage);
        return ResponseEntity.ok(json);
    }
}
