package br.com.danilopaixao.vehicle.track.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;

@Repository
public interface VehicleTrackRedisRepository extends CrudRepository<VehicleTrackCache, String> {
}
