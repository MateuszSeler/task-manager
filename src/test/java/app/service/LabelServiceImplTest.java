package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.dto.label.LabelUpdateRequestDto;
import app.exception.EntityNotFoundException;
import app.repository.LabelRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {
    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    void updateLabel() {
        Long labelId = 31L;

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> labelService.updateLabel(labelId, new LabelUpdateRequestDto()));

        assertEquals("Label with id: " + labelId + " not found", exception.getMessage());
    }

    @Test
    void deleteLabelById() {
        Long labelId = 31L;

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> labelService.deleteLabelById(labelId));

        assertEquals("Label with id: " + labelId + " not found", exception.getMessage());
    }

    @Test
    void getLabelById() {
        Long labelId = 31L;

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> labelService.getLabelById(labelId));

        assertEquals("Label with id: " + labelId + " not found", exception.getMessage());
    }
}
