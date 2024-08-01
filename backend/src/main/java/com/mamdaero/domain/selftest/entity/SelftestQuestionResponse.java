package com.mamdaero.domain.selftest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class SelftestQuestionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selftest_question_response_id")
    private Integer id;
    @OneToOne(fetch = FetchType.LAZY)
    private SelftestQuestionOption selftestQuestionOption;
    @OneToOne(fetch = FetchType.LAZY)
    private MemberSelftestList memberSelftestList;
    @OneToOne(fetch = FetchType.LAZY)
    private SelftestQuestion selftestQuestion;
    private Integer selftestMemberQuestionScore;
}
