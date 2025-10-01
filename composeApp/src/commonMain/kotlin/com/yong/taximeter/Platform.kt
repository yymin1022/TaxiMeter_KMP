package com.yong.taximeter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform