package de.peteral.softplc.version;

/**
 * Parses java version string and allows comparison.
 * <p>
 * Expected format:
 * <ul>
 * <li>major.minor.build-prefix_build-suffix
 * <li>for example: 1.8.0_45
 * </ul>
 *
 * @author peteral
 *
 */
public class JavaVersion implements Comparable<JavaVersion> {

	private boolean valid;
	private int major;
	private int minor;
	private int buildPrefix;
	private int buildSuffix;

	/**
	 * Creates a JavaVersion instance based on version string passed in as
	 * parameter
	 *
	 * @param version
	 *            version string for example: 1.8.0_45
	 */
	public JavaVersion(String version) {
		try {
			String[] split = version.split("\\.");
			if (split.length != 3) {
				return;
			}

			major = Integer.parseInt(split[0]);
			minor = Integer.parseInt(split[1]);

			String[] build = split[2].split("_");
			buildPrefix = Integer.parseInt(build[0]);
			buildSuffix = Integer.parseInt(build[1]);

			valid = true;
		} catch (Exception e) {
			// instance invalid if we get here, handling afterwards
		}
	}

	@Override
	public int compareTo(JavaVersion o) {
		int result = Integer.compare(getMajor(), o.getMajor());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(getMinor(), o.getMinor());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(getBuildPrefix(), o.getBuildPrefix());
		if (result != 0) {
			return result;
		}

		return Integer.compare(getBuildSuffix(), o.getBuildSuffix());
	}

	/**
	 *
	 * @return true - version could be parsed, it has valid format
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 *
	 * @return major version
	 */
	public int getMajor() {
		return major;
	}

	/**
	 *
	 * @return minor version
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 *
	 * @return build prefix
	 */
	public int getBuildPrefix() {
		return buildPrefix;
	}

	/**
	 *
	 * @return build suffix
	 */
	public int getBuildSuffix() {
		return buildSuffix;
	}

}
