package com.scb.transmitter.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scb.transmitter.model.TransmitterModel;

public interface TransmitterRepository extends JpaRepository<TransmitterModel, TransmitterModel> {
	
	@Query(value="SELECT * FROM TransmitterModel tm WHERE tm.transactionType = ?1 AND tm.transactionSubType=?2", nativeQuery=true)
	List<TransmitterModel> findByType(String transactionType, String subType);

	@Query(value="SELECT * FROM TransmitterModel tm WHERE tm.transactionType = ?1 AND tm.transactionSubType=?2 AND tm.transmitterMethod=?3", nativeQuery=true)
	public List<TransmitterModel> findByMethod(String transactionType, String subType, String transmitterMethod);
	
//	@Query(value="SELECT * FROM TransmitterModel", nativeQuery=true)
//	public List<TransmitterModel> getAllTransmitterData();
	
	@Query(value="SELECT * FROM TransmitterModel tm WHERE tm.transmitterId = ?1", nativeQuery=true)
	public TransmitterModel findByTransmitterId(long transmitterId);

	@Modifying
	@Transactional
	@Query("UPDATE TransmitterModel tm SET tm.transactionType=:transactionType, tm.transactionSubType=:transactionSubType, tm.transmitterMethod=:transmitterMethod, tm.updatedOn=:updatedOn WHERE tm.transmitterId=:transmitterId")
	public int updateById(@Param("transactionType") String transactionType, @Param("transactionSubType") String transactionSubType, @Param("transmitterMethod") String transmitterMethod, @Param("updatedOn") String updatedOn, @Param("transmitterId") long transmitterId);
}
