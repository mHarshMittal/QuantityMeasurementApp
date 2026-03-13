package com.UnitConversion.UnitConversion.repository;

import com.UnitConversion.UnitConversion.entity.QuantityMeasurementEntity;
import com.UnitConversion.UnitConversion.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    @Override
    public void save(QuantityMeasurementEntity entity) {

        String sql = "INSERT INTO quantity_measurements(value,unit,measurement_type,operation) VALUES (?,?,?,?)";

        try (
                Connection conn = ConnectionPool.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setDouble(1, entity.getValue());
            stmt.setString(2, entity.getUnit());
            stmt.setString(3, entity.getMeasurementType());
            stmt.setString(4, entity.getOperation());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public List<QuantityMeasurementEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}