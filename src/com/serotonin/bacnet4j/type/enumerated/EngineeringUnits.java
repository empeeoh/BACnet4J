/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class EngineeringUnits extends Enumerated {
    private static final long serialVersionUID = -1334755490239859845L;
    // Acceleration
    public static final EngineeringUnits metersPerSecondPerSecond = new EngineeringUnits(166);
    // Area
    public static final EngineeringUnits squareMeters = new EngineeringUnits(0);
    public static final EngineeringUnits squareCentimeters = new EngineeringUnits(116);
    public static final EngineeringUnits squareFeet = new EngineeringUnits(1);
    public static final EngineeringUnits squareInches = new EngineeringUnits(115);
    // Currency
    public static final EngineeringUnits currency1 = new EngineeringUnits(105);
    public static final EngineeringUnits currency2 = new EngineeringUnits(106);
    public static final EngineeringUnits currency3 = new EngineeringUnits(107);
    public static final EngineeringUnits currency4 = new EngineeringUnits(108);
    public static final EngineeringUnits currency5 = new EngineeringUnits(109);
    public static final EngineeringUnits currency6 = new EngineeringUnits(110);
    public static final EngineeringUnits currency7 = new EngineeringUnits(111);
    public static final EngineeringUnits currency8 = new EngineeringUnits(112);
    public static final EngineeringUnits currency9 = new EngineeringUnits(113);
    public static final EngineeringUnits currency10 = new EngineeringUnits(114);
    // Electrical
    public static final EngineeringUnits milliamperes = new EngineeringUnits(2);
    public static final EngineeringUnits amperes = new EngineeringUnits(3);
    public static final EngineeringUnits amperesPerMeter = new EngineeringUnits(167);
    public static final EngineeringUnits amperesPerSquareMeter = new EngineeringUnits(168);
    public static final EngineeringUnits ampereSquareMeters = new EngineeringUnits(169);
    public static final EngineeringUnits farads = new EngineeringUnits(170);
    public static final EngineeringUnits henrys = new EngineeringUnits(171);
    public static final EngineeringUnits ohms = new EngineeringUnits(4);
    public static final EngineeringUnits ohmMeters = new EngineeringUnits(172);
    public static final EngineeringUnits milliohms = new EngineeringUnits(145);
    public static final EngineeringUnits kilohms = new EngineeringUnits(122);
    public static final EngineeringUnits megohms = new EngineeringUnits(123);
    public static final EngineeringUnits siemens = new EngineeringUnits(173); // 1 mho equals 1 siemens
    public static final EngineeringUnits siemensPerMeter = new EngineeringUnits(174);
    public static final EngineeringUnits teslas = new EngineeringUnits(175);
    public static final EngineeringUnits volts = new EngineeringUnits(5);
    public static final EngineeringUnits millivolts = new EngineeringUnits(124);
    public static final EngineeringUnits kilovolts = new EngineeringUnits(6);
    public static final EngineeringUnits megavolts = new EngineeringUnits(7);
    public static final EngineeringUnits voltAmperes = new EngineeringUnits(8);
    public static final EngineeringUnits kilovoltAmperes = new EngineeringUnits(9);
    public static final EngineeringUnits megavoltAmperes = new EngineeringUnits(10);
    public static final EngineeringUnits voltAmperesReactive = new EngineeringUnits(11);
    public static final EngineeringUnits kilovoltAmperesReactive = new EngineeringUnits(12);
    public static final EngineeringUnits megavoltAmperesReactive = new EngineeringUnits(13);
    public static final EngineeringUnits voltsPerDegreeKelvin = new EngineeringUnits(176);
    public static final EngineeringUnits voltsPerMeter = new EngineeringUnits(177);
    public static final EngineeringUnits degreesPhase = new EngineeringUnits(14);
    public static final EngineeringUnits powerFactor = new EngineeringUnits(15);
    public static final EngineeringUnits webers = new EngineeringUnits(178);
    // Energy
    public static final EngineeringUnits joules = new EngineeringUnits(16);
    public static final EngineeringUnits kilojoules = new EngineeringUnits(17);
    public static final EngineeringUnits kilojoulesPerKilogram = new EngineeringUnits(125);
    public static final EngineeringUnits megajoules = new EngineeringUnits(126);
    public static final EngineeringUnits wattHours = new EngineeringUnits(18);
    public static final EngineeringUnits kilowattHours = new EngineeringUnits(19);
    public static final EngineeringUnits megawattHours = new EngineeringUnits(146);
    public static final EngineeringUnits btus = new EngineeringUnits(20);
    public static final EngineeringUnits kiloBtus = new EngineeringUnits(147);
    public static final EngineeringUnits megaBtus = new EngineeringUnits(148);
    public static final EngineeringUnits therms = new EngineeringUnits(21);
    public static final EngineeringUnits tonHours = new EngineeringUnits(22);
    // Enthalpy
    public static final EngineeringUnits joulesPerKilogramDryAir = new EngineeringUnits(23);
    public static final EngineeringUnits kilojoulesPerKilogramDryAir = new EngineeringUnits(149);
    public static final EngineeringUnits megajoulesPerKilogramDryAir = new EngineeringUnits(150);
    public static final EngineeringUnits btusPerPoundDryAir = new EngineeringUnits(24);
    public static final EngineeringUnits btusPerPound = new EngineeringUnits(117);
    // Entropy
    public static final EngineeringUnits joulesPerDegreeKelvin = new EngineeringUnits(127);
    public static final EngineeringUnits kilojoulesPerDegreeKelvin = new EngineeringUnits(151);
    public static final EngineeringUnits megajoulesPerDegreeKelvin = new EngineeringUnits(152);
    public static final EngineeringUnits joulesPerKilogramDegreeKelvin = new EngineeringUnits(128);
    // Force
    public static final EngineeringUnits newton = new EngineeringUnits(153);
    // Frequency
    public static final EngineeringUnits cyclesPerHour = new EngineeringUnits(25);
    public static final EngineeringUnits cyclesPerMinute = new EngineeringUnits(26);
    public static final EngineeringUnits hertz = new EngineeringUnits(27);
    public static final EngineeringUnits kilohertz = new EngineeringUnits(129);
    public static final EngineeringUnits megahertz = new EngineeringUnits(130);
    public static final EngineeringUnits perHour = new EngineeringUnits(131);
    // Humidity
    public static final EngineeringUnits gramsOfWaterPerKilogramDryAir = new EngineeringUnits(28);
    public static final EngineeringUnits percentRelativeHumidity = new EngineeringUnits(29);
    // Length
    public static final EngineeringUnits millimeters = new EngineeringUnits(30);
    public static final EngineeringUnits centimeters = new EngineeringUnits(118);
    public static final EngineeringUnits meters = new EngineeringUnits(31);
    public static final EngineeringUnits inches = new EngineeringUnits(32);
    public static final EngineeringUnits feet = new EngineeringUnits(33);
    // Light
    public static final EngineeringUnits candelas = new EngineeringUnits(179);
    public static final EngineeringUnits candelasPerSquareMeter = new EngineeringUnits(180);
    public static final EngineeringUnits wattsPerSquareFoot = new EngineeringUnits(34);
    public static final EngineeringUnits wattsPerSquareMeter = new EngineeringUnits(35);
    public static final EngineeringUnits lumens = new EngineeringUnits(36);
    public static final EngineeringUnits luxes = new EngineeringUnits(37);
    public static final EngineeringUnits footCandles = new EngineeringUnits(38);
    // Mass
    public static final EngineeringUnits kilograms = new EngineeringUnits(39);
    public static final EngineeringUnits poundsMass = new EngineeringUnits(40);
    public static final EngineeringUnits tons = new EngineeringUnits(41);
    // Mass Flow
    public static final EngineeringUnits gramsPerSecond = new EngineeringUnits(154);
    public static final EngineeringUnits gramsPerMinute = new EngineeringUnits(155);
    public static final EngineeringUnits kilogramsPerSecond = new EngineeringUnits(42);
    public static final EngineeringUnits kilogramsPerMinute = new EngineeringUnits(43);
    public static final EngineeringUnits kilogramsPerHour = new EngineeringUnits(44);
    public static final EngineeringUnits poundsMassPerSecond = new EngineeringUnits(119);
    public static final EngineeringUnits poundsMassPerMinute = new EngineeringUnits(45);
    public static final EngineeringUnits poundsMassPerHour = new EngineeringUnits(46);
    public static final EngineeringUnits tonsPerHour = new EngineeringUnits(156);
    // Power
    public static final EngineeringUnits milliwatts = new EngineeringUnits(132);
    public static final EngineeringUnits watts = new EngineeringUnits(47);
    public static final EngineeringUnits kilowatts = new EngineeringUnits(48);
    public static final EngineeringUnits megawatts = new EngineeringUnits(49);
    public static final EngineeringUnits btusPerHour = new EngineeringUnits(50);
    public static final EngineeringUnits kiloBtusPerHour = new EngineeringUnits(157);
    public static final EngineeringUnits horsepower = new EngineeringUnits(51);
    public static final EngineeringUnits tonsRefrigeration = new EngineeringUnits(52);
    // Pressure
    public static final EngineeringUnits pascals = new EngineeringUnits(53);
    public static final EngineeringUnits hectopascals = new EngineeringUnits(133);
    public static final EngineeringUnits kilopascals = new EngineeringUnits(54);
    public static final EngineeringUnits millibars = new EngineeringUnits(134);
    public static final EngineeringUnits bars = new EngineeringUnits(55);
    public static final EngineeringUnits poundsForcePerSquareInch = new EngineeringUnits(56);
    public static final EngineeringUnits centimetersOfWater = new EngineeringUnits(57);
    public static final EngineeringUnits inchesOfWater = new EngineeringUnits(58);
    public static final EngineeringUnits millimetersOfMercury = new EngineeringUnits(59);
    public static final EngineeringUnits centimetersOfMercury = new EngineeringUnits(60);
    public static final EngineeringUnits inchesOfMercury = new EngineeringUnits(61);
    // Temperature
    public static final EngineeringUnits degreesCelsius = new EngineeringUnits(62);
    public static final EngineeringUnits degreesKelvin = new EngineeringUnits(63);
    public static final EngineeringUnits degreesKelvinPerHour = new EngineeringUnits(181);
    public static final EngineeringUnits degreesKelvinPerMinute = new EngineeringUnits(182);
    public static final EngineeringUnits degreesFahrenheit = new EngineeringUnits(64);
    public static final EngineeringUnits degreeDaysCelsius = new EngineeringUnits(65);
    public static final EngineeringUnits degreeDaysFahrenheit = new EngineeringUnits(66);
    public static final EngineeringUnits deltaDegreesFahrenheit = new EngineeringUnits(120);
    public static final EngineeringUnits deltaDegreesKelvin = new EngineeringUnits(121);
    // Time
    public static final EngineeringUnits years = new EngineeringUnits(67);
    public static final EngineeringUnits months = new EngineeringUnits(68);
    public static final EngineeringUnits weeks = new EngineeringUnits(69);
    public static final EngineeringUnits days = new EngineeringUnits(70);
    public static final EngineeringUnits hours = new EngineeringUnits(71);
    public static final EngineeringUnits minutes = new EngineeringUnits(72);
    public static final EngineeringUnits seconds = new EngineeringUnits(73);
    public static final EngineeringUnits hundredthsSeconds = new EngineeringUnits(158);
    public static final EngineeringUnits milliseconds = new EngineeringUnits(159);
    // Torque
    public static final EngineeringUnits newtonMeters = new EngineeringUnits(160);
    // Velocity
    public static final EngineeringUnits millimetersPerSecond = new EngineeringUnits(161);
    public static final EngineeringUnits millimetersPerMinute = new EngineeringUnits(162);
    public static final EngineeringUnits metersPerSecond = new EngineeringUnits(74);
    public static final EngineeringUnits metersPerMinute = new EngineeringUnits(163);
    public static final EngineeringUnits metersPerHour = new EngineeringUnits(164);
    public static final EngineeringUnits kilometersPerHour = new EngineeringUnits(75);
    public static final EngineeringUnits feetPerSecond = new EngineeringUnits(76);
    public static final EngineeringUnits feetPerMinute = new EngineeringUnits(77);
    public static final EngineeringUnits milesPerHour = new EngineeringUnits(78);
    // Volume
    public static final EngineeringUnits cubicFeet = new EngineeringUnits(79);
    public static final EngineeringUnits cubicMeters = new EngineeringUnits(80);
    public static final EngineeringUnits imperialGallons = new EngineeringUnits(81);
    public static final EngineeringUnits liters = new EngineeringUnits(82);
    public static final EngineeringUnits usGallons = new EngineeringUnits(83);
    // Volumetric Flow
    public static final EngineeringUnits cubicFeetPerSecond = new EngineeringUnits(142);
    public static final EngineeringUnits cubicFeetPerMinute = new EngineeringUnits(84);
    public static final EngineeringUnits cubicMetersPerSecond = new EngineeringUnits(85);
    public static final EngineeringUnits cubicMetersPerMinute = new EngineeringUnits(165);
    public static final EngineeringUnits cubicMetersPerHour = new EngineeringUnits(135);
    public static final EngineeringUnits imperialGallonsPerMinute = new EngineeringUnits(86);
    public static final EngineeringUnits litersPerSecond = new EngineeringUnits(87);
    public static final EngineeringUnits litersPerMinute = new EngineeringUnits(88);
    public static final EngineeringUnits litersPerHour = new EngineeringUnits(136);
    public static final EngineeringUnits usGallonsPerMinute = new EngineeringUnits(89);
    // Other
    public static final EngineeringUnits degreesAngular = new EngineeringUnits(90);
    public static final EngineeringUnits degreesCelsiusPerHour = new EngineeringUnits(91);
    public static final EngineeringUnits degreesCelsiusPerMinute = new EngineeringUnits(92);
    public static final EngineeringUnits degreesFahrenheitPerHour = new EngineeringUnits(93);
    public static final EngineeringUnits degreesFahrenheitPerMinute = new EngineeringUnits(94);
    public static final EngineeringUnits jouleSeconds = new EngineeringUnits(183);
    public static final EngineeringUnits kilogramsPerCubicMeter = new EngineeringUnits(186);
    public static final EngineeringUnits kilowattHoursPerSquareMeter = new EngineeringUnits(137);
    public static final EngineeringUnits kilowattHoursPerSquareFoot = new EngineeringUnits(138);
    public static final EngineeringUnits megajoulesPerSquareMeter = new EngineeringUnits(139);
    public static final EngineeringUnits megajoulesPerSquareFoot = new EngineeringUnits(140);
    public static final EngineeringUnits noUnits = new EngineeringUnits(95);
    public static final EngineeringUnits newtonSeconds = new EngineeringUnits(187);
    public static final EngineeringUnits newtonsPerMeter = new EngineeringUnits(188);
    public static final EngineeringUnits partsPerMillion = new EngineeringUnits(96);
    public static final EngineeringUnits partsPerBillion = new EngineeringUnits(97);
    public static final EngineeringUnits percent = new EngineeringUnits(98);
    public static final EngineeringUnits percentObscurationPerFoot = new EngineeringUnits(143);
    public static final EngineeringUnits percentObscurationPerMeter = new EngineeringUnits(144);
    public static final EngineeringUnits percentPerSecond = new EngineeringUnits(99);
    public static final EngineeringUnits perMinute = new EngineeringUnits(100);
    public static final EngineeringUnits perSecond = new EngineeringUnits(101);
    public static final EngineeringUnits psiPerDegreeFahrenheit = new EngineeringUnits(102);
    public static final EngineeringUnits radians = new EngineeringUnits(103);
    public static final EngineeringUnits radiansPerSecond = new EngineeringUnits(184);
    public static final EngineeringUnits revolutionsPerMinute = new EngineeringUnits(104);
    public static final EngineeringUnits squareMetersPerNewton = new EngineeringUnits(185);
    public static final EngineeringUnits wattsPerMeterPerDegreeKelvin = new EngineeringUnits(189);
    public static final EngineeringUnits wattsPerSquareMeterDegreeKelvin = new EngineeringUnits(141);

    public static final EngineeringUnits[] ALL = { squareMeters, squareFeet, milliamperes, amperes, ohms, volts,
            kilovolts, megavolts, voltAmperes, kilovoltAmperes, megavoltAmperes, voltAmperesReactive,
            kilovoltAmperesReactive, megavoltAmperesReactive, degreesPhase, powerFactor, joules, kilojoules, wattHours,
            kilowattHours, btus, therms, tonHours, joulesPerKilogramDryAir, btusPerPoundDryAir, cyclesPerHour,
            cyclesPerMinute, hertz, gramsOfWaterPerKilogramDryAir, percentRelativeHumidity, millimeters, meters,
            inches, feet, wattsPerSquareFoot, wattsPerSquareMeter, lumens, luxes, footCandles, kilograms, poundsMass,
            tons, kilogramsPerSecond, kilogramsPerMinute, kilogramsPerHour, poundsMassPerMinute, poundsMassPerHour,
            watts, kilowatts, megawatts, btusPerHour, horsepower, tonsRefrigeration, pascals, kilopascals, bars,
            poundsForcePerSquareInch, centimetersOfWater, inchesOfWater, millimetersOfMercury, centimetersOfMercury,
            inchesOfMercury, degreesCelsius, degreesKelvin, degreesFahrenheit, degreeDaysCelsius, degreeDaysFahrenheit,
            years, months, weeks, days, hours, minutes, seconds, metersPerSecond, kilometersPerHour, feetPerSecond,
            feetPerMinute, milesPerHour, cubicFeet, cubicMeters, imperialGallons, liters, usGallons,
            cubicFeetPerMinute, cubicMetersPerSecond, imperialGallonsPerMinute, litersPerSecond, litersPerMinute,
            usGallonsPerMinute, degreesAngular, degreesCelsiusPerHour, degreesCelsiusPerMinute,
            degreesFahrenheitPerHour, degreesFahrenheitPerMinute, noUnits, partsPerMillion, partsPerBillion, percent,
            percentPerSecond, perMinute, perSecond, psiPerDegreeFahrenheit, radians, revolutionsPerMinute, currency1,
            currency2, currency3, currency4, currency5, currency6, currency7, currency8, currency9, currency10,
            squareInches, squareCentimeters, btusPerPound, centimeters, poundsMassPerSecond, deltaDegreesFahrenheit,
            deltaDegreesKelvin, kilohms, megohms, millivolts, kilojoulesPerKilogram, megajoules, joulesPerDegreeKelvin,
            joulesPerKilogramDegreeKelvin, kilohertz, megahertz, perHour, milliwatts, hectopascals, millibars,
            cubicMetersPerHour, litersPerHour, kilowattHoursPerSquareMeter, kilowattHoursPerSquareFoot,
            megajoulesPerSquareMeter, megajoulesPerSquareFoot, wattsPerSquareMeterDegreeKelvin, cubicFeetPerSecond,
            percentObscurationPerFoot, percentObscurationPerMeter, milliohms, megawattHours, kiloBtus, megaBtus,
            kilojoulesPerKilogramDryAir, megajoulesPerKilogramDryAir, kilojoulesPerDegreeKelvin,
            megajoulesPerDegreeKelvin, newton, gramsPerSecond, gramsPerMinute, tonsPerHour, kiloBtusPerHour,
            hundredthsSeconds, milliseconds, newtonMeters, millimetersPerSecond, millimetersPerMinute, metersPerMinute,
            metersPerHour, cubicMetersPerMinute, metersPerSecondPerSecond, amperesPerMeter, amperesPerSquareMeter,
            ampereSquareMeters, farads, henrys, ohmMeters, siemens, siemensPerMeter, teslas, voltsPerDegreeKelvin,
            voltsPerMeter, webers, candelas, candelasPerSquareMeter, degreesKelvinPerHour, degreesKelvinPerMinute,
            jouleSeconds, radiansPerSecond, squareMetersPerNewton, kilogramsPerCubicMeter, newtonSeconds,
            newtonsPerMeter, wattsPerMeterPerDegreeKelvin, };

    public EngineeringUnits(int value) {
        super(value);
    }

    public EngineeringUnits(ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        int type = intValue();
        if (type == metersPerSecondPerSecond.intValue())
            return "meters per second per second";
        if (type == squareMeters.intValue())
            return "square meters";
        if (type == squareCentimeters.intValue())
            return "square centimeters";
        if (type == squareFeet.intValue())
            return "square feet";
        if (type == squareInches.intValue())
            return "square inches";
        if (type == currency1.intValue())
            return "currency 1";
        if (type == currency2.intValue())
            return "currency 2";
        if (type == currency3.intValue())
            return "currency 3";
        if (type == currency4.intValue())
            return "currency 4";
        if (type == currency5.intValue())
            return "currency 5";
        if (type == currency6.intValue())
            return "currency 6";
        if (type == currency7.intValue())
            return "currency 7";
        if (type == currency8.intValue())
            return "currency 8";
        if (type == currency9.intValue())
            return "currency 9";
        if (type == currency10.intValue())
            return "currency 10";
        if (type == milliamperes.intValue())
            return "milliamperes";
        if (type == amperes.intValue())
            return "amperes";
        if (type == amperesPerMeter.intValue())
            return "amperes per meter";
        if (type == amperesPerSquareMeter.intValue())
            return "amperes per square meter";
        if (type == ampereSquareMeters.intValue())
            return "ampere square meters";
        if (type == farads.intValue())
            return "farads";
        if (type == henrys.intValue())
            return "henrys";
        if (type == ohms.intValue())
            return "ohms";
        if (type == ohmMeters.intValue())
            return "ohm meters";
        if (type == milliohms.intValue())
            return "milliohms";
        if (type == kilohms.intValue())
            return "kilohms";
        if (type == megohms.intValue())
            return "megohms";
        if (type == siemens.intValue())
            return "siemens";
        if (type == siemensPerMeter.intValue())
            return "siemens per meter";
        if (type == teslas.intValue())
            return "teslas";
        if (type == volts.intValue())
            return "volts";
        if (type == millivolts.intValue())
            return "millivolts";
        if (type == kilovolts.intValue())
            return "kilovolts";
        if (type == megavolts.intValue())
            return "megavolts";
        if (type == voltAmperes.intValue())
            return "volt amperes";
        if (type == kilovoltAmperes.intValue())
            return "kilovolt amperes";
        if (type == megavoltAmperes.intValue())
            return "megavolt amperes";
        if (type == voltAmperesReactive.intValue())
            return "volt amperes reactive";
        if (type == kilovoltAmperesReactive.intValue())
            return "kilovolt amperes reactive";
        if (type == megavoltAmperesReactive.intValue())
            return "megavolt amperes reactive";
        if (type == voltsPerDegreeKelvin.intValue())
            return "volts per degree kelvin";
        if (type == voltsPerMeter.intValue())
            return "volts per meter";
        if (type == degreesPhase.intValue())
            return "degrees phase";
        if (type == powerFactor.intValue())
            return "power factor";
        if (type == webers.intValue())
            return "webers";
        if (type == joules.intValue())
            return "joules";
        if (type == kilojoules.intValue())
            return "kilojoules";
        if (type == kilojoulesPerKilogram.intValue())
            return "kilojoules per kilogram";
        if (type == megajoules.intValue())
            return "megajoules";
        if (type == wattHours.intValue())
            return "watt hours";
        if (type == kilowattHours.intValue())
            return "kilowatt hours";
        if (type == megawattHours.intValue())
            return "megawatt hours";
        if (type == btus.intValue())
            return "btus";
        if (type == kiloBtus.intValue())
            return "kilo btus";
        if (type == megaBtus.intValue())
            return "mega btus";
        if (type == therms.intValue())
            return "therms";
        if (type == tonHours.intValue())
            return "ton hours";
        if (type == joulesPerKilogramDryAir.intValue())
            return "joules per kilogram dry air";
        if (type == kilojoulesPerKilogramDryAir.intValue())
            return "kilojoules per kilogram dry air";
        if (type == megajoulesPerKilogramDryAir.intValue())
            return "megajoules per kilogram dry air";
        if (type == btusPerPoundDryAir.intValue())
            return "btus per pound dry air";
        if (type == btusPerPound.intValue())
            return "btus per pound";
        if (type == joulesPerDegreeKelvin.intValue())
            return "joules per degree kelvin";
        if (type == kilojoulesPerDegreeKelvin.intValue())
            return "kilojoules per degree kelvin";
        if (type == megajoulesPerDegreeKelvin.intValue())
            return "megajoules per degree kelvin";
        if (type == joulesPerKilogramDegreeKelvin.intValue())
            return "joules per kilogram degree kelvin";
        if (type == newton.intValue())
            return "newton";
        if (type == cyclesPerHour.intValue())
            return "cycles per hour";
        if (type == cyclesPerMinute.intValue())
            return "cycles per minute";
        if (type == hertz.intValue())
            return "hertz";
        if (type == kilohertz.intValue())
            return "kilohertz";
        if (type == megahertz.intValue())
            return "megahertz";
        if (type == perHour.intValue())
            return "per hour";
        if (type == gramsOfWaterPerKilogramDryAir.intValue())
            return "grams of water per kilogram dry air";
        if (type == percentRelativeHumidity.intValue())
            return "percent relative humidity";
        if (type == millimeters.intValue())
            return "millimeters";
        if (type == centimeters.intValue())
            return "centimeters";
        if (type == meters.intValue())
            return "meters";
        if (type == inches.intValue())
            return "inches";
        if (type == feet.intValue())
            return "feet";
        if (type == candelas.intValue())
            return "candelas";
        if (type == candelasPerSquareMeter.intValue())
            return "candelas per square meter";
        if (type == wattsPerSquareFoot.intValue())
            return "watts per square foot";
        if (type == wattsPerSquareMeter.intValue())
            return "watts per square meter";
        if (type == lumens.intValue())
            return "lumens";
        if (type == luxes.intValue())
            return "luxes";
        if (type == footCandles.intValue())
            return "foot candles";
        if (type == kilograms.intValue())
            return "kilograms";
        if (type == poundsMass.intValue())
            return "pounds mass";
        if (type == tons.intValue())
            return "tons";
        if (type == gramsPerSecond.intValue())
            return "grams per second";
        if (type == gramsPerMinute.intValue())
            return "grams per minute";
        if (type == kilogramsPerSecond.intValue())
            return "kilograms per second";
        if (type == kilogramsPerMinute.intValue())
            return "kilograms per minute";
        if (type == kilogramsPerHour.intValue())
            return "kilograms per hour";
        if (type == poundsMassPerSecond.intValue())
            return "pounds mass per second";
        if (type == poundsMassPerMinute.intValue())
            return "pounds mass per minute";
        if (type == poundsMassPerHour.intValue())
            return "pounds mass per hour";
        if (type == tonsPerHour.intValue())
            return "tons per hour";
        if (type == milliwatts.intValue())
            return "milliwatts";
        if (type == watts.intValue())
            return "watts";
        if (type == kilowatts.intValue())
            return "kilowatts";
        if (type == megawatts.intValue())
            return "megawatts";
        if (type == btusPerHour.intValue())
            return "btus per hour";
        if (type == kiloBtusPerHour.intValue())
            return "kilo btus per hour";
        if (type == horsepower.intValue())
            return "horsepower";
        if (type == tonsRefrigeration.intValue())
            return "tons refrigeration";
        if (type == pascals.intValue())
            return "pascals";
        if (type == hectopascals.intValue())
            return "hectopascals";
        if (type == kilopascals.intValue())
            return "kilopascals";
        if (type == millibars.intValue())
            return "millibars";
        if (type == bars.intValue())
            return "bars";
        if (type == poundsForcePerSquareInch.intValue())
            return "pounds force per square inch";
        if (type == centimetersOfWater.intValue())
            return "centimeters of water";
        if (type == inchesOfWater.intValue())
            return "inches of water";
        if (type == millimetersOfMercury.intValue())
            return "millimeters of mercury";
        if (type == centimetersOfMercury.intValue())
            return "centimeters of mercury";
        if (type == inchesOfMercury.intValue())
            return "inches of mercury";
        if (type == degreesCelsius.intValue())
            return "degrees celsius";
        if (type == degreesKelvin.intValue())
            return "degrees kelvin";
        if (type == degreesKelvinPerHour.intValue())
            return "degrees kelvin per hour";
        if (type == degreesKelvinPerMinute.intValue())
            return "degrees kelvin per minute";
        if (type == degreesFahrenheit.intValue())
            return "degrees fahrenheit";
        if (type == degreeDaysCelsius.intValue())
            return "degree days celsius";
        if (type == degreeDaysFahrenheit.intValue())
            return "degree days fahrenheit";
        if (type == deltaDegreesFahrenheit.intValue())
            return "delta degrees fahrenheit";
        if (type == deltaDegreesKelvin.intValue())
            return "delta degrees kelvin";
        if (type == years.intValue())
            return "years";
        if (type == months.intValue())
            return "months";
        if (type == weeks.intValue())
            return "weeks";
        if (type == days.intValue())
            return "days";
        if (type == hours.intValue())
            return "hours";
        if (type == minutes.intValue())
            return "minutes";
        if (type == seconds.intValue())
            return "seconds";
        if (type == hundredthsSeconds.intValue())
            return "hundredths seconds";
        if (type == milliseconds.intValue())
            return "milliseconds";
        if (type == newtonMeters.intValue())
            return "newton meters";
        if (type == millimetersPerSecond.intValue())
            return "millimeters per second";
        if (type == millimetersPerMinute.intValue())
            return "millimeters per minute";
        if (type == metersPerSecond.intValue())
            return "meters per second";
        if (type == metersPerMinute.intValue())
            return "meters per minute";
        if (type == metersPerHour.intValue())
            return "meters per hour";
        if (type == kilometersPerHour.intValue())
            return "kilometers per hour";
        if (type == feetPerSecond.intValue())
            return "feet per second";
        if (type == feetPerMinute.intValue())
            return "feet per minute";
        if (type == milesPerHour.intValue())
            return "miles per hour";
        if (type == cubicFeet.intValue())
            return "cubic feet";
        if (type == cubicMeters.intValue())
            return "cubic meters";
        if (type == imperialGallons.intValue())
            return "imperial gallons";
        if (type == liters.intValue())
            return "liters";
        if (type == usGallons.intValue())
            return "us gallons";
        if (type == cubicFeetPerSecond.intValue())
            return "cubic feet per second";
        if (type == cubicFeetPerMinute.intValue())
            return "cubic feet per minute";
        if (type == cubicMetersPerSecond.intValue())
            return "cubic meters per second";
        if (type == cubicMetersPerMinute.intValue())
            return "cubic meters per minute";
        if (type == cubicMetersPerHour.intValue())
            return "cubic meters per hour";
        if (type == imperialGallonsPerMinute.intValue())
            return "imperial gallons per minute";
        if (type == litersPerSecond.intValue())
            return "liters per second";
        if (type == litersPerMinute.intValue())
            return "liters per minute";
        if (type == litersPerHour.intValue())
            return "liters per hour";
        if (type == usGallonsPerMinute.intValue())
            return "us gallons per minute";
        if (type == degreesAngular.intValue())
            return "degrees angular";
        if (type == degreesCelsiusPerHour.intValue())
            return "degrees celsius per hour";
        if (type == degreesCelsiusPerMinute.intValue())
            return "degrees celsius per minute";
        if (type == degreesFahrenheitPerHour.intValue())
            return "degrees fahrenheit per hour";
        if (type == degreesFahrenheitPerMinute.intValue())
            return "degrees fahrenheit per minute";
        if (type == jouleSeconds.intValue())
            return "joule seconds";
        if (type == kilogramsPerCubicMeter.intValue())
            return "kilograms per cubic meter";
        if (type == kilowattHoursPerSquareMeter.intValue())
            return "kilowatt hours per square meter";
        if (type == kilowattHoursPerSquareFoot.intValue())
            return "kilowatt hours per square foot";
        if (type == megajoulesPerSquareMeter.intValue())
            return "megajoules per square meter";
        if (type == megajoulesPerSquareFoot.intValue())
            return "megajoules per square foot";
        if (type == noUnits.intValue())
            return "";
        if (type == newtonSeconds.intValue())
            return "newton seconds";
        if (type == newtonsPerMeter.intValue())
            return "newtons per meter";
        if (type == partsPerMillion.intValue())
            return "parts per million";
        if (type == partsPerBillion.intValue())
            return "parts per billion";
        if (type == percent.intValue())
            return "percent";
        if (type == percentObscurationPerFoot.intValue())
            return "percent obscuration per foot";
        if (type == percentObscurationPerMeter.intValue())
            return "percent obscuration per meter";
        if (type == percentPerSecond.intValue())
            return "percent per second";
        if (type == perMinute.intValue())
            return "per minute";
        if (type == perSecond.intValue())
            return "per second";
        if (type == psiPerDegreeFahrenheit.intValue())
            return "psi per degree fahrenheit";
        if (type == radians.intValue())
            return "radians";
        if (type == radiansPerSecond.intValue())
            return "radians per second";
        if (type == revolutionsPerMinute.intValue())
            return "revolutions per minute";
        if (type == squareMetersPerNewton.intValue())
            return "square meters per Newton";
        if (type == wattsPerMeterPerDegreeKelvin.intValue())
            return "watts per meter per degree kelvin";
        if (type == wattsPerSquareMeterDegreeKelvin.intValue())
            return "watts per square meter degree kelvin";
        return "Unknown: " + type;
    }
}
