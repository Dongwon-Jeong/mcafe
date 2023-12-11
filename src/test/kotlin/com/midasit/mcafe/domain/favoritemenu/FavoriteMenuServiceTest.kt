package com.midasit.mcafe.domain.favoritemenu

import com.midasit.mcafe.domain.favoritemenu.dto.FavoriteMenuDto
import com.midasit.mcafe.domain.member.MemberService
import com.midasit.mcafe.model.Member
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class FavoriteMenuServiceTest : BehaviorSpec({

    val memberService = mockk<MemberService>()
    val favoriteMenuRepository = mockk<FavoriteMenuRepository>()
    val favoriteMenuService = FavoriteMenuService(favoriteMenuRepository,memberService)

    afterContainer {
        clearAllMocks()
    }

    given("member Sn이 주어지면") {
        val memberSn = 1L
        val member = Member()
        val favoriteMenu = FavoriteMenu("test", member)
        every { memberService.findBySn(any()) } answers { member }
        every { favoriteMenuRepository.findByMember(any()) } answers { listOf(favoriteMenu) }
        `when`("해당 멤버의 즐겨찾기 메뉴를 조회하면") {
            val result = favoriteMenuService.findFavoriteMenu(memberSn)
            then("해당 멤버의 즐겨찾기 메뉴를 반환한다") {
                result[0] shouldBe FavoriteMenuDto.from(favoriteMenu)
            }
        }
    }
})
