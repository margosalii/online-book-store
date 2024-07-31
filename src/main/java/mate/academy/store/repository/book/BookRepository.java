package mate.academy.store.repository.book;

import java.util.List;
import mate.academy.store.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "categories")
    @Query("FROM Book b JOIN FETCH b.categories c WHERE c.id = :id")
    List<Book> findAllByCategoryId(@Param("id") Long categoryId, Pageable pageable);
}
