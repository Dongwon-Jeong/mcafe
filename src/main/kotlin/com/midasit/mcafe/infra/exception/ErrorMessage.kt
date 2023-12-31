package com.midasit.mcafe.infra.exception

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "에러 메시지", enumAsRef = true)
enum class ErrorMessage(
    val message: String,
    val httpStatus: HttpStatus
) {
    INTERNAL_SERVER_ERROR("서버 오류", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("유효하지 않은 요청", HttpStatus.BAD_REQUEST),

    // 회원가입, 로그인
    INVALID_UCHEF_AUTH("m cafe 인증 정보가 맞지 않습니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_ID("중복된 아이디가 존재합니다.", HttpStatus.CONFLICT),
    INVALID_LOGIN_REQUEST("아이디가 존재하지 않거나 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD_CHECK("비밀번호 확인이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT("인증 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    // 방
    DUPLICATE_ROOM_NAME("이미 존재하는 방 이름입니다.", HttpStatus.CONFLICT),
    ALREADY_ENTERED_ROOM("이미 입장한 방입니다.", HttpStatus.CONFLICT),
    INVALID_ROOM_INFO("잘못된 방 정보입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ROOM_PASSWORD("방 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED),
    HOST_CANT_EXIT("방장은 방을 나갈 수 없습니다.", HttpStatus.BAD_REQUEST),
    UCHEF_ORDER_FAILED("m cafe 주문에 실패하였습니다.", HttpStatus.BAD_REQUEST),

    // 주문
    INVALID_ORDER_LIST("유효하지 않은 주문번호가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),

    //즐겨찾기
    INVALID_MEMBER("잘못된 회원입니다.", HttpStatus.BAD_REQUEST),
    INVALID_FAVORITE_MENU("잘못된 즐겨찾기 메뉴입니다.", HttpStatus.BAD_REQUEST),

    // 게임
    NO_GAME_READY_STATUS("게임 준비 상태가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
}