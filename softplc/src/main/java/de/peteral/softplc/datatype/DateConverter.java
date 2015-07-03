package de.peteral.softplc.datatype;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Converts S7 DATE_AND_TIME from / to byte arrays.
 * <p>
 *
 * <pre>
 * BCd coded
 * 
 * Byte Inhalt Bereich
 * 
 * 0 Jahr 1990.....2069
 * 1 Monat 1....12
 * 2 Tag 1....31
 * 3Stunde 0...23
 * 4 Minute 0....59
 * 5 Sekunde 0....59
 * 6MSD v. ms 00...99
 * 7(MSB) LSV v. ms0....9
 * 7(LSB) Wochentag1....7 (1 = Sonntag)
 * </pre>
 *
 * String format: 2015-01-02T10:20:30.150 (local ISO)
 *
 * @author peteral
 *
 */
public class DateConverter implements Converter<LocalDateTime> {
	@Override
	public LocalDateTime[] createArray(int count) {
		return new LocalDateTime[count];
	}

	@Override
	public void toBytes(LocalDateTime value, ParsedAddress address,
			byte[] buffer, int offset) {
		buffer[offset] = BCD.toBCD(value.getYear() % 100);
		buffer[offset + 1] = BCD.toBCD(value.getMonthValue());
		buffer[offset + 2] = BCD.toBCD(value.getDayOfMonth());
		buffer[offset + 3] = BCD.toBCD(value.getHour());
		buffer[offset + 4] = BCD.toBCD(value.getMinute());
		buffer[offset + 5] = BCD.toBCD(value.getSecond());
		long millis = value.getNano() / 1000 / 1000;

		buffer[offset + 6] = BCD.toBCD((int) (millis / 10));
		buffer[offset + 7] = BCD.toBCD((int) ((millis % 10) << 4)
				+ getDayOfWeek(value));
	}

	private int getDayOfWeek(LocalDateTime value) {
		switch (value.getDayOfWeek()) {
		case SUNDAY:
			return 1;
		case MONDAY:
			return 2;
		case TUESDAY:
			return 3;
		case WEDNESDAY:
			return 4;
		case THURSDAY:
			return 5;
		case FRIDAY:
			return 6;
		default:
			return 7;
		}
	}

	@Override
	public LocalDateTime fromBytes(byte[] bytes, ParsedAddress address,
			int offset) {

		int year = 2000 + BCD.fromBCD(bytes[offset]);
		int month = BCD.fromBCD(bytes[offset + 1]);
		int dayOfMonth = BCD.fromBCD(bytes[offset + 2]);
		int hour = BCD.fromBCD(bytes[offset + 3]);
		int minute = BCD.fromBCD(bytes[offset + 4]);
		int second = BCD.fromBCD(bytes[offset + 5]);
		int millis = BCD.fromBCD(bytes[offset + 6]) * 10;
		millis += (bytes[offset + 7] & 0xf0) >> 4;

		return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second,
				millis * 1000 * 1000);
	}

	@Override
	public void parseToBytes(String value, ParsedAddress address,
			byte[] buffer, int offset) {

		toBytes(LocalDateTime.parse(value,
				DateTimeFormatter.ISO_LOCAL_DATE_TIME), address, buffer, offset);
	}

}
