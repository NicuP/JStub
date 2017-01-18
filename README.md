Basic Java library used for automatically stubbing with random data any Java POJOs that are Java Beans.

This class has the purpose of automatically creating stubs for POJOs that are
 Java Beans, i.e. classes that have only public setters and getters on fields.
 StubFactory automatically generates random values for these fields if they
 are of the type:
 <ul>
 <li>Basic types: int, long, double, float, byte, char (including wrappers)</li>
 <li>Java common used types: String, Date, BigInteger, BigDecimal, Date, Instant</li>
 <li>Any Enum</li>
 <li>Array, List, Set or Map of type of any of the above</li>
 <li>Any Java Bean that recursively decomposes to any of the above</li>
 <li>Interfaces that have return types in methods any of the above - proxy is created</li>
 </ul>
 <p>
 There are two ways of instantiating, using the default and the custom
 instance; using default instance creates random values for all the fields:
 <pre class="code">
 public class MyBean {                               
 &#09; private String string;                        
 &#09; private MyEnum enum;                          
 &#09; private List<Integer> integers;               
 &#09; private Map<Double,Date> map;                 
 &#09; private int[] ints;
 &#09; //other fields, getters and setters                 
 }                                                   
 <br>                                                    
 Bean myBean = StubFactory.get().create(MyBean.class);
 Map<String, BigInteger> map = stubFactory.create(Map.class, String.class, BigInteger.class);  
 </pre>
 
 Besides the auto-generated random values, custom values can be provided for
 certain bean values; talking for example the MyBean from above, if
 custom values are needed for string field then the StubFactory needs
 to be instantiated:
 <pre class="code">
 StubFactory stubFactory = StubFactory.builder()
    &#09; .addCustomValue("setString", "ab", "c")                
    &#09; .addCustomValue("setOtherField", otherValues);
    &#09; .addIgnoredSetter("setNothing");
    &#09; .build();
 </pre>
 In this way the value for String field will be randomly selected from those provided.
