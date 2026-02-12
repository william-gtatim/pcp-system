package tatim.william.application.rawmaterial;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tatim.william.application.product.ProductService;
import tatim.william.application.rawmaterial.dtos.RawMaterialRequest;
import tatim.william.application.rawmaterial.dtos.RawMaterialResponse;
import tatim.william.domain.DomainException;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    RawMaterialRepository repository;

    @Mock
    RawMaterialMapper mapper;

    @Mock
    ProductService productService;

    @InjectMocks
    RawMaterialService service;

    RawMaterial rawMaterial;
    RawMaterialRequest request;

    @BeforeEach
    void setup() {
        rawMaterial = new RawMaterial();
        request = mock(RawMaterialRequest.class);
    }

    @Test
    void shouldReturnRawMaterialById() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(rawMaterial));
        when(mapper.toDto(rawMaterial)).thenReturn(mock(RawMaterialResponse.class));

        RawMaterialResponse response = service.get(1L);

        assertNotNull(response);
        verify(mapper).toDto(rawMaterial);
    }

    @Test
    void shouldThrowWhenRawMaterialNotFoundOnGet() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.get(1L));
    }

    @Test
    void shouldUpdateRawMaterial() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(rawMaterial));
        when(mapper.toDto(rawMaterial)).thenReturn(mock(RawMaterialResponse.class));

        RawMaterialResponse response = service.update(request, 1L);

        assertNotNull(response);

        verify(mapper).updateEntity(request, rawMaterial);
        verify(repository).persist(rawMaterial);
        verify(mapper).toDto(rawMaterial);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingRawMaterial() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.update(request, 1L));
    }

    @Test
    void shouldListRawMaterials() {

        when(repository.listAll()).thenReturn(List.of(rawMaterial));
        when(mapper.toDto(rawMaterial)).thenReturn(mock(RawMaterialResponse.class));

        List<RawMaterialResponse> result = service.list();

        assertEquals(1, result.size());
        verify(mapper).toDto(rawMaterial);
    }
    @Test
    void shouldDeleteRawMaterialWhenNotUsed() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(rawMaterial));
        when(productService.existsByRawMaterial(1L)).thenReturn(false);

        service.delete(1L);

        verify(repository).delete(rawMaterial);
    }

    @Test
    void shouldThrowDomainExceptionWhenDeletingUsedRawMaterial() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(rawMaterial));
        when(productService.existsByRawMaterial(1L)).thenReturn(true);

        assertThrows(DomainException.class,
                () -> service.delete(1L));

        verify(repository, never()).delete(rawMaterial);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingRawMaterial() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1L));
    }

    @Test
    void shouldThrowWhenNotFoundInGetByIdOrThrow() {

        when(repository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getByIdOrThrow(1L));
    }
}