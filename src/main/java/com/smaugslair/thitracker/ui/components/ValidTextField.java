package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.ArrayList;
import java.util.List;

public class ValidTextField extends TextField {

  class Content {
    String content;
    public String getContent() {
      return content;
    }
    public void setContent(String content) {
      this.content = content;
    }
  }

  private Content content = new Content();
  private Binder<Content> binder = new Binder<>();
  private List<Validator<String>> validators = new ArrayList<>();

  public ValidTextField() {
    binder.setBean(content);
  }

  public void addValidator(
    SerializablePredicate<String> predicate,
    String errorMessage) {
      addValidator(Validator.from(predicate, errorMessage));
  }

  public void addValidator(Validator<String> validator) {
    validators.add(validator);
    build();
  }

  public boolean isValid() {
    return binder.isValid();
  }

  private void build() {
    Binder.BindingBuilder<Content, String> builder =
      binder.forField(this);

    for(Validator<String> v: validators) {
      builder.withValidator(v);
    }

    builder.bind(
      Content::getContent, Content::setContent);
  }
}