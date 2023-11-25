package com.midasit.mcafe.domain.order

import com.midasit.mcafe.domain.member.Member
import com.midasit.mcafe.domain.member.MemberService
import com.midasit.mcafe.domain.order.dto.OrderDto
import com.midasit.mcafe.domain.order.dto.OrderRequest
import com.midasit.mcafe.domain.order.dto.OrderResponse
import com.midasit.mcafe.domain.room.Room
import com.midasit.mcafe.domain.room.RoomService
import com.midasit.mcafe.domain.roommember.RoomMemberRepository
import com.midasit.mcafe.infra.component.UChefComponent
import com.midasit.mcafe.model.OrderStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val uChefComponent: UChefComponent,
    private val orderRepository: OrderRepository,
    private val roomMemberRepository: RoomMemberRepository,
    private val roomService: RoomService,
    private val memberService: MemberService
) {

    fun getMenuList(): OrderResponse.GetMenuList {
        return OrderResponse.GetMenuList(uChefComponent.getMenuList())
    }

    fun getMenuInfo(menuCode: Long): OrderResponse.GetMenuInfo {
        return OrderResponse.GetMenuInfo(uChefComponent.getMenuInfo(menuCode))
    }

    @Transactional
    fun createOrder(memberSn: Long, request: OrderRequest.Create): OrderDto {

        val member = memberService.findBySn(memberSn)
        val room = roomService.findByRoomSn(request.roomSn)
        roomService.checkMemberInRoom(member, room)

        val order = findDuplicateOrder(member, room, request.menuCode, request.optionList)
        order?.addQuantity() ?: run {
            val newOrder = Order(OrderStatus.PENDING, request.menuCode, member, room, 1)
            request.optionList.forEach {
                newOrder.addOption(it)
            }
            return OrderDto.of(orderRepository.save(newOrder))
        }

        return OrderDto.of(order)
    }

    private fun findDuplicateOrder(member: Member, room: Room, menuCode: String, optionList: List<Long>): Order? {
        val orderList = orderRepository.findByMemberAndRoomAndMenuCodeAndStatus(member, room, menuCode, OrderStatus.PENDING)
        return orderList.find { order ->
            val optionSet = order.orderOptions.map { it.optionValue }.toHashSet()
            optionSet == optionList.toHashSet()
        }
    }

    fun getOrderList(memberSn: Long, roomSn: Long): List<OrderDto> {
        val member = memberService.findBySn(memberSn)
        val room = roomService.findByRoomSn(roomSn)
        roomService.checkMemberInRoom(member, room)

        val orderList = orderRepository.findByRoomAndStatus(room, OrderStatus.PENDING)
        return orderList.map { OrderDto.of(it) }
    }
}