package gov.nsf.components.rest.model.patch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PatchRequest
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchRequest<T extends Patch> implements Patchable {

    private List<T> patches;

    public PatchRequest(List<T> patches) {
        this.patches = patches;
    }

    public PatchRequest() {
    }

    public List<T> getPatches() {
        return patches;
    }

    public void setPatches(List<T> patches) {
        this.patches = patches;
    }

    public <T> T patch(T target, Class<T> type) throws PatchException {
        if (!isPatchable(target, type)) {
            throw new PatchException("cannot patch, request contained an invalid patch segment");
        }

        T result = target;

        for (Patch patch : patches) {
            result = patch.patch(result, type);
        }

        return result;
    }

    @Override
    public <T> boolean isPatchable(T target, Class<T> type) {
        if (patches == null || patches.isEmpty()) {
            throw new IllegalArgumentException("patches must not be null or empty");
        }

		for (Patch patch : patches){
			if (patch == null || !patch.isPatchable(target,type)){
				return false;
			}
		}

		return true;
    }

    @JsonIgnore
    @Override
    public Set<? extends Enum> getPatchMatchers() {
        Set<Enum> matchers = new HashSet<>();

        for (Patch patch : patches) {
            matchers.addAll(patch.getPatchMatchers());
        }

        return matchers;
    }
}
