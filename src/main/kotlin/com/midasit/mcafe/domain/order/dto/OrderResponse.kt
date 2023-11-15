package com.midasit.mcafe.domain.order.dto

import io.swagger.v3.oas.annotations.media.Schema

class OrderResponse {

    @Schema(name = "OrderGetMenuList", description = "메뉴 리스트 요청 응답 객체")
    class GetMenuList(
        @Schema(description = "메뉴 리스트", required = true)
        val menuCategoryList: ArrayList<MenuCategoryDto>
    )

    @Schema(name = "OrderGetMenuInfo", description = "메뉴 정보 요청 응답 객체")
    class GetMenuInfo(
        @Schema(description = "메뉴 정보", required = true)
        val menuInfo: MenuInfoDto
    )
}