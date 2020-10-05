/**
 *
 */
package gov.nsf.components.rest.model.ember;

import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author KGURUGUB
 */
public class EmberModel extends ConcurrentHashMap<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EmberModel() {
        super();
    }

    public static class ModelBuilder<T> implements Builder<EmberModel> {
        private final ConcurrentMap<String, Object> sideLoadedItems = new ConcurrentHashMap<String, Object>();
        private final ConcurrentMap<String, Object> metaData = new ConcurrentHashMap<String, Object>();

        public ModelBuilder(final Object entity) {
            Assert.notNull(entity, "object cannot be null");
            sideLoad(entity);
        }

        public ModelBuilder(final String rootName, final Object entity) {
            Assert.notNull(entity, "object cannot be null");
            this.sideLoad(rootName, entity);
        }

        public ModelBuilder(final Class<T> clazz, final Collection<T> entities) {
            Assert.notNull(entities, "collection cannot be null");
            sideLoad(clazz, entities);
        }

        public ModelBuilder(final String rootName, final Collection<?> entities) {
            Assert.notNull(entities, "collection cannot be null");
            sideLoad(rootName, entities);
        }

        public ModelBuilder<T> addMeta(final String key, final Object value) {
            metaData.put(key, value);
            return this;
        }

        public ModelBuilder<T> sideLoad(final Object entity) {
            if (entity != null) {
                sideLoadedItems.put(getSingularName(entity.getClass()), entity);
            }
            return this;
        }


        public ModelBuilder<T> sideLoad(final String rootName, final Object entity) {
            if (entity != null) {
                sideLoadedItems.put(rootName, entity);
            }
            return this;
        }

        public <K> ModelBuilder<T> sideLoad(final Class<K> clazz, final Collection<K> entities) {
            if (entities != null) {
                sideLoadedItems.put(getPluralName(clazz), entities);
            }
            return this;
        }

        public ModelBuilder<T> sideLoad(final String rootName, final Collection<?> entities) {
            if (entities != null) {
                sideLoadedItems.put(English.plural(rootName), entities);
            }
            return this;
        }

        private String getSingularName(final Class<?> clazz) {
            Assert.notNull(clazz, "class type cannot be null");
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazz.getSimpleName());
        }

        private String getPluralName(final Class<?> clazz) {
            Assert.notNull(clazz, "class type cannot be null");
            return English.plural(getSingularName(clazz));
        }

        @Override
        public EmberModel build() {
            if (!metaData.isEmpty()) {
                sideLoadedItems.put("meta", metaData);
            }
            EmberModel sideLoader = new EmberModel();
            sideLoader.putAll(sideLoadedItems);
            return sideLoader;
        }
    }
}
