package com.mamdaero.domain.counselor_board.service;

import com.mamdaero.domain.counselor_board.dto.request.BoardRequest;
import com.mamdaero.domain.counselor_board.dto.response.BoardDetailResponse;
import com.mamdaero.domain.counselor_board.dto.response.BoardResponse;
import com.mamdaero.domain.counselor_board.entity.CounselorBoard;
import com.mamdaero.domain.counselor_board.repository.BoardRepository;
import com.mamdaero.domain.counselor_item.exception.CounselorNotFoundException;
import com.mamdaero.domain.member.repository.MemberRepository;
import com.mamdaero.domain.notice.exception.NoticeBadRequestException;
import com.mamdaero.domain.notice.exception.NoticeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<BoardResponse> findAll() {
        List<CounselorBoard> counselorBoards = boardRepository.findAll();
        List<BoardResponse> list = new ArrayList<>();

        for(CounselorBoard counselorBoard : counselorBoards) {
            String writer = memberRepository.findById(counselorBoard.getMemberId())
                    .orElseThrow(CounselorNotFoundException::new)
                    .getName();

            BoardResponse boardResponse = BoardResponse.of(counselorBoard, writer);
            list.add(boardResponse);
        }

        return list;
    }

    @Transactional
    public BoardDetailResponse findDetail(Long id) {
        CounselorBoard board = boardRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);

        String writer = memberRepository.findById(board.getMemberId())
                .orElseThrow(CounselorNotFoundException::new)
                .getName();

        board.clickCounselorBoard();
        boardRepository.save(board);

        return BoardDetailResponse.of(board, writer);
    }

    @Transactional
    public void create(BoardRequest request) {
        // 토큰 확인 후 상담사인지 확인
        Long memberId = 1L;

        if(request.getTitle() == null || request.getContent() == null) {
            throw new NoticeBadRequestException();
        }

        boardRepository.save(BoardRequest.toEntity(memberId, request));
    }

    @Transactional
    public BoardDetailResponse update(Long id, BoardRequest request) {
        // 토큰 확인 후 상담사인지 확인
        Long memberId = 1L;

        CounselorBoard board = boardRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);

        String writer = memberRepository.findById(board.getMemberId())
                .orElseThrow(CounselorNotFoundException::new)
                .getName();

        if (request.getTitle() != null) {
            board.updateTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            board.updateContent(request.getContent());
        }

        boardRepository.save(board);
        return BoardDetailResponse.of(board, writer);
    }

    @Transactional
    public void delete(Long id) {
        // 토큰 확인 후 상담사인지 확인
        Long memberId = 1L;

        CounselorBoard board = boardRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);

        boardRepository.delete(board);
    }
}