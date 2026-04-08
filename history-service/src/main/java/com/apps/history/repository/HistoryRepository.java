package com.apps.history.repository;

import com.apps.history.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findByUserEmail(String email);
    List<QuantityMeasurementEntity> findByOperation(String operation);
    List<QuantityMeasurementEntity> findByUserEmailAndOperation(String email, String operation);
    long countByOperationAndErrorFalse(String operation);
    void deleteByUserEmail(String email);
}
