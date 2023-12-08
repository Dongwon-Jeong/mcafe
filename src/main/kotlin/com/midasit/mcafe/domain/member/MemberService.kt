package com.midasit.mcafe.domain.member

import com.midasit.mcafe.domain.member.dto.LoginDto
import com.midasit.mcafe.domain.member.dto.MemberDto
import com.midasit.mcafe.domain.member.dto.MemberRequest
import com.midasit.mcafe.domain.roommember.RoomMemberRepository
import com.midasit.mcafe.infra.component.UChefComponent
import com.midasit.mcafe.infra.config.jwt.JwtTokenProvider
import com.midasit.mcafe.infra.exception.CustomException
import com.midasit.mcafe.infra.exception.ErrorMessage
import com.midasit.mcafe.model.PasswordEncryptUtil
import com.midasit.mcafe.model.Role
import com.midasit.mcafe.model.validate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val uChefComponent: UChefComponent,
    private val memberRepository: MemberRepository,
    private val roomMemberRepository: RoomMemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun getUChefAuth(request: MemberRequest.UChefAuth): String {
        return uChefComponent.login(request.phone, request.securityId, request.password)
    }

    fun existsMemberByUsername(username: String): Boolean {
        return memberRepository.existsByUsername(username)
    }

    @Transactional
    fun signup(request: MemberRequest.Signup): MemberDto {
        val phone = validateMember(request)

        val member = Member(
            phone = phone,
            username = request.username,
            password = request.password,
            nickname = request.nickname,
            role = Role.USER
        )
        return memberRepository.save(member).toDto()
    }

    fun login(request: MemberRequest.Login): LoginDto {
        return memberRepository.findByUsername(request.username)?.let { member ->
            if (PasswordEncryptUtil.match(request.password, member.password).not()) {
                throw CustomException(ErrorMessage.INVALID_LOGIN_REQUEST)
            }
            val accessToken = jwtTokenProvider.generateAccessToken(member.sn)
            LoginDto(phone = member.phone, name = member.nickname, token = accessToken)
        } ?: throw CustomException(ErrorMessage.INVALID_LOGIN_REQUEST)
    }

    fun findMemberInfo(memberSn: Long): MemberDto {
        return findBySn(memberSn).toDto()
    }

    fun findBySn(memberSn: Long): Member {
        return memberRepository.getOrThrow(memberSn)
    }

    @Transactional
    fun updateNickname(memberSn: Long, nickname: String): MemberDto {
        val member = findBySn(memberSn)
        member.updateNickname(nickname)
        return member.toDto()
    }


    @Transactional
    fun updatePassword(memberSn: Long, password: String, passwordCheck: String): MemberDto {
        val member = findBySn(memberSn)
        validatePassword(password, passwordCheck)
        member.updatePassword(password)
        return member.toDto()
    }

    @Transactional
    fun deleteMember(memberSn: Long) {
        val member = findBySn(memberSn)
        roomMemberRepository.deleteByMember(member)
        member.delete()
    }

    private fun validateMember(request: MemberRequest.Signup): String {
        // 비밀번호 체크 검사
        validatePassword(request.password, request.passwordCheck)
        // 아이디 중복체크 검사
        validate(ErrorMessage.DUPLICATE_ID) { !memberRepository.existsByUsername(request.username) }

        // u chef 인증 검사
        val valueOperations = redisTemplate.opsForValue()
        val phone =
            valueOperations.getAndDelete(request.certKey) ?: throw CustomException(ErrorMessage.INVALID_UCHEF_AUTH)

        return phone.toString()
    }

    private fun validatePassword(password: String, passwordCheck: String) {
        validate(ErrorMessage.INVALID_PASSWORD_CHECK) { password == passwordCheck }
    }

    private fun Member.toDto(): MemberDto {
        return MemberDto.of(this)
    }
}