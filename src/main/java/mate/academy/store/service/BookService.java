package mate.academy.store.service;

import java.util.List;
import mate.academy.store.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
