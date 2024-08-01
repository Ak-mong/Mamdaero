package com.mamdaero.domain.counselor_board.controller;

import com.mamdaero.domain.counselor_board.dto.request.BoardRequest;
import com.mamdaero.domain.counselor_board.service.BoardFindService;
import com.mamdaero.domain.counselor_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardFindService boardFindService;

    @GetMapping("/ca/counselor-board")
    public ResponseEntity<?> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                     @RequestParam(name = "condition", defaultValue = "new") String condition,
                                     @RequestParam(name = "searchField", required = false) String searchField,
                                     @RequestParam(name = "searchValue", required = false) String searchValue) {
        return ResponseEntity.ok(boardFindService.findAll(page, size, condition, searchField, searchValue));
    }

    @GetMapping("/ca/counselor-board/{boardId}")
    public ResponseEntity<?> findDetail(@PathVariable("boardId") Long id) {
        return ResponseEntity.ok(boardService.findDetail(id));
    }

    @PostMapping("/c/counselor-board")
    public ResponseEntity<?> create(@RequestPart(name = "file", required = false) List<MultipartFile> file,
                                    @RequestPart("data") BoardRequest request) throws IOException {
        boardService.create(request, file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/c/counselor-board/{boardId}")
    public ResponseEntity<?> update(@PathVariable("boardId") Long id, @RequestBody BoardRequest request) {
        return ResponseEntity.ok(boardService.update(id, request));
    }

    @DeleteMapping("/ca/counselor-board/{boardId}")
    public ResponseEntity<?> delete(@PathVariable("boardId") Long id) {
        boardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/c/counselor-board/{boardId}/complaint")
    public ResponseEntity<?> complaint(@PathVariable("boardId") Long id) {
        if(!boardService.complaint(id)){
            return ResponseEntity.ok("이미 신고한 글입니다.");
        }
        return ResponseEntity.ok().build();
    }
}