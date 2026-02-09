package tatim.william.application.product.composition;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tatim.william.domain.product.ProductComposition;

import java.util.List;

@ApplicationScoped
public class ProductCompositionRepository implements PanacheRepository<ProductComposition> {

    public List<ProductComposition> findByProductIdWithMaterials(Long productId) {
        return getEntityManager()
                .createQuery("""
                        select pc
                        from ProductComposition pc
                        join fetch pc.rawMaterial
                        where pc.product.id = :productId
                    """, ProductComposition.class)
                            .setParameter("productId", productId)
                            .getResultList();
    }
}
