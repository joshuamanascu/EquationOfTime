import java.util.Scanner;

/**
 * Uses the physical properties of the Earth's orbit around the Sun to
 * calculate the equation of time for a particular time of year. Alternatively,
 * it displays the equation for an entire year.
 *
 * @author Joshua Manascu and Philippe Gabriel
 * @version 2018-05-22
 */
public class EquationOfTime_Final
{
	public static final double epsilon = Math.toRadians(23.4392); // Obliquity of the Ecliptic
	public static final double TwoPI = 2*Math.PI;
	public static final double e = 0.0167; // Eccentricity of the Earth's orbit
	public static final double SiderealYear = 365.25636; // In mean solar days
	public static final double MeanDay = 86400.; // Mean solar day in mean solar seconds
	public static final double SiderealDay = 86164.09053; // In mean solar seconds

	public static Scanner keyb = new Scanner(System.in); // User input


	public static void main(String[] args)
	{
		System.out.println("Choose a mode");
		System.out.println("Mode 1: Enter a day of the year to find the Equation of Time for that day");
		System.out.println("Mode 2: Output for the whole year");
		System.out.print("Choose 1 or 2: ");
		int answer = keyb.nextInt();

		if (answer== 1)
		{
			dayOfYear();
		}
		else if (answer== 2)
		{
			fullYear();
		}
		else
		{
			System.out.println("Invalid selection, please try again");
		}


	}
	/**
	* Calculates the equation of time contribution from the eccentricity
	*
	* @param  day   Day of the year
	* @return diff  The apparent - mean solar time contribution for the eccentricity
	*
	*/
	public static double eccentricity(int day)
	{

		double meanAngle = TwoPI*(day/(SiderealYear)); // In radians
		double correctionAngle = meanAngle + 2*e*Math.sin(meanAngle) + (5./4.)*e*e*Math.sin(2*meanAngle) + e*e*e*((13./12.)*Math.sin(3*meanAngle) - ((1./4.)*Math.sin(meanAngle)));

		double separation = meanAngle - correctionAngle; // Angular separation is equal to the separation between apparent time and mean time
		double diff = (separation/TwoPI) * SiderealDay;

		return diff; // apparent - mean solar time

	}

	/**
	* Calculates the equation of time contribution from the Obliquity
	*
	* @param 	day   Days since the Winter solstice
	* @return diff  The apparent - mean solar time contribution for the Obliquity
	*
	*/
	public static double obliquity(int day) // Days since Winter solstice (Dec 21)
	{

		// Obliquity contribution is composed of four cycles differing only by a sign

		if (day > 0 && day < 92)
		{
		double meanAngle = TwoPI*(day/(SiderealYear));
		double projection = Math.atan(Math.tan(meanAngle) / Math.cos(epsilon));

		double separation = meanAngle - projection;
		double diff = (separation/TwoPI) * SiderealDay;

		return diff;
		}
		else if (day >= 92 && day < 183)
		{
		day -= 91;

		double meanAngle = TwoPI*(day/(SiderealYear));
		double projection = Math.atan(Math.tan(meanAngle) / Math.cos(epsilon));

		double separation = meanAngle - projection;
		double diff = (separation/TwoPI) * SiderealDay;

		return -diff;
		}
		else if (day >= 183 && day < 274)
		{
		day -= 182;

		double meanAngle = TwoPI*(day/(SiderealYear));
		double projection = Math.atan(Math.tan(meanAngle) / Math.cos(epsilon));

		double separation = meanAngle - projection;
		double diff = (separation/TwoPI) * SiderealDay;

		return diff;
		}
		else if (day >= 274 && day < 366)
		{
		day -= 274;

		double meanAngle = TwoPI*(day/(SiderealYear));
		double projection = Math.atan(Math.tan(meanAngle) / Math.cos(epsilon));

		double separation = meanAngle - projection;
		double diff = (separation/TwoPI) * SiderealDay;

		return -diff;
		}
		return 0;
	}

	/**
	* Calculates and displays the equation of time for a particular day of the year
	*
	*/
	public static void dayOfYear()
	{
		int leap, day, daySinceWinter;
		do
		{
			System.out.print("How many years since the last leap year?(0/1/2/3): ");
			leap = keyb.nextInt();
		} while (leap < 0 || leap > 3);

		System.out.println("Enter the day (Jan 1 = 1, Jan 2 = 2, etc)");

		if (leap == 0)
		{
			do
			{
				System.out.print("Between 1 and 366: ");
				day = keyb.nextInt();
			} while (day < 1 || day > 366);

			if (day > 356)
			{
				daySinceWinter = day - 356;
			}
			else
			{
				daySinceWinter = day + 10;
			}

			double eqnOfTime = eccentricity(day) + obliquity(daySinceWinter);

			System.out.println("Eccentricity contribution: " + eccentricity(day));
			System.out.println("Obliquity contribution: " + obliquity(daySinceWinter));

			System.out.printf("The Apparent - Mean solar time is: %.2f seconds\n", eqnOfTime);

		}
		else
		{
			do
			{
				System.out.println("Between 1 and 365: ");
				day = keyb.nextInt();
			} while (day < 1 || day > 365);

			if (day > 355)
			{
				daySinceWinter = day - 355;
			}
			else
			{
				daySinceWinter = day + 10;
			}

			double eqnOfTime = eccentricity(day) + obliquity(daySinceWinter);
			System.out.println("Eccentricity contribution: " + eccentricity(day));
			System.out.println("Obliquity contribution: " + obliquity(daySinceWinter));

			System.out.printf("The Apparent - Mean solar time is: %.2f seconds\n", eqnOfTime);
		}
	}

	/**
	* Calculates and displays the equation of time for an entire year
	*
	*/
	public static void fullYear()
	{
		int leap, day, daySinceWinter;
		double eqnOfTime;

		do
		{
			System.out.print("How many years since the last leap year?(0/1/2/3): ");
			leap = keyb.nextInt();
		} while (leap < 0 || leap > 3);

		System.out.println("Apparent - Mean Solar Time:");
		if (leap == 0)
		{
			for (day = 1; day <= 366; day++)
			{
				if (day > 356)
				{
					daySinceWinter = day - 356;
				}
				else
				{
					daySinceWinter = day + 10;
				}

				eqnOfTime = eccentricity(day) + obliquity(daySinceWinter);

				System.out.printf("%d:		%.2f\n", day, eqnOfTime);
			}

		}
		else
		{

			for (day = 1; day <= 365; day++)
			{
				if (day > 355)
				{
					daySinceWinter = day - 355;
				}
				else
				{
					daySinceWinter = day + 10;
				}

				eqnOfTime = eccentricity(day) + obliquity(daySinceWinter);

				System.out.printf("%d:		%.2f\n", day, eqnOfTime);
			}
		}
	}
}
