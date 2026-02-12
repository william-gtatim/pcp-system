package tatim.william.application.product;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tatim.william.application.DuplicatedeCodeException;
import tatim.william.application.GenerateCodeService;
import tatim.william.application.product.composition.dtos.ProductCompositionRequest;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;
import tatim.william.application.rawmaterial.RawMaterialService;
import tatim.william.domain.product.Product;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    @Mock
    GenerateCodeService codeService;

    @Mock
    RawMaterialService rawMaterialService;

    @InjectMocks
    CreateProductUseCase useCase;

    ProductRequest request;
    RawMaterial rawMaterial;

    @BeforeEach
    void setup() {
        request = new ProductRequest(
                "Produto",
                new BigDecimal("10.00"),
                List.of(
                        new ProductCompositionRequest(1L, 2f)
                )
        );

        rawMaterial = new RawMaterial();
    }

    private Product newProductInstance() {
        Product product = new Product();
        product.setComposition(new ArrayList<>());
        return product;
    }

    @Test
    void shouldCreateProductSuccessfully() {

        Product product = newProductInstance();

        when(mapper.toEntity(request)).thenReturn(product);
        when(codeService.generate()).thenReturn("123");
        when(rawMaterialService.getByIdOrThrow(1L)).thenReturn(rawMaterial);
        when(mapper.toDto(product)).thenReturn(mock(ProductResponse.class));

        ProductResponse response = useCase.create(request);

        assertNotNull(response);
        assertEquals("P-123", product.getCode());
        assertEquals(1, product.getComposition().size());

        verify(repository).persist(product);
        verify(codeService).generate();
        verify(rawMaterialService).getByIdOrThrow(1L);
    }

    @Test
    void shouldRetryWhenConstraintViolationOccurs() {

        Product firstAttempt = newProductInstance();
        Product secondAttempt = newProductInstance();

        when(mapper.toEntity(request))
                .thenReturn(firstAttempt)
                .thenReturn(secondAttempt);

        when(codeService.generate())
                .thenReturn("1")
                .thenReturn("2");

        when(rawMaterialService.getByIdOrThrow(1L)).thenReturn(rawMaterial);

        doThrow(new ConstraintViolationException("error", new SQLException(), null))
                .doNothing()
                .when(repository)
                .persist(any(Product.class));

        when(mapper.toDto(secondAttempt)).thenReturn(mock(ProductResponse.class));

        ProductResponse response = useCase.create(request);

        assertNotNull(response);

        verify(repository, times(2)).persist(any(Product.class));
        verify(codeService, times(2)).generate();
    }

    @Test
    void shouldThrowDuplicatedCodeExceptionAfterMaxRetries() {

        when(mapper.toEntity(request)).thenAnswer(invocation -> newProductInstance());
        when(codeService.generate()).thenReturn("fail");
        when(rawMaterialService.getByIdOrThrow(1L)).thenReturn(rawMaterial);

        doThrow(new ConstraintViolationException("error", new SQLException(), null))
                .when(repository)
                .persist(any(Product.class));

        assertThrows(DuplicatedeCodeException.class,
                () -> useCase.create(request));

        verify(repository, times(10)).persist(any(Product.class));
        verify(codeService, times(10)).generate();
    }
}