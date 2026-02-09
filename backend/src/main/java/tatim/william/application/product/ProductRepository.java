package tatim.william.application.product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tatim.william.domain.product.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

}
