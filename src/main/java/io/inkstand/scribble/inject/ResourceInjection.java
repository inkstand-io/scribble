/**
 *
 */
package io.inkstand.scribble.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.util.AnnotationLiteral;

/**
 * A handle for performing an injection operation of a {@link Resource} annotated field. Matching criteria can be
 * defined using the according by-methods, where multiple criterias are processes using OR (first match).
 *
 * @author Gerald Muecke, gerald@moskito.li
 */
public class ResourceInjection extends Injection {

    private final List<Resource> matchingResources = new ArrayList<>();

    /**
     * @param value
     *            the injection value
     */
    public ResourceInjection(final Object value) {

        super(value);
    }

    /**
     * Determines the injection target {@link Resource} by its name
     *
     * @param name
     *            the name for the resoure
     * @return this {@link ResourceInjection}
     */
    public ResourceInjection byName(final String name) {

        final ResourceLiteral resource = new ResourceLiteral();
        resource.setName(name);
        matchingResources.add(resource);
        return this;
    }

    /**
     * Determines the injection target {@link Resource} by its mapped name
     *
     * @param lookup
     *            the mapped name for the resoure
     * @return this {@link ResourceInjection}
     */
    public ResourceInjection byMappedName(final String name) {

        final ResourceLiteral resource = new ResourceLiteral();
        resource.setMappedName(name);
        matchingResources.add(resource);
        return this;
    }

    /**
     * Determines the injection target {@link Resource} by its lookup name
     *
     * @param lookup
     *            the lookup name for the resoure
     * @return this {@link ResourceInjection}
     */
    public ResourceInjection byLookup(final String lookup) {

        final ResourceLiteral resource = new ResourceLiteral();
        resource.setLookup(lookup);
        matchingResources.add(resource);
        return this;
    }

    /**
     * In addition to the type check the method checks if the field is annotated with {@link Resource} and the
     * {@link Resource} annotation matches on of the specified criteria.
     */
    @Override
    protected boolean isMatching(final Field field) {

        if (!super.isMatching(field)) {
            return false;
        }

        final Resource resourceAnnotation = field.getAnnotation(Resource.class);
        if (resourceAnnotation == null) {
            return false;
        }

        for (final Resource expectedResources : matchingResources) {
            if (expectedResources.equals(resourceAnnotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Literal for representing a {@link Resource} annotation
     *
     * @author Gerald Muecke, gerald@moskito.li
     */
    static class ResourceLiteral extends AnnotationLiteral<Resource> implements Resource {

        private static final long serialVersionUID = -1648754563401613075L;
        private String name = "";
        private String lookup = "";
        @SuppressWarnings("rawtypes")
        private Class type = Object.class;
        private String mappedName = "";
        private AuthenticationType authenticationType = AuthenticationType.CONTAINER;
        private boolean shareable = true;
        private String description = "";

        ResourceLiteral() {

        }

        ResourceLiteral(final String name, final String lookup, final String mappedName,
                @SuppressWarnings("rawtypes") final Class type, final AuthenticationType authenticationType,
                final boolean shareable, final String description) {

            super();
            this.name = name;
            this.lookup = lookup;
            this.mappedName = mappedName;
            this.type = type;
            this.authenticationType = authenticationType;
            this.shareable = shareable;
            this.description = description;
        }

        @Override
        public String name() {

            return name;
        }

        @Override
        public String lookup() {

            return lookup;
        }

        @Override
        public String mappedName() {

            return mappedName;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Class type() {

            return type;
        }

        @Override
        public AuthenticationType authenticationType() {

            return authenticationType;
        }

        @Override
        public boolean shareable() {

            return shareable;
        }

        @Override
        public String description() {

            return description;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(final String name) {

            this.name = name;
        }

        /**
         * @param lookup
         *            the lookup to set
         */
        public void setLookup(final String lookup) {

            this.lookup = lookup;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(@SuppressWarnings("rawtypes") final Class type) {

            this.type = type;
        }

        /**
         * @param mappedName
         *            the mappedName to set
         */
        public void setMappedName(final String mappedName) {

            this.mappedName = mappedName;
        }

        /**
         * @param authenticationType
         *            the authenticationType to set
         */
        public void setAuthenticationType(final AuthenticationType authenticationType) {

            this.authenticationType = authenticationType;
        }

        /**
         * @param shareable
         *            the shareable to set
         */
        public void setShareable(final boolean shareable) {

            this.shareable = shareable;
        }

        /**
         * @param description
         *            the description to set
         */
        public void setDescription(final String description) {

            this.description = description;
        }

        @Override
        public int hashCode() {

            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((authenticationType == null)
                    ? 0
                    : authenticationType.hashCode());
            result = prime * result + ((description == null)
                    ? 0
                    : description.hashCode());
            result = prime * result + ((lookup == null)
                    ? 0
                    : lookup.hashCode());
            result = prime * result + ((mappedName == null)
                    ? 0
                    : mappedName.hashCode());
            result = prime * result + ((name == null)
                    ? 0
                    : name.hashCode());
            result = prime * result + (shareable
                    ? 1231
                    : 1237);
            return result;
        }

        @Override
        public boolean equals(final Object obj) {

            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (!(obj instanceof Resource)) {
                return false;
            }
            final Resource other = (Resource) obj;
            if (authenticationType != other.authenticationType()) {
                return false;
            }
            if (description == null) {
                if (other.description() != null) {
                    return false;
                }
            } else if (!description.equals(other.description())) {
                return false;
            }
            if (lookup == null) {
                if (other.lookup() != null) {
                    return false;
                }
            } else if (!lookup.equals(other.lookup())) {
                return false;
            }
            if (mappedName == null) {
                if (other.mappedName() != null) {
                    return false;
                }
            } else if (!mappedName.equals(other.mappedName())) {
                return false;
            }
            if (name == null) {
                if (other.name() != null) {
                    return false;
                }
            } else if (!name.equals(other.name())) {
                return false;
            }
            if (shareable != other.shareable()) {
                return false;
            }
            return true;
        }

    }
}
