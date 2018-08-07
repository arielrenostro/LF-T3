package br.furb.linguagensformais.trabalho3.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import br.furb.linguagensformais.trabalho3.view.View;

/**
 * @author ariel
 */
public abstract class SwingViewFactory {

	private static final Map<String, View> instances = new HashMap<>();

	private SwingViewFactory() {
		super();
	}

	private static <T extends View> String getStringClass(Class<T> viewClass) {
		return viewClass.getName();
	}

	public static <T extends View> T getView(Class<T> viewClass) {
		return getView(viewClass, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(Class<T> viewClass, JFrame owner) {
		String chave = getStringClass(viewClass);
		View view = instances.get(chave);
		if (null == view) {
			try {
				if (null != owner) {
					Constructor<T> constructor;
					try {
						constructor = viewClass.getConstructor(JFrame.class);
						view = constructor.newInstance(owner);
					} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}

				if (null == view) {
					view = viewClass.newInstance();
				}

				if (view instanceof JDialog) {
					((JDialog) view).setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}

				instances.put(chave, view);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return (T) view;
	}

}
