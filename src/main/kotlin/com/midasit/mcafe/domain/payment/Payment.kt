package com.midasit.mcafe.domain.payment

import com.midasit.mcafe.domain.member.Member
import com.midasit.mcafe.model.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "payment")
class Payment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_sn", nullable = false, foreignKey = ForeignKey(name = "fk_payment_member_sn"))
    val member: Member,
    @Column(name = "uchef_order_no", nullable = false)
    val uChefOrderNo: String,
) : BaseEntity()