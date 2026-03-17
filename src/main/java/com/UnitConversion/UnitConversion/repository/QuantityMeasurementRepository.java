package com.UnitConversion.UnitConversion.repository;

import com.UnitConversion.UnitConversion.model.OperationType;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperation(OperationType operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String thisMeasurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("select q from QuantityMeasurementEntity q where q.operation = :operation and q.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(OperationType operation);

    long countByOperationAndErrorFalse(OperationType operation);

    List<QuantityMeasurementEntity> findByErrorTrue();
}

