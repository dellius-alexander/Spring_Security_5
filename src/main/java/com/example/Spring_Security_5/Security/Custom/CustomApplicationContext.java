package com.example.Spring_Security_5.Security.Custom;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

public class CustomApplicationContext implements ApplicationContext {

    private ApplicationContext ctx;
    public CustomApplicationContext(ApplicationContext ctx){
        this.ctx = ctx;
    }
    /**
     * Return the unique id of this application context.
     *
     * @return the unique id of the context, or {@code null} if none
     */
    @Override
    public String getId() {
        return ctx.getId();
    }

    /**
     * Return a name for the deployed application that this context belongs to.
     *
     * @return a name for the deployed application, or the empty String by default
     */
    @Override
    public String getApplicationName() {
        return ctx.getApplicationName();
    }

    /**
     * Return a friendly name for this context.
     *
     * @return a display name for this context (never {@code null})
     */
    @Override
    public String getDisplayName() {
        return ctx.getDisplayName();
    }

    /**
     * Return the timestamp when this context was first loaded.
     *
     * @return the timestamp (ms) when this context was first loaded
     */
    @Override
    public long getStartupDate() {
        return ctx.getStartupDate();
    }

    /**
     * Return the parent context, or {@code null} if there is no parent
     * and this is the root of the context hierarchy.
     *
     * @return the parent context, or {@code null} if there is no parent
     */
    @Override
    public ApplicationContext getParent() {
        return ctx;
    }

    /**
     * Expose AutowireCapableBeanFactory functionality for this context.
     * <p>This is not typically used by application code, except for the purpose of
     * initializing bean instances that live outside of the application context,
     * applying the Spring bean lifecycle (fully or partly) to them.
     * <p>Alternatively, the internal BeanFactory exposed by the
     * {@link ConfigurableApplicationContext} interface offers access to the
     * {@link AutowireCapableBeanFactory} interface too. The present method mainly
     * serves as a convenient, specific facility on the ApplicationContext interface.
     * <p><b>NOTE: As of 4.2, this method will consistently throw IllegalStateException
     * after the application context has been closed.</b> In current Spring Framework
     * versions, only refreshable application contexts behave that way; as of 4.2,
     * all application context implementations will be required to comply.
     *
     * @return the AutowireCapableBeanFactory for this context
     * @throws IllegalStateException if the context does not support the
     *                               {@link AutowireCapableBeanFactory} interface, or does not hold an
     *                               autowire-capable bean factory yet (e.g. if {@code refresh()} has
     *                               never been called), or if the context has been closed already
     * @see ConfigurableApplicationContext#refresh()
     * @see ConfigurableApplicationContext#getBeanFactory()
     */
    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return ctx.getAutowireCapableBeanFactory();
    }

    /**
     * Return the parent bean factory, or {@code null} if there is none.
     */
    @Override
    public BeanFactory getParentBeanFactory() {
        return ctx.getParentBeanFactory();
    }

    /**
     * Return whether the local bean factory contains a bean of the given name,
     * ignoring beans defined in ancestor contexts.
     * <p>This is an alternative to {@code containsBean}, ignoring a bean
     * of the given name from an ancestor bean factory.
     *
     * @param name the name of the bean to query
     * @return whether a bean with the given name is defined in the local factory
     * @see BeanFactory#containsBean
     */
    @Override
    public boolean containsLocalBean(String name) {
        return ctx.containsLocalBean(name);
    }

    /**
     * Check if this bean factory contains a bean definition with the given name.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     *
     * @param beanName the name of the bean to look for
     * @return if this bean factory contains a bean definition with the given name
     * @see #containsBean
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return ctx.containsBeanDefinition(beanName);
    }

    /**
     * Return the number of beans defined in the factory.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     *
     * @return the number of beans defined in the factory
     */
    @Override
    public int getBeanDefinitionCount() {
        return ctx.getBeanDefinitionCount();
    }

    /**
     * Return the names of all beans defined in this factory.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     *
     * @return the names of all beans defined in this factory,
     * or an empty array if none defined
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return ctx.getBeanDefinitionNames();
    }

    /**
     * Return a provider for the specified bean, allowing for lazy on-demand retrieval
     * of instances, including availability and uniqueness options.
     *
     * @param requiredType   type the bean must match; can be an interface or superclass
     * @param allowEagerInit whether stream-based access may initialize <i>lazy-init
     *                       singletons</i> and <i>objects created by FactoryBeans</i> (or by factory methods
     *                       with a "factory-bean" reference) for the type check
     * @return a corresponding provider handle
     * @see #getBeanProvider(ResolvableType, boolean)
     * @see #getBeanProvider(Class)
     * @see #getBeansOfType(Class, boolean, boolean)
     * @see #getBeanNamesForType(Class, boolean, boolean)
     * @since 5.3
     */
    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType, boolean allowEagerInit) {
        return ctx.getBeanProvider(requiredType,allowEagerInit);
    }

    /**
     * Return a provider for the specified bean, allowing for lazy on-demand retrieval
     * of instances, including availability and uniqueness options.
     *
     * @param requiredType   type the bean must match; can be a generic type declaration.
     *                       Note that collection types are not supported here, in contrast to reflective
     *                       injection points. For programmatically retrieving a list of beans matching a
     *                       specific type, specify the actual bean type as an argument here and subsequently
     *                       use {@link ObjectProvider#orderedStream()} or its lazy streaming/iteration options.
     * @param allowEagerInit whether stream-based access may initialize <i>lazy-init
     *                       singletons</i> and <i>objects created by FactoryBeans</i> (or by factory methods
     *                       with a "factory-bean" reference) for the type check
     * @return a corresponding provider handle
     * @see #getBeanProvider(ResolvableType)
     * @see ObjectProvider#iterator()
     * @see ObjectProvider#stream()
     * @see ObjectProvider#orderedStream()
     * @see #getBeanNamesForType(ResolvableType, boolean, boolean)
     * @since 5.3
     */
    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType, boolean allowEagerInit) {
        return ctx.getBeanProvider(requiredType,allowEagerInit);
    }

    /**
     * Return the names of beans matching the given type (including subclasses),
     * judging from either bean definitions or the value of {@code getObjectType}
     * in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>This version of {@code getBeanNamesForType} matches all kinds of beans,
     * be it singletons, prototypes, or FactoryBeans. In most implementations, the
     * result will be the same as for {@code getBeanNamesForType(type, true, true)}.
     * <p>Bean names returned by this method should always return bean names <i>in the
     * order of definition</i> in the backend configuration, as far as possible.
     *
     * @param type the generically typed class or interface to match
     * @return the names of beans (or objects created by FactoryBeans) matching
     * the given object type (including subclasses), or an empty array if none
     * @see #isTypeMatch(String, ResolvableType)
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, ResolvableType)
     * @since 4.2
     */
    @Override
    public String[] getBeanNamesForType(ResolvableType type) {
        return ctx.getBeanNamesForType(type);
    }

    /**
     * Return the names of beans matching the given type (including subclasses),
     * judging from either bean definitions or the value of {@code getObjectType}
     * in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
     * which means that FactoryBeans will get initialized. If the object created by the
     * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
     * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
     * (which doesn't require initialization of each FactoryBean).
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>Bean names returned by this method should always return bean names <i>in the
     * order of definition</i> in the backend configuration, as far as possible.
     *
     * @param type                 the generically typed class or interface to match
     * @param includeNonSingletons whether to include prototype or scoped beans too
     *                             or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit       whether to initialize <i>lazy-init singletons</i> and
     *                             <i>objects created by FactoryBeans</i> (or by factory methods with a
     *                             "factory-bean" reference) for the type check. Note that FactoryBeans need to be
     *                             eagerly initialized to determine their type: So be aware that passing in "true"
     *                             for this flag will initialize FactoryBeans and "factory-bean" references.
     * @return the names of beans (or objects created by FactoryBeans) matching
     * the given object type (including subclasses), or an empty array if none
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, ResolvableType, boolean, boolean)
     * @since 5.2
     */
    @Override
    public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
        return ctx.getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
    }

    /**
     * Return the names of beans matching the given type (including subclasses),
     * judging from either bean definitions or the value of {@code getObjectType}
     * in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>This version of {@code getBeanNamesForType} matches all kinds of beans,
     * be it singletons, prototypes, or FactoryBeans. In most implementations, the
     * result will be the same as for {@code getBeanNamesForType(type, true, true)}.
     * <p>Bean names returned by this method should always return bean names <i>in the
     * order of definition</i> in the backend configuration, as far as possible.
     *
     * @param type the class or interface to match, or {@code null} for all bean names
     * @return the names of beans (or objects created by FactoryBeans) matching
     * the given object type (including subclasses), or an empty array if none
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, Class)
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return ctx.getBeanNamesForType(type);
    }

    /**
     * Return the names of beans matching the given type (including subclasses),
     * judging from either bean definitions or the value of {@code getObjectType}
     * in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
     * which means that FactoryBeans will get initialized. If the object created by the
     * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
     * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
     * (which doesn't require initialization of each FactoryBean).
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>Bean names returned by this method should always return bean names <i>in the
     * order of definition</i> in the backend configuration, as far as possible.
     *
     * @param type                 the class or interface to match, or {@code null} for all bean names
     * @param includeNonSingletons whether to include prototype or scoped beans too
     *                             or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit       whether to initialize <i>lazy-init singletons</i> and
     *                             <i>objects created by FactoryBeans</i> (or by factory methods with a
     *                             "factory-bean" reference) for the type check. Note that FactoryBeans need to be
     *                             eagerly initialized to determine their type: So be aware that passing in "true"
     *                             for this flag will initialize FactoryBeans and "factory-bean" references.
     * @return the names of beans (or objects created by FactoryBeans) matching
     * the given object type (including subclasses), or an empty array if none
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return ctx.getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
    }

    /**
     * Return the bean instances that match the given object type (including
     * subclasses), judging from either bean definitions or the value of
     * {@code getObjectType} in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
     * will get initialized. If the object created by the FactoryBean doesn't match,
     * the raw FactoryBean itself will be matched against the type.
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beansOfTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>This version of getBeansOfType matches all kinds of beans, be it
     * singletons, prototypes, or FactoryBeans. In most implementations, the
     * result will be the same as for {@code getBeansOfType(type, true, true)}.
     * <p>The Map returned by this method should always return bean names and
     * corresponding bean instances <i>in the order of definition</i> in the
     * backend configuration, as far as possible.
     *
     * @param type the class or interface to match, or {@code null} for all concrete beans
     * @return a Map with the matching beans, containing the bean names as
     * keys and the corresponding bean instances as values
     * @throws BeansException if a bean could not be created
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beansOfTypeIncludingAncestors(ListableBeanFactory, Class)
     * @since 1.1.2
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return ctx.getBeansOfType(type);
    }

    /**
     * Return the bean instances that match the given object type (including
     * subclasses), judging from either bean definitions or the value of
     * {@code getObjectType} in the case of FactoryBeans.
     * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
     * check nested beans which might match the specified type as well.
     * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
     * which means that FactoryBeans will get initialized. If the object created by the
     * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
     * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
     * (which doesn't require initialization of each FactoryBean).
     * <p>Does not consider any hierarchy this factory may participate in.
     * Use BeanFactoryUtils' {@code beansOfTypeIncludingAncestors}
     * to include beans in ancestor factories too.
     * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
     * by other means than bean definitions.
     * <p>The Map returned by this method should always return bean names and
     * corresponding bean instances <i>in the order of definition</i> in the
     * backend configuration, as far as possible.
     *
     * @param type                 the class or interface to match, or {@code null} for all concrete beans
     * @param includeNonSingletons whether to include prototype or scoped beans too
     *                             or just singletons (also applies to FactoryBeans)
     * @param allowEagerInit       whether to initialize <i>lazy-init singletons</i> and
     *                             <i>objects created by FactoryBeans</i> (or by factory methods with a
     *                             "factory-bean" reference) for the type check. Note that FactoryBeans need to be
     *                             eagerly initialized to determine their type: So be aware that passing in "true"
     *                             for this flag will initialize FactoryBeans and "factory-bean" references.
     * @return a Map with the matching beans, containing the bean names as
     * keys and the corresponding bean instances as values
     * @throws BeansException if a bean could not be created
     * @see FactoryBean#getObjectType
     * @see BeanFactoryUtils#beansOfTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        return ctx.getBeansOfType(type,includeNonSingletons,allowEagerInit);
    }

    /**
     * Find all names of beans which are annotated with the supplied {@link Annotation}
     * type, without creating corresponding bean instances yet.
     * <p>Note that this method considers objects created by FactoryBeans, which means
     * that FactoryBeans will get initialized in order to determine their object type.
     *
     * @param annotationType the type of annotation to look for
     *                       (at class, interface or factory method level of the specified bean)
     * @return the names of all matching beans
     * @see #findAnnotationOnBean
     * @since 4.0
     */
    @Override
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return ctx.getBeanNamesForAnnotation(annotationType);
    }

    /**
     * Find all beans which are annotated with the supplied {@link Annotation} type,
     * returning a Map of bean names with corresponding bean instances.
     * <p>Note that this method considers objects created by FactoryBeans, which means
     * that FactoryBeans will get initialized in order to determine their object type.
     *
     * @param annotationType the type of annotation to look for
     *                       (at class, interface or factory method level of the specified bean)
     * @return a Map with the matching beans, containing the bean names as
     * keys and the corresponding bean instances as values
     * @throws BeansException if a bean could not be created
     * @see #findAnnotationOnBean
     * @since 3.0
     */
    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        return ctx.getBeansWithAnnotation(annotationType);
    }

    /**
     * Find an {@link Annotation} of {@code annotationType} on the specified bean,
     * traversing its interfaces and super classes if no annotation can be found on
     * the given class itself, as well as checking the bean's factory method (if any).
     *
     * @param beanName       the name of the bean to look for annotations on
     * @param annotationType the type of annotation to look for
     *                       (at class, interface or factory method level of the specified bean)
     * @return the annotation of the given type if found, or {@code null} otherwise
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBeanNamesForAnnotation
     * @see #getBeansWithAnnotation
     * @see #getType(String)
     * @since 3.0
     */
    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
        return ctx.findAnnotationOnBean(beanName,annotationType);
    }

    /**
     * Find an {@link Annotation} of {@code annotationType} on the specified bean,
     * traversing its interfaces and super classes if no annotation can be found on
     * the given class itself, as well as checking the bean's factory method (if any).
     *
     * @param beanName             the name of the bean to look for annotations on
     * @param annotationType       the type of annotation to look for
     *                             (at class, interface or factory method level of the specified bean)
     * @param allowFactoryBeanInit whether a {@code FactoryBean} may get initialized
     *                             just for the purpose of determining its object type
     * @return the annotation of the given type if found, or {@code null} otherwise
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBeanNamesForAnnotation
     * @see #getBeansWithAnnotation
     * @see #getType(String, boolean)
     * @since 5.3.14
     */
    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        return ctx.findAnnotationOnBean(beanName, annotationType, allowFactoryBeanInit);
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * <p>This method allows a Spring BeanFactory to be used as a replacement for the
     * Singleton or Prototype design pattern. Callers may retain references to
     * returned objects in the case of Singleton beans.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     * @throws NoSuchBeanDefinitionException if there is no bean with the specified name
     * @throws BeansException                if the bean could not be obtained
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return ctx.getBean(name);
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
     * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
     * required type. This means that ClassCastException can't be thrown on casting
     * the result correctly, as can happen with {@link #getBean(String)}.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name         the name of the bean to retrieve
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the bean
     * @throws NoSuchBeanDefinitionException  if there is no such bean definition
     * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
     * @throws BeansException                 if the bean could not be created
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return ctx.getBean(name, requiredType);
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * <p>Allows for specifying explicit constructor arguments / factory method arguments,
     * overriding the specified default arguments (if any) in the bean definition.
     *
     * @param name the name of the bean to retrieve
     * @param args arguments to use when creating a bean instance using explicit arguments
     *             (only applied when creating a new instance as opposed to retrieving an existing one)
     * @return an instance of the bean
     * @throws NoSuchBeanDefinitionException if there is no such bean definition
     * @throws BeanDefinitionStoreException  if arguments have been given but
     *                                       the affected bean isn't a prototype
     * @throws BeansException                if the bean could not be created
     * @since 2.5
     */
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return ctx.getBean(name, args);
    }

    /**
     * Return the bean instance that uniquely matches the given object type, if any.
     * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
     * but may also be translated into a conventional by-name lookup based on the name
     * of the given type. For more extensive retrieval operations across sets of beans,
     * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
     *
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the single bean matching the required type
     * @throws NoSuchBeanDefinitionException   if no bean of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
     * @throws BeansException                  if the bean could not be created
     * @see ListableBeanFactory
     * @since 3.0
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return ctx.getBean(requiredType);
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     * <p>Allows for specifying explicit constructor arguments / factory method arguments,
     * overriding the specified default arguments (if any) in the bean definition.
     * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
     * but may also be translated into a conventional by-name lookup based on the name
     * of the given type. For more extensive retrieval operations across sets of beans,
     * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
     *
     * @param requiredType type the bean must match; can be an interface or superclass
     * @param args         arguments to use when creating a bean instance using explicit arguments
     *                     (only applied when creating a new instance as opposed to retrieving an existing one)
     * @return an instance of the bean
     * @throws NoSuchBeanDefinitionException if there is no such bean definition
     * @throws BeanDefinitionStoreException  if arguments have been given but
     *                                       the affected bean isn't a prototype
     * @throws BeansException                if the bean could not be created
     * @since 4.1
     */
    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return ctx.getBean(requiredType, args);
    }

    /**
     * Return a provider for the specified bean, allowing for lazy on-demand retrieval
     * of instances, including availability and uniqueness options.
     * <p>For matching a generic type, consider {@link #getBeanProvider(ResolvableType)}.
     *
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return a corresponding provider handle
     * @see #getBeanProvider(ResolvableType)
     * @since 5.1
     */
    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return ctx.getBeanProvider(requiredType);
    }

    /**
     * Return a provider for the specified bean, allowing for lazy on-demand retrieval
     * of instances, including availability and uniqueness options. This variant allows
     * for specifying a generic type to match, similar to reflective injection points
     * with generic type declarations in method/constructor parameters.
     * <p>Note that collections of beans are not supported here, in contrast to reflective
     * injection points. For programmatically retrieving a list of beans matching a
     * specific type, specify the actual bean type as an argument here and subsequently
     * use {@link ObjectProvider#orderedStream()} or its lazy streaming/iteration options.
     * <p>Also, generics matching is strict here, as per the Java assignment rules.
     * For lenient fallback matching with unchecked semantics (similar to the ´unchecked´
     * Java compiler warning), consider calling {@link #getBeanProvider(Class)} with the
     * raw type as a second step if no full generic match is
     * {@link ObjectProvider#getIfAvailable() available} with this variant.
     *
     * @param requiredType type the bean must match; can be a generic type declaration
     * @return a corresponding provider handle
     * @see ObjectProvider#iterator()
     * @see ObjectProvider#stream()
     * @see ObjectProvider#orderedStream()
     * @since 5.1
     */
    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return ctx.getBeanProvider(requiredType);
    }

    /**
     * Does this bean factory contain a bean definition or externally registered singleton
     * instance with the given name?
     * <p>If the given name is an alias, it will be translated back to the corresponding
     * canonical bean name.
     * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
     * be found in this factory instance.
     * <p>If a bean definition or singleton instance matching the given name is found,
     * this method will return {@code true} whether the named bean definition is concrete
     * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
     * return value from this method does not necessarily indicate that {@link #getBean}
     * will be able to obtain an instance for the same name.
     *
     * @param name the name of the bean to query
     * @return whether a bean with the given name is present
     */
    @Override
    public boolean containsBean(String name) {
        return ctx.containsBean(name);
    }

    /**
     * Is this bean a shared singleton? That is, will {@link #getBean} always
     * return the same instance?
     * <p>Note: This method returning {@code false} does not clearly indicate
     * independent instances. It indicates non-singleton instances, which may correspond
     * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
     * check for independent instances.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name the name of the bean to query
     * @return whether this bean corresponds to a singleton instance
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #isPrototype
     */
    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return ctx.isSingleton(name);
    }

    /**
     * Is this bean a prototype? That is, will {@link #getBean} always return
     * independent instances?
     * <p>Note: This method returning {@code false} does not clearly indicate
     * a singleton object. It indicates non-independent instances, which may correspond
     * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
     * check for a shared singleton instance.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name the name of the bean to query
     * @return whether this bean will always deliver independent instances
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #isSingleton
     * @since 2.0.3
     */
    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return ctx.isPrototype(name);
    }

    /**
     * Check whether the bean with the given name matches the specified type.
     * More specifically, check whether a {@link #getBean} call for the given name
     * would return an object that is assignable to the specified target type.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name        the name of the bean to query
     * @param typeToMatch the type to match against (as a {@code ResolvableType})
     * @return {@code true} if the bean type matches,
     * {@code false} if it doesn't match or cannot be determined yet
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #getType
     * @since 4.2
     */
    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return ctx.isTypeMatch(name, typeToMatch);
    }

    /**
     * Check whether the bean with the given name matches the specified type.
     * More specifically, check whether a {@link #getBean} call for the given name
     * would return an object that is assignable to the specified target type.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name        the name of the bean to query
     * @param typeToMatch the type to match against (as a {@code Class})
     * @return {@code true} if the bean type matches,
     * {@code false} if it doesn't match or cannot be determined yet
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #getType
     * @since 2.0.1
     */
    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return ctx.isTypeMatch(name, typeToMatch);
    }

    /**
     * Determine the type of the bean with the given name. More specifically,
     * determine the type of object that {@link #getBean} would return for the given name.
     * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
     * as exposed by {@link FactoryBean#getObjectType()}. This may lead to the initialization
     * of a previously uninitialized {@code FactoryBean} (see {@link #getType(String, boolean)}).
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name the name of the bean to query
     * @return the type of the bean, or {@code null} if not determinable
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #isTypeMatch
     * @since 1.1.2
     */
    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return ctx.getType(name);
    }

    /**
     * Determine the type of the bean with the given name. More specifically,
     * determine the type of object that {@link #getBean} would return for the given name.
     * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
     * as exposed by {@link FactoryBean#getObjectType()}. Depending on the
     * {@code allowFactoryBeanInit} flag, this may lead to the initialization of a previously
     * uninitialized {@code FactoryBean} if no early type information is available.
     * <p>Translates aliases back to the corresponding canonical bean name.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name                 the name of the bean to query
     * @param allowFactoryBeanInit whether a {@code FactoryBean} may get initialized
     *                             just for the purpose of determining its object type
     * @return the type of the bean, or {@code null} if not determinable
     * @throws NoSuchBeanDefinitionException if there is no bean with the given name
     * @see #getBean
     * @see #isTypeMatch
     * @since 5.2
     */
    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        return ctx.getType(name, allowFactoryBeanInit);
    }

    /**
     * Return the aliases for the given bean name, if any.
     * <p>All of those aliases point to the same bean when used in a {@link #getBean} call.
     * <p>If the given name is an alias, the corresponding original bean name
     * and other aliases (if any) will be returned, with the original bean name
     * being the first element in the array.
     * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
     *
     * @param name the bean name to check for aliases
     * @return the aliases, or an empty array if none
     * @see #getBean
     */
    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    /**
     * Notify all <strong>matching</strong> listeners registered with this
     * application of an event.
     * <p>If the specified {@code event} is not an {@link ApplicationEvent},
     * it is wrapped in a {@link PayloadApplicationEvent}.
     * <p>Such an event publication step is effectively a hand-off to the
     * multicaster and does not imply synchronous/asynchronous execution
     * or even immediate execution at all. Event listeners are encouraged
     * to be as efficient as possible, individually using asynchronous
     * execution for longer-running and potentially blocking operations.
     *
     * @param event the event to publish
     * @see #publishEvent(ApplicationEvent)
     * @see PayloadApplicationEvent
     * @since 4.2
     */
    @Override
    public void publishEvent(Object event) {
        ctx.publishEvent(event);
    }

    /**
     * Try to resolve the message. Return default message if no message was found.
     *
     * @param code           the message code to look up, e.g. 'calculator.noRateSet'.
     *                       MessageSource users are encouraged to base message names on qualified class
     *                       or package names, avoiding potential conflicts and ensuring maximum clarity.
     * @param args           an array of arguments that will be filled in for params within
     *                       the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *                       or {@code null} if none
     * @param defaultMessage a default message to return if the lookup fails
     * @param locale         the locale in which to do the lookup
     * @return the resolved message if the lookup was successful, otherwise
     * the default message passed as a parameter (which may be {@code null})
     * @see #getMessage(MessageSourceResolvable, Locale)
     * @see MessageFormat
     */
    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return ctx.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code   the message code to look up, e.g. 'calculator.noRateSet'.
     *               MessageSource users are encouraged to base message names on qualified class
     *               or package names, avoiding potential conflicts and ensuring maximum clarity.
     * @param args   an array of arguments that will be filled in for params within
     *               the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *               or {@code null} if none
     * @param locale the locale in which to do the lookup
     * @return the resolved message (never {@code null})
     * @throws NoSuchMessageException if no corresponding message was found
     * @see #getMessage(MessageSourceResolvable, Locale)
     * @see MessageFormat
     */
    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return ctx.getMessage(code, args, locale);
    }

    /**
     * Try to resolve the message using all the attributes contained within the
     * {@code MessageSourceResolvable} argument that was passed in.
     * <p>NOTE: We must throw a {@code NoSuchMessageException} on this method
     * since at the time of calling this method we aren't able to determine if the
     * {@code defaultMessage} property of the resolvable is {@code null} or not.
     *
     * @param resolvable the value object storing attributes required to resolve a message
     *                   (may include a default message)
     * @param locale     the locale in which to do the lookup
     * @return the resolved message (never {@code null} since even a
     * {@code MessageSourceResolvable}-provided default message needs to be non-null)
     * @throws NoSuchMessageException if no corresponding message was found
     *                                (and no default message was provided by the {@code MessageSourceResolvable})
     * @see MessageSourceResolvable#getCodes()
     * @see MessageSourceResolvable#getArguments()
     * @see MessageSourceResolvable#getDefaultMessage()
     * @see MessageFormat
     */
    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return ctx.getMessage(resolvable, locale);
    }

    /**
     * Return the {@link Environment} associated with this component.
     */
    @Override
    public Environment getEnvironment() {
        return ctx.getEnvironment();
    }

    /**
     * Resolve the given location pattern into {@code Resource} objects.
     * <p>Overlapping resource entries that point to the same physical
     * resource should be avoided, as far as possible. The result should
     * have set semantics.
     *
     * @param locationPattern the location pattern to resolve
     * @return the corresponding {@code Resource} objects
     * @throws IOException in case of I/O errors
     */
    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return ctx.getResources(locationPattern);
    }

    /**
     * Return a {@code Resource} handle for the specified resource location.
     * <p>The handle should always be a reusable resource descriptor,
     * allowing for multiple {@link Resource#getInputStream()} calls.
     * <p><ul>
     * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".
     * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".
     * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".
     * (This will be implementation-specific, typically provided by an
     * ApplicationContext implementation.)
     * </ul>
     * <p>Note that a {@code Resource} handle does not imply an existing resource;
     * you need to invoke {@link Resource#exists} to check for existence.
     *
     * @param location the resource location
     * @return a corresponding {@code Resource} handle (never {@code null})
     * @see #CLASSPATH_URL_PREFIX
     * @see Resource#exists()
     * @see Resource#getInputStream()
     */
    @Override
    public Resource getResource(String location) {
        return ctx.getResource(location);
    }

    /**
     * Expose the {@link ClassLoader} used by this {@code ResourceLoader}.
     * <p>Clients which need to access the {@code ClassLoader} directly can do so
     * in a uniform manner with the {@code ResourceLoader}, rather than relying
     * on the thread context {@code ClassLoader}.
     *
     * @return the {@code ClassLoader}
     * (only {@code null} if even the system {@code ClassLoader} isn't accessible)
     * @see ClassUtils#getDefaultClassLoader()
     * @see ClassUtils#forName(String, ClassLoader)
     */
    @Override
    public ClassLoader getClassLoader() {
        return ctx.getClassLoader();
    }
}
