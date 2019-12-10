package br.com.danilopaixao.vehicle.track.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;

//"D:\redis-2.4.5-win32-win64\64bit\redis-cli.exe"
//FLUSHDB
@Repository
public interface VehicleTrackRedisRepository extends CrudRepository<VehicleTrackCache, String> {
}
