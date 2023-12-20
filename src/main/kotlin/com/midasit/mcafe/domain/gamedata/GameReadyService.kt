package com.midasit.mcafe.domain.gamedata

import com.midasit.mcafe.domain.member.Member
import com.midasit.mcafe.domain.room.Room
import com.midasit.mcafe.infra.exception.CustomException
import com.midasit.mcafe.infra.exception.ErrorMessage
import com.midasit.mcafe.model.GameType
import com.midasit.mcafe.model.ReadyStatus
import jakarta.validation.constraints.NotNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GameReadyService(
    val gameReadyRepository: GameReadyRepository
) {
    @Transactional
    fun updateGameReadyStatus(memberSn: Long, roomSn: Long, gameType: GameType, @NotNull readyStatus: ReadyStatus) : GameReady {

        return gameReadyRepository.findGameReadyByMemberSnAndRoomSnAndGameType(memberSn, roomSn, gameType)?.also {
            it.updateReadyStatus(readyStatus)
        } ?: throw CustomException(ErrorMessage.NO_GAME_READY_STATUS)
    }

    fun getGameReadyStatusOfRoomMember(roomSn: Long, gameType: GameType): List<GameReady> {
        return gameReadyRepository.findGameReadyByRoomSnAndGameType(roomSn, gameType)
    }

    @Transactional
    fun createGameReady(member: Member, room: Room, gameType: GameType) : GameReady {
        return gameReadyRepository.save(GameReady(member, room, gameType))
    }

    @Transactional
    fun deleteGameReadyStatusByRoomAndGameTypeAndReadyStatus(room: Room, gameType: GameType, readyStatus: ReadyStatus) {
        gameReadyRepository.deleteGameReadyByRoomAndGameTypeAndReadyStatus(room, gameType, readyStatus)
    }

    @Transactional
    fun deleteGameReadyStatusByRoomAndMemberAndGameType(room: Room, member: Member, gameType: GameType) {
        gameReadyRepository.deleteGameReadyByRoomAndMemberAndGameType(room, member, gameType)
    }
}