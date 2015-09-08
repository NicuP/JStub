package org.oss.jstub.pojo;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class FullPojo {
    private String string;
    private int anInt;
    private byte aByte;
    private char aChar;
    private double aDouble;
    private long aLong;
    private float aFloat;
    private Date date;
    private BigInteger bigInteger;
    private BigDecimal bigDecimal;
    private Timestamp timestamp;
    private Calendar calendar;
    private GregorianCalendar gregorianCalendar;
    private XMLGregorianCalendar xmlGregorianCalendar;

    private BasicEnum basicEnum;
    private SimplePojo simplePojo;
    private BasicInterface basicInterface;

    private List<String> stringList;
    private Set<Date> dateSet;
    private int[] intArray;
    private Map<Long, BigInteger> map;

    private BasicEnum[] enumArray;
    private Set<SimplePojo> simplePojos;
    private Map<BasicEnum, BasicInterface> complexMap;


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public GregorianCalendar getGregorianCalendar() {
        return gregorianCalendar;
    }

    public void setGregorianCalendar(GregorianCalendar gregorianCalendar) {
        this.gregorianCalendar = gregorianCalendar;
    }

    public XMLGregorianCalendar getXmlGregorianCalendar() {
        return xmlGregorianCalendar;
    }

    public void setXmlGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        this.xmlGregorianCalendar = xmlGregorianCalendar;
    }

    public BasicEnum getBasicEnum() {
        return basicEnum;
    }

    public void setBasicEnum(BasicEnum basicEnum) {
        this.basicEnum = basicEnum;
    }

    public SimplePojo getSimplePojo() {
        return simplePojo;
    }

    public void setSimplePojo(SimplePojo simplePojo) {
        this.simplePojo = simplePojo;
    }

    public BasicInterface getBasicInterface() {
        return basicInterface;
    }

    public void setBasicInterface(BasicInterface basicInterface) {
        this.basicInterface = basicInterface;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Set<Date> getDateSet() {
        return dateSet;
    }

    public void setDateSet(Set<Date> dateSet) {
        this.dateSet = dateSet;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public Map<Long, BigInteger> getMap() {
        return map;
    }

    public void setMap(Map<Long, BigInteger> map) {
        this.map = map;
    }

    public BasicEnum[] getEnumArray() {
        return enumArray;
    }

    public void setEnumArray(BasicEnum[] enumArray) {
        this.enumArray = enumArray;
    }

    public Set<SimplePojo> getSimplePojos() {
        return simplePojos;
    }

    public void setSimplePojos(Set<SimplePojo> simplePojos) {
        this.simplePojos = simplePojos;
    }

    public Map<BasicEnum, BasicInterface> getComplexMap() {
        return complexMap;
    }

    public void setComplexMap(Map<BasicEnum, BasicInterface> complexMap) {
        this.complexMap = complexMap;
    }
}
