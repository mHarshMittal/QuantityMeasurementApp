package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.dto.QuantityInputDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.entity.User;
import com.apps.quantitymeasurement.repository.UserRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QuantityMeasurementAppApplicationTest {

	@Autowired
	private IQuantityMeasurementService service;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Long testUserId;

	@BeforeEach
	public void setUp() {
		// Create a test user if not present
		User user = userRepository.findByEmail("test@test.com").orElseGet(() -> {
			User u = new User();
			u.setName("Test User");
			u.setEmail("test@test.com");
			u.setPassword(passwordEncoder.encode("password"));
			return userRepository.save(u);
		});
		testUserId = user.getId();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCompare_FeetToInches_Equal() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "FEET", "LENGTH"),
				new QuantityDTO(12.0, "INCHES", "LENGTH"));
		QuantityMeasurementEntity result = service.compare(testUserId, input);
		assertEquals("true", result.getResultString());
		assertFalse(result.isError());
	}

	@Test
	public void testCompare_NotEqual() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "FEET", "LENGTH"),
				new QuantityDTO(5.0, "INCHES", "LENGTH"));
		QuantityMeasurementEntity result = service.compare(testUserId, input);
		assertEquals("false", result.getResultString());
	}

	@Test
	public void testConvert_FeetToInches() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "FEET", "LENGTH"),
				new QuantityDTO(0.0, "INCHES", "LENGTH"));
		QuantityMeasurementEntity result = service.convert(testUserId, input);
		assertEquals(12.0, result.getResultValue());
		assertFalse(result.isError());
	}

	@Test
	public void testAdd_FeetAndInches() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "FEET", "LENGTH"),
				new QuantityDTO(12.0, "INCHES", "LENGTH"));
		QuantityMeasurementEntity result = service.add(testUserId, input);
		assertNotNull(result.getResultValue());
		assertFalse(result.isError());
	}

	@Test
	public void testSubtract_Success() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(2.0, "FEET", "LENGTH"),
				new QuantityDTO(6.0, "INCHES", "LENGTH"));
		QuantityMeasurementEntity result = service.subtract(testUserId, input);
		assertNotNull(result.getResultValue());
		assertFalse(result.isError());
	}

	@Test
	public void testDivide_Success() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(10.0, "FEET", "LENGTH"),
				new QuantityDTO(2.0, "FEET", "LENGTH"));
		QuantityMeasurementEntity result = service.divide(testUserId, input);
		assertEquals(5.0, result.getResultValue());
		assertFalse(result.isError());
	}

	@Test
	public void testDivide_ByZero_SetsError() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(10.0, "FEET", "LENGTH"),
				new QuantityDTO(0.0, "FEET", "LENGTH"));
		QuantityMeasurementEntity result = service.divide(testUserId, input);
		assertTrue(result.isError());
	}

	@Test
	public void testTemperatureConversion() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
				new QuantityDTO(0.0, "FAHRENHEIT", "TEMPERATURE"));
		QuantityMeasurementEntity result = service.convert(testUserId, input);
		assertEquals(212.0, result.getResultValue());
		assertFalse(result.isError());
	}

	@Test
	public void testTemperatureAdd_SetsError() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE"),
				new QuantityDTO(50.0, "CELSIUS", "TEMPERATURE"));
		QuantityMeasurementEntity result = service.add(testUserId, input);
		assertTrue(result.isError());
	}

	@Test
	public void testGetHistory_NotEmpty() {
		service.compare(testUserId, new QuantityInputDTO(
				new QuantityDTO(1.0, "FEET", "LENGTH"),
				new QuantityDTO(12.0, "INCHES", "LENGTH")));
		assertFalse(service.getHistory().isEmpty());
	}

	@Test
	public void testGetHistoryByOperation() {
		service.convert(testUserId, new QuantityInputDTO(
				new QuantityDTO(1.0, "LITRE", "VOLUME"),
				new QuantityDTO(0.0, "MILLILITRE", "VOLUME")));
		assertFalse(service.getHistoryByOperation("CONVERT").isEmpty());
	}

	@Test
	public void testWeightComparison() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "KILOGRAM", "WEIGHT"),
				new QuantityDTO(1000.0, "GRAM", "WEIGHT"));
		QuantityMeasurementEntity result = service.compare(testUserId, input);
		assertEquals("true", result.getResultString());
	}

	@Test
	public void testVolumeConversion() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(1.0, "LITRE", "VOLUME"),
				new QuantityDTO(0.0, "MILLILITRE", "VOLUME"));
		QuantityMeasurementEntity result = service.convert(testUserId, input);
		assertEquals(1000.0, result.getResultValue());
	}

	@Test
	public void testMultiply_Success() {
		QuantityInputDTO input = new QuantityInputDTO(
				new QuantityDTO(2.0, "FEET", "LENGTH"),
				new QuantityDTO(3.0, "FEET", "LENGTH"));
		QuantityMeasurementEntity result = service.multiply(testUserId, input);
		assertNotNull(result.getResultValue());
		assertFalse(result.isError());
	}
}
