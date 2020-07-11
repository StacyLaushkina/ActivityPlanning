package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.repository.PlanRepository
import com.laushkina.activityplanning.repository.TrackRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.security.InvalidParameterException
import java.util.*

class TrackService(private val trackRepository: TrackRepository,
                   private val planRepository: PlanRepository) {

    fun startTracking(): Maybe<List<Track>> {
        return planRepository.get()
            .flatMap { plans: List<Plan> ->  createTracksForPlans(plans) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createTracksForPlans(plans: List<Plan>): Maybe<List<Track>>  {
        val tracks = mutableListOf<Track>()
        for (plan in plans) {
            tracks.add(
                Track(
                    Random().nextInt(),
                    plan,
                    null,
                    null,
                    false,
                    Date()
                )
            )
        }
        trackRepository.insertAll(tracks)
        return trackRepository.get(Date())
    }

    fun getTracks(date: Date): Maybe<List<Track>> {
        return trackRepository.get(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateTrack(track: Track): Maybe<List<Track>> {
        return Maybe.just(track)
            .map { trackRepository.insert(track) }
            .flatMap { trackRepository.get(Date()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        fun getTimeDiff(startTime: Long, endTime: Long?, isInProgress: Boolean): Long {
            return if (isInProgress) {
                System.currentTimeMillis() - startTime
            } else {
                if (endTime == null) throw InvalidParameterException("Incorrect!!!")
                else {
                    endTime - startTime
                }
            }
        }
    }
}