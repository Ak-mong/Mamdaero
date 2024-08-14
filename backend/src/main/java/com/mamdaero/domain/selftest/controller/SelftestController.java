package com.mamdaero.domain.selftest.controller;

import com.mamdaero.domain.member.security.service.FindUserService;
import com.mamdaero.domain.selftest.dto.request.TestRequestDto;
import com.mamdaero.domain.selftest.dto.response.MemberSelftestResponseDto;
import com.mamdaero.domain.selftest.dto.response.MemberSelftestResultResponseDto;
import com.mamdaero.domain.selftest.dto.response.SelftestQuestionResponseDto;
import com.mamdaero.domain.selftest.dto.response.SelftestResponseDto;
import com.mamdaero.domain.selftest.entity.MemberSelftestList;
import com.mamdaero.domain.selftest.service.SelftestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class SelftestController {

    private final SelftestService selftestService;
    private final FindUserService findUserService;

    @GetMapping("/p/selftest")
    public ResponseEntity<List<SelftestResponseDto>> findAll() {

        List<SelftestResponseDto> selftestList = selftestService.findAll();

        return new ResponseEntity<>(selftestList, HttpStatus.OK);
    }

    @GetMapping("/p/selftest/{testId}")
    public ResponseEntity<List<SelftestQuestionResponseDto>> getQuestionsWithOptionsByTestId(@PathVariable(name = "testId") Integer testId) {

        List<SelftestQuestionResponseDto> selftestQuestionList = selftestService.getQuestionsWithOptionsByTestId(testId);

        return new ResponseEntity<>(selftestQuestionList, HttpStatus.OK);
    }

    @PostMapping("/m/selftest/{testId}")
    public ResponseEntity<MemberSelftestList> createByTestId(@PathVariable(name = "testId") Integer testId, @RequestBody TestRequestDto requestDto) {

        if (Objects.equals(findUserService.findMemberRole(), "내담자")) {

            selftestService.createByTestId(findUserService.findMemberId(), testId, requestDto);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /*
    가장 최근 심리검사 리스트 조회
     */
    @GetMapping("/m/selftest")
    public ResponseEntity<List<MemberSelftestResponseDto>> getSelftestList() {

        return ResponseEntity.ok(selftestService.getMemberSelftestList(findUserService.findMemberId()));
    }

    @GetMapping("/m/selftest/result")
    public ResponseEntity<Page<MemberSelftestResultResponseDto>> getSelftestQuestionList(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

        return ResponseEntity.ok(selftestService.getMemberSelftestListAll(findUserService.findMemberId(), page, size));
    }

    @GetMapping("/m/selftest/result/{resultId}")
    public ResponseEntity<MemberSelftestResponseDto> getSelftestQuestionList(@PathVariable(name = "resultId") Integer resultId) {

        return ResponseEntity.ok(selftestService.getMemberSelftestDetail(findUserService.findMemberId(), resultId));
    }
}
