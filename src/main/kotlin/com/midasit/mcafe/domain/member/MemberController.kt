package com.midasit.mcafe.domain.member

import com.midasit.mcafe.domain.member.dto.MemberRequest
import com.midasit.mcafe.domain.member.dto.MemberResponse
import com.midasit.mcafe.model.BaseController
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "멤버 컨트롤러")
@RequestMapping("/member")
class MemberController(private val memberService: MemberService) : BaseController {

    @Operation(summary = "uchef 인증")
    @PostMapping("/uchef-auth")
    fun uChefAuth(@RequestBody request: MemberRequest.UChefAuth): MemberResponse.CertKey {
        return MemberResponse.CertKey(memberService.getUChefAuth(request))
    }

    @Operation(summary = "username 중복검사")
    @GetMapping("/id-check/{username}")
    fun usernameCheck(@PathVariable username: String): MemberResponse.UsernameCheck {
        return MemberResponse.UsernameCheck(!memberService.existsMemberByUsername(username))
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(@RequestBody request: MemberRequest.Signup): MemberResponse.Result {
        return MemberResponse.Result.of(memberService.signup(request))
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@RequestBody request: MemberRequest.Login): MemberResponse.Login {
        val login = memberService.login(request)
        return MemberResponse.Login(name = login.name, phone = login.phone, token = login.token)
    }

    @Operation(summary = "회원정보 조회")
    @GetMapping
    fun findMemberInfo(authentication: Authentication): MemberResponse.Result {
        return MemberResponse.Result.of(memberService.findMemberInfo(getMemberSn(authentication)))
    }
}