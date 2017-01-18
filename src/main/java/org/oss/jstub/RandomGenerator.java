package org.oss.jstub;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Random class which extends and relies on java.util.Random to generate other random
 * basic types.
 * @author nicu
 */
public class RandomGenerator extends Random {
    public String nextAlphabeticString() {
        return nextAlphabeticString(5);
    }

    public String nextAlphabeticString(int size) {
        // there is an equivalent of this in Apache Commons RandomStringUtils
        // but high school programming...
        byte[] bytes = new byte[size];
        super.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] += Byte.MIN_VALUE;
            }
            bytes[i] %= ('z' - 'a');
            bytes[i] += 'a';
            if (bytes[i] % 2 == 0) {
                bytes[i] -= ('a' - 'A');
            }
        }
        return new String(bytes);
    }

    public char nextChar() {
        return nextAlphabeticString(1).charAt(0);
    }

    public byte nextByte() {
        byte[] bytes = new byte[1];
        nextBytes(bytes);
        return bytes[0];
    }

    // before today random date
    public Date nextDate() {
        Date date = new Date();
        long time = date.getTime();
        date.setTime(Math.abs(super.nextLong() % time));
        return date;
    }

    public BigInteger nextBigInteger() {
        return new BigInteger(24, this);
    }

    public long nextPositiveLong() {
        return Math.abs(nextLong());
    }

    public int nextPositiveInt() {
        return Math.abs(nextInt());
    }

    public Character nextCharacter() {
        return nextChar();
    }

    public Byte nextByteObj() {
        return nextByte();
    }

    public Integer nextInteger() {
        return nextInt();
    }

    public Long nextLongObj() {
        return nextLong();
    }

    public Float nextFloatObj() {
        return nextFloat();
    }

    public Double nextDoubleObj() {
        return nextDouble();
    }

    public BigDecimal nextBigDecimal() {
        return new BigDecimal(nextBigInteger());
    }

    public Timestamp nextTimestamp() {
        return new Timestamp(nextDate().getTime());
    }

    public Calendar nextCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextDate());
        return calendar;
    }

    public GregorianCalendar nextGregorianCalendar() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(nextDate());
        return gregorianCalendar;
    }

    public XMLGregorianCalendar nextXmlGregorianCalendar() {
        return new XMLGregorianCalendarImpl(nextGregorianCalendar());
    }

    public LocalDate nextLocalDate() {
        return nextLocalDateTime().toLocalDate();
    }

    public LocalDateTime nextLocalDateTime() {
        return LocalDateTime.now();
    }

    public Instant nextInstant() {
        return Instant.ofEpochMilli(nextDate().getTime());
    }

    private int getRandomNumber() {
        return 4;// chosen by fair dice roll; guaranteed to be random
    }
}
