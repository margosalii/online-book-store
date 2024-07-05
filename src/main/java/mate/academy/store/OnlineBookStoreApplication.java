package mate.academy.store;

import java.math.BigDecimal;
import mate.academy.store.model.Book;
import mate.academy.store.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    private final BookService bookService;

    @Autowired
    public OnlineBookStoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book newBook = new Book();
                newBook.setTitle("The Witcher");
                newBook.setPrice(BigDecimal.valueOf(260));
                newBook.setIsbn("isbn");
                newBook.setAuthor("Anjey Sapkovskiy");

                bookService.save(newBook);
                System.out.println(bookService.findAll());
            }
        };
    }
}
