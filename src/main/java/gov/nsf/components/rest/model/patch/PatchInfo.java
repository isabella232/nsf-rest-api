package gov.nsf.components.rest.model.patch;

import java.util.Set;

/**
 * PatchInfo
 */
public class PatchInfo<T> {

    private final T patched;
    private final Set<? extends Enum> matchers;

    public PatchInfo(T patched, Set<? extends Enum> matchers) {
        this.patched = patched;
        this.matchers = matchers;
    }

    public T getPatched() {
        return patched;
    }

    public Set<? extends Enum> getMatchers() {
        return matchers;
    }
}
