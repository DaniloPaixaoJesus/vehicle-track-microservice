package br.com.danilopaixao.vehicle.track.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.danilopaixao.vehicle.track.model.VehicleTrackRedis;

@Repository
public interface VehicleTrackRedisRepository extends CrudRepository<VehicleTrackRedis, String> {
}
