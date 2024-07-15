package mate.academy.store.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.BookSearchParameters;
import mate.academy.store.model.Book;
import mate.academy.store.repository.SpecificationBuilder;
import mate.academy.store.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
              .getSpecificationProvider("author")
              .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
              .getSpecificationProvider("title")
              .getSpecification(searchParameters.titles()));
        }
        return specification;
    }
}
