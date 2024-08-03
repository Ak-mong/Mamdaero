package com.mamdaero.domain.review.service;

import com.mamdaero.domain.reservation.entity.Reservation;
import com.mamdaero.domain.reservation.repository.ReservationRepository;
import com.mamdaero.domain.review.dto.request.ReviewRequestDto;
import com.mamdaero.domain.review.dto.response.ReviewResponseDto;
import com.mamdaero.domain.review.entity.Review;
import com.mamdaero.domain.review.exception.ReviewAlreadyException;
import com.mamdaero.domain.review.exception.ReviewNoReviewException;
import com.mamdaero.domain.review.exception.ReviewNoScoreException;
import com.mamdaero.domain.review.exception.ReviewNotFoundException;
import com.mamdaero.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public List<ReviewResponseDto> findAllByReservation_CounselorItem_CounselorId(Long id) {

        if (reviewRepository.findAllByReservation_CounselorItem_CounselorId(id).isEmpty()) {
            throw new ReviewNotFoundException();
        }

        return reviewRepository.findAllByReservation_CounselorItem_CounselorId(id).stream()
                .map(ReviewResponseDto::toDto)
                .toList();
    }

    @Transactional
    public void create(Long id, ReviewRequestDto requestDto) {

        Optional<Review> optionalReview = reviewRepository.findById(id);
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReview.isEmpty()) {

            if (requestDto.getReview().isEmpty()) {
                throw new ReviewNoReviewException();
            } else if (requestDto.getScore().isNaN()) {
                throw new ReviewNoScoreException();
            }
            if (optionalReservation.isPresent()) {

                Review review = Review.builder()
                        .reservation(optionalReservation.get())
                        .review(requestDto.getReview())
                        .score(requestDto.getScore())
                        .build();

                reviewRepository.save(review);
            }
        }
        else {
            throw new ReviewAlreadyException();
        }
    }

    @Transactional
    public void update(Long id, ReviewRequestDto requestDto) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {

            Review review = optionalReview.get();

            if (requestDto.getReview().isEmpty()) {
                throw new ReviewNoReviewException();
            }
            else if (requestDto.getScore().isNaN()) {
                throw new ReviewNoScoreException();
            }

            review.update(requestDto);
        }
        else {
            throw new ReviewNotFoundException();
        }
    }

    @Transactional
    public void delete(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();

            reviewRepository.delete(review);
        }
        else {
            throw new ReviewNotFoundException();
        }
    }
}
