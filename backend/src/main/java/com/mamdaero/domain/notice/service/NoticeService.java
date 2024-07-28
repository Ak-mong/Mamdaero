package com.mamdaero.domain.notice.service;

import com.mamdaero.domain.notice.dto.request.NoticeRequest;
import com.mamdaero.domain.notice.dto.response.NoticeDetailResponse;
import com.mamdaero.domain.notice.dto.response.NoticeResponse;
import com.mamdaero.domain.notice.entity.Notice;
import com.mamdaero.domain.notice.exception.NoticeBadRequestException;
import com.mamdaero.domain.notice.exception.NoticeNotFoundException;
import com.mamdaero.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponse> findAll() {
        return noticeRepository.findAll()
                .stream()
                .map(notice -> NoticeResponse.of(notice, "관리자"))
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeDetailResponse findDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);

        notice.clickNotice();
        noticeRepository.save(notice);

        return NoticeDetailResponse.of(notice);
    }

    @Transactional
    public void create(NoticeRequest request) {
        // 토큰 확인 후 관리자인지 확인
        Long memberId = 2L;

        if(request.getTitle() == null || request.getContent() == null) {
            throw new NoticeBadRequestException();
        }

        noticeRepository.save(NoticeRequest.toEntity(memberId, request));
    }
}
