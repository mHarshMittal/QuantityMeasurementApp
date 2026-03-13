package com.UnitConversion.UnitConversion;

import com.UnitConversion.UnitConversion.controller.QuantityMeasurementController;
import com.UnitConversion.UnitConversion.entity.QuantityDTO;
import com.UnitConversion.UnitConversion.repository.IQuantityMeasurementRepository;
import com.UnitConversion.UnitConversion.repository.QuantityMeasurementCacheRepository;
import com.UnitConversion.UnitConversion.repository.QuantityMeasurementDatabaseRepository;
import com.UnitConversion.UnitConversion.service.QuantityMeasurementServiceImpl;

public class App {

    public static void main(String[] args) {

    	IQuantityMeasurementRepository repo =
    	        new QuantityMeasurementDatabaseRepository();

        QuantityMeasurementServiceImpl service =
                new QuantityMeasurementServiceImpl(repo);

        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        QuantityDTO q1 = new QuantityDTO(1, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(12, "INCHES", "LENGTH");

        controller.compare(q1, q2);
        controller.add(q1, q2);
    }
}