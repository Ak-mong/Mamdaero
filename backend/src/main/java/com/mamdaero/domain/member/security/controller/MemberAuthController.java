package com.mamdaero.domain.member.security.controller;

import com.mamdaero.domain.counselor_board.dto.request.CounselorBoardRequest;
import com.mamdaero.domain.member.security.apiresult.ApiResponse;
import com.mamdaero.domain.member.security.dto.request.*;
import com.mamdaero.domain.member.security.dto.response.IsDuplicateDTO;
import com.mamdaero.domain.member.security.dto.response.IsSuccessDTO;
import com.mamdaero.domain.member.security.dto.response.MemberSignUpResponseDTO;
import com.mamdaero.domain.member.security.dto.response.ResultDTO;
import com.mamdaero.domain.member.security.service.FindUserService;
import com.mamdaero.domain.member.security.service.MailService;
import com.mamdaero.domain.member.security.service.MemberAuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberAuthController
{
    private final MemberAuthService memberService;
    private final MailService mailService;
    private final FindUserService findUserService;

    //유저 가입
    @PostMapping("/p/member/client-join")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberSignUpResponseDTO> memberJoin(@Valid @RequestBody MemberSignUpDTO request) throws Exception
    {
        memberService.memberJoin(request);
        MemberSignUpResponseDTO signupResponseDto = MemberSignUpResponseDTO.builder().email(request.getEmail()).isSuccess(true).build();
        return ApiResponse.onSuccess(signupResponseDto);
    }

    //상담사 가입
    @PostMapping("/p/member/counselor-join")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberSignUpResponseDTO> counselorJoin(@RequestPart(name = "file", required = false) MultipartFile file,
                                                              @RequestPart("data") CounselorSignUpDTO request) throws Exception
    {
        memberService.counselorJoin(request, file);
        MemberSignUpResponseDTO signupResponseDto = MemberSignUpResponseDTO.builder().email(request.getEmail()).isSuccess(true).build();
        return ApiResponse.onSuccess(signupResponseDto);
    }

    //닉네임 중복 체크
    @PostMapping("/p/member/nickname-check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<IsDuplicateDTO> nicknameDuplicated(@RequestBody NicknameCheckDTO request) throws  Exception
    {
        String check = memberService.nicknameDuplicated(request);

        if (check.equals("true"))
        {
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(true).build());
        }
        return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(false).build());
    }

    //이메일 중복 체크
    @PostMapping("/p/member/email-check")
    public ApiResponse<IsDuplicateDTO> isDuplicated(@Valid @RequestBody EmailCheckRequestDTO request) throws Exception
    {
        String check = memberService.isDuplicated(request);

        if (check.equals("true"))
        {
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(true).build());
        }
        return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(false).build());
    }

    //상담사 이메일 인증 코드 발송
    @PostMapping("/p/member/counselor-email-request")
    public ApiResponse<IsDuplicateDTO> emailCheckRequest(@RequestBody EmailAuthRequestDTO request) throws Exception
    {
        boolean check = mailService.emailAuthRequest(request);

        if(check)
        {
            log.info("이메일 발송 됨!");
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(true).build());
        }
        else
        {
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(false).build());
        }
    }

    //이메일 발송 인증 코드 검증
    @PostMapping("/p/member/counselor-email-auth")
    public ApiResponse<IsDuplicateDTO> emailCheckAuth(@RequestBody EmailAuthTokenRequstDTO request) throws Exception
    {
        boolean check = mailService.check_auth_token(request);
        if(check)
        {
            log.info("검증 됨!");
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(true).build());
        }
        else
        {
            return ApiResponse.onSuccess(IsDuplicateDTO.builder().isDuplicate(false).build());
        }
    }

    @PatchMapping("/cm/member/password-modify")
    public ApiResponse<ResultDTO> passwordModify(@RequestBody PasswordResetDTO request) throws Exception
    {
        String email = findUserService.findMember().getMemberEmail();

        boolean check = memberService.modifyPassword(email, request);
        if(check)
        {
            return ApiResponse.onSuccess(ResultDTO.builder().message("변경 완료").build());
        }
        else
        {
            return ApiResponse.onSuccess(ResultDTO.builder().message("변경 실패").build());
        }
    }

    @Data
    static class LoginRequestDto
    {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }
}