package br.com.danilopaixao.vehicle.track.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.geo.Point;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public interface VehicleTrackMongoRepository extends MongoRepository<VehicleTrack, String> {
	// GeoResults<VehicleTrack> findByLocationWithin(Point point, Distance distance);
}