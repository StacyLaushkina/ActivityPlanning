package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.model.plan.Plan

data class Track(val id: Int, val plan: Plan, var startTime: Long?, var endTime: Long?)