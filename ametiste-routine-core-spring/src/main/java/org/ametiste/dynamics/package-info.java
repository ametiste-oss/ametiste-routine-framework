/**
 * Classes and objects to provide the definition and a reference implementation of the <i>Surface Model</i>.
 *
 * <p>The key abstraction introduced in this model is {@link org.ametiste.dynamics.Surface}, a construction that
 * represents a runtime graph as a stream of calculations over a <i>structure</i> defind by a set of <i>features</i>.
 *
 * <p>This abstraction is implemented as the set of reference classes {@link org.ametiste.dynamics.BaseSurface},
 * {@link org.ametiste.dynamics.BaseRightSurface},
 * {@link org.ametiste.dynamics.BaseLeftSurface}.
 *
 * <p>The classes is the foundation to build the model applications, so each of them may be used as a base
 * class to start definition of some application.
 *
 * <p>Along with the {@link org.ametiste.dynamics.Surface}, this package provides definition of
 * {@link org.ametiste.dynamics.Surge} element, that represents a process of a <i>surface</i> modification.
 *
 * <p>This pair of concepts, the <i>surface</i> and the <i>surge</i>, forms the structure that known
 * as the {@link org.ametiste.dynamics.Planar} object, such that holds a set of <i>surfaces</i> and a set
 * of <i>surges</i> defined over these <i>surfaces</i>.
 *
 * <p>The reference <i>planar</i> object is implemented by the {@link org.ametiste.dynamics.Plane} class. This class
 * provides capabilities to bind a set of <i>surges</i> over a <i>surface</i> calculation, so that the class usually
 * will be a starting point of the model application.
 *
 * <h3>Applications</h3>
 *
 * In fact, although this model was conceived as a foundation of an alternative approach
 * to implement the <i>Reflection API</i>, this model could be used to express other application domains.
 *
 * <h4>Foundation</h4>
 *
 * Foundation package {@link org.ametiste.dynamics.foundation} contatins classes to implement
 * base application of the model - <i>Dynamics Reflection API</i>.
 *
 * <p>This packages contains of the {@link org.ametiste.dynamics.foundation.reflection} module, the module of calsses to
 * describe low-level concepts of the object graph in the aspect of reflection. And of the
 * {@link org.ametiste.dynamics.foundation.elements} module, the module of calsses to
 * describe high-level concepts of the object graph, that could be used to build APIs over it.
 *
 * @see org.ametiste.dynamics.Surface
 * @see org.ametiste.dynamics.Surge
 * @see org.ametiste.dynamics.Planar
 * @since 1.0
 */
package org.ametiste.dynamics;