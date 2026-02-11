package tatim.william.application.product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tatim.william.domain.product.Product;

import java.util.List;

@ApplicationScoped
class ProductRepository implements PanacheRepository<Product> {
    List<Product> findAllProductsWhitCompositions() {
        return find("""
                    SELECT DISTINCT p
                    FROM Product p
                    JOIN FETCH p.composition pc
                    JOIN FETCH pc.rawMaterial
                """).list();
    }

    boolean isRawMaterialUsed(Long rawMaterialId) {
        return getEntityManager()
                .createQuery("""
            SELECT COUNT(pc)
            FROM ProductComposition pc
            WHERE pc.rawMaterial.id = :rawMaterialId
        """, Long.class)
                .setParameter("rawMaterialId", rawMaterialId)
                .getSingleResult() > 0;
    }
}