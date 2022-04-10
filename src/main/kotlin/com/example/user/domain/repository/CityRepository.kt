package com.example.user.domain.repository

import com.example.common.CityId
import com.example.user.domain.model.City

interface CityRepository {
    fun getById(id: CityId): City
}