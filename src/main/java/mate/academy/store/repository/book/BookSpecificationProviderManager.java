package mate.academy.store.repository.book;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.store.model.Book;
import mate.academy.store.repository.SpecificationProvider;
import mate.academy.store.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders
          .stream()
          .filter(provider -> provider.getKey().equals(key))
          .findFirst()
          .orElseThrow(() -> new NoSuchElementException("Can't find correct specification "
              + "provider for key: " + key));
    }
}
