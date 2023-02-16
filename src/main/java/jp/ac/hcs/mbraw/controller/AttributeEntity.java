package jp.ac.hcs.mbraw.controller;

import java.util.Optional;

import org.springframework.ui.Model;

import lombok.NonNull;
import lombok.Value;

@Value
public class AttributeEntity<
	T> {

	private final @NonNull String attributeName;
	private final @NonNull Optional<T> responseEntity;

	public void addAttribute(Model model) {
		if (this.getResponseEntity().isEmpty()) {
			throw new NullPointerException(
					"Detect nulls that cannot be passed to the front end." + this.getAttributeName() + "is null");
		}

		model.addAttribute(this.getAttributeName(), this.getResponseEntity().get());
	}
}
