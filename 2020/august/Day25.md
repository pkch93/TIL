# 2020.08.25 - Tuesday: Spring PropertyEditor

참고: 전문가를 위한 스프링 5 - 4.7

PropertyEditor는 프로퍼티 값을 원래 자료 타입에서 String 또는 그 반대역할을 하는 인터페이스이다.
본래는 편집기에서 입력한 String 타입의 프로퍼티를 사용하고자 하는 타입으로 변환하는 용도였다. 다만 PropertyEditor 구현 클래스가 가벼운 클래스라 많은 곳에서 응용되고 있다.

Spring은 대부분의 프로퍼티 값을 BeanFactory 구성 파일에서 읽어온다. 기본적으로 String이다. 단, 빈에 필요한 프로퍼티 값이 String이 아닌 다른 타입이 필요할 수 있다.

따라서, String 기반의 프로퍼티를 다른 적절한 타입으로 변환할 수 있도록 PropertyEditor가 도와준다. 

Spring에서 기본 제공하는 PropertyEditor는 다음과 같다.

![https://user-images.githubusercontent.com/30178507/91179337-54bf9480-e721-11ea-9e28-b55eb8b29bcb.png](https://user-images.githubusercontent.com/30178507/91179337-54bf9480-e721-11ea-9e28-b55eb8b29bcb.png)

이들 PropertyEditor들은 `org.springframework:spring-beans` 라이브러리에 존재한다.
모두 `java.beans.PropertyEditorSupport`를 상속받고 있다. String 문자열을 빈에 주입될 프로퍼티로 암묵적 형변환을 할 때 사용한다.

# Custom PropertyEditor

스프링은 Custom PropertyEditor 구현을 완벽히 지원한다. 다만 PropertyEditor 인터페이스를 그대로 구현한다면 PropertyEditor가 가진 많은 메서드를 구현해야만 한다.

```java
package java.beans;

public interface PropertyEditor {

    void setValue(Object value);

    Object getValue();

    boolean isPaintable();

    void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box);

    String getJavaInitializationString();

    String getAsText();

    void setAsText(String text) throws java.lang.IllegalArgumentException;

    String[] getTags();

    java.awt.Component getCustomEditor();

    boolean supportsCustomEditor();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

}
```

PropertyEditor를 그대로 구현한다면 위와 같이 12개의 메서드를 재정의해야한다.

이를 간편히하기 위해 Java 5 버전에서 PropertyEditorSupport가 등장했다.

```java
package java.beans;

import java.beans.*;

public class PropertyEditorSupport implements PropertyEditor {

    public PropertyEditorSupport() {
        setSource(this);
    }

    public PropertyEditorSupport(Object source) {
        if (source == null) {
           throw new NullPointerException();
        }
        setSource(source);
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public void setValue(Object value) {
        this.value = value;
        firePropertyChange();
    }

    public Object getValue() {
        return value;
    }

    public boolean isPaintable() {
        return false;
    }

    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
    }

    public String getJavaInitializationString() {
        return "???";
    }

    public String getAsText() {
        return (this.value != null)
                ? this.value.toString()
                : null;
    }

    /**
     * Sets the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     *
     * @param text  The string to be parsed.
     */
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        if (value instanceof String) {
            setValue(text);
            return;
        }
        throw new java.lang.IllegalArgumentException(text);
    }

    public String[] getTags() {
        return null;
    }

    public java.awt.Component getCustomEditor() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return false;
    }

    public synchronized void addPropertyChangeListener(
                                PropertyChangeListener listener) {
        if (listeners == null) {
            listeners = new java.util.Vector<>();
        }
        listeners.addElement(listener);
    }

    public synchronized void removePropertyChangeListener(
                                PropertyChangeListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.removeElement(listener);
    }

    public void firePropertyChange() {
        java.util.Vector<PropertyChangeListener> targets;
        synchronized (this) {
            if (listeners == null) {
                return;
            }
            targets = unsafeClone(listeners);
        }
        // Tell our listeners that "everything" has changed.
        PropertyChangeEvent evt = new PropertyChangeEvent(source, null, null, null);

        for (int i = 0; i < targets.size(); i++) {
            PropertyChangeListener target = targets.elementAt(i);
            target.propertyChange(evt);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> java.util.Vector<T> unsafeClone(java.util.Vector<T> v) {
        return (java.util.Vector<T>)v.clone();
    }

    //----------------------------------------------------------------------

    private Object value;
    private Object source;
    private java.util.Vector<PropertyChangeListener> listeners;
}
```

PropertyEditorSupport는 위와 같이 정의되어 있으며 Custom을 위해서는 `setValue`나 `setAsText`만 재정의하여 구현하면 된다.