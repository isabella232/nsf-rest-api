package gov.nsf.components.rest.model.patch;

import java.util.Set;

/**
 * Patchable
 */
public interface Patchable {

    <T> T patch(T target, Class<T> type) throws PatchException;

    <T> boolean isPatchable(T target, Class<T> type);

    Set<? extends Enum> getPatchMatchers();
}
