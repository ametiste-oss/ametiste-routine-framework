package org.ametiste.dynamics;

import java.lang.annotation.Documented;

/**
 * Marks an element as <i>structure</i>.
 *
 * <p>This annotation is for the marker purposes only.
 *
 * @see Surface#ofPreSurface(Object)
 * @since 1.0
 */
@Documented
public @interface SurfaceStructure {

    /**
     * Contains link to a super-structure for this structure.
     */
    Class<?>[] superStructure() default Void.class;

}
