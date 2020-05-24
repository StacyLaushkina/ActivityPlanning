package com.laushkina.activityplanning.model.track

import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class TrackService(private val trackDbRepository: TrackDBRepository,
                   private val plansDbRepository: PlanDBRepository) {

    fun startTracking(): Maybe<List<Track>> {
        return plansDbRepository.get()
            .flatMap { plans: List<Plan> ->  createTracksForPlans(plans) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createTracksForPlans(plans: List<Plan>): Maybe<List<Track>>  {
        val tracks = mutableListOf<Track>()
        for (plan in plans) {
            tracks.add(Track(Random().nextInt(), plan, null, null, Date()))
        }
        trackDbRepository.insertAll(tracks)
        return trackDbRepository.get(Date())
    }

    fun getTracks(date: Date): Maybe<List<Track>> {
        return trackDbRepository.get(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateTrack(track: Track): Maybe<List<Track>> {
        return Maybe.just(track)
            .map { trackDbRepository.insert(track) }
            .flatMap { trackDbRepository.get(Date()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        fun getTimeDiff(startTime: Long, endTime: Long?): Long {
            return if (endTime == null) {
                System.currentTimeMillis() - startTime
            } else {
                endTime - startTime
            }
        }
    }
}