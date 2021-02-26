package com.hms.learn.repository;

import com.hms.learn.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> getBookEntitiesByAuthorStartingWith(String author);

    List<BookEntity> getBookEntitiesByPriceGreaterThan(Float price);

    @Query(value = "select * from t_book where id = (select max(id) from t_book)", nativeQuery = true)
    BookEntity getMaxIdBook();

    @Query(value = "select b from t_book where b.id>:id and b.author=:author", nativeQuery = true)
    List<BookEntity> getBookListByIdAndAuthor(@Param("id") Long id, @Param("author") String author);

    @Query(value = "select b from t_book where b.id<?1 and b.name like %?2%", nativeQuery = true)
    List<BookEntity> getBookListByIdAndName(Long id, String name);
}
