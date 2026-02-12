package tatim.william.application.rawmaterial;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tatim.william.domain.rawmaterial.RawMaterial;

@ApplicationScoped
class RawMaterialRepository implements PanacheRepository<RawMaterial> {
}
