package tatim.william.domain.rawmaterial;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "raw_material")
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name="code", nullable = false, unique = true)
    private String code;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="stock_quantity", nullable = false)
    private Long stockQuantity;

}
