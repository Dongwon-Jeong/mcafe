package com.midasit.mcafe.domain.room

import com.midasit.mcafe.domain.room.dto.RoomRequest
import com.midasit.mcafe.domain.room.dto.RoomResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/room")
class RoomController(val roomService: RoomService) {

    @Operation(summary = "방 생성")
    @PostMapping("/create")
    fun createRoom(@RequestBody request: RoomRequest.Create,
                   authentication: Authentication): RoomResponse.Result {
        val memberSn = getMemberSn(authentication)
        return RoomResponse.Result.of(roomService.createRoom(request, memberSn))
    }

    private fun getMemberSn(authentication: Authentication): Long {
        return (authentication.principal as Long)
    }
}