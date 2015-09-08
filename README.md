Basic Java library used for automatically stubbing with random data any Java POJOs that are Java Beans.

This class has the purpose of automatically creating stubs for POJOs that are
 Java Beans, i.e. classes that have only public setters and getters on fields.
 StubFactory automatically generates random values for these fields if they
 are of the type:
 <ul>
 <li>Basic types: int, long, double, float, byte, char (including wrappers)</li>
 <li>Java common used types: String, Date, BigInteger, BigDecimal</li>
 <li>Any Enum</li>
 <li>Interfaces that have return types any of the above - proxy is created</li>
 <li>Array, List, Set or Map of type of any of the above</li>
 <li>Any Java Bean that recursively decomposes to any of the above</li>
 </ul>
 <p>
 There are two ways of instantiating, using the default and the custom
 instance; using default instance creates random values for all the fields:
 <pre class="code">
 public class MyBean {                               <br>
 &#09; private String string;                        <br>
 &#09; private MyEnum enum;                          <br>
 &#09; private List<Integer> integers;               <br>
 &#09; private Map<Double,Date> map;                 <br>
 &#09; private int[] ints;
 //other fields, getters and setters                 <br>
 }                                                   <br>
                                                     <br>
 Bean myBean = StubFactory.instance().create(MyBean.class);                                    <br>
 Map<String, BigInteger> map = stubFactory.create(Map.class, String.class, BigInteger.class);  <br>
 </pre>
 
 Besides the auto-generated random values, custom values can be provided for
 certain bean values; talking for example the {@code MyBean} from above, if
 custom values are needed for {@code string} field then the StubFactory needs
 to be instantiated:
 <pre class="code">
 StubFactory stubFactory = StubFactory.customInstance()          <br> 
    &#09; .addCustomValue("setString", "ab", "c")                <br>
    &#09; .addCustomValue("setOtherField", otherValues);         <br>
    &#09; .build();
 </pre>
 In this way the value for String field will be randomly selected from those provided.
