# Kafka script transformers

The project is a sample and example of a kafka-connect transformers. 

The kafka transformer for applying custom scripts to kafka record (key and value). kafka scheme not using.

supported languages:
* Groovy
* JavaScript
* Python
* Jruby
* Kotlin

JavaScript language is the only of supported languages, which have restrictions to call java API. JavaScript can't execute code System.exit() File.delete and others.

It is recommended to use JavaScript language, because it is optimal by performance and security.

# Configuration

## Examples:

### groovy
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "groovy",
  "transforms.ScriptEngineTransformer.key.script": "def keyTransform(def source) {return source + '123' }",
  "transforms.ScriptEngineTransformer.value.script": "def valueTransform(def source) {source.put('qweqweq', 12312312); return source; }"
}
```

### python
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "python",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.key.script": "def keyTransform(source): source['qweqweq'] = 12312312; return source",
  "transforms.ScriptEngineTransformer.value.script": "def valueTransform(source): source['qweqweq'] = 12312312; return source"
}
```

### java script
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "javascript",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.key.script": "function keyTransform(source){ source.qweqweq = 12312312; return source;}",
  "transforms.ScriptEngineTransformer.value.script": "function valueTransform(source){ source.qweqweq = 12312312; return source;}"
}
```
### ruby
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "jruby",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.key.script": "def keyTransform(source) return source + '123' end",
  "transforms.ScriptEngineTransformer.value.script": "def valueTransform(source) source['qweqweq'] = 12312312; return source; end"
}
```
### kotlin
```json
{
  "transforms": "ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.scrip_engine_name": "kotlin",
  "transforms.ScriptEngineTransformer.type": "seko.kafka.connect.transformer.script.ScriptEngineTransformer",
  "transforms.ScriptEngineTransformer.key.script": "fun keyTransform(source: String): String { return source + \"123\"}",
  "transforms.ScriptEngineTransformer.value.script": "fun valueTransform(source: MutableMap<String, Any>): Map<String, Any> { source[\"qweqweq\"] = 12312312; return source }"
}
```

# JMH test results

###### JMH version: 1.21
###### VM version: JDK 1.8.0_222, OpenJDK 64-Bit Server VM, 25.222-b05
###### VM invoker: /usr/lib/jvm/java-8-openjdk/jre/bin/java
###### VM options: -Xms2G -Xmx2G
###### Warmup: 5 iterations, 10 s each
###### Measurement: 5 iterations, 10 s each
###### Timeout: 10 min per iteration
###### Threads: 1 thread, will synchronize iterations
###### Benchmark mode: Average time, time/op
###### Benchmark: seko.kafka.connect.transformer.jmh.tests.TransformersTest.groovyTransformer
###### Parameters: (N = 10000000)

Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.javaTransformer":
  - 51.584 ±(99.9%) 0.470 ns/op [Average]
  - (min, avg, max) = (51.151, 51.584, 52.191), stdev = 0.311
  - CI (99.9%): [51.113, 52.054] (assumes normal distribution)

Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.groovyTransformer":
  - 471.638 ±(99.9%) 21.454 ns/op [Average]
  - (min, avg, max) = (457.382, 471.638, 493.197), stdev = 14.190
  - CI (99.9%): [450.184, 493.091] (assumes normal distribution)

Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.jsTransformer":
  - 173.391 ±(99.9%) 5.602 ns/op [Average]
  - (min, avg, max) = (169.248, 173.391, 177.589), stdev = 3.705
  - CI (99.9%): [167.788, 178.993] (assumes normal distribution)

Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.pythonTransformer":
  - 688.370 ±(99.9%) 15.014 ns/op [Average]
  - (min, avg, max) = (676.190, 688.370, 709.107), stdev = 9.931
  - CI (99.9%): [673.356, 703.385] (assumes normal distribution)

Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.groovySeTransformer":
  - 125.317 ±(99.9%) 8.525 ns/op [Average]
  - (min, avg, max) = (119.522, 125.317, 131.116), stdev = 5.639
  - CI (99.9%): [116.792, 133.842] (assumes normal distribution)
  
Result "seko.kafka.connect.transformer.jmh.tests.TransformersTest.rubyTransformer":
  - 101496.807 ±(99.9%) 3020.123 ns/op [Average]
  - (min, avg, max) = (99087.414, 101496.807, 105133.209), stdev = 1997.624
  - CI (99.9%): [98476.684, 104516.929] (assumes normal distribution)

##### Run complete. Total time: 00:10:06

### REMEMBER: 
The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.



|Benchmark                           |      (N)  | Mode  | Cnt |       Score |       Error | Units |
| ---------------------------------- | --------- | ----- | --- | ----------- | ----------- | ----- |
|TransformersTest.javaTransformer    |  10000000 | avgt  | 10  |      51.584 |±     0.470  | ns/op |
|TransformersTest.groovySeTransformer|  10000000 | avgt  | 10  |     125.317 |±     8.525  | ns/op |
|TransformersTest.jsTransformer      |  10000000 | avgt  | 10  |     173.391 |±     5.602  | ns/op |
|TransformersTest.groovyTransformer  |  10000000 | avgt  | 10  |     471.638 |±    21.454  | ns/op |
|TransformersTest.pythonTransformer  |  10000000 | avgt  | 10  |     688.370 |±    15.014  | ns/op |
|TransformersTest.kotlinTransformer  |  10000000 | avgt  | 10  |    2833.001 |±    44.396  | ns/op |
|TransformersTest.rubyTransformer    |  10000000 | avgt  | 10  |  101496.807 |±  3020.123  | ns/op |


