package com.midasit.mcafe.domain.roommember.dto

import com.midasit.mcafe.domain.member.Member
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "MemberDto", description = "회원 정보")
class RoomMemberDto(var memberSn: Long, var nickname: String) {
    companion object {
        fun of(member: Member): RoomMemberDto {
            return RoomMemberDto(member.sn, member.nickname)
        }
    }
}