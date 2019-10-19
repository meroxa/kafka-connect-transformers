package seko.kafka.connect.transformer.js;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seko.kafka.connect.transformer.script.AbstractScriptTransformer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JavaScriptTransformer<R extends ConnectRecord<R>> extends AbstractScriptTransformer<R> {
    private static final Logger log = LoggerFactory.getLogger(JavaScriptTransformer.class);
    private Invocable inv;

    @Override
    public Map<String, Object> transform(Map<String, Object> source, String script) {
        if (this.keyScript != null) {
            return jsTransform(source, "keyTransform");
        }
        if (this.valueScript != null) {
            return jsTransform(source, "valueTransform");
        }
        return source;
    }

    private Map<String, Object> jsTransform(Map<String, Object> source, String keyTransform) {
        try {
            return (Map<String, Object>) inv.invokeFunction(keyTransform, source);
        } catch (Exception e) {
            List<String> tags = Optional.ofNullable(source.get("tags"))
                    .map(it -> (List<String>) it)
                    .orElse(new ArrayList<>());
            tags.add("groovy_transformer: " + e.getMessage());
            source.put("tags", tags);
            log.warn("Fallout groovy script evaluation: ", e);
            return source;
        }
    }

    @Override
    public void configure(Map<String, ?> configs) {
        super.configure(configs);

        StringBuilder stringBuilder = new StringBuilder();

        if (this.keyScript != null) {
            stringBuilder.append(keyScript);
        }
        stringBuilder.append("\n");

        if (this.valueScript != null) {
            stringBuilder.append(valueScript);
        }
        this.inv = (Invocable) getScript(stringBuilder.toString());

    }

    private ScriptEngine getScript(String script) {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            scriptEngine.eval(script);
            return scriptEngine;
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }
    }
}