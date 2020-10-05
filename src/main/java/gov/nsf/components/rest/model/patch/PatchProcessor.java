package gov.nsf.components.rest.model.patch;

/**
 * PatchProcessor
 */
public class PatchProcessor {

    public static <T> PatchInfo<T> processPatchable(Patchable patchable, T target, Class<T> type) throws PatchException {
        if (patchable == null || target == null || type == null) {
            throw new PatchException("The patchable, target, and type must not be null");
        }

        return new PatchInfo<T>(patchable.patch(target, type), patchable.getPatchMatchers());
    }
}
