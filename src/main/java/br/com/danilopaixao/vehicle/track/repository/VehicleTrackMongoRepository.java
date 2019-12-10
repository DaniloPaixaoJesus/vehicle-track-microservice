package br.com.danilopaixao.vehicle.track.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public interface VehicleTrackMongoRepository extends MongoRepository<VehicleTrack, String> {
	
	// GeoResults<VehicleTrack> findByLocationWithin(Point point, Distance distance);
	
	//@Query(value="{vin : ?0}", fields="{ vin : 0 }")
	List<VehicleTrack> findByVin(String vin);
}