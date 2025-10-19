import java.util.Optional;
import com.cafepos.catalog.Product;

public interface Catalog {
    void add(Product p);

    Optional<Product> findById(String id);
}

