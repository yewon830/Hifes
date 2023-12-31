package com.ssafy.hifes.data.repository.festival

import com.ssafy.hifes.data.model.ErrorResponse
import com.ssafy.hifes.data.model.FCMForGroupDto
import com.ssafy.hifes.data.model.MarkerDto
import com.ssafy.hifes.data.model.TimeTable
import com.ssafy.hifes.util.network.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.Path

interface FestivalRepository {

    suspend fun getMarkerList(@Path("festivalId") festivalId: Int): NetworkResponse<List<MarkerDto>, ErrorResponse>

    suspend fun getFestivalTimeTable(@Path("festivalId") festivalId: Int): NetworkResponse<List<TimeTable>, ErrorResponse>

    suspend fun callGroupNotification(@Body fcmForGroupDto: FCMForGroupDto): NetworkResponse<String, ErrorResponse>
    suspend fun subscribeFestivalNotice(@Path("festivalId") festivalId: Int): NetworkResponse<Boolean, ErrorResponse>
}