package codekoi.apiserver.domain.user.controller;

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

    @GetMapping
    public ResponseEntity<Object> test() {


        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "id", 1L
        ));
    }
}
