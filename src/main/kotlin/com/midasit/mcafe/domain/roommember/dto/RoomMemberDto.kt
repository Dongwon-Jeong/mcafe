package com.midasit.mcafe.domain.roommember.dto

import com.midasit.mcafe.domain.member.Member
import com.midasit.mcafe.infra.exception.CustomException
import com.midasit.mcafe.infra.exception.ErrorMessage
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "MemberDto", description = "회원 정보")
class RoomMemberDto(var memberSn: Long, var nickname: String) {
    companion object {
        fun of(member: Member): RoomMemberDto {
            require(member.sn != null) { throw CustomException(ErrorMessage.INTERNAL_SERVER_ERROR) }
            return RoomMemberDto(member.sn, member.nickname)
        }
    }
}