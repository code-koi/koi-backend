package codekoi.apiserver.domain.user.controller;

import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Object> test() {
        final User user = new User();
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "id", 1L
        ));
    }
}
