package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.dto.comment.CommentCreateRequestDto;
import app.exception.EntityNotFoundException;
import app.repository.CommentRepository;
import app.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void isUserIsTheAuthor_nonExistingUser_EntityNotFound() throws IOException {
        Long commentId = 31L;
        String userEmail = "email@gmail.com";

        Mockito.when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> commentService.isUserIsTheAuthor(commentId, userEmail));

        assertEquals("User with email: " + userEmail + " not found", exception.getMessage());
    }

    @Test
    void deleteCommentById_nonExistingComment_EntityNotFound() {
        Long commentId = 31L;

        Mockito.when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> commentService.deleteCommentById(commentId));

        assertEquals("Comment with id: " + commentId + " not found", exception.getMessage());
    }

    @Test
    void updateCommentById() {
        Long commentId = 31L;

        Mockito.when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> commentService.updateCommentById(
                                commentId, new CommentCreateRequestDto()));

        assertEquals("Comment with id: " + commentId + " not found",
                exception.getMessage());
    }
}
