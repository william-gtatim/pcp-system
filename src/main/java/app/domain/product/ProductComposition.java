package tatim.william.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tatim.william.domain.DomainException;
import tatim.william.domain.rawmaterial.RawMaterial;

@Entity
@Table(name = "product_composition")
@Getter
@Setter
public class ProductComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "quantity_required", nullable = false)
    private float quantityRequired;

    public void setQuantityRequired(float value){
        if(value <= 0.0f){
            throw new DomainException("A quantidade necessária deve ser maior que zero");
        }

        quantityRequired = value;
    }

}
