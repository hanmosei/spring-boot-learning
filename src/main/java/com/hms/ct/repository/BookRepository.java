package com.hms.ct.repository;

import com.hms.ct.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 书籍数据仓储
 *
 * @author chentay
 * @date 2020-03-04 12:45:00
 */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

    BookEntity getBookEntityByAuthorAndName(String author, String name);

    List<BookEntity> getBookEntitiesByAuthorStartingWith(String author);

    List<BookEntity> getBookEntitiesByPriceGreaterThan(Float price);

    @Query(value = "select * from t_book where id = (select max(id) from t_book)", nativeQuery = true)
    BookEntity getMaxIdBook();

    @Query(value = "select b from t_book where b.id>:id and b.author=:author", nativeQuery = true)
    List<BookEntity> getBookListByIdAndAuthor(@Param("id") Long id, @Param("author") String author);

    @Query(value = "select b from t_book where b.id<?1 and b.name like %?2%", nativeQuery = true)
    List<BookEntity> getBookListByIdAndName(Long id, String name);
}
