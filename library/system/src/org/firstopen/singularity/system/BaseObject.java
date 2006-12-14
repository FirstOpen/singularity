/*
 * Copyright 2005 Jeff Bride
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.system;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Base class for all business objects in MarketStream
 * 
 * @author TomJoseph
 */
public abstract class BaseObject {
	
	
	static Logger log = Logger.getLogger(BaseObject.class);
	
	private String ownerName;

	private String groupName;

	private Date creationDate;

	private Date modificationDate;

	public String displayName;


	public static final String GROUP_NAME = "groupName";

	public static final String OWNER_NAME = "ownerName";

	public static final String CREATION_DATE = "creationDate";

	public BaseObject() {
		
	}

	public boolean update(HashMap aFieldList) {
		setValues(aFieldList);
		return true;
	}

	/*
	 * pass to this method the className and the actual object of the base class
	 * that this object inherits from
	 */
	@SuppressWarnings("varargs")
	public boolean update(String className, Object aSuper) throws Exception {
		log.debug("update() className = " + className);
		Class classObj = Class.forName(className);
		Class collectionClass = Class.forName("java.util.Collection");

		java.lang.reflect.Method[] methods = classObj.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get")
					&& !(methodName.startsWith("getId"))) {
				Class returnType = methods[i].getReturnType();
				Object value = null;

				if (returnType.isAssignableFrom(collectionClass))
					continue;
				else
					value = methods[i].invoke(aSuper);

				if (value != null) {
					String fieldName = methodName.substring(3);
					log.debug("fieldName = " + fieldName);
					this.setFieldValue(fieldName, value);
				}
			}
		}
		return true;
	}

	private void setValues(HashMap aFieldList) {
		// populate the objects with values from the list
		Iterator oKeys = aFieldList.keySet().iterator();
		Object aValue;
		String fieldName;
		Method oMethod;

		while (oKeys.hasNext()) {
			fieldName = (String) oKeys.next();
			// get the field value from the list
			aValue = aFieldList.get(fieldName);

			if (aValue == null)
				log
						.info("WARNING:  You've passed a hash with a NULL value for '"
								+ fieldName);

			setFieldValue(fieldName, aValue);
		}
	}

	/*
	 * " Once the bean's state has been populated and its EntityContext has been
	 * established, an ejbPostCreate() method is invoked" --pg. 262 public void
	 * ejbPostCreate(HashMap aFieldList) { log.debug("ejbPostCreate()"); }
	 * 
	 * /** set the field value
	 */
	public void setFieldValue(String fieldName, Object aValue) {
		log.debug("setFieldValue() fieldName = " + fieldName + " value = "
				+ aValue);
		Object[] args = new Object[] { aValue };

		// find the set method for each field id
		Method aMethod = getSetMethod(fieldName, args);

		/*
		 * Sorry ... can not invoke a setter using a primitive parameter .....
		 * reflection requires the use of the Wrapper objects only when passing
		 * to the following method
		 */

		try {
			// set the field value
			aMethod.invoke(this, args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadObjectType("error invoking method :"
					+ aMethod.getName() + "with arg " + aValue.getClass());
		}
	}

	/**
	 * get the 'set' method for the specified field
	 * 
	 * @param aFieldName
	 *            field for which to get the method id
	 * @param aValue
	 *            a sample field value
	 * @return method that sets the value of the field
	 * @exception BadObjectType
	 *                if the type of object is different from that of the
	 *                component class.
	 */
	protected Method getGetMethod(String aFieldName) throws BadObjectType {
		return getGetMethod(this.getClass(), aFieldName);
	}


	public static Method getGetMethod(Class aClass, String aFieldName) {
		String methodName = getGetMethodName(aFieldName);
		try {
			return aClass.getMethod(methodName);
		} catch (Exception e) {
			log.debug("Error finding method " + methodName + " in "
					+ aClass);
			throw new BadObjectType();
		}
	}

	/**
	 * get the specified method which sets the type specified
	 */
	private Method getMethod(String aMethodName, Class anArgType) {
		log.debug("getMethod trying to get method: '" + aMethodName + "'");
		Class[] args = new Class[] { anArgType };
		try {
			return this.getClass().getMethod(aMethodName, args);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * get the 'set' method for the specified field
	 * 
	 * @param aFieldName
	 *            field for which to get the method id
	 * @return method that sets the value of the field
	 * @exception BadObjectType
	 *                if the type of object is different from that of the
	 *                component class.
	 */
	protected Method getSetMethod(String aFieldName, Object[] anArgs)
			throws BadObjectType {
		log.debug("getSetMethod aFieldName = '" + aFieldName + "'");
		Object aValue = anArgs[0];
		String methodName = getSetMethodName(aFieldName);
		Class aFieldType = null;
		Class argType = null != aValue ? aValue.getClass()
				: (aFieldType = attemptToGetFieldTypeDespitePassingNullValue(aFieldName));

		Method aMethod = null;

		// first attempt with value type
		aMethod = getMethod(methodName, argType);
		if (null != aMethod)
			return aMethod;

		// handle String -> primitive case
		if (argType.equals(String.class)) {
			// need to find field type
			if (null == aFieldType)
				aFieldType = attemptToGetFieldTypeDespitePassingNullValue(aFieldName);

			if (aFieldType.isPrimitive()) {
				aMethod = getMethod(methodName, aFieldType);
				if (null != aMethod) {
					anArgs[0] = primitiveFromString(aFieldType, anArgs);
					return aMethod;
				}
			}
		}

		// try for primitive base type of the value
		Class baseType = getBaseType(argType, aValue);
		if (null != baseType) {
			aMethod = getMethod(methodName, baseType);
			if (null != aMethod)
				return aMethod;
		}

		// now try the interfaces
		Class[] interfaces = argType.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			aMethod = getMethod(methodName, interfaces[i]);
			if (null != aMethod)
				return aMethod;
		}

		// try for base classes
		baseType = argType;
		do {
			baseType = baseType.getSuperclass();
			if (null == baseType)
				break;

			aMethod = getMethod(methodName, baseType);
			if (null != aMethod)
				return aMethod;

		} while (true);

		// we have tried all combination, error out
		log.error("getSetMethod()  Error finding method " + methodName
				+ " with arg " + argType + " in " + this.getClass());
		throw new BadObjectType();
	}


	private Class attemptToGetFieldTypeDespitePassingNullValue(String aFieldName) {
		Class aType = getClass();
		while (null != aType) {
			try {
				/*
				 * EJB proxy object do not have fields. Only methods. So we'll
				 * have to get the Class object via the accessor method return
				 * aType.getDeclaredField (aFieldName).getType ();
				 */
				String methodName = getMethodName(aFieldName, false);
				Method meth = (this.getClass()).getMethod(methodName);
				return meth.getReturnType();
			} catch (Exception e) {
				log
						.error("attemptToGetFieldTypeDespitePassingNullValue()  Exception = "
								+ e);
				aType = aType.getSuperclass();
			}
		}
		log.error("Error finding field " + aFieldName + " in "
				+ this.getClass());
		throw new BadObjectType();
	}

	/**
	 * get the base type of the runtime class
	 * 
	 * @param aClass
	 *            a runtime class
	 * @param aValue
	 *            an instance of the runtime class
	 * @return base type (eq, double for Double, int for Integer etc)
	 */
	public static Class getBaseType(Class aClass, Object aValue) {
		try {
			Field oTypeField = aClass.getField("TYPE");
			return (Class) oTypeField.get(aValue);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * get the 'get' method id
	 * 
	 * @param aFieldName
	 *            field for which to get the method id
	 * @return id of the method that retrieves the value of the field
	 */
	public static String getGetMethodName(String fieldName) {
		return getMethodName(fieldName, false);
	}

	/**
	 * get the 'set' method id
	 * 
	 * @param aFieldName
	 *            field for which to get the method id
	 * @return id of the method that sets the value of the field
	 */
	public static String getSetMethodName(String fieldName) {
		return getMethodName(fieldName, true);
	}

	/**
	 * get the 'set'or 'get' method for the specified field id
	 * 
	 * @param aFieldName
	 *            id of the field for which to get the method
	 * @param isSet
	 *            get the 'set' method if true, else get the 'get' method
	 * @return get or set method for the field
	 */
	protected static String getMethodName(String fieldName, boolean isSet) {
		StringBuffer oBuffer = new StringBuffer(isSet ? "set" : "get");
		oBuffer.append(Character.toUpperCase(fieldName.charAt(0)));
		oBuffer.append(fieldName.substring(1));
		return oBuffer.toString();
	}

	/**
	 * get the specified field's value from the object
	 */
	@SuppressWarnings("varargs")
	public Object getFieldValue(String aFieldName) {
		// find the set method for each field id
		Method aMethod = getGetMethod(aFieldName);

		try {
			// set the field value
			return aMethod.invoke(this);
		} catch (Exception e) {
			log.error("error finding " + aMethod + " on " + getClass());
			e.printStackTrace();
			throw new BadObjectType();
		}

	}

	private Object primitiveFromString(Class aPrimitiveType, Object[] anArgs) {
		try {
			Class aWrapClass = getWrapperClass(aPrimitiveType);
			Constructor aStringCons = aWrapClass
					.getConstructor(new Class[] { String.class });
			return aStringCons.newInstance(anArgs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Class getWrapperClass(Class aPrimitive) {
		if (aPrimitive == double.class)
			return Double.class;
		else if (aPrimitive == boolean.class)
			return Boolean.class;
		else if (aPrimitive == byte.class)
			return Byte.class;
		else if (aPrimitive == short.class)
			return Short.class;
		else if (aPrimitive == int.class)
			return Integer.class;
		else if (aPrimitive == long.class)
			return Long.class;
		else if (aPrimitive == float.class)
			return Float.class;

		return String.class;
	}

	// abstract accessor methods

	/**
	 * @hibernate.property
	 * @hibernate.column id="ownerName" sql-type="varchar(40)"
	 */
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String x) {
		ownerName = x;
	}

	/**
	 * @hibernate.property
	 * @hibernate.column id="groupName" sql-type="varchar(40)"
	 */
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String x) {
		groupName = x;
	}

	/**
	 * @hibernate.property
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date x) {
		creationDate = x;
	}

	/**
	 * @hibernate.property
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date x) {
		modificationDate = x;
	}

	public String getDisplayName() {
		return displayName;
	}

	public abstract void setDisplayName();
}// BaseObject
