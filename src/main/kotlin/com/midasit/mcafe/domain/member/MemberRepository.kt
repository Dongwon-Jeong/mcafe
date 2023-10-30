package com.midasit.mcafe.domain.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByPhoneAndPassword(phone: String, password: String) : Member?
}