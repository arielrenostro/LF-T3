package br.furb.linguagensformais.trabalho3.factory;

import java.util.HashMap;
import java.util.Map;

import br.furb.linguagensformais.trabalho3.controller.Controller;

/**
 * @author ariel
 */
public abstract class ControllerFactory {

	private static final Map<String, Controller> instances = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <C extends Controller> C getController(Class<C> controllerClass) {
		String chave = getStringClass(controllerClass);
		Controller controller = instances.get(chave);
		if (null == controller) {
			try {
				controller = controllerClass.newInstance();
				instances.put(chave, controller);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return (C) controller;
	}

	private static <C extends Controller> String getStringClass(Class<C> controllerClass) {
		return controllerClass.getName();
	}
}
