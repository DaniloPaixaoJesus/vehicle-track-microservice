package br.com.danilopaixao.vehicle.track.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Circle;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public interface VehicleTrackMongoRepository extends MongoRepository<VehicleTrack, String> {
	
	//GeoResults<VehicleTrack> findByLocationWithin(Point point, Distance distance);
	/**
	 * // { 'location' : { '$near' : [point.x, point.y], '$maxDistance' : distance}}
  		List<VehicleTrack> findByLocationNear(Point location, Distance distance);
	 * @param vin
	 * @return
	 */
	List<VehicleTrack> findByGeolocationWithin(Circle circle);
	
	//@Query(value="{vin : ?0}", fields="{ vin : 0 }")
	List<VehicleTrack> findByVin(String vin);
}