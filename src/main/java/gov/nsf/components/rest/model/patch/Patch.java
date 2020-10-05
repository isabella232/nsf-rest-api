package gov.nsf.components.rest.model.patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * Patch
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Patch implements Patchable {

    private PatchOperation operation;
    private String path;
    private Object value;

    private static final ObjectMapper MAPPER = new ObjectMapper() {{
        setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }};

    public Patch(PatchOperation operation, String path, Object value) {
        if (operation == null || path == null) {
            throw new IllegalArgumentException("operation and path cannot be null");
        }

        this.operation = operation;
        this.path = path;
        this.value = value;
    }

    public Patch() {

    }

    @Override
    @SuppressWarnings("deprecation")
    public <T> T patch(T target, Class<T> type) throws PatchException {
        if (!isPatchable(target, type)) {
            throw new PatchException("cannot patch: the target object and the specified type are not compatible");
        }

        String _path = path;
        if (path.charAt(0) == '/') {
            _path = path.substring(1);
        }
        String[] pathParts = _path.split("/");

        T patched = null;
        try {
            JsonNode replacement = replacement(); //Get replacement node based on PatchOperation

            JsonNode tree = MAPPER.valueToTree(target);
            JsonNode resolvedNode = tree;
            for (int i = 0; i < pathParts.length - 1; i++) {
                resolvedNode = resolvedNode.get(pathParts[i]);

                if (resolvedNode == null) {
                    throw new PatchException("path not found");
                }
            }
            String finalPart = pathParts[pathParts.length - 1];
            ((ObjectNode) resolvedNode).put(finalPart, replacement);

            patched = MAPPER.readValue(tree.toString(), type);
        } catch (Exception ex) {
            throw new PatchException("Unable to patch, parsing failed: " + ex.getMessage(), ex);
        }

        return patched;
    }

    private JsonNode replacement() {
        switch (this.getOperation()) {
            case REPLACE:
                return MAPPER.valueToTree(value);
            case REMOVE:
                return null;
            default:
                throw new IllegalStateException("Patch Operation not currently supposed");
        }
    }


    @Override
    public <T> boolean isPatchable(T target, Class<T> type) {
        if (target == null || type == null) {
            throw new IllegalArgumentException("target object and type cannot be null");
        }

        return type.isAssignableFrom(target.getClass());
    }


    public PatchOperation getOperation() {
        return operation;
    }

    public void setOperation(PatchOperation operation) {
        this.operation = operation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
