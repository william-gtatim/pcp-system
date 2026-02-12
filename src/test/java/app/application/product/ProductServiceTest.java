package tatim.william.application.product;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;
import tatim.william.application.rawmaterial.RawMaterialService;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    @Mock
    RawMaterialService rawMaterialService;

    @InjectMocks
    ProductService service;

    Product product;
    ProductRequest request;
    RawMaterial rawMaterial;

    @BeforeEach
    void setup() {

        product = new Product();
        product.setComposition(new ArrayList<>());

        request = new ProductRequest(
                "Updated",
                new BigDecimal("20"),
                List.of(
                        new tatim.william.application.product.composition.dtos.ProductCompositionRequest(1L, 5f)
                )
        );

        rawMaterial = new RawMaterial();
    }

    @Test
    void shouldUpdateProductAndRebuildComposition() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        when(rawMaterialService.getByIdOrThrow(1L)).thenReturn(rawMaterial);
        when(mapper.toDto(product)).thenReturn(mock(ProductResponse.class));

        ProductResponse response = service.update(request, 1L);

        assertNotNull(response);
        assertEquals(1, product.getComposition().size());

        ProductComposition composition = product.getComposition().getFirst();
        assertEquals(product, composition.getProduct());
        assertEquals(rawMaterial, composition.getRawMaterial());
        assertEquals(5f, composition.getQuantityRequired());

        verify(mapper).updateEntity(request, product);
        verify(rawMaterialService).getByIdOrThrow(1L);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingProduct() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.update(request, 1L));
    }

    @Test
    void shouldDeleteProduct() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repository).delete(product);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingProduct() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1L));
    }

    @Test
    void shouldReturnProductResponseOnGet() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(mock(ProductResponse.class));

        ProductResponse response = service.get(1L);

        assertNotNull(response);
        verify(mapper).toDto(product);
    }

    @Test
    void shouldThrowWhenGettingNonExistingProduct() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.get(1L));
    }

    @Test
    void shouldListProducts() {

        List<Product> products = List.of(product);
        when(repository.findAllProductsWhitCompositions()).thenReturn(products);
        when(mapper.toDto(product)).thenReturn(mock(ProductResponse.class));

        List<ProductResponse> result = service.list();

        assertEquals(1, result.size());
        verify(mapper).toDto(product);
    }

    @Test
    void shouldCheckIfRawMaterialExistsInProducts() {

        when(repository.isRawMaterialUsed(1L)).thenReturn(true);

        boolean result = service.existsByRawMaterial(1L);

        assertTrue(result);
        verify(repository).isRawMaterialUsed(1L);
    }

    @Test
    void shouldReturnAllProductsWithCompositions() {

        List<Product> products = List.of(product);
        when(repository.findAllProductsWhitCompositions()).thenReturn(products);

        List<Product> result = service.getAllProductsWithCompositions();

        assertEquals(1, result.size());
        verify(repository).findAllProductsWhitCompositions();
    }

    @Test
    void shouldThrowWhenProductNotFoundInGetByIdOrThrow() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getByIdOrThrow(1L));
    }
}